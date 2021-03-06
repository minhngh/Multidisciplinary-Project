from mqtt import MQTT
mqtt = MQTT()

from app.model import *
from app.auth.auth_bearer import JWTBearer
from app.auth.auth_handler import sign_jwt, decode_jwt

from fastapi import Body, Header, Depends, FastAPI
from fastapi import HTTPException

from datetime import datetime

from .api_helper import *

app = FastAPI()

# turn_on_thread = KillableThread(target=turn_on,args=[turn_on_thread,mqtt])
caution_thread = None


# route handlers
@app.post("/user/signup", tags=["user"])
async def create_user(user: UserSignup = Body(...)):
    insert_user(user)

    return sign_jwt(user.username)


@app.post("/user/login", tags=["user"])
async def user_login(user: UserLogin = Body(...)):
    if is_user_exists(user):
        return sign_jwt(user.username)
    raise HTTPException(status_code=401, detail="Wrong username/password.")


@app.post("/check-door")
async def check_door(user: CheckDoor = Body(...)):
    #TODO: check token
    now = datetime.now()
    mqtt_output = mqtt.receive_door_state()
    state = 'open' if mqtt_output else 'close'

    return {
        'door_state': state,
        'time': now.strftime("%H:%M:%S, %d/%m/%Y")
    }


@app.post("/check-mode")
async def check_door(user: CheckMode = Body(...)):
    #TODO: check token
    now = datetime.now()
    mode = get_mode().lower()

    return {
        'mode': mode,
        'time': now.strftime("%H:%M:%S, %d/%m/%Y")
    }


@app.post("/mute")
async def mute(user: Mute = Body(...)):
    #TODO: check token
    mqtt.send__speaker_data(0)
    return {
        'mute': 'successful'
    }


@app.post("/unmute")
async def unmute(user: Unmute = Body(...)):
    #TODO: check token
    mqtt.send__speaker_data(1000)
    return {
        'unmute': 'successful'
    }


@app.post("/get-schedule-change", dependencies=[Depends(JWTBearer())], tags=['schedule'])
async def get_schedules(authorization: str = Header(None), version: int = Body(...)) -> List[Schedule]:
    username = decode_jwt(authorization[len("Bearer "):])['username']
    return get_all_schedules(username)


@app.post("/remove-schedule", dependencies=[Depends(JWTBearer())], tags=['schedule'])
async def remove_schedule(
        authorization: str = Header(None), version: int = Body(...), schedule: Schedule = Body(...)
):
    username = decode_jwt(authorization[len("Bearer "):])['username']
    delete_schedule(username, schedule)


@app.post("/add-schedule", dependencies=[Depends(JWTBearer())], tags=['schedule'])
async def add_schedule(
        authorization: str = Header(None), version: int = Body(...), schedule: Schedule = Body(...)
) -> bool:
    username = decode_jwt(authorization[len("Bearer "):])['username']
    return insert_schedule(username, schedule)


@app.post("/update-schedule", dependencies=[Depends(JWTBearer())], tags=['schedule'])
async def update_schedule(
        authorization: str = Header(None), version: int = Body(...),
        schedule_remove: Schedule = Body(...), schedule_add: Schedule = Body(...),
) -> bool:
    username = decode_jwt(authorization[len("Bearer "):])['username']
    schedule_remove = get_schedule(username, schedule_remove)
    if schedule_remove is None:
        return False

    delete_schedule(username, schedule_remove)

    if not insert_schedule(username, schedule_add):
        insert_schedule(username, schedule_remove)
        return False

    return True


@app.post("/turn-on-caution")
async def caution(user: Caution = Body(...)):
    #TODO: check token
    if get_mode() == 'CAUTION':
        return {"turn-on-caution":"Mode is already CAUTION, do nothing"}
    
    global caution_thread
    caution_thread = CautionThread(mqtt=mqtt)
    caution_thread.start()
    return {"turn-on-caution":'successful'}


@app.post("/turn-off-caution")
async def normal(user: Normal = Body(...)):
    #TODO: check token
    if caution_thread is not None:
        caution_thread.kill()
    return {"turn-off-caution":'successful'}


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
        return {"remove": 'successful'}
    except Exception as e:
        return {"remove": str(e)}
