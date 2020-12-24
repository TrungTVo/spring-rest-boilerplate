FROM openjdk

COPY . /usr/apps/spring-rest-boilerplate

WORKDIR /usr/apps/spring-rest-boilerplate

# ARG JAR_FILE=target/boilerplate-0.0.1-SNAPSHOT.jar

RUN ./mvnw clean package

# COPY ${JAR_FILE} boilerplate-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","target/boilerplate-0.0.1-SNAPSHOT.jar"]