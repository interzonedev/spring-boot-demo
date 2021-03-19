spring-boot-demo
================

Spring Boot demo app.

Code based on the [https://spring.io/guides/gs/spring-boot](https://spring.io/guides/gs/spring-boot) tutorial.

# Web URLs
Heroku direct: [https://interzonedev-spring-boot-demo.herokuapp.com](https://interzonedev-spring-boot-demo.herokuapp.com)  
interzonedev.com CNAME: [http://interzonedev-spring-boot-demo.interzonedev.com](http://interzonedev-spring-boot-demo.interzonedev.com)  

# Local and remote access commands

## Heroku app name
`interzonedev-spring-boot-demo`

## PSQL - Local Main
`psql -d springbootdemo_main -U springbootdemouser -W`

## PSQL - Local Test
`psql -d springbootdemo_test -U springbootdemouser -W`

## PSQL - Remote
`heroku pg:psql`

## Run Tasks - Remote
Gradle task: `heroku run "./gradlew help"`  
Shell script: `heroku run "sh target/bin/migrate history"`  

## Update server code
`git push heroku master`  
`git push heroku featurebranch:master`  

# Command Line API Requests

Get all users: `curl -i -X GET "http://localhost:8080/user"`  
Get specific user: `curl -i -X GET "http://localhost:8080/user/1"`  
Create new user: `curl -i -X POST "http://localhost:8080/user/" -F 'firstName=Uncle' -F 'lastName=Fester' -F "email=uncle@fester.com"`  
Update existing user: `curl -i -X PUT "http://localhost:8080/user/14" -F 'firstName=Uncle' -F 'lastName=Fester' -F "email=uncle7@fester.com"`  
