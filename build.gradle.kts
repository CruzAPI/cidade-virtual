import java.io.BufferedReader
import java.io.InputStreamReader

plugins {
    id("java")
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

task("deploy")
{
    dependsOn("build")

    val user = "cruzapi"
    val host = "192.168.1.35"
    val containerName = "cidade-virtual-server"

    println("Deploying in remote...")
    val commonDeployProcess = Runtime.getRuntime().exec("scp ./commons/build/libs/*.jar $user@$host:~/cidade-virtual/server/plugins")
    val pluginDeployProcess = Runtime.getRuntime().exec("scp ./plugin/build/libs/*.jar $user@$host:~/cidade-virtual/server/plugins")

    if(commonDeployProcess.waitFor() != 0)
    {
        throw StopExecutionException("Failed to deploy: Commons (exit code: ${commonDeployProcess.exitValue()})")
    }

    if(pluginDeployProcess.waitFor() != 0)
    {
        throw StopExecutionException("Failed to deploy: Plugin (exit code: ${pluginDeployProcess.exitValue()})")
    }

    println("Stopping server...")
    val stopServerProcess = Runtime.getRuntime().exec("ssh -t $user@$host /bin/bash -ic \"stop\\ $containerName\"")

    if(stopServerProcess.waitFor() != 0)
    {
        throw StopExecutionException("Failed to stop server! (exit code: ${stopServerProcess.exitValue()})")
    }

    dependsOn("clean")
}