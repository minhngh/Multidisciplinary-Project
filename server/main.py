import uvicorn
import sys
import os
path = os.path.abspath(__file__)
sys.path.append(path.rsplit(os.path.sep, 2)[0])
sys.path.append(os.path.join(path.rsplit(os.path.sep, 2)[0],'ESP32_CAM'))
sys.path.append(os.path.join(path.rsplit(os.path.sep, 2)[0],'recognition'))
sys.path.append(os.path.join(path.rsplit(os.path.sep, 2)[0],'server'))

if __name__ == "__main__":
    uvicorn.run("app.api:app", host="0.0.0.0", port=8000, reload=True)
