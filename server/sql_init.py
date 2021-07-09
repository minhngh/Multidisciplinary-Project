from db_access import TableName

from config_access import ConfigAccessor

import mysql.connector
from mysql.connector import MySQLConnection, CMySQLConnection

from typing import Union


MySQLConnectionType = Union[MySQLConnection, CMySQLConnection]


def _drop_database(conn: MySQLConnectionType, name_database: str):
    cursor = conn.cursor()

    sql = """
        DROP DATABASE IF EXISTS {}
    """.format(name_database)
    cursor.execute(sql)


def _create_database(conn: MySQLConnectionType, name_database: str, to_drop: bool = False):
    if to_drop:
        _drop_database(conn, name_database)

    cursor = conn.cursor()

    sql = """
        CREATE DATABASE {}
    """.format(name_database)
    cursor.execute(sql)


def _create_user_table(conn):
    cursor = conn.cursor()

    sql = """
        CREATE TABLE {}
        (
            username VARCHAR(128),
            password VARCHAR(128) NOT NULL,
            CONSTRAINT account_pk
                PRIMARY KEY(username)
        )
    """.format(TableName.User.value)
    cursor.execute(sql)

    sql = """
        INSERT INTO {}(username, password)
        VALUES (%s, %s)
    """.format(TableName.User.value)
    params = [
        ('cuong', '1'),
        ('admin', 'admin'),
        ('user', 'pass')
    ]
    cursor.executemany(sql, params)
    conn.commit()


def _create_log_table(conn):
    cursor = conn.cursor()

    sql = """
        CREATE TABLE {}
        (
            id INT AUTO_INCREMENT,
            timestamp DATETIME NOT NULL,
            image VARCHAR(128) NOT NULL,
            type VARCHAR(128) NOT NULL,
            CONSTRAINT Log_pk
                PRIMARY KEY(id)
        )
    """.format(TableName.Log.value)
    cursor.execute(sql)

    sql = """
        INSERT INTO {}(timestamp, image, type)
        VALUES (%s, %s, %s)
    """.format(TableName.Log.value)
    params = [
        ('2021-06-01 01:22:23', 'img_0.jpg', 'unknown'),
        ('2021-06-12 10:40:31', 'img_1.jpg', 'unknown')
    ]
    cursor.executemany(sql, params)
    conn.commit()


def _create_schedule_table(conn):
    cursor = conn.cursor()


    sql = """
        CREATE TABLE {}
        (
            username VARCHAR(128) NOT NULL,
            time TIME NOT NULL ,
            description VARCHAR(128) NOT NULL,
            is_actives INT NOT NULL,
            to_cautious_mode BOOLEAN NOT NULL,
            is_enabled BOOLEAN NOT NULL,
            CONSTRAINT Schedule_pk
                PRIMARY KEY(username, time, is_actives),
            CONSTRAINT Schedule_Accounts_username_fk
                FOREIGN KEY(username) REFERENCES {}(username)
                    ON UPDATE CASCADE ON DELETE CASCADE
        )
    """.format(TableName.Schedule.value, TableName.User.value)
    cursor.execute(sql)

    sql = """
        INSERT INTO {}(username, time, description, is_actives, to_cautious_mode, is_enabled)
        VALUES (%s, %s, %s, %s, %s, %s)
    """.format(TableName.Schedule.value)
    params = [
        ('admin', '07:30:00', 'Off to work', 5, True, True),
        ('admin', '16:30:00', 'Home', 127, False, False)
    ]
    cursor.executemany(sql, params)
    conn.commit()


def _create_tables(conn):
    _create_user_table(conn)
    _create_log_table(conn)
    _create_schedule_table(conn)


if __name__ == "__main__":
    config_accessor = ConfigAccessor.get_instance()

    _create_database(
        conn=mysql.connector.connect(
            host=config_accessor.host_database,
            user=config_accessor.username_database,
            password=config_accessor.password_database
        ),
        name_database=config_accessor.name_database,
        to_drop=True
    )

    _create_tables(
        conn=mysql.connector.connect(
            host=config_accessor.host_database,
            user=config_accessor.username_database,
            password=config_accessor.password_database,
            database=config_accessor.name_database
        )
    )
