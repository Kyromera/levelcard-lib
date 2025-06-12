package me.diamondforge.kyromera.levelcardlib

/**
 * Configuration class for the level card.
 * Contains settings for the card appearance and behavior.
 */
class CardConfiguration private constructor(
    val width: Int,
    val height: Int,
    val accentColor: Int,
    val showGenerationTime: Boolean,
    // Avatar configuration
    val avatarSize: Int,
    val avatarMargin: Int,
    // Text margins and offsets
    val textMargin: Int,
    val usernameYOffset: Int,
    val rankLevelYOffset: Int,
    val xpTextYOffset: Int,
    val timeTextYOffset: Int,
    // Progress bar configuration
    val progressBarHeight: Int,
    val progressBarYOffset: Int,
    val progressBarMargin: Int,
    // Font sizes
    val usernameFontSize: Float,
    val rankLevelFontSize: Float,
    val xpFontSize: Float,
    val timeFontSize: Float,
    // Background configuration
    val shadowBlur: Float,
    val cornerRadius: Float
) {
    companion object {
        // Default dimensions
        private const val DEFAULT_WIDTH = 950
        private const val DEFAULT_HEIGHT = 300
        private const val DEFAULT_ACCENT_COLOR = 0xFF00A8E8.toInt() // Kryo blue

        // Default avatar configuration
        private const val DEFAULT_AVATAR_SIZE = 200
        private const val DEFAULT_AVATAR_MARGIN = 50

        // Default text margins and offsets
        private const val DEFAULT_TEXT_MARGIN = 270
        private const val DEFAULT_USERNAME_Y_OFFSET = 83
        private const val DEFAULT_RANK_LEVEL_Y_OFFSET = 133
        private const val DEFAULT_XP_TEXT_Y_OFFSET = 200
        private const val DEFAULT_TIME_TEXT_Y_OFFSET = 267

        // Default progress bar configuration
        private const val DEFAULT_PROGRESS_BAR_HEIGHT = 33
        private const val DEFAULT_PROGRESS_BAR_Y_OFFSET = 217
        private const val DEFAULT_PROGRESS_BAR_MARGIN = 50

        // Default font sizes
        private const val DEFAULT_USERNAME_FONT_SIZE = 47f
        private const val DEFAULT_RANK_LEVEL_FONT_SIZE = 33f
        private const val DEFAULT_XP_FONT_SIZE = 27f
        private const val DEFAULT_TIME_FONT_SIZE = 20f

        // Default background configuration
        private const val DEFAULT_SHADOW_BLUR = 13f
        private const val DEFAULT_CORNER_RADIUS = 25f

        /**
         * Creates a default configuration.
         *
         * @return A default configuration
         */
        fun getDefault(): CardConfiguration = Builder().build()
    }

    /**
     * Builder class for CardConfiguration.
     */
    class Builder {
        private var width = DEFAULT_WIDTH
        private var height = DEFAULT_HEIGHT
        private var accentColor = DEFAULT_ACCENT_COLOR
        private var showGenerationTime = false

        // Avatar configuration
        private var avatarSize = DEFAULT_AVATAR_SIZE
        private var avatarMargin = DEFAULT_AVATAR_MARGIN

        // Text margins and offsets
        private var textMargin = DEFAULT_TEXT_MARGIN
        private var usernameYOffset = DEFAULT_USERNAME_Y_OFFSET
        private var rankLevelYOffset = DEFAULT_RANK_LEVEL_Y_OFFSET
        private var xpTextYOffset = DEFAULT_XP_TEXT_Y_OFFSET
        private var timeTextYOffset = DEFAULT_TIME_TEXT_Y_OFFSET

        // Progress bar configuration
        private var progressBarHeight = DEFAULT_PROGRESS_BAR_HEIGHT
        private var progressBarYOffset = DEFAULT_PROGRESS_BAR_Y_OFFSET
        private var progressBarMargin = DEFAULT_PROGRESS_BAR_MARGIN

        // Font sizes
        private var usernameFontSize = DEFAULT_USERNAME_FONT_SIZE
        private var rankLevelFontSize = DEFAULT_RANK_LEVEL_FONT_SIZE
        private var xpFontSize = DEFAULT_XP_FONT_SIZE
        private var timeFontSize = DEFAULT_TIME_FONT_SIZE

        // Background configuration
        private var shadowBlur = DEFAULT_SHADOW_BLUR
        private var cornerRadius = DEFAULT_CORNER_RADIUS

        /**
         * Sets the dimensions of the card.
         *
         * @param width The width in pixels
         * @param height The height in pixels
         * @return This builder for chaining
         */
        fun dimensions(width: Int, height: Int): Builder {
            require(width > 0 && height > 0) { "Width and height must be positive" }
            this.width = width
            this.height = height
            return this
        }

        /**
         * Sets the accent color of the card.
         *
         * @param accentColor The accent color in ARGB format
         * @return This builder for chaining
         */
        fun accentColor(accentColor: Int): Builder {
            this.accentColor = accentColor
            return this
        }

        /**
         * Sets whether to show generation time on the card.
         *
         * @param showGenerationTime true to show generation time, false to hide it
         * @return This builder for chaining
         */
        fun showGenerationTime(showGenerationTime: Boolean): Builder {
            this.showGenerationTime = showGenerationTime
            return this
        }

        /**
         * Sets the avatar size and margin.
         *
         * @param size The size of the avatar in pixels
         * @param margin The margin around the avatar in pixels
         * @return This builder for chaining
         */
        fun avatarConfig(size: Int, margin: Int): Builder {
            require(size > 0) { "Avatar size must be positive" }
            require(margin >= 0) { "Avatar margin must be non-negative" }
            this.avatarSize = size
            this.avatarMargin = margin
            return this
        }

        /**
         * Sets the text margin (horizontal distance from avatar to text).
         *
         * @param margin The margin in pixels
         * @return This builder for chaining
         */
        fun textMargin(margin: Int): Builder {
            require(margin >= 0) { "Text margin must be non-negative" }
            this.textMargin = margin
            return this
        }

        /**
         * Sets the vertical offsets for text elements.
         *
         * @param usernameOffset Y-offset for username text
         * @param rankLevelOffset Y-offset for rank and level text
         * @param xpTextOffset Y-offset for XP text
         * @param timeTextOffset Y-offset for generation time text
         * @return This builder for chaining
         */
        fun textOffsets(
            usernameOffset: Int,
            rankLevelOffset: Int,
            xpTextOffset: Int,
            timeTextOffset: Int
        ): Builder {
            this.usernameYOffset = usernameOffset
            this.rankLevelYOffset = rankLevelOffset
            this.xpTextYOffset = xpTextOffset
            this.timeTextYOffset = timeTextOffset
            return this
        }

        /**
         * Sets the progress bar configuration.
         *
         * @param height The height of the progress bar in pixels
         * @param yOffset The Y-offset of the progress bar
         * @param margin The right margin of the progress bar
         * @return This builder for chaining
         */
        fun progressBarConfig(height: Int, yOffset: Int, margin: Int): Builder {
            require(height > 0) { "Progress bar height must be positive" }
            require(margin >= 0) { "Progress bar margin must be non-negative" }
            this.progressBarHeight = height
            this.progressBarYOffset = yOffset
            this.progressBarMargin = margin
            return this
        }

        /**
         * Sets the font sizes for text elements.
         *
         * @param username Font size for username
         * @param rankLevel Font size for rank and level
         * @param xpText Font size for XP text
         * @param timeText Font size for generation time text
         * @return This builder for chaining
         */
        fun fontSizes(
            username: Float,
            rankLevel: Float,
            xpText: Float,
            timeText: Float
        ): Builder {
            require(username > 0 && rankLevel > 0 && xpText > 0 && timeText > 0) { "Font sizes must be positive" }
            this.usernameFontSize = username
            this.rankLevelFontSize = rankLevel
            this.xpFontSize = xpText
            this.timeFontSize = timeText
            return this
        }

        /**
         * Sets the background configuration.
         *
         * @param shadowBlur The blur amount for the shadow
         * @param cornerRadius The radius for rounded corners
         * @return This builder for chaining
         */
        fun backgroundConfig(shadowBlur: Float, cornerRadius: Float): Builder {
            require(shadowBlur >= 0) { "Shadow blur must be non-negative" }
            require(cornerRadius >= 0) { "Corner radius must be non-negative" }
            this.shadowBlur = shadowBlur
            this.cornerRadius = cornerRadius
            return this
        }

        /**
         * Builds the CardConfiguration instance.
         *
         * @return A new CardConfiguration instance
         */
        fun build(): CardConfiguration {
            return CardConfiguration(
                width, height, accentColor, showGenerationTime,
                avatarSize, avatarMargin,
                textMargin, usernameYOffset, rankLevelYOffset, xpTextYOffset, timeTextYOffset,
                progressBarHeight, progressBarYOffset, progressBarMargin,
                usernameFontSize, rankLevelFontSize, xpFontSize, timeFontSize,
                shadowBlur, cornerRadius
            )
        }
    }
}
