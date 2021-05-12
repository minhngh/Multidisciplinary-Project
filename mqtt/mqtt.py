import yaml
from Adafruit_IO import MQTTClient
import json

class MQTT:
    def __init__(self):
        # edit configuration in config.yml
        with open('config.yml') as f:
            config = yaml.load(f, Loader = yaml.FullLoader)

        self.client = MQTTClient(config['IO_USERNAME'], config['IO_KEY'])
        self.speaker_feed = config['feed']['output'][0]
        self.magnetic_feed = config['feed']['input'][0]
        self.is_open = False
        def connected(client):
            client.subscribe(self.magnetic_feed)
        def message(client, feed_id, payload):
            is_open = parse_magnetic_format(payload)
            self.is_open = is_open        
        def parse_magnetic_format(data):
            data = json.loads(data)
            return int(data['data']) == 1
        self.client.on_connect = connected
        self.client.on_message = message
        self.client.connect()
        self.client.loop_background()
    def __get_speaker_format(self, value):
        return json.dumps({
            'id': '3',
            'name': 'SPEAKER',
            'data': str(value),
            'unit': ''
        })
    def send__speaker_data(self, value):
        """
            send value to MQTT server
            Arguments:
                - value: integer, range from 0 to 1023
            Return: None
        """
        self.client.publish(self.speaker_feed,  self.__get_speaker_format(value))
    def receive_door_state(self):
        """
            receive the state of door
            Arguments: None
            Return: True if the door is open, otherwise return False
        """
        return self.is_open
