import os
import glob
import json
from tqdm.auto import tqdm
import numpy as np
import cv2 as cv
import pandas as pd
from collections import defaultdict
from face_detector import FaceDetector
from feature_extractor import FeatureExtractor
from utils import *


if __name__ == '__main__':
    config = get_config()
    detector = FaceDetector(config)
    extractor = FeatureExtractor()

    all_paths = get_all_image_paths()
    faces = []
    class2idx = defaultdict(lambda: 1)
    class_list = defaultdict(list)
    idx = 0
    for i, path in tqdm(enumerate(all_paths)):
        crops = detector.detect_faces(path)
        dirs = path.rsplit(os.path.sep, 2)
        parent_folder = os.path.join('cropped_faces', dirs[1])
        if not os.path.exists(parent_folder):
            os.makedirs(parent_folder)
        for crop in crops:
            cv.imwrite(os.path.join(parent_folder, f'{class2idx[dirs[1]]}.jpg'), cv.cvtColor(crop, cv.COLOR_RGB2BGR))
            class2idx[dirs[1]] += 1
            class_list[dirs[1]].append(idx)
            idx += 1
        faces.extend(crops)
    faces = np.array(faces)
    embeddings = extractor.get_embeddings(faces)
    
    idx2class = {}
    for k, lst in class_list.items():
        for v in lst:
            idx2class[v] = k
    df = pd.DataFrame(idx2class.items(), columns = ['index', 'class'])
    df.to_csv(config['classes_path'], index = False)
    np.save(config['embeddings_path'], embeddings)
