from mqtt import MQTT

from Get_Image_Firebase.main import listen
from firebase_admin import messaging
from recognizer import FaceRecognizer

from fcm import notify

from app.model import *

from db_access import DbAccessor
from db_access.model import *

from config_access import ConfigAccessor

from util import get_root_path, N_DAY_OF_THE_WEEK, TIME_FORMAT

import base64

import logging

from functools import reduce
from operator import or_
import glob
import os
import schedule
import threading
import time

from datetime import datetime
from typing import Any, Dict, List, Optional


__logger = logging.getLogger('api_helper')


class CautionThread(threading.Thread):
    def __init__(self, mqtt, sleep_interval=1):
        super().__init__()
        self._kill = threading.Event()
        self._interval = sleep_interval
        self.mqtt = mqtt

    def run(self):
        global mode

        mode = 'CAUTION'
        logging.info('TURN ON CAUTION MODE')

        count = 0
        while mode == 'CAUTION':
            time.sleep(5)

            if count == 1:
                break

            if debug or self.mqtt.receive_door_state():  # when door opens
                pre_number_of_img = len(glob.glob(os.path.join(IMG_DIR, '*.*')))
                listen()
                post_number_of_img = len(glob.glob(os.path.join(IMG_DIR, '*.*')))

                if pre_number_of_img != post_number_of_img:
                    img_name = os.path.join(IMG_DIR, 'img_{}.jpg'.format(post_number_of_img - 1))

                    if debug or (face_recognizer.recognize(img_name) != owner):
                        notify("Unknown")
                        self.mqtt.send__speaker_data(1001)
                        write_log(img_name, "unknown")
                    else:
                        notify(owner)
                        write_log(img_name, owner)
                    count += 1

            is_killed = self._kill.wait(self._interval)
            if is_killed:
                self._kill.clear()
                mode = 'NORMAL'
                break

    def kill(self):
        global mode
        mode = 'NORMAL'
        self._kill.set()
        logging.info('TURN OFF CAUTION MODE')


# Config and data accessor
config_accessor = ConfigAccessor.get_instance()
db_accessor = DbAccessor.get_instance()

# Debug mode
debug = config_accessor.log_mode == 'DEBUG'

# Global variable to control system's mode
mode = 'NORMAL'  # NORMAL/CAUTION
owner = 'LeLong'
relative_path = 'ESP32_CAM/images'
IMG_DIR = os.path.join(get_root_path(), relative_path)

try:
    os.makedirs(IMG_DIR)
except OSError:
    pass

# Face recognizer
face_recognizer = FaceRecognizer()

# MQTT
global_mqtt = MQTT()
caution_thread: Optional[CautionThread] = None


def get_mode():
    return mode


def turn_on_caution_thread():
    global caution_thread
    caution_thread = CautionThread(mqtt=global_mqtt)
    caution_thread.start()


def turn_off_caution_thread():
    if caution_thread is not None:
        caution_thread.kill()


def write_log(img, label):
    log = LogSchema(
        timestamp=datetime.now(),
        image=img.split('/')[-1],
        type=label
    )

    logging.debug("Try to insert log")
    if not db_accessor.insert_log(log):
        logging.warning("Failed to insert into Log")
    

def read_log(start_time, end_time) -> List[Dict[str, Any]]:
    if start_time == '-1':
        start_time = None

    if end_time == '-1':
        end_time = None

    result = []
    print('dasdasd', start_time, end_time)
    for log in db_accessor.get_logs_in_interval(start_time, end_time):
        with open(os.path.join(IMG_DIR, log.image), 'rb') as img_file:
            encoded = base64.b64encode(img_file.read())
        new_log = {
            'id': log.id,
            'time': log.timestamp.strftime("%H:%M:%S, %d/%m/%Y"),
            'image': encoded.decode("utf-8"),
            'type': log.type,
        }
        result.append(new_log)

    return result


def db_remove_log(_id):
    if _id == '-1':
        _id = None

    db_accessor.delete_log(_id)


