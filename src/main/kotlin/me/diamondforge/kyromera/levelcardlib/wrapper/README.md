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
    .xp(100, 500, 250)
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
    .xp(100, 500, 250)
    .build()
```

### Using Extension Functions

```kotlin
// Create a level card using extension function
val levelCard = user.createLevelCard()
    .rank(5)
    .level(10)
    .xp(100, 500, 250)
    .build()

// Send directly to a channel
levelCard.sendToChannel(channel, "level-card.png", "Here's your level card!")

// Or use the all-in-one convenience method
user.sendLevelCard(
    channel = channel,
    rank = 5,
    level = 10,
    currentXP = 250,
    maxXpForCurrentLevel = 500
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

Customize the card dimensions and accent color:

```kotlin
JDALevelCard.builder(user)
    .dimensions(1000, 350)
    .accentColor(0xFF00FF00.toInt()) // Green
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

### Advanced Customization

For more detailed customization, you can provide a custom CardConfiguration object:

```kotlin
// Create a custom configuration
val customConfig = CardConfiguration.Builder()
    .dimensions(1000, 350)
    .accentColor(0xFF00FF00.toInt())
    .avatarConfig(180, 45)
    .textMargin(290)
    .textOffsets(85, 135, 205, 270)
    .progressBarConfig(35, 220, 55)
    .fontSizes(48f, 34f, 28f, 21f)
    .backgroundConfig(14f, 26f)
    .build()

// Use the custom configuration
JDALevelCard.builder(user)
    .rank(5)
    .level(10)
    .xp(100, 500, 250)
    .customConfig(customConfig)
    .build()
```

### Precise Element Positioning

For even more control over the layout, you can use the LayoutConfig to precisely position each element on the card:

```kotlin
// Create a layout configuration
val layout = LayoutConfig.Builder()
    .avatarPosition(50, 50)
    .usernamePosition(270, 85)
    .rankLevelPosition(270, 135)
    .xpTextPosition(270, 205)
    .progressBarPosition(270, 220, 50)
    .build()

// Apply the layout configuration
JDALevelCard.builder(user)
    .rank(5)
    .level(10)
    .xp(100, 500, 250)
    .layoutConfig(layout)
    .build()
```

You can also use relative offsets from default positions:

```kotlin
val layout = LayoutConfig.Builder()
    .usernameOffset(xOffset = 10, yOffset = -5)
    .progressBarOffset(yOffset = 10)
    .build()
```

Note: When using a custom configuration, any explicitly set values from the builder (dimensions, accent color, showGenerationTime) will override the corresponding values in the custom configuration. The layoutConfig can be used with or without a custom CardConfiguration.

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
    val maxXpForCurrentLevel = 500 // calculate from your leveling system
    val minXpForCurrentLevel = 0 // calculate from your leveling system

    val levelCard = member.createLevelCard()
        .rank(rank)
        .level(level)
        .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
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
