from Get_Image_Firebase.main import listen
from firebase_admin import messaging
from recognizer import FaceRecognizer

from fcm import notify

from app.model import *

from db_access import DbAccessor
from db_access.model import *

from config_access import ConfigAccessor

from util import get_root_path

import base64

import logging

from functools import reduce
from operator import or_
import glob
import os
import threading
import time

from datetime import datetime
from typing import Any, Dict, List, Optional


# Config and data accessor
config_accessor = ConfigAccessor.get_instance()
db_accessor = DbAccessor.get_instance()

# Debug mode
debug = config_accessor.log_mode == 'DEBUG'

# Global variable to control system's mode
mode = 'NORMAL'  # NORMAL/CAUTION
owner = 'LeLong'
relative_path = '../ESP32_CAM/images'
IMG_DIR = os.path.join(get_root_path(), relative_path)

try:
    os.makedirs(IMG_DIR)
except:
    pass

# Face recognizer
face_recognizer = FaceRecognizer()


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
                try:
                    listen()
                except:
                    continue
                post_number_of_img = len(glob.glob(os.path.join(IMG_DIR, '*.*')))

                if pre_number_of_img != post_number_of_img:
                    img_name = os.path.join(IMG_DIR, 'img_{}.jpg'.format(post_number_of_img-1))

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


def get_mode():
    return mode


def write_log(img, label):
    log = LogSchema(
        id = 0, #db will generate this automatic
        timestamp=datetime.now(),
        image=img.split('/')[-1],
        type=label
    )

    logging.debug("Try to insert log")
    if not db_accessor.insert_log(log):
        logging.warning("Failed to insert into Log")
    

def read_log(start_time, end_time) -> List[Dict[str, Any]]:
    dbresponse = []
    if start_time == '-1' and end_time == "-1":
        dbresponse = db_accessor.get_all_logs()
    else: 
        try:
            start_time = datetime.strptime(start_time,"%d/%m/%Y")
            end_time = datetime.strptime(end_time,"%d/%m/%Y")
        except:
            return []
        dbresponse = db_accessor.get_logs_in_interval(start_time, end_time)

    result = []
    for log in dbresponse:
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
def get_all_schedules(username: str) -> List[Schedule]:
    return [
        Schedule(
            time=str(schedule.time),
            description=schedule.description,
            isActives=[bool(schedule.is_actives & 2**i) for i in range(7)],
            toCautiousMode=schedule.to_cautious_mode,
            isEnabled=schedule.is_enabled,
        )
        for schedule in db_accessor.get_schedules(username)
    ]


def get_schedule(username: str, schedule: Schedule) -> Optional[Schedule]:
    schedule = db_accessor.get_schedule(
        username,
        schedule.time,
        reduce(or_, (isActive << i for i, isActive in enumerate(schedule.isActives)), 0)
    )

    if schedule is None:
        return None

    return Schedule(
        time=str(schedule.time),
        description=schedule.description,
        isActives=[bool(schedule.is_actives & 2**i) for i in range(7)],
        toCautious_mode=schedule.to_cautious_mode,
        isEnabled=schedule.is_enabled,
    )



def delete_schedule(username: str, schedule: Schedule) -> None:
    format_time = "%H:%M:%S"

    db_accessor.delete_schedule(ScheduleSchema(
        username=username,
        time=datetime.datetime.strptime(schedule.time, format_time).time(),
        description=schedule.description,
        isActives=reduce(or_, (isActive << i for i, isActive in enumerate(schedule.isActives)), 0),
        toCautious_mode=schedule.toCautiousMode,
        isEnabled=schedule.isEnabled
    ))


def insert_schedule(username: str, schedule: Schedule) -> bool:
    format_time = "%H:%M:%S"

    return db_accessor.insert_schedule(ScheduleSchema(
        username=username,
        time=datetime.datetime.strptime(schedule.time, format_time).time(),
        description=schedule.description,
        is_actives=reduce(or_, (isActive << i for i, isActive in enumerate(schedule.isActives)), 0),
        to_cautious_mode=schedule.toCautiousMode,
        is_enabled=schedule.isEnabled
    ))
