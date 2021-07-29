from config_access import ConfigAccessor
from db_access.model import *

import mysql.connector
from mysql.connector import MySQLConnection, CMySQLConnection

import logging
import time

from enum import Enum
from typing import Optional, Union

from datetime import timedelta

class TableName(Enum):
    User = 'Account'
    Log = 'log'
    Schedule = 'schedule'


class DbAccessor:
    __instance: 'DbAccessor' = None

    MYSQL_DATE_FORMAT: str = "%Y-%m-%d %H:%M:%S"

    def __init__(self):
        self._logger = logging.getLogger('DbAccessor')
        self._db_conn = self._create_conn(3000)

    def _create_conn(self, retry_ms: Optional[int] = None) -> Union[CMySQLConnection, MySQLConnection]:
        retry_sec = retry_ms / 1000 if type(retry_ms) is int else None

        if retry_sec is None:
            return self._create_conn_helper()
        else:
            while True:
                try:
                    return self._create_conn_helper()
                except mysql.connector.Error:
                    self._logger.info("Retry connecting in %d seconds", retry_sec)
                    time.sleep(retry_sec)

    def _create_conn_helper(self) -> Union[CMySQLConnection, MySQLConnection]:
        config_accessor = ConfigAccessor.get_instance()

        db_host = config_accessor.host_database
        db_username = config_accessor.username_database
        db_password = config_accessor.password_database
        db_name = config_accessor.name_database

        try:
            self._logger.info("Connecting to %s database...", db_name)

            conn = mysql.connector.connect(
                host=db_host,
                user=db_username,
                password=db_password,
                database=db_name
            )

            self._logger.info("Connected to %s database", db_name)

            return conn
        except mysql.connector.Error as err:
            self._logger.error("Failed to create to %s database: %s", db_name, str(err))
            raise err

    def is_user_exist(self, user: UserSchema) -> bool:
        self._logger.debug("is_user_exist(%s)", str(user.dict()))

        cursor = self._db_conn.cursor()

        param = (user.username, user.password)
        sql = """
            SELECT username
            FROM {}
            WHERE username = %s AND password = %s
        """.format(TableName.User.value)

        cursor.execute(sql, param)

        return len(cursor.fetchall()) != 0

    def insert_user(self, user: UserSchema) -> bool:
        self._logger.debug("insert_user(%s)", str(user.dict()))

        cursor = self._db_conn.cursor()

        sql = """
            INSERT INTO {}(username, password)
            VALUES (%s, %s)
        """.format(TableName.User.value)

        param = (user.username, user.password)

        try:
            cursor.execute(sql, param)
        except mysql.connector.IntegrityError:
            return False

        self._db_conn.commit()
        return True

    def insert_log(self, log: LogSchema) -> bool:
        self._logger.debug("insert_log(%s)", str(log.dict()))

        cursor = self._db_conn.cursor()

        sql = """
            INSERT INTO {}(timestamp, image, type)
            VALUES(%s, %s, %s)
        """.format(TableName.Log.value)

        param = (log.timestamp.strftime(self.MYSQL_DATE_FORMAT), log.image, log.type)

        try:
            cursor.execute(sql, param)
        except mysql.connector.IntegrityError:
            return False

        self._db_conn.commit()
        return True

    def get_all_logs(self) -> List[LogSchema]:
        self._logger.debug("get_all_logs")
        cursor = self._db_conn.cursor()

        sql = """
            SELECT *
            FROM {}
        """.format(TableName.Log.value)

        cursor.execute(sql)
        result = cursor.fetchall()
        return [
            LogSchema(
                id=tup[0],
                timestamp=tup[1],
                image=tup[2],
                type=tup[3]
            )
            for tup in result
        ]


    def get_logs_in_interval(
            self,
            start_time: datetime.datetime = datetime.datetime.min,
            end_time: datetime.datetime = datetime.datetime.max
    ) -> List[LogSchema]:
        end_time = end_time + timedelta(1) #bias 1 day
        start_time: str = start_time.strftime(self.MYSQL_DATE_FORMAT)
        end_time: str = end_time.strftime(self.MYSQL_DATE_FORMAT)

        self._logger.debug("get_logs_in_interval(%s, %s)", start_time, end_time)

        cursor = self._db_conn.cursor()

        sql = """
            SELECT *
            FROM {}
            WHERE timestamp >= %s AND timestamp <= %s
        """.format(TableName.Log.value)

        param = (start_time, end_time)

        cursor.execute(sql, param)
        result = cursor.fetchall()
        return [
            LogSchema(
                id=tup[0],
                timestamp=tup[1],
                image=tup[2],
                type=tup[3]
            )
            for tup in result
        ]

    def delete_log(self, log_id: Optional[int]) -> None:
        self._logger.debug("delete_log(%s)", str(log_id))

        cursor = self._db_conn.cursor()

        if log_id is None:
            sql = """
                DELETE FROM {}
            """.format(TableName.Log.value)

            cursor.execute(sql)
        else:
            sql = """
                DELETE FROM {}
                WHERE id = %s
            """.format(TableName.Log.value)
            param = (log_id,)

            cursor.execute(sql, param)

        self._db_conn.commit()

    def get_schedule(self, username: str, time_: datetime.time, is_actives: int) -> Optional[ScheduleSchema]:
        self._logger.debug("get_schedule(%s, %s, %s)", username, time_, is_actives)

        cursor = self._db_conn.cursor()

        sql = """
            SELECT *
            FROM {}
            WHERE username = %s AND time = %s AND isActives
        """.format(TableName.Schedule.value)

        param = (username, time_, is_actives)

        cursor.execute(sql, param)
        result = cursor.fetchall()

        if not result:
            return None

        tup = result[0]
        return ScheduleSchema(
            username=tup[0],
            time=(tup[1] + datetime.datetime.min).time(),
            description=tup[2],
            is_actives=tup[3],
            to_cautious_mode=tup[4],
            is_enabled=tup[5]
        )

    def get_schedules(self, username: str) -> List[ScheduleSchema]:
        self._logger.debug("get_schedules(%s)", username)

        cursor = self._db_conn.cursor()
        
        sql = """
            SELECT *
            FROM {}
            WHERE username = %s
        """.format(TableName.Schedule.value)

        param = (username,)

        cursor.execute(sql, param)
        result = cursor.fetchall()

        return [
            ScheduleSchema(
                username=tup[0],
                time=(tup[1] + datetime.datetime.min).time(),
                description=tup[2],
                is_actives=tup[3],
                to_cautious_mode=tup[4],
                is_enabled=tup[5]
            )
            for tup in result
        ]

    def insert_schedule(self, schedule: ScheduleSchema) -> bool:
        self._logger.debug("insert_schedule(%s)", str(schedule.dict()))

        cursor = self._db_conn.cursor()

        sql = """
            INSERT INTO {}(username, time, description, is_actives, to_cautious_mode, is_enabled)
            VALUES (%s, %s, %s, %s, %s, %s)
        """.format(TableName.Schedule.value)

        param = (
            schedule.username,
            schedule.time,
            schedule.description,
            schedule.is_actives,
            schedule.to_cautious_mode,
            schedule.is_enabled
        )

        try:
            cursor.execute(sql, param)
        except mysql.connector.IntegrityError:
            return False

        self._db_conn.commit()
        return True

    def delete_schedule(self, schedule: ScheduleSchema) -> None:
        self._logger.debug("delete_schedule(%s)", str(schedule.dict()))

        cursor = self._db_conn.cursor()

        sql = """
            DELETE FROM {}
            WHERE username = %s AND time = %s AND is_actives = %s
        """.format(TableName.Schedule.value)

        param = (
            schedule.username,
            schedule.time,
            schedule.is_actives,
        )

        cursor.execute(sql, param)

        self._db_conn.commit()

    @classmethod
    def get_instance(cls):
        if cls.__instance is None:
            cls.__instance = DbAccessor()
        return cls.__instance
