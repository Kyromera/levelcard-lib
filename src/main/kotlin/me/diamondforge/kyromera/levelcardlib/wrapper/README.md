# JDA Level Card Wrapper

A simple and powerful wrapper for the Level Card Library that provides seamless integration with JDA (Java Discord API).

## Features

- Easy integration with JDA User and Member objects
- Automatic handling of Discord avatars and online status
- Extension functions for convenient usage
- Bulletproof error handling with fallbacks
- Fluent builder API for customization

## Usage Examples

### Basic Usage

```kotlin
// Create a level card for a JDA User
val levelCard = JDALevelCard.builder(user)
    .rank(5)
    .level(10)
    .xp(250, 500) // currentXP, nextLevelXP
    .messagesCount(1500) // Optional
    .voiceTime("10h") // Optional
    .streak(7) // Optional
    .build()

// Save the image
ImageIO.write(levelCard, "png", File("level-card.png"))
```

### Using with Guild Members

```kotlin
// Create a level card for a JDA Member (uses nickname and role color automatically)
val levelCard = JDALevelCard.builder(member)
    .rank(5)
    .level(10)
    .xp(250, 500) // currentXP, nextLevelXP
    .messagesCount(1500) // Optional
    .build()
```

### Using Extension Functions

```kotlin
// Create a level card using extension function
val levelCard = user.createLevelCard()
    .rank(5)
    .level(10)
    .xp(250, 500) // currentXP, nextLevelXP
    .streak(7) // Optional
    .build()

// Send directly to a channel
levelCard.sendToChannel(channel, "level-card.png", "Here's your level card!")

// Or use the all-in-one convenience method
user.sendLevelCard(
    channel = channel,
    rank = 5,
    level = 10,
    currentXP = 250,
    nextLevelXP = 500,
    messagesCount = 1500 // Optional
)
```

## Customization Options

### Online Status

The wrapper automatically detects the user's online status from the Member object. You can also set a custom status:

```kotlin
JDALevelCard.builder(user)
    .onlineStatus(OnlineStatus.DND)
    .build()
```

### Avatar Handling

By default, the wrapper downloads the user's avatar for best quality. You can disable this to use the URL directly:

```kotlin
JDALevelCard.builder(user)
    .downloadAvatar(false)
    .build()
```

### Card Appearance

Customize the card dimensions and colors:

```kotlin
JDALevelCard.builder(user)
    .dimensions(500, 280)
    .primaryColor(0xFF00FF00.toInt()) // Green primary color
    .secondaryColor(0xFF80FF80.toInt()) // Light green for gradients
    .backgroundColor(0xFF0F1729.toInt()) // Dark blue-gray background
    .textColor(0xFFFFFFFF.toInt()) // White text
    .accentColor(0xFF00FF00.toInt()) // Green accent color
    .build()
```

### Status Indicator

Control the visibility of the status indicator:

```kotlin
JDALevelCard.builder(user)
    .showStatusIndicator(false)
    .build()
```

### Generation Time

Show or hide the generation time on the card:

```kotlin
JDALevelCard.builder(user)
    .showGenerationTime(true)
    .build()
```

### Direct LevelCard Usage

For more advanced customization, you can create a LevelCard instance directly:

```kotlin
// Create user data
val userData = UserData.Builder("Username")
    .discriminator("1234")
    .rank(5)
    .level(10)
    .xp(250, 500)
    .messagesCount(1500)
    .voiceTime("10h")
    .streak(7)
    .avatarBytes(avatarBytes) // or .avatarUrl("https://example.com/avatar.png")
    .onlineStatus(OnlineStatus.ONLINE)
    .showStatusIndicator(true)
    .build()

// Create color configuration
val colorConfig = ColorConfig(
    primaryColor = 0xFF00FF00.toInt(), // Green
    secondaryColor = 0xFF80FF80.toInt(), // Light green
    backgroundColor = 0xFF0F1729.toInt(), // Dark blue-gray
    textColor = 0xFFFFFFFF.toInt(), // White
    accentColor = 0xFF00FF00.toInt() // Green accent
)

// Create the level card
val levelCard = LevelCard(userData, colorConfig)

// Save as image
levelCard.saveAsImage("level-card.png", 500, 280)
```

This approach gives you complete control over all aspects of the level card.

## Integration with Bot Commands

Here's an example of how to integrate with a slash command:

```kotlin
@JDASlashCommand(name = "level", description = "Show your level card")
fun levelCommand(event: SlashCommandInteractionEvent) {
    event.deferReply().queue()

    val member = event.member ?: return
    val rank = 5 // calculate from your ranking system
    val level = 10 // calculate from your leveling system
    val currentXP = 250 // Get from your database
    val nextLevelXP = 500 // calculate from your leveling system
    val messagesCount = 1500 // Optional: get from your database
    val voiceTime = "10h" // Optional: calculate from your voice tracking
    val streak = 7 // Optional: calculate from your streak system

    val levelCard = member.createLevelCard()
        .rank(rank)
        .level(level)
        .xp(currentXP, nextLevelXP)
        .messagesCount(messagesCount)
        .voiceTime(voiceTime)
        .streak(streak)
        .build()

    val imageBytes = levelCard.toByteArray()
    val fileUpload = FileUpload.fromData(imageBytes, "level-card.png")

    event.hook.sendFiles(fileUpload).queue()
}
```

## Error Handling

The wrapper includes robust error handling:

- If avatar download fails, it falls back to using the URL
- If member color is not available, it uses the default accent color
- If online status cannot be determined, it defaults to ONLINE
