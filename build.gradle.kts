import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.protobuf") version "0.9.4"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.6"
//    id("org.flywaydb.flyway") version "11.8.0"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.postgresql:postgresql")
    //runtimeOnly("com.mysql:mysql-connector-j:8.0.33")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // testing
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    //testImplementation("org.testcontainers:mysql:1.19.1")

    // kafka
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // protobuf
    implementation("com.google.protobuf:protobuf-java:4.33.0")
    implementation("io.confluent:kafka-protobuf-serializer:8.0.3")

    // DB migration
    implementation("org.liquibase:liquibase-core")
    //implementation("org.flywaydb:flyway-core")
    //implementation("org.flywaydb:flyway-database-postgresql")
}

//flyway {
//    driver = "org.postgresql.Driver"
//    url = "jdbc:postgresql:5432/mydb"
//    user = "myuser"
//    password = "mypassword"
//}

protobuf {
    // Align protoc with protobuf-java
    protoc { artifact = "com.google.protobuf:protoc:4.33.0" }
}

kotlin { jvmToolchain(17) }

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
    }
}