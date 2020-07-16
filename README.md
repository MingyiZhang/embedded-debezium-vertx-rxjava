# Embedded Debezium with Vert.x and RxJava

This example shows how to use Embedded Debezium with Vert.x, RxJava, PostgreSQL and MySQL. 

## Architecture
There are four verticles:
- `db.DBVerticle` periodically inserts row to certain table.
- `DebeziumRecordSenderVerticle` reads record from the database and publish to certain address.
- `RecordReceiverVerticle` subscribe to certain address and handle the received message.
- `MainVerticle` deploys verticles.

In the example, the following verticles are deployed as child verticles of `MainVerticle`:  
- two `db.DBVerticle`s add random rows to tables `foo` and `bar`, respectively.  
- one `DebeziumRecordSenderVerticle` is deployed.
- two `RecordReceiverVerticle`s print records based on tables' names. 

## Requirements
- JDK 11 or later
- Docker 19.03.8 or later
- Gradle 6.1.1 or later
- Vert.x 3.9.1
- RxJava 2
- Debezium 1.2.0.Final
- PostgreSQL 12
- MySQL 8

## Start database
A database should be running before deploy the main verticle. In order to start a database, 
one of the following scripts can be used:
- PostgreSQL
  ```shell script
  ./start_postgres.sh  
  ```
- MySQL  
  ```shell script
  ./start_mysql.sh  
  ```

## Run in Intellij
In `MainVerticle`, run the main function. You need to configure to choose `conf-postgres.json` or 
`conf-mysql.json` as the argument. 


> TODO: Need to figure out how to parse config
> ## Compile and run
> ```shell script
> ./gradlew clean build
> ```
> ```shell script
> java -jar ./build/libs/embedded-debezium-vertx-rxjava-1.0-SNAPSHOT-all.jar
> ```

## Expected output
```shell script
INFO: Succeeded in deploying verticle
Receiver 1 is ready!
Receiver 2 is ready!
Receiver 1: Received news: foo
Receiver 2: Received news: bar
Receiver 1: Received news: foo
Receiver 2: Received news: bar
```