from pydantic import BaseModel, Field


class UserSchema(BaseModel):
    username: str = Field(...)
    password: str = Field(...)


class UserLoginSchema(BaseModel):
    username: str = Field(...)
    password: str = Field(...)

class CheckDoor(BaseModel):
    access_token: str = Field(...)

class CheckMode(BaseModel):
    access_token: str = Field(...)

class Mute(BaseModel):
    access_token: str = Field(...)

class Unmute(BaseModel):
    access_token: str = Field(...)

class Caution(BaseModel):
    access_token: str = Field(...)
class Normal(BaseModel):
    access_token: str = Field(...)

class GetLog(BaseModel):
    access_token: str = Field(...)
    start_time: str = Field(...)
    end_time: str = Field(...)

class RemoveLog(BaseModel):
    access_token: str = Field(...)
    id: str = Field(...)