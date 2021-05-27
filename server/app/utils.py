debug = True

import os
import sys
import time
import glob

#Global variable to control system's mode
mode = 'NORMAL' #NORMAL/CAUTION
owner = 'LeLong'
path = os.path.abspath(__file__)
IMG_DIR = os.path.join(path.rsplit(os.path.sep, 3)[0], 'ESP32_CAM/images')
from Get_Image_Firebase.main import listen
from firebase_admin import messaging
from recognizer import FaceRecognizer
face_recognizer = FaceRecognizer()


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
                    print(img_name)
                    if debug or (face_recognizer.recognize(img_name) != owner):
                        #TODO: notify, turn-on speaker
                        # mess = messaging.AndroidNotification(title='Security Camera',body='Phát hiện người lạ!!!')
                        # mess.send()
                        self.mqtt.send__speaker_data(1001)
                    else: print("owner")

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