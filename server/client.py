import requests

if __name__ == "__main__":
    while True:
        username = input()
        password = input()
        request = requests.post('http://127.0.0.1:8000/user/login', json={
            'username': username,
            'password': password
        })

        print(request.json()['access_token'])
