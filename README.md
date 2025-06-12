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
    implementation("com.github.kyromera:levelcard-lib:version")
}
```

### Gradle (Groovy DSL)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.kyromera:levelcard-lib:version'
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
        <version>version</version>
    </dependency>
</dependencies>
```

## Usage

The library provides functionality for creating level cards. Here's a basic example using the Builder pattern:

### Standard Usage

```kotlin
// Create and render a level card using the Builder pattern
val levelCard = LevelCardDrawer.builder("Username")
    .avatarBytes(avatarImageBytes) // or .avatarUrl("https://example.com/avatar.png")
    .rank(5)
    .level(10)
    .xp(1500, 2000, 1750) // minXpForCurrentLevel, maxXpForCurrentLevel, currentXP
    .accentColor(0xFFEA397C.toInt()) // Pink color
    .dimensions(950, 300) // width, height
    .onlineStatus(OnlineStatus.ONLINE)
    .showStatusIndicator(true)
    .showGenerationTime(false)
    .build()

// Save the image
ImageIO.write(levelCard, "PNG", File("levelcard.png"))
```

### Legacy API

The library also supports a legacy API for backward compatibility:

```kotlin
// Generate a level card using the legacy API
val levelCard = LevelCardDrawer.drawLevelCard(
    avatarBytes = avatarImageBytes,
    avatarUrl = null,
    downloadFromUrl = false,
    username = "Username",
    rank = 5,
    level = 10,
    minXpForCurrentLevel = 1500, // calculated from xp and current level
    maxXpForCurrentLevel = 2000, // calculated from xp and current level
    currentXP = 1750,
    accentColor = 0xFFEA397C.toInt(),
    width = 950,
    height = 300,
    onlineStatus = OnlineStatus.ONLINE,
    showStatusIndicator = true,
    showGenerationTime = false
)
```

### JDA Integration

The library includes a wrapper for easy integration with JDA (Java Discord API):

```kotlin
// Create a level card for a JDA User
val levelCard = JDALevelCard.builder(user)
    .rank(5)
    .level(10)
    .xp(100, 500, 250)
    .build()

// Or use extension functions
val levelCard = member.createLevelCard()
    .rank(5)
    .level(level)
    .xp(0, maxXP, currentXP)
    .build()

// Send directly to a Discord channel
levelCard.sendToChannel(channel, "level-card.png", "Here's your level card!")
```

The JDA wrapper automatically handles:
- User avatars and online status
- Member nicknames and role colors
- Error recovery with fallbacks

For more detailed examples and documentation, see the [JDA Wrapper README](src/main/kotlin/me/diamondforge/kyromera/levelcardlib/wrapper/README.md).

For more general examples, see the `LevelCardApp` class in the source code.

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
- CardRenderer tests
- AvatarManager tests
- StatusIndicator tests
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

# Self-Using infomation
This library is build for the Kyromera Bot. Its not intended for public use, but you can use it if you want, but I won't provide any support for it.
