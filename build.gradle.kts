val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project

val expose_version = "0.36.2"

plugins {
    application
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
    id("org.flywaydb.flyway") version "5.2.4"
}

flyway {
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    baselineOnMigrate = true
    locations = arrayOf("filesystem:resources/db/migration")
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
    // Kotlin dependencies
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Database dependencies
    implementation("org.jetbrains.exposed:exposed-core:$expose_version")
    implementation("org.jetbrains.exposed:exposed-dao:$expose_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$expose_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$expose_version")
    implementation("com.zaxxer:HikariCP:2.7.8")
    implementation("org.postgresql:postgresql:42.2.2")
    implementation("org.flywaydb:flyway-core:5.2.4")
}
