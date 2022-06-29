@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "me.nopox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization)

    implementation(libs.jda.core)
    implementation(libs.jda.kotlin)
    implementation(libs.kotlin.mongo)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.client.cio)
}