import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("com.diffplug.spotless") version "5.15.0"
    java
    application
}
spotless {
    kotlin {
        // version, setUseExperimental, userData and editorConfigOverride are all optional
        ktlint("0.45.2")
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("net.dv8tion:JDA:5.0.0-beta.5")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

application {
    mainClass.set("MainKt")
}
