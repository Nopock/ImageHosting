@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "me.nopox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.mongo)

    implementation("net.dv8tion:JDA:5.0.0-alpha.13")
    implementation("com.github.minndevelopment:jda-ktx:d5c5d9d")
}