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

* Install nodejs and npm packages. Also install forever npm package globally

> sudo apt-get update <br/>
> sudo apt-get install nodejs npm <br/>
> sudo npm install forever -g

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
> <br/>
> //Keep pressing enter for the below command to install redis with default config <br/>
> sudo utils/install_server.sh <br/>

* Comment out the line "bind 127.0.0.1" in redis.conf file (typically /etc/redis/6379.conf)
* In the same config file search for requirepass and uncomment it and change the password to an alphanumeric and complicated.

> requirepass "your_redis_password"

* Start the server

> sudo service redis_6379 restart

* Download openwoz latest code using

> cd ~ <br/>
> wget https://github.com/amirajdhawan/openwoz/archive/release.tar.gz <br/>
> tar xzf release.tar.gz <br/>
> cd openwoz-release/server <br/>

* Edit the file server.js and set the redis password in the first line which you set in the redis configuration file

> var redis_pass = "redis_password_that_you_set_above"

* Install npm dependencies.

> sudo npm install <br/>

* Start the server using forever

> forever start server.js

### Make it available across server restarts

> crontab -u {username} -e <br/>

* At the end append the following line

> @reboot /usr/local/bin/forever start /path/to/openwoz/folder/server/server.js

* Restart and the server will automatically start across reboots

The server is accessible in ip_address

## Usage
In the folder server, to restart the server use the below

> forever restart server.js

In the folder server, start the server or to stop the server using the below

> forever start|stop server.js

## Available Links

> GET ip_address/

> GET ip_address/robots

> GET ip_address/robots/{profile_name}

> GET ip_address/robots/{profile_name}/{event_name}

> GET ip_address/robots/{profile_name}/{event_name}/trigger
