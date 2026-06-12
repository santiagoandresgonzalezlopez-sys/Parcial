FROM openjdk:17
COPY "./target/CRUDBASICO-1.jar" "app.jar"
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]