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
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    implementation("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

fun getFinalJarAbsolutePath(): String {
    val shadowJarTask = tasks.reobfJar
    return shadowJarTask.get().outputJar.get().asFile.absolutePath
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

task("cpLocal")
{
    val bashPath = project.property("bash.path") as String

    doLast {
        val buildPath = getFinalJarAbsolutePath()
        val cpProcess = ProcessBuilder(bashPath, "-c", "${rootDir.absolutePath}/cp.sh $buildPath ${rootDir.absolutePath}").start()

        if(cpProcess.waitFor() != 0)
        {
            val errorMessage = cpProcess.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to cp PluginValidator!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${cpProcess.exitValue()})")
        }
    }
}

task("scpRemote")
{
    val bashPath = project.property("bash.path") as String
    val user = project.property("remote.user") as String
    val hostname = project.property("remote.hostname") as String

    doLast {
        val scpProcess = ProcessBuilder(bashPath, "-c", "./scp.sh ${getFinalJarAbsolutePath()} $user $hostname").start()

        if (scpProcess.waitFor() != 0)
        {
            val errorMessage = scpProcess.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to scp PluginValidator!\n" +
                    "$errorMessage\n" +
                    "(exit code: ${scpProcess.exitValue()})")
        }
    }
}

task("deployLocal")
{
    dependsOn(tasks.clean, tasks.build, rootProject.tasks.named("stopLocal"), tasks.named("cpLocal"))

    tasks.build.configure {
        mustRunAfter(tasks.clean)
    }

    rootProject.tasks.named("stopLocal").configure {
        mustRunAfter(tasks.build)
    }

    tasks.named("cpLocal").configure {
        mustRunAfter(rootProject.tasks.named("stopLocal"))
    }
}

task("deployRemote")
{
    dependsOn(tasks.clean, tasks.build, rootProject.tasks.named("stopRemote"), tasks.named("scpRemote"))

    tasks.build.configure {
        mustRunAfter(tasks.clean)
    }

    rootProject.tasks.named("stopRemote").configure {
        mustRunAfter(tasks.build)
    }

    tasks.named("scpRemote").configure {
        mustRunAfter(rootProject.tasks.named("stopRemote"))
    }
}
