import jwt
from config_access import ConfigAccessor

import time
from typing import Dict, Optional

config_accessor = ConfigAccessor.get_instance()

__jwt_secret = config_accessor.key_to_encrypt
__jwt_algo = config_accessor.algorithm_to_encrypt


def token_response(token: bytes):
    return {
        "access_token": token
    }


def sign_jwt(username: str) -> Dict[str, bytes]:
    payload = {
        "username": username,
        "exp": time.time() + 3600
    }
    token = jwt.encode(payload, __jwt_secret, algorithm=__jwt_algo)

    return token_response(token)


def decode_jwt(token: str) -> Optional[dict]:
    try:
        return jwt.decode(token, __jwt_secret, algorithms=[__jwt_algo], require=["exp"], verify_exp=True)
    except:
        return None
