from mqtt import MQTT
mqtt = MQTT()

from app.model import *
from app.auth.auth_bearer import JWTBearer
from app.auth.auth_handler import sign_jwt, decode_jwt

from fastapi import Body, Header, Depends, FastAPI
from fastapi import  HTTPException

import mysql.connector
import os
import sys

from datetime import datetime

import yaml

from app.utils import *
from threading import Thread

path = os.path.abspath(__file__)
with open(os.path.join(path.rsplit(os.path.sep, 2)[0], 'config.yml'), 'r') as f:
    config = yaml.load(f, Loader = yaml.FullLoader)

app = FastAPI()

my_db = mysql.connector.connect(
    host = config['host'],
    user = config['user'],
    password = config['password'],
    database = config['database']
)

# turn_on_thread = KillableThread(target=turn_on,args=[turn_on_thread,mqtt])
caution_thread = None


# helpers

def check_user(data: UserLoginSchema):
    my_cursor = my_db.cursor()

    sql = "SELECT username FROM Accounts WHERE username = %s AND password = %s"
    param = (data.username, data.password)

    my_cursor.execute(sql, param)
    my_result = my_cursor.fetchone()

    return my_result is not None


# route handlers

@app.get("/", tags=["root"])
async def read_root() -> dict:
    return {"message": "Welcome!."}


@app.post("/", dependencies=[Depends(JWTBearer())], tags=["root"])
async def add_sth(sth: str) -> dict:
    return {
        "data": sth
    }


@app.post("/user/signup", tags=["user"])
async def create_user(user: UserSchema = Body(...)):
    my_cursor = my_db.cursor()

    sql = "INSERT INTO Accounts VALUES (%s, %s)"
    param = (user.username, user.password)

    my_cursor.execute(sql, param)
    my_cursor.commit()

    return sign_jwt(user.username)


@app.post("/user/login", tags=["user"])
async def user_login(user: UserLoginSchema = Body(...)):
    if check_user(user):
        return sign_jwt(user.username)
    raise HTTPException(status_code=401, detail="Wrong username/password.")

@app.post("/check-door")
async def check_door(user: CheckDoor = Body(...)):
    #TODO: check token
    now = datetime.now()
    mqtt_output = mqtt.receive_door_state()
    state = "open" if mqtt_output else "close"

    return {"door_state":state, "time": now.strftime("%H:%M:%S, %d/%m/%Y")}

@app.post("/check-mode")
async def check_door(user: CheckMode = Body(...)):
    #TODO: check token
    now = datetime.now()
    mode = get_mode().lower()

    return {"mode":mode, "time": now.strftime("%H:%M:%S, %d/%m/%Y")}

@app.post("/mute")
async def mute(user: Mute = Body(...)):
    #TODO: check token

    mqtt.send__speaker_data(0)
    return {"mute":"successful"}

@app.post("/unmute")
async def unmute(user: Unmute = Body(...)):
    #TODO: check token

    mqtt.send__speaker_data(1000)
    return {"unmute": "successful"}


@app.post("/get-schedule-change", dependencies=[Depends(JWTBearer())], tags=['schedule'])
async def get_schedule_log(authorization: str = Header(None), version: int = Body(...)) -> List[Schedule]:
    username = decode_jwt(authorization[len("Bearer "):])['username']

    db_cursor = my_db.cursor()

    sql = "SELECT * FROM Schedule WHERE username = %s"
    param = (username,)

    db_cursor.execute(sql, param)
    schedules = db_cursor.fetchall()

    return [
        Schedule(
            time=str((datetime.min + schedule[1]).time())[:-len(":HH")],
            description=schedule[2],
            isActives=[bool(int(schedule[3]) & 2**i) for i in range(7)],
            toCautiousMode=bool(schedule[4]),
            isEnabled=bool(schedule[5]),
        )
        for schedule in schedules
    ]


@app.post("/change-schedule", dependencies=[Depends(JWTBearer())], tags=['schedule'])
async def change_schedule(change_log: ScheduleChangeLog) -> int:
    return -1


@app.post("/turn-on-caution")
async def caution(user: Caution = Body(...)):
    #TODO: check token
    if get_mode() == 'CAUTION':
        return {"turn-on-caution":"Mode is already CAUTION, do nothing"}
    
    global caution_thread
    caution_thread = CautionThread(mqtt=mqtt)
    caution_thread.start()
    return {"turn-on-caution":"successful"}


@app.post("/turn-off-caution")
async def normal(user: Normal = Body(...)):
    #TODO: check token
    if caution_thread is not None:
        caution_thread.kill()
    return {"turn-off-caution":"successful"}

@app.post("/get-log")
async def get_log(user: GetLog = Body(...)):
    #TODO: check token
    log = read_log(user.start_time,user.end_time)
    return {"log": str(log)}

@app.post("/remove-log")
async def remove_log(user: RemoveLog = Body(...)):
    #TODO: check token
    id = user.id
    try:
        db_remove_log(id)
        return {"remove": "successful"}
    except Exception as e:
        return {"remove": str(e)}
