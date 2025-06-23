FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

ARG VERSION=0.0.1

# Gradle 설정 및 wrapper 복사
COPY gradlew ./
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# gradlew 실행 권한 부여
RUN chmod +x gradlew

# 의존성 다운로드 (캐시용)
RUN ./gradlew dependencies

# 이후 전체 소스 복사
COPY . .

RUN ./gradlew test --build-cache || (cat build/reports/tests/test/*.txt && exit 1)
RUN ./gradlew bootJar --build-cache
RUN mkdir -p build/extracted && \
    java -Djarmode=layertools \
         -jar $(find build/libs -name "*.jar" | head -n 1) \
         extract --destination build/extracted

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

VOLUME /tmp

ARG EXTRACTED=/workspace/app/build/extracted

RUN apk update && apk --no-cache add curl

COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.launch.JarLauncher"]
