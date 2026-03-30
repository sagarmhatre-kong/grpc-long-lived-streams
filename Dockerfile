FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy POMs first for dependency caching
COPY pom.xml .
COPY core/pom.xml core/
COPY server/pom.xml server/
COPY client/pom.xml client/
COPY examples/pom.xml examples/
RUN mvn dependency:go-offline -pl examples -am -q 2>/dev/null || true

# Copy source and proto, then build
COPY core/src core/src/
COPY server/src server/src/
COPY client/src client/src/
COPY examples/src examples/src/
COPY examples/proto examples/proto/
RUN mvn clean package -DskipTests -pl examples -am -q

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/examples/target/examples-*.jar app.jar
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "app.jar"]


# Build
# cd /Users/sagar.mhatre/Documents/src/grpc-long-lived-streams
# docker build -t grpc-helloworld .
# Run
# docker run -p 8099:8099 grpc-helloworld

# Rebuild & run
# clear && docker build -t grpc-helloworld . && docker run -p 8099:8099 grpc-helloworld
