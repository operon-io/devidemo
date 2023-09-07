FROM registry.access.redhat.com/ubi8/openjdk-17:1.17

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

# We make distinct layers so if there are application changes the library layers can be re-used
COPY --chown=185 quarkus-app/lib/ /deployments/lib/
COPY --chown=185 quarkus-app/*.jar /deployments/
COPY --chown=185 quarkus-app/app/ /deployments/app/
COPY --chown=185 quarkus-app/quarkus/ /deployments/quarkus/
COPY --chown=185 quarkus-app/routes/ /deployments/routes/
COPY --chown=185 quarkus-app/scripts/ /deployments/scripts/
#
# These are copied from the ConfigMaps
#
COPY --chown=185 quarkus-app/extra-routes/ /deployments/extra-routes/
COPY --chown=185 quarkus-app/extra-scripts/ /deployments/extra-scripts/

EXPOSE 8080
USER 185
ENV AB_JOLOKIA_OFF=""
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
