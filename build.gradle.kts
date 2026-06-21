plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "me.kaeternn"
version = project.property("projectVersion")!!

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 21
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.processResources {
    val projectVersion = project.property("projectVersion")
    inputs.property("projectVersion", projectVersion)
    filesMatching("paper-plugin.yml") {
        expand(mapOf("projectVersion" to projectVersion))
    }
    filesMatching("config.yml") {
        expand(mapOf("projectVersion" to projectVersion))
    }
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}

tasks {
    runServer {
        //minecraftVersion("1.21.6")
        //minecraftVersion("1.21.7")
        //minecraftVersion("1.21.8")
        //minecraftVersion("1.21.9")
        //minecraftVersion("1.21.10")
        //minecraftVersion("1.21.11")
        //minecraftVersion("26.1.2")
        minecraftVersion("26.2")
    }
}