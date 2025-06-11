plugins {
    id("java")
    id("application")
    kotlin("jvm") version "2.1.0"
}

group = "me.diamondforge.kyromera"
version = "1.0-SNAPSHOT"

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
