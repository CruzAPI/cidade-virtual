import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.github.patrick.remapper") version "1.4.0"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation(project(":common"))
    implementation(platform("com.intellectualsites.bom:bom-newest:1.42"))
    implementation(project(mapOf("path" to ":plugin-validator"))) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.remap {
    shouldRunAfter(tasks.shadowJar)
    version.set("1.20.4")
}

tasks.shadowJar {
    shouldRunAfter(tasks.clean)
    mergeServiceFiles()

    archiveClassifier = null

    dependencies {
        include(project(":common"))
    }
}