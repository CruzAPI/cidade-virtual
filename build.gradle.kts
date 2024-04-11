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
        val user = "cruzapi"
        val host = "45.233.112.53"

        val containerName = "cidade-virtual-server"

        println("Stopping server...")
        val stopServerProcess = Runtime.getRuntime().exec("ssh -t $user@$host /bin/bash -ic \"stop\\ $containerName\"")

        if(stopServerProcess.waitFor() != 0)
        {
            throw StopExecutionException("Failed to stop server! (exit code: ${stopServerProcess.exitValue()})")
        }

        println("Sleeping 10s...")
        Thread.sleep(10000L);

        println("Deploying in remote...")
        val pluginDeployProcess = Runtime.getRuntime().exec("scp ./plugin/build/libs/*.jar $user@$host:~/cidade-virtual/server/plugins")

        if(pluginDeployProcess.waitFor() != 0)
        {
            throw StopExecutionException("Failed to deploy: Plugin (exit code: ${pluginDeployProcess.exitValue()})")
        }
    }
}