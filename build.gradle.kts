plugins {
    id("java")
    id("io.github.patrick.remapper") version "1.4.0"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

task("deploy")
{
    dependsOn("plugin:clean")
    dependsOn("plugin:shadowJar")
    dependsOn("plugin:remap")

    doLast {
        val process = ProcessBuilder("./stop.sh").start()

        if(process.waitFor() != 0)
        {
            throw GradleException("Failed to stop server! (exit code: ${process.exitValue()})")
        }

        println("Sleeping 3s...")
        Thread.sleep(3000L);

        val scpProcess = ProcessBuilder("./scp.sh").start()

        if(scpProcess.waitFor() != 0)
        {
            throw GradleException("Failed to deploy: Plugin (exit code: ${scpProcess.exitValue()})")
        }
    }
}