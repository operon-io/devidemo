FROM registry.access.redhat.com/ubi8/openjdk-17:1.17

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

COPY target/lib/* /deployments/lib/
COPY target/*-runner.jar /deployments/quarkus-run.jar
COPY target/routes/* /deployments/routes/
COPY target/scripts/* /deployments/scripts/

EXPOSE 8080
USER 185
ENV AB_JOLOKIA_OFF=""
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
