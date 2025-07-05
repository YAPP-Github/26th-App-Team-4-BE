plugins {
    java
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "2.0.10"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"

    // ktlint
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"

    // REST Docs & Swagger UI
    id("com.epages.restdocs-api-spec") version "0.18.2"
    id("org.hidetake.swagger.generator") version "2.18.2"
}

group = "com.yapp"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2024.0.0"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // rest assured
    testImplementation("io.rest-assured:rest-assured:5.5.0")

    // db
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.2")
    testImplementation("com.epages:restdocs-api-spec-restassured:0.18.2")

    // Spring Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // openFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // Spring Data Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Testcontainer
    implementation("org.testcontainers:testcontainers-bom:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:testcontainers")

    // xml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api")
    implementation("com.sun.xml.bind:jaxb-impl")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

openapi3 {
    this.setServer("http://fitrun.p-e.kr")
    title = "FitRun API"
    description = "FitRun API 명세서"
    version = "0.1.0"
    format = "yaml"
    snippetsDirectory = "build/generated-snippets"
    outputDirectory = "build/docs"
}

tasks.register<Copy>("makeDocument") {
    group = "documentation"
    description = "Generate API Docs and copy to static folder."
    dependsOn("documentTest")
    dependsOn("openapi3") // openapi3 Task가 먼저 실행되도록 설정
    delete("src/main/resources/static/docs/openapi3.yaml")
    copy {
        from("build/docs/") // 복제할 OAS 파일 지정
        into("src/main/resources/static/docs/") // 타겟 디렉터리로 파일 복제
    }
}
tasks.test {
    exclude("**/document/**")
}
tasks.register<Test>("documentTest") {
    description = "Runs tests from document package"
    filter {
        includeTestsMatching("*.document.*")
    }
    useJUnitPlatform()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("initSetting")
}

tasks.named("bootJar") {
    dependsOn("makeDocument")
}

tasks.register("initSetting") {
    group = "custom tasks"
    description = "Execute both copyHooks and copySecret tasks."

    dependsOn("copyHooks", "copySecret")
}

tasks.register("copyHooks") {
    group = "git hooks"
    description = "Copy pre-commit and pre-push git hooks from .githooks to .git/hooks folder."

    doLast {
        // pre-commit hook 복사
        copy {
            from("$rootDir/.githooks/pre-commit")
            into("$rootDir/.git/hooks")
        }
        // pre-push hook 복사
        copy {
            from("$rootDir/.githooks/pre-push")
            into("$rootDir/.git/hooks")
        }
        // pre-push hook에 실행 권한 부여
        file("$rootDir/.git/hooks/pre-push").setExecutable(true)
        file("$rootDir/.git/hooks/pre-commit").setExecutable(true)
        println("Git pre-commit 및 pre-push hook이 성공적으로 등록되었습니다.")
    }
}

tasks.register<Copy>("copySecret") {
    from("./Yapp-26th-env")
    include("*.yml")
    into("src/main/resources")
    println("Secret files이 성공적으로 복사되었습니다.")
}

tasks.register<Exec>("initSubmodule") {
    group = "git"
    description = "Git submodule을 원격 저장소의 최신 버전으로 업데이트합니다."
    commandLine("git", "submodule", "update", "--remote")
}

tasks.register<Exec>("md") {
    commandLine("./gradlew", "makeDocument", "--exclude-task", "test")
}
