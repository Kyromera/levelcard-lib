# Kyromera LevelCard Library

A multi-architecture library for creating level cards.

## Installation

The library is available through JitPack. Add the JitPack repository and dependency to your project.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("me.diamondforge.kyromera:levelcardlib:1.0.0")
}
```

### Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'me.diamondforge.kyromera:levelcardlib:1.0.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>me.diamondforge.kyromera</groupId>
        <artifactId>levelcardlib</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Usage

The library provides functionality for creating level cards. Here's a basic example:

```kotlin
import me.diamondforge.kyromera.levelcardlib.CardConfiguration
import me.diamondforge.kyromera.levelcardlib.CardRenderer
import me.diamondforge.kyromera.levelcardlib.UserData

// Create user data
val userData = UserData.Builder()
    .setUsername("Username")
    .setLevel(10)
    .setXp(1500)
    .setMaxXp(2000)
    .build()

// Create card configuration
val config = CardConfiguration.Builder()
    .setWidth(800)
    .setHeight(300)
    .build()

// Render the card
val cardRenderer = CardRenderer(config)
val image = cardRenderer.renderCard(userData)

// Save or display the image
```

For more detailed examples, see the `LevelCardApp` class in the source code.

## Multi-Architecture Support

This library includes support for multiple platforms:
- Windows x64
- Linux x64
- macOS x64
- macOS arm64

The fat JAR (with classifier 'all') includes all platform-specific dependencies.