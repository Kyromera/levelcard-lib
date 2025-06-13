package me.diamondforge.kyromera.levelcardlib.wrapper

import me.diamondforge.kyromera.levelcardlib.ColorConfig
import me.diamondforge.kyromera.levelcardlib.LevelCard
import me.diamondforge.kyromera.levelcardlib.LevelCardDrawer
import me.diamondforge.kyromera.levelcardlib.UserData
import me.diamondforge.kyromera.levelcardlib.ImageMode
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Surface

/**
 * A wrapper class for the LevelCard that provides easy integration with JDA.
 * This class simplifies the process of creating level cards for Discord users.
 */
object JDALevelCard {

    /**
     * Creates a new builder for creating level cards for JDA users.
     *
     * @param user The JDA User to create a level card for
     * @return A new Builder instance
     */
    fun builder(user: User): Builder {
        return Builder(user)
    }

    /**
     * Creates a new builder for creating level cards for JDA guild members.
     * This method uses the member's nickname if available, otherwise falls back to the username.
     *
     * @param member The JDA Member to create a level card for
     * @return A new Builder instance
     */
    fun builder(member: Member): Builder {
        return Builder(member)
    }

    /**
     * Builder class for creating level cards for JDA users.
     */
    class Builder {
        private val user: User
        private val username: String
        private var discriminator: String = ""
        private var member: Member? = null
        private var rank: Int = 1
        private var level: Int = 1
        private var currentXP: Int = 0
        private var nextLevelXP: Int = 100
        private var messagesCount: Int = 0
        private var voiceTime: String = ""
        private var streak: Int = 0
        private var primaryColor: Int = 0xFF00A9FF.toInt()  // Default Kryo blue
        private var secondaryColor: Int = 0xFF80CFFF.toInt() // Lighter blue
        private var backgroundColor: Int = 0xFF0F1729.toInt() // Dark blue-gray
        private var textColor: Int = 0xFFFFFFFF.toInt() // White
        private var accentColor: Int = 0xFF00A9FF.toInt() // Same as primary by default
        private var width: Int = LevelCard.DEFAULT_WIDTH
        private var height: Int = LevelCard.DEFAULT_HEIGHT
        private var showStatusIndicator: Boolean = true
        private var downloadAvatar: Boolean = true

        /**
         * Creates a builder for a JDA User.
         *
         * @param user The JDA User to create a level card for
         */
        constructor(user: User) {
            this.user = user
            this.username = user.name
            this.discriminator = user.discriminator
        }

        /**
         * Creates a builder for a JDA Member.
         * Uses the member's nickname if available, otherwise falls back to the username.
         *
         * @param member The JDA Member to create a level card for
         */
        constructor(member: Member) {
            this.user = member.user
            this.username = member.effectiveName
            this.discriminator = member.user.discriminator
            this.member = member

            // If the member has a color, use it as the primary and accent color
            if (member.colorRaw != 0) {
                this.primaryColor = member.colorRaw
                this.accentColor = member.colorRaw
                // Generate a lighter secondary color
                this.secondaryColor = adjustColor(member.colorRaw, 1.3f)
            }
        }

        /**
         * Sets the user's rank.
         *
         * @param rank User's rank
         * @return This builder for chaining
         */
        fun rank(rank: Int): Builder {
            require(rank > 0) { "Rank must be positive" }
            this.rank = rank
            return this
        }

        /**
         * Sets the user's level.
         *
         * @param level User's level
         * @return This builder for chaining
         */
        fun level(level: Int): Builder {
            require(level >= 0) { "Level must be positive" }
            this.level = level
            return this
        }

        /**
         * Sets the XP values.
         *
         * @param currentXP Current XP of the user
         * @param nextLevelXP XP required for the next level
         * @return This builder for chaining
         */
        fun xp(currentXP: Int, nextLevelXP: Int): Builder {
            require(currentXP >= 0 && nextLevelXP > 0 && currentXP <= nextLevelXP) {
                "Invalid XP values: currentXP must be >= 0, nextLevelXP > 0, and currentXP <= nextLevelXP"
            }
            this.currentXP = currentXP
            this.nextLevelXP = nextLevelXP
            return this
        }

        /**
         * Sets the user's message count.
         *
         * @param messagesCount The number of messages
         * @return This builder for chaining
         */
        fun messagesCount(messagesCount: Int): Builder {
            require(messagesCount >= 0) { "Messages count must be non-negative" }
            this.messagesCount = messagesCount
            return this
        }

        /**
         * Sets the user's voice time.
         *
         * @param voiceTime The voice time as a formatted string (e.g., "127h")
         * @return This builder for chaining
         */
        fun voiceTime(voiceTime: String): Builder {
            this.voiceTime = voiceTime
            return this
        }

        /**
         * Sets the user's streak.
         *
         * @param streak The streak count
         * @return This builder for chaining
         */
        fun streak(streak: Int): Builder {
            require(streak >= 0) { "Streak must be non-negative" }
            this.streak = streak
            return this
        }

        /**
         * Sets the primary color.
         *
         * @param primaryColor Primary color for the card (ARGB format)
         * @return This builder for chaining
         */
        fun primaryColor(primaryColor: Int): Builder {
            this.primaryColor = primaryColor
            return this
        }

