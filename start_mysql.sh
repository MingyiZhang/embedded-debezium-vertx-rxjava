#!/usr/bin/env bash

cd docker/mysql && ./build.sh
docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -e MYSQL_USER=debezium -e MYSQL_PASSWORD=dbz -e MYSQL_DATABASE=test debezium-mysql:8