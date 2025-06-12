package me.diamondforge.kyromera.levelcardlib.wrapper

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
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
 */
fun BufferedImage.toByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(this, "png", outputStream)
    return outputStream.toByteArray()
}

/**
 * Sends this level card image to the specified channel.
 *
 * @param channel The channel to send the image to
 * @param fileName The name of the file (default: "level-card.png")
 * @param content Optional message content to send with the image
 * @return The sent message
 */
fun BufferedImage.sendToChannel(
    channel: MessageChannel,
    fileName: String = "level-card.png",
    content: String? = null
): Message {
    val imageBytes = this.toByteArray()
    val fileUpload = FileUpload.fromData(imageBytes, fileName)

    return if (content != null) {
        channel.sendMessage(content).addFiles(fileUpload).complete()
    } else {
        channel.sendFiles(fileUpload).complete()
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
 * @param fileName The name of the file (default: "level-card.png")
 * @param content Optional message content to send with the image
 * @return The sent message
 */
fun User.sendLevelCard(
    channel: MessageChannel,
    rank: Int,
    level: Int,
    currentXP: Int,
    maxXpForCurrentLevel: Int,
    fileName: String = "level-card.png",
    content: String? = null
): Message {
    val levelCard = this.createLevelCard()
        .rank(rank)
        .level(level)
        .xp(0, maxXpForCurrentLevel, currentXP)
        .build()

    return levelCard.sendToChannel(channel, fileName, content)
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
 * @param fileName The name of the file (default: "level-card.png")
 * @param content Optional message content to send with the image
 * @return The sent message
 */
fun Member.sendLevelCard(
    channel: MessageChannel,
    rank: Int,
    level: Int,
    currentXP: Int,
    maxXpForCurrentLevel: Int,
    fileName: String = "level-card.png",
    content: String? = null
): Message {
    val levelCard = this.createLevelCard()
        .rank(rank)
        .level(level)
        .xp(0, maxXpForCurrentLevel, currentXP)
        .build()

    return levelCard.sendToChannel(channel, fileName, content)
}
