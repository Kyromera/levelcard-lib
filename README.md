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
    .xp(1750, 2000) // currentXP, nextLevelXP
    .messagesCount(1500) // Optional: messages count
    .voiceTime("10h") // Optional: voice time
    .streak(7) // Optional: day streak
    .accentColor(0xFFEA397C.toInt()) // Pink color
    .dimensions(500, 280) // width, height (new default size)
    .onlineStatus(OnlineStatus.ONLINE)
    .showStatusIndicator(true)
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
    currentXP = 1750,
    nextLevelXP = 2000,
    messagesCount = 1500, // Optional
    voiceTime = "10h", // Optional
    streak = 7, // Optional
    primaryColor = 0xFFEA397C.toInt(), // Pink color
    secondaryColor = 0xFFF06EA9.toInt(), // Lighter pink for gradients
    backgroundColor = 0xFF0F1729.toInt(), // Dark blue-gray
    textColor = 0xFFFFFFFF.toInt(), // White
    accentColor = 0xFFEA397C.toInt(), // Same as primary
    width = 500,
    height = 280,
    onlineStatus = OnlineStatus.ONLINE,
    showStatusIndicator = true
)
```

### JDA Integration

The library includes a wrapper for easy integration with JDA (Java Discord API):

```kotlin
// Create a level card for a JDA User
val levelCard = JDALevelCard.builder(user)
    .rank(5)
    .level(10)
    .xp(250, 500) // currentXP, nextLevelXP
    .messagesCount(1500) // Optional
    .voiceTime("10h") // Optional
    .streak(7) // Optional
    .primaryColor(0xFFEA397C.toInt()) // Optional: customize colors
    .build()

// Or use extension functions
val levelCard = member.createLevelCard()
    .rank(5)
    .level(level)
    .xp(currentXP, maxXP)
    .dimensions(600, 320) // Optional: custom dimensions
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

- ColorConfig tests
- UserData tests
- LevelCard tests
- LevelCardDrawer tests
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
