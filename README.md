# Friend-Management

## About this task
This application is created by author `Hanstevin Tandian`. It includes feature to connect friend, list friends, get common friends, subscribe to an update, block friend / updates, and get update recipients list.

## Libraries used
 - spring-boot-jpa
 - spring-boot-web
 - h2database
 To handle data in memory

## About application
Upon start, there will be a seeding process where 8 dummy data is pumped into the h2 memory db to be used by services.

## Running application

```sh
mvn clean install
java -jar target/friends-management-0.0.1-SNAPSHOT.jar
```
## Running Test
```sh
mvn test
```

## How to use

Available users will be:
- userA@hans.com
- userB@hans.com
- userD@hans.com
- userE@hans.com
- userF@hans.com
- userG@hans.com
- userH@hans.com

Access services using Postman / curl

request payload:
- /connect
```json
{
  "friends":
    [
      "userA@hans.com",
      "userB@hans.com"
    ]
}
```
- /list
```json
{
  "email": "userA@hans.com"
}
```
- /common
```json
{
  "friends": [
     "userA@hans.com",
      "userC@hans.com"
      ]
}
```
- /subscribe
```json
{
  "requestor": "userD@hans.com",
  "target": "userA@hans.com"
}
```
- /block
```json
{
  "requestor": "userC@hans.com",
  "target": "userA@hans.com"
}
```
- /send
```json
{
  "sender": "userA@hans.com",
  "text": "Hi! userE@hans.com"
}
```