# Accounts
def is_user_exists(user: UserLogin) -> bool:
    return db_accessor.is_user_exist(UserSignup(
        username=user.username,
        password=user.password
    ))


def insert_user(user: UserSignup) -> bool:
    return db_accessor.insert_user(UserSignup(
        username=user.username,
        password=user.password
    ))


# Schedule
def toScheduleSchema(username: str, schedule_: Schedule):
    return ScheduleSchema(
        username=username,
        time=datetime.datetime.strptime(schedule_.time, TIME_FORMAT).time(),
        description=schedule_.description,
        is_actives=reduce(or_, (isActive << i for i, isActive in enumerate(schedule_.isActives)), 0),
        to_cautious_mode=schedule_.toCautiousMode,
        is_enabled=schedule_.isEnabled
    )


def toSchedule(schedule_schema: ScheduleSchema):
    return Schedule(
        time=str(schedule_schema.time),
        description=schedule_schema.description,
        is_actives=[bool(schedule_schema.is_actives & (1 << i)) for i in range(N_DAY_OF_THE_WEEK)],
        to_cautious_mode=schedule_schema.to_cautious_mode,
        is_enabled=schedule_schema.is_enabled,
    )


def register_mode_change_task(schedule_schema: ScheduleSchema):
    name_text = schedule_schema.username
    time_text = schedule_schema.time.strftime(TIME_FORMAT)
    is_actives_text = str(schedule_schema.is_actives)
    if schedule_schema.to_cautious_mode:
        task = turn_on_caution_thread
        mode_text = 'cautious'
    else:
        task = turn_off_caution_thread
        mode_text = 'normal'

    name_tag = 'name:' + name_text
    time_tag = 'time:' + time_text
    key_tag = 'key:' + ",".join([name_text, time_text, is_actives_text])
    mode_tag = 'mode:' + mode_text

    for i, job in enumerate([
        schedule.every().monday,
        schedule.every().tuesday,
        schedule.every().wednesday,
        schedule.every().thursday,
        schedule.every().friday,
        schedule.every().saturday,
        schedule.every().sunday,
    ]):
        if schedule_schema.is_actives & (1 << i):
            day_id_text = str(i)
            day_tag = 'day:' + day_id_text
            job.do(task, name_tag).tag(key_tag, name_tag, mode_tag, time_tag, day_tag)

            job.do(lambda: print(key_tag)).tag(key_tag, name_tag, mode_tag, time_tag, day_tag)


def unregister_mode_change_task(schedule_schema: ScheduleSchema):
    name_text = schedule_schema.username
    time_text = schedule_schema.time.strftime(TIME_FORMAT)
    is_actives_text = str(schedule_schema.is_actives)

    key_tag = 'key:' + ",".join([name_text, time_text, is_actives_text])

    schedule.clear(key_tag)


def unregister_user_mode_change_tasks(username: str):
    schedule.clear(username)


def get_all_schedules(username: str) -> List[Schedule]:
    return [toSchedule(schedule_schema) for schedule_schema in db_accessor.get_schedules(username)]


def get_schedule(username: str, schedule_schema: Schedule) -> Optional[Schedule]:
    schedule_schema = db_accessor.get_schedule(
        username,
        schedule_schema.time,
        reduce(or_, (isActive << i for i, isActive in enumerate(schedule_schema.isActives)), 0)
    )

    if schedule_schema is None:
        return None

    return toSchedule(schedule_schema)


def delete_schedule(username: str, schedule_: Schedule) -> None:
    schedule_schema = toScheduleSchema(username, schedule_)

    db_accessor.delete_schedule(schedule_schema)
    unregister_mode_change_task(schedule_schema)


def insert_schedule(username: str, schedule_: Schedule) -> bool:
    schedule_schema = toScheduleSchema(username, schedule_)

    is_successful = db_accessor.insert_schedule(schedule_schema)

    if is_successful:
        register_mode_change_task(schedule_schema)

    return is_successful


def register_all_mode_change_tasks():
    schedule_schemas = db_accessor.get_all_schedules()

    for schedule_schema in schedule_schemas:
        register_mode_change_task(schedule_schema)
