plugins {
    id("java")
    id("java-library")
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    paperweight.devBundle("com.eul4", "1.21.1-R0.1-SNAPSHOT")
    compileOnlyApi("io.papermc.paper:paper-bundler:1.21.1-R0.1-SNAPSHOT-mojmap")
    compileOnlyApi("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
