plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("maven-publish")
}

group = "org.library"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:2.2.3") // 최신 2.x 버전
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("org.springframework:spring-tx") // 트랜잭션 관리만 사용할 경우
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "org.library"
            artifactId = "personal-library"
            version = "1.0.0"
        }
    }
    repositories {
        maven {
            url = uri("file://${project.buildDir}/maven-repo")
        }
    }
}