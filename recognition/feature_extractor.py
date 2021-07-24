import os
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import load_model
from net.inception_resnet import InceptionResNetV2

class FeatureExtractor:
    def __init__(self):
        path = os.path.abspath(__file__)
        model_weights_path = os.path.join(path.rsplit(os.path.sep, 1)[0], 'pretrained-models', 'facenet_keras_weights.h5')
        self.facenet = InceptionResNetV2()
        self.facenet.load_weights(model_weights_path)
    def normalize_image(self, images):
        mean = np.mean(images)
        std = np.std(images)
        return (images - mean) / std
    def get_embeddings(self, images):
        images = self.normalize_image(images)
        if len(images.shape) == 3:
            images = np.expand_dims(images, axis = 0)
        return self.facenet.predict(images)