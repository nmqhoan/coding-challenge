# FiveInRowChallenge

This game was developed using following technologies:

1. Spring boot
2. Websocket
3. Java 8

## GameServer

The source code of game server is located at GameServer directory
#### Deployment

Move to GameServer directory and run the following command in terminal to deploy the Game server.

```
cd GameServer

./mvnw

```

#### Packaging as jar

To build the final jar and optimize the FiveInRowChallenge application for production, run:

```

mvn clean verify


```

To ensure everything worked, run:

```

java -jar target/*.jar


```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

#### Testing

To launch your application's tests, run:

```
./mvnw verify
```

## GameClient

The source code of game client is located at GameClient directory
#### Deployment

Move to GameClient directory and run the following command in terminal to pack the Game client as jar file.

```
cd GameClient

mvn clean install

```

To ensure everything worked, run:

```

java -jar target/*.jar


```

## Play game

The game starts with two windows which illustrate for 2 game player. The board game size is configurable at the 
application properties of game server. The default size is 6x9 and default win count is 5.