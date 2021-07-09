import os


def get_root_path() -> str:
    file_path = os.path.abspath(__file__)
    return file_path.rsplit(os.path.sep, 2)[0]
