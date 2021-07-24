import glob
import yaml
from collections import defaultdict

def get_all_image_paths(path = '.'):
    all_paths = sorted(glob.glob(path + '/images/*/*.jpg') + glob.glob(path + '/images/*/*.png'))
    return all_paths
def get_config(config_path = 'config.yml'):
    with open(config_path, 'r') as f:
        config = yaml.load(f, Loader = yaml.FullLoader)
    return config
def get_class(lst):
    dt = defaultdict(lambda: 0)
    for c in lst:
        dt[c] += 1
    lst = sorted(dt.items(), key = lambda x: x[1], reverse = True)
    return lst[0][0]