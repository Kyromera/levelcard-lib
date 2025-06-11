package me.diamondforge.kyromera.levelcardlib

/**
 * Configuration class for the level card.
 * Contains settings for the card appearance and behavior.
 */
class CardConfiguration private constructor(
    val width: Int,
    val height: Int,
    val accentColor: Int,
    val showGenerationTime: Boolean
) {
    companion object {
        private const val DEFAULT_WIDTH = 950
        private const val DEFAULT_HEIGHT = 300
        private const val DEFAULT_ACCENT_COLOR = 0xFF00A8E8.toInt() // Kryo blue

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
         * Builds the CardConfiguration instance.
         *
         * @return A new CardConfiguration instance
         */
        fun build(): CardConfiguration {
            return CardConfiguration(width, height, accentColor, showGenerationTime)
        }
    }
}
