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

task("stopRemote")
{
    val bashPath = project.property("bash.path") as String
    val user = project.property("remote.user") as String
    val hostname = project.property("remote.hostname") as String

    doLast {
        val process = ProcessBuilder(bashPath, "-c", "${buildFile.parent}/stop-remote.sh $user $hostname").start()

        if(process.waitFor() != 0)
        {
            val errorMessage = process.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to stop server!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${process.exitValue()})")
        }
    }
}

task("stopLocal")
{
    val bashPath = project.property("bash.path") as String

    doLast {
        val process = ProcessBuilder(bashPath, "-c", "${buildFile.parent}/stop-local.sh").start()

        if(process.waitFor() != 0)
        {
            val errorMessage = process.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to stop server$errorMessage (exit code: ${process.exitValue()})")
        }
    }
}

task("deployAllLocal")
{
    val plugin = project("plugin")
    val pluginValidator = project("plugin-validator")

    val stopLocalTask = tasks.named("stopLocal")

    dependsOn(tasks.clean, tasks.build, stopLocalTask)

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
        }
    }
}

tasks.build {
    dependsOn(subprojects.map { it.tasks.build })
}