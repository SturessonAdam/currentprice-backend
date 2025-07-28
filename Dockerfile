# Multi-stage build for optimal image size
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy dependency files first for better layer caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application with optimizations
RUN ./mvnw clean package -DskipTests -B && \
    mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

# Production image with minimal JRE
FROM eclipse-temurin:17-jre-alpine AS runtime

# Install dumb-init for proper signal handling
RUN apk add --no-cache dumb-init

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

# Set working directory
WORKDIR /app

# Copy application layers from build stage
COPY --from=build --chown=appuser:appgroup /app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=build --chown=appuser:appgroup /app/target/dependency/META-INF /app/META-INF
COPY --from=build --chown=appuser:appgroup /app/target/dependency/BOOT-INF/classes /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/health || exit 1

# Run application with optimized JVM settings
ENTRYPOINT ["dumb-init", "--"]
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-XX:+UseG1GC", \
     "-XX:+UseStringDeduplication", \
     "-XX:+OptimizeStringConcat", \
     "-XX:+UseCompressedOops", \
     "-XX:+UseCompressedClassPointers", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dspring.profiles.active=prod", \
     "-cp", ".:lib/*", \
     "org.example.elprisappbackend.CurrentPriceBackendApplication"]

