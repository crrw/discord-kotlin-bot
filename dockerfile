# Base image with the latest version of OpenJDK
FROM openjdk:latest

# Set the working directory to /app
WORKDIR /discord-kotlin-bot

# Copy the source code into the container at /app
COPY . .

# Build the application
RUN ./gradlew build

# Expose port 8080 for the application
EXPOSE 8080

# Start the application when the container starts
CMD ["java", "-jar", "./build/libs/discord-kotlin-bot-1.0-SNAPSHOT.jar"]
