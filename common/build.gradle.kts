plugins {
    id("java")
    id("java-library")
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    api("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnlyApi("com.weesftw.minecraft:wespigot-server:1.0.0.RC1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
