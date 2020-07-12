# Embedded Debezium with Vert.x and RxJava

This example shows how to use Embedded Debezium with Vert.x and RxJava. 

## Architecture
There are three four verticles:
- `db.PostgresVerticle` periodically inserts row to certain table.
- `DebeziumRecordSenderVerticle` reads records from PostgreSQL database and publish to certain address.
- `RecordReceiverVerticle` subscribe to certain address and handle the received message.
- `PostgresMainVerticle` deploys verticles.

In the example, the following verticles are deployed as child verticles of `PostgresMainVerticle`:  
- two `db.PostgresVerticle`s add random rows to tables `foo` and `bar`, respectively.  
- one `DebeziumRecordSenderVerticle` is deployed.
- two `RecordReceiverVerticle`s print records based on tables' names. 

## Requirements
- JDK 11 or later
- Gradle 6.1.1
- Vert.x 3.9.1
- RxJava 2
- Debezium 1.2.0.Final

## Start PostgreSQL
```shell script
./start_postgres.sh
```

## To run
```shell script
./gradlew vertxRun
```

## To compile and run `.jar`
```shell script
./gradlew clean build
```
```shell script
java -jar ./build/libs/embedded-debezium-vertx-rxjava-1.0-SNAPSHOT-all.jar
```

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