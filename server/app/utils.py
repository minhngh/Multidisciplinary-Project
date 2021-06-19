debug = True

import os
import sys
import time
import glob
from datetime import datetime, date
import base64
import json

import mysql.connector
import yaml

path = os.path.abspath(__file__)
with open(os.path.join(path.rsplit(os.path.sep, 2)[0], 'config.yml'), 'r') as f:
    config = yaml.load(f, Loader = yaml.FullLoader)

my_db = mysql.connector.connect(
    host = config['host'],
    user = config['user'],
    password = config['password'],
    database = config['database']
)

#Global variable to control system's mode
mode = 'NORMAL' #NORMAL/CAUTION
owner = 'LeLong'
path = os.path.abspath(__file__)
IMG_DIR = os.path.join(path.rsplit(os.path.sep, 3)[0], 'ESP32_CAM/images')

#mkdir if not exits
try:
    os.makedirs(IMG_DIR)
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
                        # notify("Unknown")
                        self.mqtt.send__speaker_data(1001)
                        write_log(img_name,"unknown")
                    else: 
                        # notify(owner)
                        write_log(img_name,owner)

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

def write_log(img, label):
    now = datetime.now()
    timestamp = now.strftime("%Y-%m-%d %H:%M:%S")
    image = img.split('/')[-1]
    type = label

    my_cursor = my_db.cursor()

    sql = "INSERT INTO Log(timestamp,image,type) VALUES(%s,%s,%s)"
    param = (timestamp,image,type)

    try:
        my_cursor.execute(sql, param)
        my_db.commit()
    except Exception as e:
        raise e
    

def read_log(start_time, end_time):
    sql = None
    if (start_time == '-1') and (end_time == '-1'): sql = "SELECT * FROM Log"
    elif (start_time != '-1') and (end_time != '-1'): 
        start = '-'.join(start_time.split('/')[::-1]) #convert from dd/mm/yyyy to yyyy-mm-dd
        end = '-'.join(end_time.split('/')[::-1])
        if start != end:
            sql = f"SELECT * FROM Log WHERE timestamp > '{start}' AND timestamp < '{end}'"
        else:
            sql = f"SELECT * FROM Log WHERE DATE(timestamp) = '{start}'"
    print(sql)
    my_cursor = my_db.cursor()
    try:
        result = []
        my_cursor.execute(sql)
        mydb_response = my_cursor.fetchall()
        for item in mydb_response:
            timestamp, image, type, id = item
            with open(os.path.join(IMG_DIR,image), "rb") as img_file:
                encoded = base64.b64encode(img_file.read())
            new_log = {"id":id, "image": encoded.decode("utf-8"), "type":type, "time": timestamp.strftime("%H:%M:%S, %d/%m/%Y")}
            result.append(new_log)
        return result
    except Exception as e:
        raise e

    #{"log":[{'image':'...', 'type':'...', 'time':'...}, {}, {}]}

def db_remove_log(id):
    sql = None
    if (id == "-1"): sql = "DELETE FROM Log"
    else: sql = f"DELETE FROM Log WHERE id = {id}"

    my_cursor = my_db.cursor()
    try:
        my_cursor.execute(sql)
        my_db.commit()
        return True
    except Exception as e:
        raise e