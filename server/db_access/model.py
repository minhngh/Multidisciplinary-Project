from pydantic import BaseModel, Field
import datetime

from abc import ABC

from typing import List

N_DAY_OF_THE_WEEK = 7
class UserSchema(BaseModel):
    username: str = Field(max_length=128)
    password: str = Field(max_length=128)


class ScheduleSchema(BaseModel):
    username: str = Field(max_length=128)
    time: datetime.time = Field(...)
    description: str = Field(max_length=128)
    is_actives: int = Field(ge=0, lt=2 ** N_DAY_OF_THE_WEEK)
    to_cautious_mode: bool = Field(...)
    is_enabled: bool = Field(...)


class LogSchema(BaseModel):
    id: int = Field(...)
    timestamp: datetime.datetime = Field(...)
    image: str = Field(max_length=128)
    type: str = Field(max_length=128)
