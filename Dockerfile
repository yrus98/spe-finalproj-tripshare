FROM openjdk:12-alpine

COPY target/"tripshare-0.0.1-SNAPSHOT.jar" /tripshare_backend.jar

CMD ["java","-jar","/tripshare_backend.jar"]
