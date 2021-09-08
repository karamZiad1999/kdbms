FROM openjdk:8
ADD target/kdbms.jar kdbms.jar
ENTRYPOINT ["java", "-jar", "kdbms.jar"]
EXPOSE 2000