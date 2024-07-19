plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.eul4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.20-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.mindrot:jbcrypt:0.4")
}

tasks.shadowJar {
    shouldRunAfter(tasks.clean)
    mergeServiceFiles()

    archiveClassifier = null

    dependencies {
        include(dependency("org.mindrot:jbcrypt:0.4"))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
