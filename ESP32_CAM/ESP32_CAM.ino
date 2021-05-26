
/**
 * Created by K. Suwatchai (Mobizt)
 * 
 * Email: k_suwatchai@hotmail.com
 * 
 * Github: https://github.com/mobizt
 * 
 * Copyright (c) 2021 mobizt
 *
*/

#include "esp_camera.h"

#if defined(ESP32)
#include <WiFi.h>
#include <FirebaseESP32.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#endif

#include "Base64.h"

/* 1. Define the WiFi credentials */
#define WIFI_SSID "Neutron"
#define WIFI_PASSWORD "longle2000"

/* 2. Define the API Key */
#define API_KEY "tNhiqwGKbWFmObXJQD0eXFeFUSClt7NsYRE1sIJ6"

/* 3. Define the RTDB URL */
#define DATABASE_URL "esp32-camera-66eeb-default-rtdb.firebaseio.com" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app


//Define Firebase Data object
FirebaseData fbdo;

//CAMERA_MODEL_AI_THINKER
#define PWDN_GPIO_NUM     32
#define RESET_GPIO_NUM    -1
#define XCLK_GPIO_NUM      0
#define SIOD_GPIO_NUM     26
#define SIOC_GPIO_NUM     27

#define Y9_GPIO_NUM       35
#define Y8_GPIO_NUM       34
#define Y7_GPIO_NUM       39
#define Y6_GPIO_NUM       36
#define Y5_GPIO_NUM       21
#define Y4_GPIO_NUM       19
#define Y3_GPIO_NUM       18
#define Y2_GPIO_NUM        5
#define VSYNC_GPIO_NUM    25
#define HREF_GPIO_NUM     23
#define PCLK_GPIO_NUM     22


const int timerInterval = 6000;    // time between each HTTP POST image
unsigned long previousMillis = 0;   // last time image was sent

bool taskCompleted = false;

FirebaseJson json;

void setup()
{

  Serial.begin(115200);

  
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;

  // init with high specs to pre-allocate larger buffers
  if(psramFound()){
    config.frame_size = FRAMESIZE_SVGA;
    config.jpeg_quality = 10;  //0-63 lower number means higher quality
    config.fb_count = 2;
  } else {
    config.frame_size = FRAMESIZE_CIF;
    config.jpeg_quality = 12;  //0-63 lower number means higher quality
    config.fb_count = 1;
  }
  
  // camera init
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    delay(1000);
    ESP.restart();
  }

  sensor_t * s = esp_camera_sensor_get();
  s->set_framesize(s, FRAMESIZE_QQVGA);

  Firebase.begin(DATABASE_URL, API_KEY);
  Firebase.reconnectWiFi(true);


  Firebase.setMaxRetry(fbdo, 3);
  Firebase.setMaxErrorQueue(fbdo, 30); 
  Firebase.enableClassicRequest(fbdo, true);
  


  /**
   * This option allows get and delete functions (PUT and DELETE HTTP requests) works for device connected behind the
   * Firewall that allows only GET and POST requests.
   * 
   * Firebase.enableClassicRequest(&fbdo, true);
  */
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
   Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
     Serial.print(".");
      delay(300);
    }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
}

void loop()
{
  
    Firebase.begin(DATABASE_URL, API_KEY);
    Firebase.reconnectWiFi(true);
//      Serial.println(sendPhoto());
   unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= timerInterval) {
    while(!taskCompleted)
    {
      taskCompleted = true;
      if(getRequire())
      {
         sendPhoto();
      }
    }
    taskCompleted = false;
    previousMillis = currentMillis;
  }
  
