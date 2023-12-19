Virtual machine spec
Machine type : e2-small <br>
Disk : 10gb <br>
OS : debian <br>

ML API deploy using compute engine

1. create a virtual machine
2. create the firewall rules for http/https
3. connect to the ssh and update package
4. install the necessary library
   
```
sudo apt update
sudo apt install python3 python3-pip
pip install flask tensorflow numpy google-cloud-storage google-cloud-firestore
```
  
6. install screen
7. create service account key
8. upload service account key, ML model, and app.py
9. create new virtual terminal using screen and run the app in there
