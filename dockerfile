FROM maven:3.8.6 as BUILD
WORKDIR /service
COPY . /service
RUN mvn clean package

FROM openjdk:17-jdk-slim
COPY --from=BUILD /service/target /opt/target
WORKDIR /opt/target
CMD ["java", "-jar", "/opt/target/funiverse-app-service-0.1.0.jar"]

