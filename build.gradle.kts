plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "2.0.10"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"

    // ktlint
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
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
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("initSetting")
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
    dependsOn(tasks.named("updateGitSubmodules"))
    from("./Yapp-26th-env")
    include("*.yml")
    into("src/main/resources")
    println("Secret files이 성공적으로 복사되었습니다.")
}

tasks.register<Exec>("updateGitSubmodules") {
    group = "git"
    description = "Git submodule을 원격 저장소의 최신 버전으로 업데이트합니다."
    commandLine("git", "submodule", "update", "--remote")
}
