plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val bashPath = project.property("bash.path") as String
val user = project.property("remote.user") as String
val hostname = project.property("remote.hostname") as String
val resetTown: Boolean = project.findProperty("resetTowns")?.toString()?.toBoolean() ?: false

task("resetTownsLocal")
{
    doLast {
        val process = ProcessBuilder(bashPath, "-c", "${rootDir.absolutePath}/reset-towns-local.sh").start()
        val exitCode = process.waitFor()
        val errorMessage = process.errorStream.bufferedReader().use { it.readText() }

        if(exitCode != 0)
        {
            throw GradleException("Failed to reset towns!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        }
    }
}

task("resetTownsRemote") {
    doLast {
        // Correctly handle file path separators for different OS
        val scriptPath = "${rootDir.absolutePath.replace("\\", "/")}/reset-towns-remote.sh"

        val process = ProcessBuilder(bashPath, "-c", "$scriptPath $user $hostname").start()
        val exitCode = process.waitFor()

        // Read the error stream once and store the result
        val errorMessage = process.errorStream.bufferedReader().use { it.readText() }

        if (exitCode != 0) {
            throw GradleException("Failed to reset towns!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        }
    }
}


task("stopLocal")
{
    doLast {
        val process = ProcessBuilder(bashPath, "-c", "${buildFile.parent}/stop-local.sh").start()
        val exitCode = process.waitFor()
        val errorMessage = process.errorStream.bufferedReader().use { it.readText() }

        if(exitCode == 1)
        {
            println("WARN: $errorMessage")
        }
        else if(exitCode != 0)
        {
            throw GradleException("Failed to stop server!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        }
        else
        {
            while(!isLocalContainerStopped())
            {
                println("Sleeping 1s... Waiting container to stop.")
                Thread.sleep(1000L)
            }

            println("Container stopped!")
        }
    }
}

task("startLocal")
{
    doLast {
        val process = ProcessBuilder(bashPath, "-c", "docker compose up -d").start()

        if(process.waitFor() != 0)
        {
            val errorMessage = process.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to start server!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        }
    }
}

task("stopRemote") {
    doLast {
        // Correctly handle file path separators for different OS
        val scriptPath = "${buildFile.parent.replace("\\", "/")}/stop-remote.sh"

        val process = ProcessBuilder(bashPath, "-c", "$scriptPath $user $hostname").start()
        val exitCode = process.waitFor()

        // Read the error stream once and store the result
        val errorMessage = process.errorStream.bufferedReader().use { it.readText() }

        if (exitCode == 1) {
            println("WARN: $errorMessage")
        } else if (exitCode != 0) {
            // Throw an exception with the previously read error message
            throw GradleException("Failed to stop server!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        } else {
            while (!isRemoteContainerStopped()) {
                println("Sleeping 1s... Waiting for the container to stop.")
                Thread.sleep(1000L)
            }
            println("Container stopped!")
        }
    }
}


task("startRemote") {
    doLast {
        // Correctly handle file path separators for different OS
        val scriptPath = "${buildFile.parent.replace("\\", "/")}/start-remote.sh"

        val process = ProcessBuilder(bashPath, "-c", "$scriptPath $user $hostname").start()

        if (process.waitFor() != 0) {
            val errorMessage = process.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to start server!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        }
    }
}


configure(subprojects.filter { it.name == "plugin" || it.name == "plugin-validator" }) {
    this.afterEvaluate {
        fun getFinalJarAbsolutePath(): String = tasks.reobfJar.get().outputJar.get().asFile.absolutePath

        task("cpLocal")
        {
            doLast {
                val buildPath = getFinalJarAbsolutePath()
                val cpProcess = ProcessBuilder(bashPath, "-c", "${rootDir.absolutePath}/cp.sh $buildPath ${rootDir.absolutePath}").start()

                if(cpProcess.waitFor() != 0)
                {
                    val errorMessage = cpProcess.errorStream.bufferedReader().use { it.readText() }
                    throw GradleException("Failed to cp ${this@configure.name}!\n" +
                            "$errorMessage\n" +
                            "(exit code: ${cpProcess.exitValue()})")
                }
            }
        }

        task("scpRemote") {
            doLast {
                // Correctly handle file path separators for different OS
                val scriptPath = "${rootDir.absolutePath.replace("\\", "/")}/scp.sh"
                val jarPath = getFinalJarAbsolutePath().replace("\\", "/")

                val scpProcess = ProcessBuilder(bashPath, "-c", "$scriptPath $jarPath $user $hostname").start()

                if (scpProcess.waitFor() != 0) {
                    val errorMessage = scpProcess.errorStream.bufferedReader().use { it.readText() }
                    throw GradleException("Failed to scp ${this@configure.name}!\n" +
                            "$errorMessage\n" +
                            "(exit code: ${scpProcess.exitValue()})")
                }
            }
        }


        task("deployLocal")
        {
            val stopLocalTask = rootProject.tasks.named("stopLocal")
            val cpLocalTask = tasks.named("cpLocal")
            val startLocalTask = rootProject.tasks.named("startLocal")
            val resetTownsLocalTask = rootProject.tasks.named("resetTownsLocal")

            dependsOn(tasks.clean, tasks.build, stopLocalTask, cpLocalTask, startLocalTask)

            if(resetTown)
            {
                dependsOn(resetTownsLocalTask)
            }

            tasks.build {
                mustRunAfter(tasks.clean)
            }

            stopLocalTask {
                mustRunAfter(tasks.build)
            }

            if(resetTown)
            {
                resetTownsLocalTask {
                    mustRunAfter(stopLocalTask)
                }

                cpLocalTask {
                    mustRunAfter(resetTownsLocalTask)
                }
            }
            else
            {
                cpLocalTask {
                    mustRunAfter(stopLocalTask)
                }
            }

            startLocalTask {
                mustRunAfter(cpLocalTask)
            }
        }

        task("deployRemote")
        {
            val stopRemoteTask = rootProject.tasks.named("stopRemote")
            val scpRemoteTask = tasks.named("scpRemote")
            val startRemoteTask = rootProject.tasks.named("startRemote")
            val resetTownsRemoteTask = rootProject.tasks.named("resetTownsRemote")

            dependsOn(tasks.clean, tasks.build, stopRemoteTask, scpRemoteTask, startRemoteTask)

            if(resetTown)
            {
                dependsOn(resetTownsRemoteTask)
            }

            tasks.build.configure {
                mustRunAfter(tasks.clean)
            }

            stopRemoteTask.configure {
                mustRunAfter(tasks.build)
            }

            scpRemoteTask.configure {
                mustRunAfter(stopRemoteTask)
            }

            if(resetTown)
            {
                resetTownsRemoteTask {
                    mustRunAfter(stopRemoteTask)
                }

                scpRemoteTask {
                    mustRunAfter(resetTownsRemoteTask)
                }
            }
            else
            {
                scpRemoteTask {
                    mustRunAfter(stopRemoteTask)
                }
            }

            startRemoteTask.configure {
                mustRunAfter(scpRemoteTask)
            }
        }
    }
}

fun isRemoteContainerStopped(): Boolean
{
    val bashPath = project.property("bash.path") as String
    val process = ProcessBuilder(bashPath, "-c", "${rootDir.absolutePath}/check-remote-container.sh $user $hostname").start()

    return process.waitFor() != 0
}

fun isLocalContainerStopped(): Boolean
{
    val bashPath = project.property("bash.path") as String
    val process = ProcessBuilder(bashPath, "-c", "${rootDir.absolutePath}/check-container.sh").start()

    return process.waitFor() != 0
}

task("deployAllLocal")
{
    val plugin = project("plugin")
    val pluginValidator = project("plugin-validator")

    val stopLocalTask = tasks.named("stopLocal")
    val startLocalTask = tasks.named("startLocal")

    dependsOn(tasks.clean, tasks.build, stopLocalTask, startLocalTask)

    tasks.build.configure {
        mustRunAfter(tasks.clean)
    }

    stopLocalTask.configure {
        mustRunAfter(tasks.build)
    }

    plugin.afterEvaluate {
        val cpPluginTask = plugin.tasks.named("cpLocal")
        this@task.dependsOn(cpPluginTask)

        pluginValidator.afterEvaluate {
            val cpPluginValidatorTask = pluginValidator.tasks.named("cpLocal")
            this@task.dependsOn(cpPluginValidatorTask)

            cpPluginTask.configure {
                mustRunAfter(cpPluginValidatorTask)
            }

            startLocalTask.configure {
                mustRunAfter(cpPluginTask)
            }
        }
    }
}

tasks.build {
    dependsOn(subprojects.map { it.tasks.build })
}