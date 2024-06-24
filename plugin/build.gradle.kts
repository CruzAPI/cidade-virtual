plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("io.github.patrick.remapper") version "1.4.2"
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
    implementation(project(":common"))
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    implementation(platform("com.intellectualsites.bom:bom-newest:1.42"))
    implementation(project(mapOf("path" to ":plugin-validator"))) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

fun getFinalJarAbsolutePath(): String {
    val shadowJarTask = tasks.reobfJar
    return shadowJarTask.get().outputJar.get().asFile.absolutePath
}

tasks.shadowJar {
    shouldRunAfter(tasks.clean)
    mergeServiceFiles()

    archiveClassifier = null

    dependencies {
        include(project(":common"))
    }
}

task("deployk")
{
//    dependsOn("plugin:clean")
    dependsOn("build")

    doLast {
//        val bashPath = "C:/Program Files/Git/bin/bash.exe"
        val bashPath = "/bin/bash"
        val process = ProcessBuilder(bashPath, "-c", "../stop.sh").start()
        val jarFileAbsolutPath = getFinalJarAbsolutePath()

        if (process.waitFor() != 0)
        {
            val errorMessage = process.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to stop server$errorMessage (exit code: ${process.exitValue()})")
        }

        println("Sleeping 3s...")
        Thread.sleep(3000L)

        val scpProcess = ProcessBuilder(bashPath, "-c", "./scp.sh $jarFileAbsolutPath").start()

        if (scpProcess.waitFor() != 0)
        {
            val errorMessage = scpProcess.errorStream.bufferedReader().use { it.readText() }
            throw GradleException("Failed to scp: Plugin$errorMessage (exit code: ${scpProcess.exitValue()})")
        }
    }
}