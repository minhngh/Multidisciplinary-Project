import numpy as np
import cv2 as cv
import pandas as pd
import faiss
from face_detector import FaceDetector
from feature_extractor import FeatureExtractor
from utils import get_config, get_config, get_class

class FaceRecognizer:
    def __init__(self, config_path = 'config.yml'):
        config = get_config(config_path)
        self.detector = FaceDetector(config)
        self.extractor = FeatureExtractor()
        self.embeddings = np.load(config['embeddings_path'])
        self.df = pd.read_csv(config['classes_path'])
        self.index = faiss.IndexFlatL2(128)
        self.index.add(self.embeddings)
        self.knn = config['k_neighbors']
        self.max_distance = config['max_distance']
    def recognize(self, image):
        """
            Recognize a face in a list of known faces
            Argument:
                - image: np.ndarray and in RGB order
            Return: None if image has no faces or unknown faces. Otherwise, return label
        """
        faces = self.detector.detect_faces(image)
        if not faces:
            return None
        face = faces[0]
        emb = self.extractor.get_embeddings(face)
        distances, indices = self.index.search(emb, self.knn)
        indices = indices[distances < self.max_distance]
        if indices.shape[0] == 0:
            return None
        classes = self.df.loc[indices, 'class']
        return get_class(classes)