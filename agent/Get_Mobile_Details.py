import platform
import wmi
import subprocess
import json
import requests
import datetime

data={}

data['make']='ARMv8'
data['platformVersion']='6.0.1'
data['model']='Oreo'

data['batteryMaxCapacity']=65
data['machineId']='EJFRXV65FN2W4EP9PeN8A'
data['serialNumber']='TYUIP22RUI2W4'
data['userName']='Prashant Shubham'
data['modelName']='SM-G935F'
data['activationDate']=317174400
data['deviceType'] = 'ELECTRONICS'
json_data = json.dumps(data)
print(json_data)
url = 'http://10.104.0.39:8080/streamservice/api/insurance/create'
post = requests.post(url, data = json_data,timeout=60)
print(post.text, post.status_code)