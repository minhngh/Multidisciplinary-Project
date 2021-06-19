from pyfcm import FCMNotification
import yaml

with open('config.yml') as f:
    config = yaml.load(f, Loader  = yaml.FullLoader)

push_service = FCMNotification(api_key = config['FIREBASE_SERVER_API'])
registration_id = config['DEVICE-IDS']

def notify(message): 
    message_title = "SecurityCamera"
    message_body = message
    return push_service.notify_multiple_devices(registration_ids=registration_id, message_title=message_title, message_body=message_body)
