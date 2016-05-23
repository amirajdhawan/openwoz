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

> sudo apt-get update <br/>
> sudo apt-get install nodejs npm

* Create symlink for nodejs

> sudo ln -s "$(which nodejs)" /usr/bin/node

* Install Redis server

> sudo apt-get install build-essential <br/>
> sudo apt-get install tcl8.5 <br/>
> wget http://download.redis.io/releases/redis-stable.tar.gz <br/>
> tar xzf redis-stable.tar.gz <br/>
> cd redis-stable <br/>
> make <br/>
> //Can skip the next step but highly suggested to detect any issues <br/>
> make test <br/>
> sudo make install <br/>
> sudo utils/install_server.sh <br/>
> //Keep pressing enter to install redis with default config <br/>

* Comment out the line "bind 127.0.0.1" in redis.conf file (typically /etc/redis/6379.conf)
* In the same config file search for requirepass and uncomment it and change the password to an alphanumeric and complicated.
* Start the server

> sudo service redis_6379 restart

* Download openwoz latest code using

> cd ~ <br/>
> wget https://github.com/amirajdhawan/openwoz/archive/release.tar.gz <br/>
> tar xzf release.tar.gz <br/>
> cd openwoz-reflection/server <br/>

* Edit the file server.js and set the redis password which you set in the redis configuration file

> redis_pass = "your_password"

* Install npm dependencies.

> npm install <br/>
> sudo npm install forever -g <br/>

> forever start server.js

## Usage
In the folder server, to restart the server use the below

> forever restart server.js

In the folder server, start the server or to stop the server using the below

> forever start|stop server.js

The server is accessible in ip_address:8080

## Available Links

> GET ip_address:8080/

> GET ip_address:8080/robots

> GET ip_address:8080/robots/{profile_name}

> GET ip_address:8080/robots/{profile_name}/{event_name}

> GET ip_address:8080/robots/{profile_name}/{event_name}/trigger
