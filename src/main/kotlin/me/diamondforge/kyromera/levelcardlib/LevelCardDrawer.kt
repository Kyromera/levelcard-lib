package me.diamondforge.kyromera.levelcardlib

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
     * Draws a level card with the specified parameters and online status.
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
     * @param onlineStatus The online status to display
     * @param showStatusIndicator Whether to show the status indicator
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
        onlineStatus: OnlineStatus,
        showStatusIndicator: Boolean,
        showGenerationTime: Boolean
    ): BufferedImage {
        return builder(username)
            .avatarSource(avatarBytes, avatarUrl, downloadFromUrl)
            .rank(rank)
            .level(level)
            .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
            .accentColor(accentColor)
            .dimensions(width, height)
            .onlineStatus(onlineStatus)
            .showStatusIndicator(showStatusIndicator)
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
        private var onlineStatus: OnlineStatus = OnlineStatus.ONLINE
        private var showStatusIndicator: Boolean = true
        private var showGenerationTime: Boolean = false
        private var layoutConfig: LayoutConfig? = null

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
         * Sets the online status.
         *
         * @param onlineStatus The online status to display
         * @return This builder for chaining
         */
        fun onlineStatus(onlineStatus: OnlineStatus): Builder {
            this.onlineStatus = onlineStatus
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
         * Sets the detailed layout configuration for the card.
         * This allows for independent positioning of all elements on the card.
         *
         * @param layoutConfig The layout configuration
         * @return This builder for chaining
         */
        fun layoutConfig(layoutConfig: LayoutConfig): Builder {
            this.layoutConfig = layoutConfig
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
                .rank(rank)
                .level(level)
                .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
                .onlineStatus(onlineStatus)
                .showStatusIndicator(showStatusIndicator)

            // Set avatar source
            val localAvatarUrl = avatarUrl
            val localAvatarBytes = avatarBytes

            if (downloadFromUrl && !localAvatarUrl.isNullOrEmpty()) {
                userDataBuilder.avatarUrl(localAvatarUrl)
            } else if (localAvatarBytes != null) {
                userDataBuilder.avatarBytes(localAvatarBytes)
            } else {
                throw IllegalArgumentException("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true")
            }

            val userData = userDataBuilder.build()

            // Create CardConfiguration object
            val configBuilder = CardConfiguration.Builder()
                .dimensions(width, height)
                .accentColor(accentColor)
                .showGenerationTime(showGenerationTime)

            // Add layout configuration if provided
            val localLayoutConfig = layoutConfig
            if (localLayoutConfig != null) {
                configBuilder.layoutConfig(localLayoutConfig)
            }

            val config = configBuilder.build()

            // Render the card
            return CardRenderer.renderCard(userData, config)
        }
    }
}
