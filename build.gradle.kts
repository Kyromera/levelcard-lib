plugins {
    id("java")
    id("application")
    id("maven-publish")
    kotlin("jvm") version "2.1.0"
}

group = "me.diamondforge"
version = "1.0.0"

application {
    mainClass.set("me.diamondforge.kyromera.levelcardlib.example.LevelCardApp")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("org.jetbrains.skiko:skiko:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-x64:0.9.4.2")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.9.4.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["fatJar"])

            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            pom {
                name.set("Kyromera LevelCard Library")
                description.set("A multi-architecture library for creating level cards")
                url.set("https://github.com/kyromera/levelcard-lib")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("fthomys")
                        name.set("Fabian Thomys")
                        email.set("git@fthomys.me")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/kyromera/levelcard-lib.git")
                    developerConnection.set("scm:git:ssh://github.com:kyromera/levelcard-lib.git")
                    url.set("https://github.com/kyromera/levelcard-lib")
                }
            }
        }
    }

    repositories {
        maven {
            name = "central"
            url = uri("https://central.sonatype.com/api/v1/publish")
        }
    }
}
