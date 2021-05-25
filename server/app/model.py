from pydantic import BaseModel, Field


class UserSchema(BaseModel):
    username: str = Field(...)
    password: str = Field(...)


class UserLoginSchema(BaseModel):
    username: str = Field(...)
    password: str = Field(...)
<<<<<<< HEAD
=======

class CheckDoor(BaseModel):
    access_token: str = Field(...)

class Mute(BaseModel):
    access_token: str = Field(...)

class Unmute(BaseModel):
    access_token: str = Field(...)
>>>>>>> feature/mqtt_services
