plugins {
    id("java")
    id("io.github.patrick.remapper") version "1.4.2"
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
    implementation("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.20.6-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.remap {
    shouldRunAfter(tasks.build)
    version.set("1.20.6")
}

task("deploy")
{
    dependsOn("clean")
    dependsOn("build")
    dependsOn("remap")

    tasks.named("build").configure {
        mustRunAfter("clean")
    }

    tasks.named("remap").configure {
        mustRunAfter("build")
    }

    doLast {
        val process = ProcessBuilder("../stop.sh").start()

        if(process.waitFor() != 0)
        {
            throw GradleException("Failed to stop server! (exit code: ${process.exitValue()})")
        }

        println("Sleeping 3s...")
        Thread.sleep(3000L)

        val scpProcess = ProcessBuilder("./scp.sh").start()

        if(scpProcess.waitFor() != 0)
        {
            throw GradleException("Failed to deploy: Plugin (exit code: ${scpProcess.exitValue()})")
        }
    }
}