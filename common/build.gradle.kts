plugins {
    id("java")
    id("java-library")
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
//    api("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    api("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnlyApi("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
