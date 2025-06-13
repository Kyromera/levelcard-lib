package me.diamondforge.kyromera.levelcardlib

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Surface
import java.awt.image.BufferedImage

/**
 * A utility class for generating level cards with user information and avatar.
 * This is the main entry point for the library.
 */
object LevelCardDrawer {
    // Default values
    private const val DEFAULT_WIDTH = 950
    private const val DEFAULT_HEIGHT = 300
    private const val DEFAULT_ACCENT_COLOR = 0xFF2CBCC9.toInt() // Cryo Blue

    /**
     * Creates a new builder for creating level cards.
     *
     * @param username Username to display on the card
     * @return A new Builder instance
     */
    fun builder(username: String): Builder {
        return Builder(username)
    }

    /**
     * Draws a level card with the specified parameters.
     *
     * @param avatarBytes Byte array of the avatar image, can be null if avatarUrl is provided
     * @param avatarUrl URL of the avatar image, can be null if avatarBytes is provided
     * @param downloadFromUrl Whether to download the avatar from the URL
     * @param username Username to display on the card
     * @param rank User's rank
     * @param level User's level
     * @param minXpForCurrentLevel Minimum XP for the current level
     * @param maxXpForCurrentLevel Maximum XP for the current level
     * @param currentXP Current XP of the user
     * @param accentColor Accent color for the card (ARGB format)
     * @param width Width of the card
     * @param height Height of the card
     * @return The generated level card as a BufferedImage
     */
    fun drawLevelCard(
        avatarBytes: ByteArray?,
        avatarUrl: String?,
        downloadFromUrl: Boolean,
        username: String,
        rank: Int,
        level: Int,
        minXpForCurrentLevel: Int,
        maxXpForCurrentLevel: Int,
        currentXP: Int,
        accentColor: Int,
        width: Int,
        height: Int
    ): BufferedImage {
        return builder(username)
            .avatarSource(avatarBytes, avatarUrl, downloadFromUrl)
            .rank(rank)
            .level(level)
            .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
            .accentColor(accentColor)
            .dimensions(width, height)
            .build()
    }

    /**
     * Draws a level card with the specified parameters.
     *
     * @param avatarBytes Byte array of the avatar image, can be null if avatarUrl is provided
     * @param avatarUrl URL of the avatar image, can be null if avatarBytes is provided
     * @param downloadFromUrl Whether to download the avatar from the URL
     * @param username Username to display on the card
     * @param rank User's rank
     * @param level User's level
     * @param minXpForCurrentLevel Minimum XP for the current level
     * @param maxXpForCurrentLevel Maximum XP for the current level
     * @param currentXP Current XP of the user
     * @param accentColor Accent color for the card (ARGB format)
     * @param width Width of the card
     * @param height Height of the card
     * @param showGenerationTime Whether to show generation time on the card
     * @return The generated level card as a BufferedImage
     */
    fun drawLevelCard(
        avatarBytes: ByteArray?,
        avatarUrl: String?,
        downloadFromUrl: Boolean,
        username: String,
        rank: Int,
        level: Int,
        minXpForCurrentLevel: Int,
        maxXpForCurrentLevel: Int,
        currentXP: Int,
        accentColor: Int,
        width: Int,
        height: Int,
        showGenerationTime: Boolean
    ): BufferedImage {
        return builder(username)
            .avatarSource(avatarBytes, avatarUrl, downloadFromUrl)
            .rank(rank)
            .level(level)
            .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
            .accentColor(accentColor)
            .dimensions(width, height)
            .showGenerationTime(showGenerationTime)
            .build()
    }

    /**
     * Overloaded method with default width and height.
     */
    fun drawLevelCard(
        avatarBytes: ByteArray?,
        avatarUrl: String?,
        downloadFromUrl: Boolean,
        username: String,
        rank: Int,
        level: Int,
        minXpForCurrentLevel: Int,
        maxXpForCurrentLevel: Int,
        currentXP: Int,
        accentColor: Int
    ): BufferedImage {
        return drawLevelCard(
            avatarBytes, avatarUrl, downloadFromUrl, username, rank, level,
            minXpForCurrentLevel, maxXpForCurrentLevel, currentXP, accentColor, DEFAULT_WIDTH, DEFAULT_HEIGHT
        )
    }

    /**
     * Overloaded method with default accent color, width, and height.
     */
    fun drawLevelCard(
        avatarBytes: ByteArray?,
        avatarUrl: String?,
        downloadFromUrl: Boolean,
        username: String,
        rank: Int,
        level: Int,
        minXpForCurrentLevel: Int,
        maxXpForCurrentLevel: Int,
        currentXP: Int
    ): BufferedImage {
        return drawLevelCard(
            avatarBytes, avatarUrl, downloadFromUrl, username, rank, level,
            minXpForCurrentLevel, maxXpForCurrentLevel, currentXP, DEFAULT_ACCENT_COLOR, DEFAULT_WIDTH, DEFAULT_HEIGHT
        )
    }

