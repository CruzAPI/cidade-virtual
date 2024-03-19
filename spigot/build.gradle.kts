plugins {
    id("java")
    id("io.github.patrick.remapper") version "1.4.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT:remapped-mojang")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Spigot 1.18+ requires Java 17+
    }
}

tasks.remap {
    version.set("1.20.4")
}

tasks.build {
    dependsOn(tasks.remap)
}