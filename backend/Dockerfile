FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the pre-built JAR
COPY target/huaiwei-yunshan-1.0.0.jar app.jar

# Create directory for uploads
RUN mkdir -p uploads/dishes

# Set environment variables with defaults (can be overridden at runtime)
ENV SERVER_PORT=8083
ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_NAME=huaiwei-v2
ENV DB_USER=root
ENV DB_PASS=123456

# Expose the application port
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
