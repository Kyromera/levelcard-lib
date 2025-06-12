# Kyromera LevelCard Library

A multi-architecture library for creating level cards.

## Installation

The library is available through JitPack. Add the dependency to your project.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.kyromera:levelcard-lib:1.0.0")
}
```

### Gradle (Groovy DSL)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.kyromera:levelcard-lib:1.0.0'
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
        <groupId>com.github.kyromera</groupId>
        <artifactId>levelcard-lib</artifactId>
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

## Testing

The library includes comprehensive unit tests for all components:

- CardConfiguration tests
- UserData tests
- LevelCardDrawer tests
- AvatarManager tests
- Enum tests (OnlineStatus, ImageMode)

To run the tests:

```bash
./gradlew test
```

## Continuous Integration

This project uses GitHub Actions for continuous integration. The workflow automatically:

1. Builds the project
2. Runs all tests
3. Creates a fat JAR
4. Tests on multiple platforms (Ubuntu, Windows, macOS)

The workflow is triggered on:
- Push to main/master branches
- Pull requests to main/master branches
- Manual triggers

See the `.github/workflows/build.yml` file for details.
