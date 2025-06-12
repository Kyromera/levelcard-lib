plugins {
    id("java")
    id("application")
    kotlin("jvm") version "2.1.21"
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
    implementation("org.jetbrains.skiko:skiko:0.9.17")
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

