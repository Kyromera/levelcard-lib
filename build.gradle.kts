plugins {
    id("java")
    id("application")
    id("maven-publish")
    kotlin("jvm") version "2.1.0"
}

group = "me.diamondforge.kyromera"
version = "1.0.0"

application {
    mainClass.set("me.diamondforge.kyromera.levelcardlib.example.LevelCardApp")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    // Skiko dependencies - common
    implementation("org.jetbrains.skiko:skiko:0.9.4.2")

    // Platform-specific Skiko dependencies
    implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-x64:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.9.4.2")

    // HTTP client for downloading images
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

// Create a fat JAR with all dependencies
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "me.diamondforge.kyromera.levelcardlib.example.LevelCardApp"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

// Configure publishing for JitPack
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["java"])

            // Also publish the fat JAR as a separate artifact
            artifact(tasks["fatJar"])
        }
    }
}
