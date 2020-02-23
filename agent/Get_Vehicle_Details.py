import platform
import wmi
import subprocess
import json
import requests
import datetime

data={}

data['make']='BMW'
data['platformVersion']='750i'
data['model']='2018'

data['batteryMaxCapacity']=89
data['machineId']='1HGBH41JXMN109186'
data['serialNumber']='KA-05-KP-3020'
data['userName']='Harsh Khatore'
data['modelName']='BMW X7'
data['activationDate']=214194400
data['deviceType'] = 'AUTOMOBILE'
json_data = json.dumps(data)
print(json_data)
url = 'http://10.104.0.39:8080/streamservice/api/insurance/create'
post = requests.post(url, data = json_data,timeout=60)
print(post.text, post.status_code)