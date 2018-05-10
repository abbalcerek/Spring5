FROM oracle/openjdk:8
MAINTAINER ubuntu
ADD Spring5 /Spring5
ADD app-lib/Spring5-1.0-SNAPSHOT.jar /Spring5/lib/Spring5-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/Spring5/lib/Spring5-1.0-SNAPSHOT.jar"]
EXPOSE 8080
