# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17

# Expose the application port
EXPOSE 8080

# Set the application home environment variable
ENV APP_HOME /usr/src/app

# Copy the built JAR file into the container
COPY target/*.jar $APP_HOME/app.jar

# Set the working directory
WORKDIR $APP_HOME

# Command to run the application
CMD ["java", "-jar", "app.jar"]
