# Paper 26.1 requires Java 21 or newer
FROM eclipse-temurin:26-jdk-alpine

RUN apk add --no-cache curl bash

# Move application files to /app instead of /minecraft
WORKDIR /app

RUN curl -o server.jar https://fill-data.papermc.io/v1/objects/0555a0b0468a5198d8fb1a16e1f9e95c81a917a2dc8f2e09867b4044742f6401/paper-26.1.2-72.jar

# Set working directory to the data folder for runtime execution
WORKDIR /minecraft
RUN mkdir -p /minecraft/plugins

EXPOSE 25565 5005

ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

# Run the jar referencing its new absolute path
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app/server.jar nogui"]
