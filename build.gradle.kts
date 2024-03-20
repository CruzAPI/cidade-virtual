plugins {
    id("java")
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

task("deploy")
{
    dependsOn("plugin:shadowJar")

    doLast {
        val user = "cruzapi"
        val host = "45.233.112.53"

        val containerName = "town"

        println("Deploying in remote...")
        val pluginDeployProcess = Runtime.getRuntime().exec("scp ./plugin/build/libs/*.jar $user@$host:~/cidade-virtual/town/plugins")

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
    }
}