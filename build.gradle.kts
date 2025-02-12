plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    kotlin("jvm") version "2.0.21"
}

group = "org.library"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("io.ktor:ktor-client-core:2.2.3") // 최신 2.x 버전
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

//    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA를 사용할 경우

    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework:spring-tx") // 트랜잭션 관리만 사용할 경우
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}