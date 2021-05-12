from mqtt import MQTT
mqtt = MQTT()

from app.model import *
from app.auth.auth_bearer import JWTBearer
from app.auth.auth_handler import sign_jwt

from fastapi import Body, Depends, FastAPI
from fastapi import  HTTPException

import mysql.connector
import os
import yaml

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

    mqtt_output = mqtt.receive_door_state()
    state = "open" if mqtt_output else "open"

    return {"door_state":state}

@app.post("/mute")
async def mute(user: Mute = Body(...)):
    #TODO: check token

    mqtt.send__speaker_data(0)
    return {"mute":"successful"}

@app.post("/unmute")
async def unmute(user: Unmute = Body(...)):
    #TODO: check token

    mqtt.send__speaker_data(1000)
    return {"unmute":"successful"}
