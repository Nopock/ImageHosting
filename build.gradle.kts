import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.intellij.settings)
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

idea {
    module {
        settings {
            for (sourceSet in kotlin.sourceSets)
                for (sourceDir in sourceSet.kotlin.sourceDirectories)
                    packagePrefix[sourceDir.toRelativeString(projectDir)] = "me.nopox.image"
        }
    }
}