import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")

}

dependencies {
    implementation(project(":common"))
    implementation(platform("com.intellectualsites.bom:bom-newest:1.42")) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
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