val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project

val expose_version = "0.36.2"
val koin_version = "3.1.4"

plugins {
    application
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
    id("org.flywaydb.flyway") version "5.2.4"
    id("com.bmuschko.docker-remote-api") version "7.1.0"
}

flyway {
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    baselineOnMigrate = true
    locations = arrayOf("filesystem:resources/db/migration")
}

tasks.create("buildImage", com.bmuschko.gradle.docker.tasks.image.DockerBuildImage::class) {
    inputDir.set(file("./"))
    images.add("mms/order-service:latest")
}

group = "com.mms"
version = "0.0.1"
application {
    mainClass.set("com.mms.oms.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor dependencies
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Koin DI
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")

    // Database dependencies
    implementation("org.jetbrains.exposed:exposed-core:$expose_version")
    implementation("org.jetbrains.exposed:exposed-dao:$expose_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$expose_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$expose_version")
    implementation("com.zaxxer:HikariCP:2.7.8")
    implementation("org.postgresql:postgresql:42.2.2")
    implementation("org.flywaydb:flyway-core:5.2.4")
    testImplementation("com.opentable.components:otj-pg-embedded:0.13.4")

    // Kafka
    implementation("org.apache.kafka:kafka-clients:2.8.1")
    testImplementation("com.consol.citrus:citrus-kafka:3.1.0")

    // Testing
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("io.mockk:mockk:1.12.1")

    // Other
    implementation("org.valiktor:valiktor-core:0.12.0")
    implementation("org.quartz-scheduler:quartz:2.3.2")
}
