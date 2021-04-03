plugins {
    java
    id("fabric-loom") version "0.6-SNAPSHOT"
    `maven-publish`
}

base {
    archivesBaseName = "serverside-item-fabric"
    group = "dev.uten2c"
    version = "1.16.5+6"
}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(mutableMapOf("version" to project.version))
    }
}

repositories {
    mavenCentral()
    maven("https://uten2c.github.io/repo/")
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = "1.16.5")
    mappings(group = "net.fabricmc", name = "yarn", version = "1.16.5+build.5", classifier = "v2")

    modImplementation("net.fabricmc:fabric-loader:0.11.3")
}

val remapJar = tasks.getByName<net.fabricmc.loom.task.RemapJarTask>("remapJar")
val remapSourcesJar = tasks.getByName<net.fabricmc.loom.task.RemapSourcesJarTask>("remapSourcesJar")

val sourcesJar = tasks.create<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.base.archivesBaseName
            version = project.version.toString()

            artifact(remapJar) {
                builtBy(remapJar)
            }
            artifact(sourcesJar) {
                builtBy(remapSourcesJar)
            }
        }
    }
    repositories {
        maven {
            url = uri("${System.getProperty("user.home")}/maven-repo")
            println(uri("${System.getProperty("user.home")}/maven-repo"))
        }
    }
}