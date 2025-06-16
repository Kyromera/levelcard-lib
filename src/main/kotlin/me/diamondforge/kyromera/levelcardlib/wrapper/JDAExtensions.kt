package me.diamondforge.kyromera.levelcardlib.wrapper

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Extension functions for JDA entities to easily create and use level cards.
 */

/**
 * Creates a level card builder for this User.
 *
 * @return A new JDALevelCard.Builder instance
 */
fun User.createLevelCard(): JDALevelCard.Builder {
    return JDALevelCard.builder(this)
}

/**
 * Creates a level card builder for this Member.
 * This will use the member's nickname and color if available.
 *
 * @return A new JDALevelCard.Builder instance
 */
fun Member.createLevelCard(): JDALevelCard.Builder {
    return JDALevelCard.builder(this)
}

/**
 * Converts a BufferedImage to a byte array in PNG format.
 *
 * @return The image as a byte array
 * @throws IOException If the image cannot be converted
 */
fun BufferedImage.toByteArray(): ByteArray {
    if (this.width <= 0 || this.height <= 0) {
        throw IOException("Invalid image dimensions: ${this.width}x${this.height}")
    }

    try {
        val outputStream = ByteArrayOutputStream()
        if (!ImageIO.write(this, "png", outputStream)) {
            throw IOException("Failed to write image as PNG")
        }
        val bytes = outputStream.toByteArray()
        if (bytes.isEmpty()) {
            throw IOException("Generated image is empty")
        }
        return bytes
    } catch (e: Exception) {
        throw IOException("Failed to convert image to byte array: ${e.message}", e)
    }
}

/**
 * Sends this level card image to the specified channel.
 *
 * @param channel The channel to send the image to
 * @param fileName The name of the file (default: "level-card.png")
 * @param content Optional message content to send with the image
 * @return The sent message
 * @throws IOException If the image cannot be converted or sent
 */
fun BufferedImage.sendToChannel(
    channel: MessageChannel,
    fileName: String = "level-card.png",
    content: String? = null
): Message {
    if (channel == null) {
        throw IllegalArgumentException("Channel cannot be null")
    }

    // Validate filename
    val safeFileName = if (fileName.isBlank()) "level-card.png" else fileName

    try {
        // Convert image to byte array
        val imageBytes = this.toByteArray()

        // Create file upload
        val fileUpload = FileUpload.fromData(imageBytes, safeFileName)

        // Send to channel with appropriate error handling
        return try {
            if (content != null) {
                channel.sendMessage(content).addFiles(fileUpload).complete()
            } else {
                channel.sendFiles(fileUpload).complete()
            }
        } catch (e: Exception) {
            throw IOException("Failed to send message to channel: ${e.message}", e)
        }
    } catch (e: IOException) {
        throw e
    } catch (e: Exception) {
        throw IOException("Failed to prepare image for sending: ${e.message}", e)
    }
}

/**
 * Creates and sends a level card for this User to the specified channel.
 * This is a convenience method that combines createLevelCard() and sendToChannel().
 *
 * @param channel The channel to send the level card to
 * @param rank User's rank
 * @param level User's level
 * @param currentXP User's current XP
 * @param maxXpForCurrentLevel Maximum XP for the current level
 * @param minXpForCurrentLevel Minimum XP for the current level (default: 0)
 * @param fileName The name of the file (default: "level-card.png")
 * @param content Optional message content to send with the image
 * @return The sent message
 * @throws IllegalArgumentException If any parameters are invalid
 * @throws IOException If the image cannot be created or sent
 */
fun User.sendLevelCard(
    channel: MessageChannel,
    rank: Int,
    level: Int,
    currentXP: Int,
    maxXpForCurrentLevel: Int,
    minXpForCurrentLevel: Int = 0,
    fileName: String = "level-card.png",
    content: String? = null
): Message {
    if (channel == null) {
        throw IllegalArgumentException("Channel cannot be null")
    }

    // Validate parameters
    if (rank <= 0) {
        throw IllegalArgumentException("Rank must be positive")
    }
    if (level < 0) {
        throw IllegalArgumentException("Level must be non-negative")
    }
    if (minXpForCurrentLevel < 0) {
        throw IllegalArgumentException("Minimum XP must be non-negative")
    }
    if (maxXpForCurrentLevel <= minXpForCurrentLevel) {
        throw IllegalArgumentException("Maximum XP must be greater than minimum XP")
    }
    if (currentXP < minXpForCurrentLevel || currentXP > maxXpForCurrentLevel) {
        throw IllegalArgumentException("Current XP must be between minimum and maximum XP")
    }

    try {
        val levelCard = this.createLevelCard()
            .rank(rank)
            .level(level)
            .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
            .build()

        return levelCard.sendToChannel(channel, fileName, content)
    } catch (e: IOException) {
        throw e
    } catch (e: Exception) {
        throw IOException("Failed to create or send level card: ${e.message}", e)
    }
}

/**
 * Creates and sends a level card for this Member to the specified channel.
 * This is a convenience method that combines createLevelCard() and sendToChannel().
 *
 * @param channel The channel to send the level card to
 * @param rank User's rank
 * @param level User's level
 * @param currentXP User's current XP
 * @param maxXpForCurrentLevel Maximum XP for the current level
 * @param minXpForCurrentLevel Minimum XP for the current level (default: 0)
 * @param fileName The name of the file (default: "level-card.png")
 * @param content Optional message content to send with the image
 * @return The sent message
 * @throws IllegalArgumentException If any parameters are invalid
 * @throws IOException If the image cannot be created or sent
 */
fun Member.sendLevelCard(
    channel: MessageChannel,
    rank: Int,
    level: Int,
    currentXP: Int,
    maxXpForCurrentLevel: Int,
    minXpForCurrentLevel: Int = 0,
    fileName: String = "level-card.png",
    content: String? = null
): Message {
    if (channel == null) {
        throw IllegalArgumentException("Channel cannot be null")
    }

    // Validate parameters
    if (rank <= 0) {
        throw IllegalArgumentException("Rank must be positive")
    }
    if (level < 0) {
        throw IllegalArgumentException("Level must be non-negative")
    }
    if (minXpForCurrentLevel < 0) {
        throw IllegalArgumentException("Minimum XP must be non-negative")
    }
    if (maxXpForCurrentLevel <= minXpForCurrentLevel) {
        throw IllegalArgumentException("Maximum XP must be greater than minimum XP")
    }
    if (currentXP < minXpForCurrentLevel || currentXP > maxXpForCurrentLevel) {
        throw IllegalArgumentException("Current XP must be between minimum and maximum XP")
    }

    try {
        val levelCard = this.createLevelCard()
            .rank(rank)
            .level(level)
            .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
            .build()

        return levelCard.sendToChannel(channel, fileName, content)
    } catch (e: IOException) {
        throw e
    } catch (e: Exception) {
        throw IOException("Failed to create or send level card: ${e.message}", e)
    }
}
