from util import get_root_path

import decouple
import dotenv
import yaml

import os

from typing import Any, Dict


class ConfigAccessor:
    __instance: 'ConfigAccessor' = None

    _dotenv_file: str = dotenv.find_dotenv()

    _yaml_file: str = 'config.yml'
    _yaml_dict: Dict[str, Any]

    _log_mode: str = 'log_mode'

    _key_to_encrypt: str = 'key_encrypt'
    _algorithm_to_encrypt: str = 'algorithm_encrypt'

    _host_database: str = 'db_host'
    _username_database: str = 'db_username'
    _password_database: str = 'db_password'
    _name_database: str = 'db_name'

    def __init__(self):
        dotenv.load_dotenv(self._dotenv_file)

        self._yaml_file = os.path.join(get_root_path(), self._yaml_file)
        self._yaml_dict = self._load_yaml()

    @property
    def log_mode(self) -> str:
        return self._get_env(self._log_mode)

    @log_mode.setter
    def log_mode(self, value: str):
        self._set_env(self._log_mode, value)

    @property
    def key_to_encrypt(self) -> str:
        return self._get_env(self._key_to_encrypt)
    
    @key_to_encrypt.setter
    def key_to_encrypt(self, value: str):
        self._set_env(self._key_to_encrypt, value)

    @property
    def algorithm_to_encrypt(self) -> str:
        return self._get_env(self._algorithm_to_encrypt)

    @algorithm_to_encrypt.setter
    def algorithm_to_encrypt(self, value: str):
        self._set_env(self._algorithm_to_encrypt, value)

    @property
    def host_database(self) -> str:
        return self._yaml_dict[self._host_database]

    @host_database.setter
    def host_database(self, value: str):
        self._yaml_dict[self._log_mode] = value
        self._dump_yaml()

    @property
    def username_database(self) -> str:
        return self._yaml_dict[self._username_database]
    
    @username_database.setter
    def username_database(self, value: str):
        self._yaml_dict[self._username_database] = value
        self._dump_yaml()
    
    @property
    def password_database(self) -> str:
        return self._yaml_dict[self._password_database]

    @password_database.setter
    def password_database(self, value: str):
        self._yaml_dict[self._password_database] = value
        self._dump_yaml()
    
    @property
    def name_database(self) -> str:
        return self._yaml_dict[self._name_database]

    @name_database.setter
    def name_database(self, value: str):
        self._yaml_dict[self._name_database] = value
        self._dump_yaml()

    def _load_yaml(self):
        with open(self._yaml_file, 'r') as yaml_f:
            return yaml.load(yaml_f, Loader=yaml.FullLoader)

    def _dump_yaml(self):
        if self._yaml_dict:
            with open(self._yaml_file, 'w') as yaml_f:
                yaml.safe_dump(self._yaml_dict, yaml_f)

    def _set_env(self, key: str, value: str) -> None:
        os.environ[key] = value
        dotenv.set_key(self._dotenv_file, key, value)

    def _get_env(self, key: str) -> Any:
        return decouple.config(key)

    @classmethod
    def get_instance(cls):
        if cls.__instance is None:
            cls.__instance = ConfigAccessor()
        return cls.__instance