    /**
     * Builder class for creating level cards.
     */
    class Builder(private val username: String) {
        private var avatarBytes: ByteArray? = null
        private var avatarUrl: String? = null
        private var downloadFromUrl: Boolean = false
        private var rank: Int = 1
        private var level: Int = 1
        private var minXpForCurrentLevel: Int = 0
        private var maxXpForCurrentLevel: Int = 100
        private var currentXP: Int = 0
        private var accentColor: Int = DEFAULT_ACCENT_COLOR
        private var width: Int = DEFAULT_WIDTH
        private var height: Int = DEFAULT_HEIGHT
        private var showGenerationTime: Boolean = false

        /**
         * Sets the avatar source.
         *
         * @param avatarBytes Byte array of the avatar image, can be null if avatarUrl is provided
         * @param avatarUrl URL of the avatar image, can be null if avatarBytes is provided
         * @param downloadFromUrl Whether to download the avatar from the URL
         * @return This builder for chaining
         */
        fun avatarSource(avatarBytes: ByteArray?, avatarUrl: String?, downloadFromUrl: Boolean): Builder {
            this.avatarBytes = avatarBytes
            this.avatarUrl = avatarUrl
            this.downloadFromUrl = downloadFromUrl
            return this
        }

        /**
         * Sets the avatar from a byte array.
         *
         * @param avatarBytes Byte array of the avatar image
         * @return This builder for chaining
         */
        fun avatarBytes(avatarBytes: ByteArray): Builder {
            this.avatarBytes = avatarBytes
            this.avatarUrl = null
            this.downloadFromUrl = false
            return this
        }

        /**
         * Sets the avatar from a URL.
         *
         * @param avatarUrl URL of the avatar image
         * @return This builder for chaining
         */
        fun avatarUrl(avatarUrl: String): Builder {
            this.avatarBytes = null
            this.avatarUrl = avatarUrl
            this.downloadFromUrl = true
            return this
        }

        /**
         * Sets the user's rank.
         *
         * @param rank User's rank
         * @return This builder for chaining
         */
        fun rank(rank: Int): Builder {
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
            this.level = level
            return this
        }

        /**
         * Sets the XP values.
         *
         * @param minXpForCurrentLevel Minimum XP for the current level
         * @param maxXpForCurrentLevel Maximum XP for the current level
         * @param currentXP Current XP of the user
         * @return This builder for chaining
         */
        fun xp(minXpForCurrentLevel: Int, maxXpForCurrentLevel: Int, currentXP: Int): Builder {
            this.minXpForCurrentLevel = minXpForCurrentLevel
            this.maxXpForCurrentLevel = maxXpForCurrentLevel
            this.currentXP = currentXP
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
            this.width = width
            this.height = height
            return this
        }


        /**
         * Sets whether to show generation time on the card.
         *
         * @param showGenerationTime Whether to show generation time
         * @return This builder for chaining
         */
        fun showGenerationTime(showGenerationTime: Boolean): Builder {
            this.showGenerationTime = showGenerationTime
            return this
        }

        /**
         * Builds and returns the level card.
         *
         * @return The generated level card as a BufferedImage
         * @throws IllegalArgumentException if no avatar source is provided
         */
        fun build(): BufferedImage {
            // Create UserData object
            val userDataBuilder = UserData.Builder(username)
                .discriminator("")  // Default empty discriminator
                .rank(rank)
                .level(level)
                .xp(currentXP, maxXpForCurrentLevel)  // Using nextLevelXP instead of min/max
                .messagesCount(0)   // Default values for new fields
                .voiceTime("")
                .streak(0)

            // Set avatar source
            val localAvatarUrl = avatarUrl
            val localAvatarBytes = avatarBytes

            if (downloadFromUrl && !localAvatarUrl.isNullOrEmpty()) {
                userDataBuilder.avatarUrl(localAvatarUrl)
            } else if (localAvatarBytes != null) {
                userDataBuilder.avatarBytes(localAvatarBytes)
            } else {
                userDataBuilder.avatarUrl("https://i.pravatar.cc/300") // No avatar source provided
            }

            val userData = userDataBuilder.build()

            // Create ColorConfig object with improved gradients
            val colorConfig = ColorConfig(
                primaryColor = accentColor,
                secondaryColor = adjustColor(accentColor, 1.3f),  // Lighter version of primary color for better gradation
                backgroundColor = 0xFF0F1729.toInt(),
                textColor = 0xFFFFFFFF.toInt(),
                accentColor = adjustColor(accentColor, 0.9f)  // Slightly darker version of primary color for accent
            )

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

        // Helper function to adjust color brightness (copied from LevelCard)
        private fun adjustColor(color: Int, factor: Float): Int {
            val a = color and 0xFF000000.toInt()
            val r = ((color and 0x00FF0000) shr 16) * factor
            val g = ((color and 0x0000FF00) shr 8) * factor
            val b = (color and 0x000000FF) * factor

            return a or ((r.toInt() shl 16) and 0x00FF0000) or ((g.toInt() shl 8) and 0x0000FF00) or (b.toInt() and 0x000000FF)
        }
    }
}
