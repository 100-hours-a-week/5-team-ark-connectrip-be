# 1. Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the application JAR file from the host into the container
# This assumes your Gradle build output is in the 'build/libs' directory
COPY build/libs/*.jar /app/app.jar

# 4. Expose the port the application will run on
EXPOSE 8080

# 5. Define the command to run the application
ENTRYPOINT ["nohup", "java", "-jar", "/app/app.jar", "> /app/nohup.out", "2>&1"]
ba