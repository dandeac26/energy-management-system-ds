This is an energy management system developed during the distributed system course.

# Deployment Diagram 
![image](https://github.com/dandeac26/energy-management-system-ds/assets/79625820/32e446af-df4c-4bb9-a5cb-3e5e17cec307)



# Helpful commands
Remove any previous containers or images - use 'sudo" if on linux! if not remove "sudo" and all appearances!<br>
`sudo docker ps -q | xargs -r sudo docker rm -f && sudo docker image ls -q | xargs -r sudo docker rmi -f`

build the user microservice container:<br>
`sudo docker build . -t users-service --no-cache && sudo docker compose up --remove-orphans`

build the device microservice container:<br>
`sudo docker build . -t devices-service --no-cache && sudo docker compose up --remove-orphans`

build the frontend container:<br>
`sudo docker build . -t frontend --no-cache && sudo docker compose up --remove-orphans`

build the reciever microservice container:<br>
`sudo docker build . -t reciever-service --no-cache && sudo docker compose up --remove-orphans`


# RUNNING TRANSMITTER
First of all, we need docker run rabbitmq queue service, with this command :<br>
`sudo docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management`

Then we can run the transmitter.py file:<br>
`python3 transmitter.py`

# Architecture
![image](https://github.com/dandeac26/energy-management-system-ds/assets/79625820/87b79173-dd2c-4afa-b628-9106129b11d0)
