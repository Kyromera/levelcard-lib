package me.diamondforge.kyromera.levelcardlib;

/**
 * Configuration class for the level card.
 * Contains settings for the card appearance and behavior.
 */
public class CardConfiguration {
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 180;
    private static final int DEFAULT_ACCENT_COLOR = 0xFFEA397C; // Pink
    
    private final int width;
    private final int height;
    private final int accentColor;
    private final boolean showGenerationTime;
    
    private CardConfiguration(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.accentColor = builder.accentColor;
        this.showGenerationTime = builder.showGenerationTime;
    }
    
    /**
     * Gets the width of the card.
     *
     * @return The width in pixels
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the card.
     *
     * @return The height in pixels
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the accent color of the card.
     *
     * @return The accent color in ARGB format
     */
    public int getAccentColor() {
        return accentColor;
    }
    
    /**
     * Checks if generation time should be shown on the card.
     *
     * @return true if generation time should be shown, false otherwise
     */
    public boolean isShowGenerationTime() {
        return showGenerationTime;
    }
    
    /**
     * Creates a default configuration.
     *
     * @return A default configuration
     */
    public static CardConfiguration getDefault() {
        return new Builder().build();
    }
    
    /**
     * Builder class for CardConfiguration.
     */
    public static class Builder {
        private int width = DEFAULT_WIDTH;
        private int height = DEFAULT_HEIGHT;
        private int accentColor = DEFAULT_ACCENT_COLOR;
        private boolean showGenerationTime = false;
        
        /**
         * Sets the dimensions of the card.
         *
         * @param width The width in pixels
         * @param height The height in pixels
         * @return This builder for chaining
         */
        public Builder dimensions(int width, int height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            this.width = width;
            this.height = height;
            return this;
        }
        
        /**
         * Sets the accent color of the card.
         *
         * @param accentColor The accent color in ARGB format
         * @return This builder for chaining
         */
        public Builder accentColor(int accentColor) {
            this.accentColor = accentColor;
            return this;
        }
        
        /**
         * Sets whether to show generation time on the card.
         *
         * @param showGenerationTime true to show generation time, false to hide it
         * @return This builder for chaining
         */
        public Builder showGenerationTime(boolean showGenerationTime) {
            this.showGenerationTime = showGenerationTime;
            return this;
        }
        
        /**
         * Builds the CardConfiguration instance.
         *
         * @return A new CardConfiguration instance
         */
        public CardConfiguration build() {
            return new CardConfiguration(this);
        }
    }
}