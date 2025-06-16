package me.diamondforge.kyromera.levelcardlib

/**
 * Configuration class for the layout of individual elements on the level card.
 * Allows for independent positioning of text elements and the progress bar.
 */
class LayoutConfig private constructor(
    // Avatar position
    val avatarXOffset: Int,
    val avatarYOffset: Int,

    // Username text position
    val usernameXOffset: Int,
    val usernameYOffset: Int,

    // Rank and level text position
    val rankLevelXOffset: Int,
    val rankLevelYOffset: Int,

    // XP text position
    val xpTextXOffset: Int,
    val xpTextYOffset: Int,

    // Time text position
    val timeTextXOffset: Int,
    val timeTextYOffset: Int,

    // Progress bar position
    val progressBarXOffset: Int,
    val progressBarYOffset: Int,
    val progressBarRightMargin: Int
) {
    companion object {
        // Default values - using the constants from CardRenderer
        private val DEFAULT_TEXT_X_OFFSET = CardRenderer.TEXT_MARGIN
        private val DEFAULT_USERNAME_Y_OFFSET = CardRenderer.USERNAME_Y_OFFSET
        private val DEFAULT_RANK_LEVEL_Y_OFFSET = CardRenderer.RANK_LEVEL_Y_OFFSET
        private val DEFAULT_XP_TEXT_Y_OFFSET = CardRenderer.XP_TEXT_Y_OFFSET
        private val DEFAULT_TIME_TEXT_Y_OFFSET = CardRenderer.TIME_TEXT_Y_OFFSET
        private val DEFAULT_PROGRESS_BAR_X_OFFSET = CardRenderer.TEXT_MARGIN
        private val DEFAULT_PROGRESS_BAR_Y_OFFSET = CardRenderer.PROGRESS_BAR_Y_OFFSET
        private val DEFAULT_PROGRESS_BAR_RIGHT_MARGIN = CardRenderer.PROGRESS_BAR_MARGIN

        /**
         * Creates a default layout configuration.
         *
         * @return A default layout configuration
         */
        fun getDefault(): LayoutConfig = Builder().build()
    }

    /**
     * Builder class for LayoutConfig.
     */
    class Builder {
        private var avatarXOffset = CardRenderer.AVATAR_MARGIN
        private var avatarYOffset = 0 // Will be calculated based on card height

        private var usernameXOffset = DEFAULT_TEXT_X_OFFSET
        private var usernameYOffset = DEFAULT_USERNAME_Y_OFFSET

        private var rankLevelXOffset = DEFAULT_TEXT_X_OFFSET
        private var rankLevelYOffset = DEFAULT_RANK_LEVEL_Y_OFFSET

        private var xpTextXOffset = DEFAULT_TEXT_X_OFFSET
        private var xpTextYOffset = DEFAULT_XP_TEXT_Y_OFFSET

        private var timeTextXOffset = DEFAULT_TEXT_X_OFFSET
        private var timeTextYOffset = DEFAULT_TIME_TEXT_Y_OFFSET

        private var progressBarXOffset = DEFAULT_PROGRESS_BAR_X_OFFSET
        private var progressBarYOffset = DEFAULT_PROGRESS_BAR_Y_OFFSET
        private var progressBarRightMargin = DEFAULT_PROGRESS_BAR_RIGHT_MARGIN

        // Default offset value
        private val DEFAULT_OFFSET = 0

        /**
         * Sets the position of the avatar.
         *
         * @param xOffset X-offset for the avatar
         * @param yOffset Y-offset for the avatar
         * @return This builder for chaining
         */
        fun avatarPosition(xOffset: Int, yOffset: Int): Builder {
            this.avatarXOffset = xOffset
            this.avatarYOffset = yOffset
            return this
        }

        /**
         * Sets the position of the avatar using relative offsets from default positions.
         *
         * @param xOffset X-offset relative to the default position (can be negative)
         * @param yOffset Y-offset relative to the default position (can be negative)
         * @return This builder for chaining
         */
        fun avatarOffset(xOffset: Int = DEFAULT_OFFSET, yOffset: Int = DEFAULT_OFFSET): Builder {
            this.avatarXOffset = CardRenderer.AVATAR_MARGIN + xOffset
            // Avatar Y is special case as it's calculated based on card height
            // We'll just store the offset and let the renderer handle it
            this.avatarYOffset = yOffset
            return this
        }

        /**
         * Sets the position of the username text.
         *
         * @param xOffset X-offset for the username text
         * @param yOffset Y-offset for the username text
         * @return This builder for chaining
         */
        fun usernamePosition(xOffset: Int, yOffset: Int): Builder {
            this.usernameXOffset = xOffset
            this.usernameYOffset = yOffset
            return this
        }

        /**
         * Sets the position of the username text using relative offsets from default positions.
         *
         * @param xOffset X-offset relative to the default position (can be negative)
         * @param yOffset Y-offset relative to the default position (can be negative)
         * @return This builder for chaining
         */
        fun usernameOffset(xOffset: Int = DEFAULT_OFFSET, yOffset: Int = DEFAULT_OFFSET): Builder {
            this.usernameXOffset = DEFAULT_TEXT_X_OFFSET + xOffset
            this.usernameYOffset = DEFAULT_USERNAME_Y_OFFSET + yOffset
            return this
        }

        /**
         * Sets the position of the rank and level text.
         *
         * @param xOffset X-offset for the rank and level text
         * @param yOffset Y-offset for the rank and level text
         * @return This builder for chaining
         */
        fun rankLevelPosition(xOffset: Int, yOffset: Int): Builder {
            this.rankLevelXOffset = xOffset
            this.rankLevelYOffset = yOffset
            return this
        }

        /**
         * Sets the position of the rank and level text using relative offsets from default positions.
         *
         * @param xOffset X-offset relative to the default position (can be negative)
         * @param yOffset Y-offset relative to the default position (can be negative)
         * @return This builder for chaining
         */
        fun rankLevelOffset(xOffset: Int = DEFAULT_OFFSET, yOffset: Int = DEFAULT_OFFSET): Builder {
            this.rankLevelXOffset = DEFAULT_TEXT_X_OFFSET + xOffset
            this.rankLevelYOffset = DEFAULT_RANK_LEVEL_Y_OFFSET + yOffset
            return this
        }

        /**
         * Sets the position of the XP text.
         *
         * @param xOffset X-offset for the XP text
         * @param yOffset Y-offset for the XP text
         * @return This builder for chaining
         */
        fun xpTextPosition(xOffset: Int, yOffset: Int): Builder {
            this.xpTextXOffset = xOffset
            this.xpTextYOffset = yOffset
            return this
        }

        /**
         * Sets the position of the XP text using relative offsets from default positions.
         *
         * @param xOffset X-offset relative to the default position (can be negative)
         * @param yOffset Y-offset relative to the default position (can be negative)
         * @return This builder for chaining
         */
        fun xpTextOffset(xOffset: Int = DEFAULT_OFFSET, yOffset: Int = DEFAULT_OFFSET): Builder {
            this.xpTextXOffset = DEFAULT_TEXT_X_OFFSET + xOffset
            this.xpTextYOffset = DEFAULT_XP_TEXT_Y_OFFSET + yOffset
            return this
        }

        /**
         * Sets the position of the generation time text.
         *
         * @param xOffset X-offset for the time text
         * @param yOffset Y-offset for the time text
         * @return This builder for chaining
         */
        fun timeTextPosition(xOffset: Int, yOffset: Int): Builder {
            this.timeTextXOffset = xOffset
            this.timeTextYOffset = yOffset
            return this
        }

        /**
         * Sets the position of the generation time text using relative offsets from default positions.
         *
         * @param xOffset X-offset relative to the default position (can be negative)
         * @param yOffset Y-offset relative to the default position (can be negative)
         * @return This builder for chaining
         */
        fun timeTextOffset(xOffset: Int = DEFAULT_OFFSET, yOffset: Int = DEFAULT_OFFSET): Builder {
            this.timeTextXOffset = DEFAULT_TEXT_X_OFFSET + xOffset
            this.timeTextYOffset = DEFAULT_TIME_TEXT_Y_OFFSET + yOffset
            return this
        }

        /**
         * Sets the position and margins of the progress bar.
         *
         * @param xOffset X-offset for the progress bar
         * @param yOffset Y-offset for the progress bar
         * @param rightMargin Right margin for the progress bar
         * @return This builder for chaining
         */
        fun progressBarPosition(xOffset: Int, yOffset: Int, rightMargin: Int): Builder {
            this.progressBarXOffset = xOffset
            this.progressBarYOffset = yOffset
            this.progressBarRightMargin = rightMargin
            return this
        }

        /**
         * Sets the position and margins of the progress bar using relative offsets from default positions.
         *
         * @param xOffset X-offset relative to the default position (can be negative)
         * @param yOffset Y-offset relative to the default position (can be negative)
         * @param rightMarginOffset Right margin offset relative to the default margin (can be negative)
         * @return This builder for chaining
         */
        fun progressBarOffset(xOffset: Int = DEFAULT_OFFSET, yOffset: Int = DEFAULT_OFFSET, rightMarginOffset: Int = DEFAULT_OFFSET): Builder {
            this.progressBarXOffset = DEFAULT_PROGRESS_BAR_X_OFFSET + xOffset
            this.progressBarYOffset = DEFAULT_PROGRESS_BAR_Y_OFFSET + yOffset
            this.progressBarRightMargin = DEFAULT_PROGRESS_BAR_RIGHT_MARGIN + rightMarginOffset
            return this
        }

        /**
         * Builds the LayoutConfig instance.
         *
         * @return A new LayoutConfig instance
         */
        fun build(): LayoutConfig {
            return LayoutConfig(
                avatarXOffset, avatarYOffset,
                usernameXOffset, usernameYOffset,
                rankLevelXOffset, rankLevelYOffset,
                xpTextXOffset, xpTextYOffset,
                timeTextXOffset, timeTextYOffset,
                progressBarXOffset, progressBarYOffset, progressBarRightMargin
            )
        }
    }
}
