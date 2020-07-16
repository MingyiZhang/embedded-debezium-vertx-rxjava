#!/usr/bin/env bash

docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -e MYSQL_DATABASE=mysql debezium-mysql:8