        /**
         * Sets the secondary color.
         *
         * @param secondaryColor Secondary color for the card (ARGB format)
         * @return This builder for chaining
         */
        fun secondaryColor(secondaryColor: Int): Builder {
            this.secondaryColor = secondaryColor
            return this
        }

        /**
         * Sets the background color.
         *
         * @param backgroundColor Background color for the card (ARGB format)
         * @return This builder for chaining
         */
        fun backgroundColor(backgroundColor: Int): Builder {
            this.backgroundColor = backgroundColor
            return this
        }

        /**
         * Sets the text color.
         *
         * @param textColor Text color for the card (ARGB format)
         * @return This builder for chaining
         */
        fun textColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        /**
         * Sets the accent color.
         *
         * @param accentColor Accent color for the card (ARGB format)
         * @return This builder for chaining
         */
        fun accentColor(accentColor: Int): Builder {
            this.accentColor = accentColor
            return this
        }

        /**
         * Sets the card dimensions.
         *
         * @param width Width of the card
         * @param height Height of the card
         * @return This builder for chaining
         */
        fun dimensions(width: Int, height: Int): Builder {
            require(width > 0 && height > 0) { "Width and height must be positive" }
            this.width = width
            this.height = height
            return this
        }

        /**
         * Sets whether to show the status indicator.
         *
         * @param showStatusIndicator Whether to show the status indicator
         * @return This builder for chaining
         */
        fun showStatusIndicator(showStatusIndicator: Boolean): Builder {
            this.showStatusIndicator = showStatusIndicator
            return this
        }

        /**
         * Sets whether to download the avatar from Discord.
         * If set to false, the avatar URL will be used directly.
         *
         * @param downloadAvatar Whether to download the avatar
         * @return This builder for chaining
         */
        fun downloadAvatar(downloadAvatar: Boolean): Builder {
            this.downloadAvatar = downloadAvatar
            return this
        }
        

        /**
         * Builds and returns the level card.
         *
         * @return The generated level card as a BufferedImage
         */
        fun build(): BufferedImage {
            // Get the effective avatar URL (with size parameter for better quality)
            val avatarUrl = user.effectiveAvatarUrl + "?size=256"

            // Create the color configuration
            val colorConfig = ColorConfig(
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                backgroundColor = backgroundColor,
                textColor = textColor,
                accentColor = accentColor
            )

            // Create the user data
            val userDataBuilder = UserData.Builder(username)
                .discriminator(discriminator)
                .rank(rank)
                .level(level)
                .xp(currentXP, nextLevelXP)
                .messagesCount(messagesCount)
                .voiceTime(voiceTime)
                .streak(streak)

            // Handle avatar
            if (downloadAvatar) {
                try {
                    // Download avatar and convert to byte array
                    val avatarBytes = downloadAvatarBytes(avatarUrl)
                    userDataBuilder.avatarBytes(avatarBytes)
                } catch (e: IOException) {
                    // Fallback to URL if download fails
                    userDataBuilder.avatarUrl(avatarUrl)
                }
            } else {
                // Use URL directly
                userDataBuilder.avatarUrl(avatarUrl)
            }

            val userData = userDataBuilder.build()

            // Create and render the level card
            val levelCard = LevelCard(userData, colorConfig)
            
            // Create a surface to render to
            val surface = Surface.makeRasterN32Premul(width, height)
            val canvas = surface.canvas
            
            // Draw the level card
            levelCard.draw(canvas, width, height)
            
            // Convert to BufferedImage
            val image = surface.makeImageSnapshot()
            val pngData = image.encodeToData(EncodedImageFormat.PNG)
                ?: throw RuntimeException("Failed to encode image to PNG")
            
            try {
                val inputStream = java.io.ByteArrayInputStream(pngData.bytes)
                val bufferedImage = javax.imageio.ImageIO.read(inputStream)
                
                // Clean up
                surface.close()
                
                return bufferedImage
            } catch (e: java.io.IOException) {
                surface.close()
                throw RuntimeException("Failed to convert Skia image to BufferedImage", e)
            }
        }
        

        /**
         * Downloads an avatar image from a URL and converts it to a byte array.
         *
         * @param url The URL to download from
         * @return The image as a byte array
         * @throws IOException If the download fails
         */
        private fun downloadAvatarBytes(url: String): ByteArray {
            val connection = URL(url).openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.setRequestProperty("User-Agent", "JDALevelCard/1.0")

            val image = ImageIO.read(connection.getInputStream())
            val outputStream = ByteArrayOutputStream()
            ImageIO.write(image, "png", outputStream)
            return outputStream.toByteArray()
        }
        
        /**
         * Helper function to adjust color brightness.
         * 
         * @param color The color to adjust
         * @param factor The brightness factor (> 1.0 for lighter, < 1.0 for darker)
         * @return The adjusted color
         */
        private fun adjustColor(color: Int, factor: Float): Int {
            val a = color and 0xFF000000.toInt()
            val r = ((color and 0x00FF0000) shr 16) * factor
            val g = ((color and 0x0000FF00) shr 8) * factor
            val b = (color and 0x000000FF) * factor

            return a or ((r.toInt() shl 16) and 0x00FF0000) or ((g.toInt() shl 8) and 0x0000FF00) or (b.toInt() and 0x000000FF)
        }
    }
}