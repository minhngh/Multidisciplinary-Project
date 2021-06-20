from pydantic import BaseModel, Field
import datetime

from abc import ABC

from typing import List


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


class Schedule(BaseModel):
    time: str = Field(...)
    description: str = Field(...)
    isActives: List[bool] = Field(...)
    toCautiousMode: bool = Field(...)
    isEnabled: bool = Field(...)


class ScheduleChange(ABC, BaseModel):
    pass


class ScheduleInsert(ScheduleChange):
    schedule: Schedule = Field(...)


class ScheduleDelete(ScheduleChange):
    schedule: Schedule = Field(...)


class ScheduleDeleteAll(ScheduleChange):
    pass


class ScheduleChangeLog(BaseModel):
    version: int = Field(...)
    scheduleChanges: List[ScheduleChange] = Field(...)


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
