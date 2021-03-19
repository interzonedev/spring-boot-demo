#!/bin/sh

export DATABASE_URL=postgres://springbootdemouser:springbootdemopass@localhost:5432/springbootdemo_main

jar_file_path=$(find $(pwd) -type f -name 'spring-boot-demo*.jar')

java \
  -server \
  -Xms200m \
  -Xmx200m \
  -XX:MetaspaceSize=50m \
  -XX:MaxMetaspaceSize=100m \
  -Xss1024k \
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081 \
  -jar ${jar_file_path}
  $@
