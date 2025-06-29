plugins {
    id("java")
}

group = "me.kebluk.tidefly"
version = "1.0.0"

val apiVersion = "1.21"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$apiVersion-R0.1-SNAPSHOT")
}

tasks.processResources {
    filesMatching(listOf("plugin.yml", "paper-plugin.yml")) {
        expand(
            "version" to version,
            "apiVersion" to apiVersion
        )
    }
}

tasks.withType<Jar> {
    archiveFileName.set("TideFly-$version-Paper-$apiVersion.jar")
}