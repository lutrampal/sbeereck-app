# S'Beer Eck app
<img src="https://raw.githubusercontent.com/lutrampal/sbeereck-app/master/.LEGACY/front-end/app/src/main/res/drawable/sbeereck.png" height="300" />

S'Beer Eck is Grenoble INP's beer club.  
We are an association dedicated to help students of Grenoble INP discover the richness of our favorite beverage.  

This app helps up to manage our events.  
Its primary feature is to store the members' accounts and update their balance automatically whenever a transaction is added at an event.

## How it works
The app's back-end relies on a MySQL database and a RESTful API built with flask-restful.
Front-end is developped with react.

## Getting started
These instructions will get you a copy of the back-end up and running on your local machine for development and testing purposes.

## Prerequisites 
To start the back-end, you'll need [Docker](https://docs.docker.com/install/) and [Docker Compose](https://docs.docker.com/compose/install/#install-compose).

## Run
Simply run `docker-compose up -d` from the `back-end/` folder.  
It is assumed that this folder contains an `ssl/` folder with the following files: `ca_bundle.cert`,  `sbeereck.cert` & `sbeereck.key`. They will be used by the Apache container for HTTPS.  
Additionally, you can provide a `mysql/` folder containing a backup of the MySQL database.
