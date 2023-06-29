FROM openjdk:20

ENV DB_NAME=bug_tracker \
    DB_HOST=localhost \
    DB_PORT=3303 \
    DB_USER=root \
    DB_PASSWORD=password \
    JWT_SECRET=secret

WORKDIR /server

COPY ./target/server-0.0.1-SNAPSHOT.jar /server

EXPOSE 8080

CMD ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]  