plugins {
    id("java")
}

group = "com.weesftw.minecraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("com.weesftw.minecraft:wespigot-server:1.0.0.RC1")
}