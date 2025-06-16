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

### Custom Layout Configuration

You can customize the position of individual elements on the card using the `LayoutConfig` class:

```kotlin
// Create a custom layout configuration with absolute positions
val layoutConfig = LayoutConfig.Builder()
    .avatarPosition(50, 50) // Custom X, Y position for avatar
    .usernamePosition(270, 83) // Custom X, Y position for username text
    .rankLevelPosition(270, 133) // Custom X, Y position for rank/level text
    .xpTextPosition(270, 200) // Custom X, Y position for XP text
    .timeTextPosition(270, 267) // Custom X, Y position for generation time text
    .progressBarPosition(270, 217, 50) // Custom X, Y position and right margin for progress bar
    .build()

// Apply the custom layout to the level card
val levelCard = LevelCardDrawer.builder("Username")
    .avatarBytes(avatarImageBytes)
    .rank(5)
    .level(10)
    .xp(1500, 2000, 1750)
    .layoutConfig(layoutConfig) // Apply the custom layout
    .build()
```

This allows for precise control over the positioning of all elements on the card, making it easy to create custom layouts for different use cases.

### Using Relative Offsets

You can also specify positions as offsets relative to the default positions, which is useful for making small adjustments:

```kotlin
// Create a layout configuration with relative offsets
val layoutConfig = LayoutConfig.Builder()
    .avatarOffset(0, 0) // Offset from default avatar position
    .usernameOffset(0, 0) // Offset from default username text position
    .rankLevelOffset(0, 0) // Offset from default rank/level text position
    .xpTextOffset(0, 0) // Offset from default XP text position
    .timeTextOffset(0, 0) // Offset from default time text position
    .progressBarOffset(0, 0, 0) // Offset from default progress bar position and margin
    .build()
```

The offset methods accept values that will be added to the default positions. For example, if the default X position for the username is 270, then `.usernameOffset(-5, 0)` will set the X position to 265.

You can also use the default offset value (0) by calling the methods without parameters:

```kotlin
val layoutConfig = LayoutConfig.Builder()
    .avatarOffset() // Uses default offset of 0 for both X and Y
    .usernameOffset() // Uses default offset of 0 for both X and Y
    .rankLevelOffset() // Uses default offset of 0 for both X and Y
    .xpTextOffset() // Uses default offset of 0 for both X and Y
    .timeTextOffset() // Uses default offset of 0 for both X and Y
    .progressBarOffset() // Uses default offset of 0 for X, Y, and right margin
    .build()
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
