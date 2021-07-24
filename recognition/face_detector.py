from mtcnn import MTCNN
import cv2 as cv
import numpy as np

class FaceDetector:
    def __init__(self, configs = None):
        self.detector = MTCNN()
        if configs:
            self.min_confidence = configs['min_confidence']
            self.min_w = configs['min_w']
            self.min_h = configs['min_h']
        else:
            self.min_confidence = 0.9
            self.min_h = 0
            self.min_w = 0
    def preprocess_image(self, image, swap_color = True):
        if swap_color:
            image = cv.cvtColor(image, cv.COLOR_BGR2RGB)
        return image
    def detect_faces(self, image, swap_color = False):
        assert image is not None, "Image can't be NONE"
        if isinstance(image, str):
            image = cv.imread(image)
        image = self.preprocess_image(image, swap_color = swap_color)
        faces = self.detector.detect_faces(image)
        faces = list(filter(lambda x: x['confidence'] >= self.min_confidence, faces))
        results = []
        for face in faces:
            keypoints = face['keypoints']
            crop_face = self.align_face(image, face['box'], keypoints['left_eye'], keypoints['right_eye'])
            crop_face = cv.resize(crop_face, (160, 160))
            results.append(crop_face)
        return results
    def align_face(self, image, box, left_eye_coord, right_eye_coord):
        dx = right_eye_coord[0] - left_eye_coord[0]
        dy = right_eye_coord[1] - left_eye_coord[1]
        angle = np.arctan2(dy, dx) * 180 / np.pi
        eye_center_coord = (left_eye_coord[0] + right_eye_coord[0]) // 2, (left_eye_coord[1] + right_eye_coord[1]) // 2
        M = cv.getRotationMatrix2D(eye_center_coord, angle, 1.)
        output = cv.warpAffine(image, M, image.shape[:2][::-1], flags = cv.INTER_CUBIC)

        x, y, w, h = box
        box = np.array([[x, y], [x + w, y + h]])
        box = np.hstack((box, np.ones((box.shape[0], 1))))
        box = (box @ M.T).astype(np.int32)
        (x1, y1), (x2, y2) = box[0], box[1]
        new_w, new_h = x2 - x1, y2 - y1
        padding_x = (new_w - w) // 2 + 1 if new_w > w else 0
        padding_y = (new_h - h) // 2 + 1 if new_h > h else 0

        height, width = image.shape[:2]
        x1 = max(x1 - padding_x, 0) 
        y1 = max(y1 - padding_y, 0)
        x2 = min(x2 + padding_x + 1, width)
        y2 = min(y2 + padding_y + 1, height)

        cropped_face = output[y1: y2, x1 : x2]
        return cropped_face