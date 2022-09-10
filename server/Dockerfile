FROM openjdk:17

CMD [ "java", "-XshowSettings:vm", "-XX:MaxRAMPercentage=80", "-jar", "server.jar" ]
EXPOSE 8080

COPY build/libs/server.jar server.jar
