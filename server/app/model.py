from pydantic import BaseModel, Field
import datetime

from abc import ABC

from typing import List


class UserSignup(BaseModel):
    username: str = Field(...)
    password: str = Field(...)


class UserLogin(BaseModel):
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
