
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("com.expediagroup.graphql") version "8.0.0"
    id("app.cash.sqldelight") version "2.0.2"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = false
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")

    implementation("com.expediagroup", "graphql-kotlin-ktor-server", "8.0.0")
    implementation("com.expediagroup", "graphql-kotlin-schema-generator", "8.0.0")

    implementation("com.graphql-java:graphql-java-extended-scalars:22.0")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.2")

    implementation("io.insert-koin:koin-ktor:3.5.6")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.6")

    implementation("com.zaxxer:HikariCP:5.1.0")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

graphql {
    schema {
        packages = listOf("com.example")
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.generated.example")
            dialect("app.cash.sqldelight:postgresql-dialect:2.0.2")
        }
    }
}
