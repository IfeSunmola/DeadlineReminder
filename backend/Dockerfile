# Won't work if you try to build outside the prod branch. The prod branch is private
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Skipping tests because the app won't run without the environment variables. The environment variables are in the portal
RUN ./mvnw install -DskipTests -Dspring.profiles.active=prod
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-Dspring.profiles.active=prod",  "-cp","app:app/lib/*", "com.example.backend.BackendApplication"]

EXPOSE 8080