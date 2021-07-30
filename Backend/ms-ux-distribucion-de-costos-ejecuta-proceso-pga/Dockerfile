FROM openjdk:8-jdk-alpine
LABEL maintainer="DevOps-LaChakra"
ENV spring.application.name ms-ux-distribucion-de-costos-ejecuta-proceso-pga-v1
COPY build/libs/ms-ux-distribucion-de-costos-ejecuta-proceso-pga-*SNAPSHOT.jar /opt/ms-ux-distribucion-de-costos-ejecuta-proceso-pga.jar
ENTRYPOINT ["java", "-Djava.file.encoding=UTF-8","-jar","/opt/ms-ux-distribucion-de-costos-ejecuta-proceso-pga.jar"]
