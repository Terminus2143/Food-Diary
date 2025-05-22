FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven

RUN mvn package -DskipTests

ENTRYPOINT ["java", "-jar", "target/foodDiary-0.0.1-SNAPSHOT.jar"]