# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# Copy Maven files first (for better layer caching)
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create directory for H2 database
RUN mkdir -p /app/data

# Expose the port your app runs on
EXPOSE 8082

# Set environment variables for production
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8082

# Run the jar file
CMD ["java", "-jar", "target/spring-boot-demo-1.0.0.jar"]