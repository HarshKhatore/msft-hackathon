import platform
import wmi
import subprocess
import json
import requests
import datetime

data={}

architecture=platform.machine()
data['make']=architecture
platform_version=platform.version()
data['platformVersion']=platform_version
platform=platform.platform()
data['platform']=platform
c = wmi.WMI()
t = wmi.WMI(moniker = "//./root/wmi")

batts1 = c.CIM_Battery(Caption = 'Portable Battery')
for i, b in enumerate(batts1):
    Design_Capacity=b.DesignCapacity

batts = t.ExecQuery('Select * from BatteryFullChargedCapacity')
for i, b in enumerate(batts):
    Fully_Charged_Capacity=b.FullChargedCapacity
    Battery_percent=((Fully_Charged_Capacity/Design_Capacity))*100
    data['batteryMaxCapacity']=round(Battery_percent,2)

current_machine_id = subprocess.check_output('wmic csproduct get uuid').decode().split('\n')[1].strip()
data['machineId']=current_machine_id
serial_number= subprocess.check_output('wmic bios get serialnumber').decode().split('\n')[1].strip()
data['serialNumber']=serial_number
system_details=subprocess.check_output('systeminfo').decode().split('\n')
userName=system_details[1].split(':')[1].strip()
modelName=system_details[13].split(':')[1].strip()
biosVersion=system_details[17].split(':')[1].strip().split(',')[1].strip().split("/")
data['userName']=userName
data['modelName']=modelName
biosVersionMonth = int(biosVersion[0])
biosVersionDate = int(biosVersion[1])
biosVersionYear = int(biosVersion[2])
data['activationDate']=round((datetime.datetime(biosVersionYear, biosVersionMonth, biosVersionDate, 0, 0) - 
datetime.datetime(1970,1,1)).total_seconds())
data['deviceType'] = 'ELECTRONICS'
json_data = json.dumps(data)
print(json_data)
url = 'http://10.104.0.39:8080/streamservice/api/insurance/create'
post = requests.post(url, data = json_data,timeout=60)
print(post.text, post.status_code)