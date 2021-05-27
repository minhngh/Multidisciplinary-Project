#!pip install firebase_admin
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import time
import logging
import urllib.request


class FirebaseUtility:
    def __init__(self, json_path, db_url):
        self.json_path = json_path
        self.db_url = db_url

        self.last_string = ""

    def get_last_string(self):
        return self.last_string

    def set_last_string(self, last_string):
        self.last_string = last_string

    @staticmethod
    def connectFirebase():
        # Fetch the service account key JSON file contents
        cred = credentials.Certificate(json_path)

        # Initialize the app with a service account, granting admin privileges
        firebase_admin.initialize_app(cred, {
            'databaseURL': db_url
        })

    def sendRequireSignal(self, flag=True):
        ref = db.reference("/require")
        if ref == None:
            logging.error("Connection fail, reconnect!");
            return False
        # Successful
        if flag == True:
            ref.set(1)
        else:
            ref.set(0)
        return True

    def getString64FromFirebase(self):
        ref = db.reference("/require")
        if ref == None:
            logging.error("Connection fail, reconnect!");
            return None, None

        if ref.get() == 1:
            # ref = db.reference("/Image")

            dictVal = db.reference("/Image").order_by_key().limit_to_last(1).get()
            return list(dictVal.keys()), list(dictVal.values())


if __name__ == '__main__':

    json_path = 'esp32-camera-66eeb-firebase-adminsdk-63n28-c778ee7859.json'
    db_url = 'https://esp32-camera-66eeb-default-rtdb.firebaseio.com'

    fb = FirebaseUtility(json_path, db_url)
    fb.connectFirebase()
    # flag = True
    isRunning = False
    while True:
        fb.sendRequireSignal()
        time.sleep(5)
        key, val = fb.getString64FromFirebase()

        last_str = fb.get_last_string()
        if last_str == "":
            fb.set_last_string(val[0])
            # print(fb.get_last_string())
            # Convert to JPG and save
            urllib.request.urlretrieve(val[0], key[0][1:] + ".jpg")

        elif last_str != "":
            if last_str != val[0]:
                fb.set_last_string(val[0])
                # print(fb.get_last_string())
                # Convert to JPG and save
                urllib.request.urlretrieve(val[0], key[0][1:] + ".jpg")
            else:
                # Set flag to 0, stop the program, update later.
                pass

        fb.sendRequireSignal(flag=False)
        break



