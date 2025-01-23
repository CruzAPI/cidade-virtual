plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.register<Exec>("deployPlugin") {
    group = "deployment"
    description = "Executa o script deployPlugin.sh"

    commandLine("C:/Program Files/Git/bin/bash.exe", "./deployPlugin.sh")
}

tasks.register<Exec>("deployAuth") {
    group = "deployment"
    description = "Executa o script deployAuth.sh"

    commandLine("C:/Program Files/Git/bin/bash.exe", "./deployAuth.sh")
}
