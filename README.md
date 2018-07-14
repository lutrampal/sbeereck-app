# S'Beer Eck app
<img src="https://raw.githubusercontent.com/lutrampal/sbeereck-app/master/.LEGACY/front-end/app/src/main/res/drawable/sbeereck.png" height="300" />

S'Beer Eck is Grenoble INP's beer club.  
We are an association dedicated to help students of Grenoble INP discover the richness of our favourite beverage.  

This app helps up to manage our events.  
Its primary feature is to store the members' accounts and update their balance automatically whenever a transaction is added at an event.

## How it works
The app's back-end relies on a MySQL database and a RESTful API built with flask-restful.
Front-end is developed with react.

## Getting started
These instructions will get you a copy of the back-end up and running on your local machine for development and testing purposes.

## Prerequisites
To start the back-end, you'll need [Docker](https://docs.docker.com/install/) and [Docker Compose](https://docs.docker.com/compose/install/#install-compose).

## Run

### Development
To run the back-end on your local machine:
1. Start a MySQL container:  
`docker run --name sbeereck-mysql -e MYSQL_ROOT_PASSWORD=toor -d -v <local data dir>:/var/lib/mysql mysql:5.7`
> *local data dir* is the location of your test MySQL DB.
2. Set the `SB_DB_HOST` env var with the container's lcoal IP:  
`export SB_DB_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' sbeereck-mysql)`

3. Run the development Flask server:  
`python3.5 ./back-end/RESTful-api/sbeereck_server.py`
> You should now be able to send requests to the back-end at `localhost:8081`  

### Production
To deploy the production environment:
1. run `docker-compose up -d` from the `back-end/` folder.  
> This folder should contain an `ssl/` directory with the following files: `ca_bundle.cert`,  `sbeereck.cert` & `sbeereck.key`. They will be used by the Apache container for HTTPS.    
Additionally, you have to provide a `mysql/` folder containing a backup of the MySQL database.
