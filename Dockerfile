FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "app.jar"]