//  if (Firebase.ready() && !taskCompleted)
//  {
//    taskCompleted = true;
//
//    
//    String path = "/Image";
//    String node;
//
//    Serial.println("------------------------------------");
//    Serial.println("Set image...");
//
//    for (uint8_t i = 0; i < 10; i++)
//    {
//      node = path + "/Double/Data" + String(i + 1);
//      //Also can use Firebase.set instead of Firebase.setDouble
//      if (Firebase.setDouble(fbdo, node.c_str(), ((i + 1) * 10) + 0.123456789))
//      {
//        Serial.println("PASSED");
//        Serial.println("PATH: " + fbdo.dataPath());
//        Serial.println("TYPE: " + fbdo.dataType());
//        Serial.println("ETag: " + fbdo.ETag());
//        Serial.print("VALUE: ");
//        Serial.println("------------------------------------");
//        Serial.println();
//      }
//      else
//      {
//        Serial.println("FAILED");
//        Serial.println("REASON: " + fbdo.errorReason());
//        Serial.println("------------------------------------");
//        Serial.println();
//      }
//    }
//
//    Serial.println("------------------------------------");
//    Serial.println("Get double test...");
//
//    for (uint8_t i = 0; i < 10; i++)
//    {
//      node = path + "/Double/Data" + String(i + 1);
//      //Also can use Firebase.get instead of Firebase.setInt
//      if (Firebase.getInt(fbdo, node.c_str()))
//      {
//        Serial.println("PASSED");
//        Serial.println("PATH: " + fbdo.dataPath());
//        Serial.println("TYPE: " + fbdo.dataType());
//        Serial.println("ETag: " + fbdo.ETag());
//        Serial.print("VALUE: ");
//        Serial.println("------------------------------------");
//        Serial.println();
//      }
//      else
//      {
//        Serial.println("FAILED");
//        Serial.println("REASON: " + fbdo.errorReason());
//        Serial.println("------------------------------------");
//        Serial.println();
//      }
//    }
//
//  }
}

bool getRequire()
{
        String path = "/require";
        String node;


        node = path;
        //Also can use Firebase.set instead of Firebase.setDouble

        if (Firebase.getInt(fbdo, node.c_str()))
        {
          if (fbdo.dataType() == "int") {
            if(fbdo.intData() == 1)
            {
              Serial.println("Require image");
              Serial.println();
              return true;
            }
            else if(fbdo.intData() == 0)
            {
              Serial.println("Not require image");
              Serial.println();
              return false;
            }
           }
        }
        else
        {
          Serial.println("Can't get the require value");
          Serial.println("REASON: " + fbdo.errorReason());
          Serial.println("------------------------------------");
          Serial.println();
          return false;
        }
}

String sendPhoto()
{

  if (Firebase.ready())
    {
      camera_fb_t * fb = NULL;
    fb = esp_camera_fb_get();  
    if(!fb) {
      Serial.println("Camera capture failed");
      return "";
    }
    else
    {
      Serial.println("Capture image successful");
    }

 
  
    String imageFile = "data:image/jpeg;base64,";
    char *input = (char *)fb->buf;
    char output[base64_enc_len(3)];
    for (int i=0;i<fb->len;i++) {
      base64_encode(output, (input++), 3);
      if (i%3==0) imageFile += urlencode(String(output));
    }

    esp_camera_fb_return(fb);
       
//
//    Serial.println("Here");
//    Serial.println(imageFile);
   
        String path = "/Image";
        String node;

//        String jsonData = "{\"photo\":\"" + Photo2Base64() + "\"}";
        node = path;
        //Also can use Firebase.set instead of Firebase.setDouble
        Serial.println("Next 1");
        if (Firebase.pushString(fbdo, node.c_str(), imageFile))
        {
          Serial.println("PASSED");
          Serial.println();
        }
        else
        {
          Serial.println("FAILED");
          Serial.println("REASON: " + fbdo.errorReason());
          Serial.println("------------------------------------");
          Serial.println();
        }
    }
    else
    {
      Serial.println("Firebase is not ready");
    }

    return "OK";
}


//String Photo2Base64() {
//    camera_fb_t * fb = NULL;
//    fb = esp_camera_fb_get();  
//    if(!fb) {
//      Serial.println("Camera capture failed");
//      return "";
//    }
//  
//    String imageFile = "data:image/jpeg;base64,";
//    char *input = (char *)fb->buf;
//    char output[base64_enc_len(3)];
//    for (int i=0;i<fb->len;i++) {
//      base64_encode(output, (input++), 3);
//      if (i%3==0) imageFile += urlencode(String(output));
//    }
//
//    esp_camera_fb_return(fb);
//    
//    return imageFile;
//}

//https://github.com/zenmanenergy/ESP8266-Arduino-Examples/
String urlencode(String str)
{
    String encodedString="";
    char c;
    char code0;
    char code1;
    char code2;
    for (int i =0; i < str.length(); i++){
      c=str.charAt(i);
      if (c == ' '){
        encodedString+= '+';
      } else if (isalnum(c)){
        encodedString+=c;
      } else{
        code1=(c & 0xf)+'0';
        if ((c & 0xf) >9){
            code1=(c & 0xf) - 10 + 'A';
        }
        c=(c>>4)&0xf;
        code0=c+'0';
        if (c > 9){
            code0=c - 10 + 'A';
        }
        code2='\0';
        encodedString+='%';
        encodedString+=code0;
        encodedString+=code1;
        //encodedString+=code2;
      }
      yield();
    }
    return encodedString;
}
