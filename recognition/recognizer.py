import json
import numpy as np
import cv2 as cv
import pandas as pd
import argparse
import faiss
from face_detector import FaceDetector
from feature_extractor import FeatureExtractor
from utils import get_config

def define_args():
    ap = argparse.ArgumentParser()
    ap.add_argument('--image', '-i', type = str)
    return ap.parse_args()

if __name__ == '__main__':
    args = define_args()
    config = get_config()

    detector = FaceDetector(config)
    extractor = FeatureExtractor()
    image = cv.imread(args.image)
    face = detector.detect_faces(image)[0]
    emb = extractor.get_embeddings(face)

    embeddings = np.load(config['embeddings_path'])
    df = pd.read_csv(config["classes_path"])
    index = faiss.IndexFlatL2(128)
    index.add(embeddings)
    print(index.search(emb, config['k_neighbors']))

