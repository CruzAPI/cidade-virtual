import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation(project(":common"))
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks {
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()

        archiveClassifier = null

        dependencies {
            include(project(":common"))
        }
    }
}