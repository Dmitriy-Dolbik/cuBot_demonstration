FROM openjdk:8
# RUN mkdir -p /cuBot
# WORKDIR /cuBot
COPY /target/cuBot-0.1.3.jar .
ENTRYPOINT ["sh", "-c", "java -Dspring.config.location=/config/application.properties -jar cuBot-0.1.3.jar"]