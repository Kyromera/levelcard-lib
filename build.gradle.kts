plugins {
    id("java")
    id("application")
    kotlin("jvm") version "1.8.0"
}

group = "me.diamondforge.kyromera"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("me.diamondforge.kyromera.levelcardlib.example.LevelCardApp")
}

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/skija/maven")
}

dependencies {
    // Skija dependencies
    implementation("org.jetbrains.skija:skija-windows:0.93.6")

    // HTTP client for downloading images
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
