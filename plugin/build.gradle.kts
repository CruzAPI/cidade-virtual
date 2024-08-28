plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://maven.enginehub.org/repo")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation(project(":common"))
//    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    paperweight.devBundle("com.eul4", "1.21.1-R0.1-SNAPSHOT")
    implementation(platform("com.intellectualsites.bom:bom-newest:1.46"))
    implementation(project(mapOf("path" to ":plugin-validator"))) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.shadowJar {
    shouldRunAfter(tasks.clean)
    mergeServiceFiles()

    archiveClassifier = null

    dependencies {
        include(project(":common"))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
