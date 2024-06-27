FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar /app/cinema-city.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/cinema-city.jar"]