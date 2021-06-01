debug = True

import os
import sys
import time
import glob
from datetime import datetime, date
import base64
import json


#Global variable to control system's mode
mode = 'NORMAL' #NORMAL/CAUTION
owner = 'LeLong'
path = os.path.abspath(__file__)
IMG_DIR = os.path.join(path.rsplit(os.path.sep, 3)[0], 'ESP32_CAM/images')
LOG_DIR = os.path.join(path.rsplit(os.path.sep, 1)[0], 'log')

#mkdir if not exits
try:
    os.makedirs(IMG_DIR)
    os.makedirs(LOG_DIR)
except:
    pass

from Get_Image_Firebase.main import listen
from firebase_admin import messaging
from recognizer import FaceRecognizer
face_recognizer = FaceRecognizer()

from fcm import notify


import threading
class CautionThread(threading.Thread):
    def __init__(self, mqtt, sleep_interval=1):
        super().__init__()
        self._kill = threading.Event()
        self._interval = sleep_interval
        self.mqtt = mqtt
    
    def run(self):
        global mode
        mode = 'CAUTION'
        print('TURN ON CAUTION MODE')
        while mode == 'CAUTION':
            time.sleep(5)
            # print('Do something...')
            if debug or self.mqtt.receive_door_state(): #when door open
                pre_number_of_img = len(glob.glob(os.path.join(IMG_DIR,'*.*')))
                listen()
                post_number_of_img = len(glob.glob(os.path.join(IMG_DIR,'*.*')))
                if pre_number_of_img != post_number_of_img:
                    img_name = os.path.join(IMG_DIR,'img_{}.jpg'.format(post_number_of_img-1))
                    if debug or (face_recognizer.recognize(img_name) != owner):
                        notify("Unknown")
                        self.mqtt.send__speaker_data(1001)
                        write_log(LOG_DIR,img_name,"unknown")
                    else: 
                        notify(owner)
                        log(LOG_PATH,img_name,owner)

            is_killed = self._kill.wait(self._interval) 
            if is_killed: 
                self._kill.clear()
                mode = 'NORMAL'
                break


    def kill(self):
        global mode
        mode = 'NORMAL'
        self._kill.set()
        print('TURN OFF CAUTION MODE')




def get_mode():
    return mode

def write_log(logdir, img, label):
    now = datetime.now()

    with open(img, "rb") as img_file:
        encoded = base64.b64encode(img_file.read())
    
    new_log = {"image": encoded.decode("utf-8"), "type":label, "time": now.strftime("%H:%M:%S, %d/%m/%Y")}

    #mkdir if not exist
    try: os.makedirs(os.path.join(logdir,now.strftime("%d-%m-%Y")))
    except: pass

    logfile = now.strftime("%d-%m-%Y/%H:%M:%S.json")
    logfile = os.path.join(logdir,logfile)
    with open(logfile, "w") as file:
        json.dump(new_log, file)

def read_log(start_time, end_time):
    #TODO: get log from start_time to end_time
    result = [] 
    for logfile in glob.glob(os.path.join(LOG_DIR,"*/*.json")):
        with open(logfile,"r") as f:
            result.append(json.load(f))
    return result