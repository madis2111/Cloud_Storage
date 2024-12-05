FROM openjdk:17-jdk-slim

EXPOSE 8080

ADD /out/artifacts/CloudStorage/CloudStorage.jar /app/CloudStorage.jar

ENTRYPOINT ["java","-jar","/app/CloudStorage.jar"]