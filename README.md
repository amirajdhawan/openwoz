# OpenWoZ
---
## Description
The project involves developing a community-standard open-source framework for a Wizard-of-Oz (WoZ) system. WoZ is a common technique enabling Human-Robot Interaction researchers to explore aspects of interaction not yet backed by autonomous systems.

The system will include a web application running on an embedded platform which controls a robotics system. It also includes a platform-agnostic client framework that gets updated from a user-accessible database. The system needs to be generic enough to ensure that new robot behavior can be added easily and during run-time.

## Dependencies
Install Packages

* NodeJS
* npm

## Installation for Ubuntu

* Install nodejs and npm packages

> sudo apt-get install nodejs npm

* Install Redis server
* Comment out the line "bind 127.0.0.1" and change protected yes to protected no in redis.conf file (/etc/redis/6379.conf)
* Create symlink for nodejs

> sudo ln -s "$(which nodejs)" /usr/bin/node

* Install npm dependencies. Go to openwoz/server folder and execute

> npm install

## Usage
In the folder openwoz/server execute

> npm start

The server is accessible in localhost:8080

## Available Links

> GET localhost:8080/

> GET localhost:8080/robots

> GET localhost:8080/robots/{profile_name}

> GET localhost:8080/robots/{profile_name}/{event_name}

> GET localhost:8080/robots/{profile_name}/{event_name}/trigger
