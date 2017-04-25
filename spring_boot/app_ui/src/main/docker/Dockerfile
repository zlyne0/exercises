FROM frolvlad/alpine-oraclejdk8:slim
EXPOSE 18080
VOLUME /tmp
COPY spring_boot_app_ui-0.0.1-SNAPSHOT.jar app.jar
COPY static/ /static/
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
#ENTRYPOINT [ "sh", "-c", "/bin/sh -i" ]