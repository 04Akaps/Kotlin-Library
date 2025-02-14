plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("maven-publish")
}

group = "com.test"
version = "1.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:2.2.3")
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("org.springframework:spring-tx:5.3.20") // 버전 추가
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.0.0") // 버전 추가
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter")

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
            groupId = "com.test"
            artifactId = "test-library"
            version = "1.0.3"
        }
    }
    repositories {
        maven {
            url = uri("file://${project.buildDir}/maven-repo")
        }
    }
}