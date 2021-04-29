import json
import glob

def get_all_image_paths(path = '.'):
    all_paths = sorted(glob.glob(path + '/images/*/*.jpg') + glob.glob(path + '/images/*/*.png'))
    return all_paths
def get_config(config_path = 'config.json'):
    with open(config_path, 'r') as f:
        return json.load(f)