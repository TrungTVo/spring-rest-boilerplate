FROM openjdk:17

COPY . /usr/apps/spring-rest-boilerplate

WORKDIR /usr/apps/spring-rest-boilerplate

RUN ./mvnw clean package

CMD ["java","-jar","target/boilerplate-0.0.1-SNAPSHOT.jar"]