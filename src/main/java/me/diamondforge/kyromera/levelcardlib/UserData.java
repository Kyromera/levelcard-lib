package me.diamondforge.kyromera.levelcardlib;

/**
 * Class that holds all user data needed to generate a level card.
 * Uses the builder pattern for flexible and readable instantiation.
 */
public class UserData {
    private final String username;
    private final int rank;
    private final int level;
    private final int minXP;
    private final int maxXP;
    private final int currentXP;
    private final byte[] avatarBytes;
    private final String avatarUrl;
    private final ImageMode imageMode;
    private final OnlineStatus onlineStatus;
    private final boolean showStatusIndicator;

    private UserData(Builder builder) {
        this.username = builder.username;
        this.rank = builder.rank;
        this.level = builder.level;
        this.minXP = builder.minXP;
        this.maxXP = builder.maxXP;
        this.currentXP = builder.currentXP;
        this.avatarBytes = builder.avatarBytes;
        this.avatarUrl = builder.avatarUrl;
        this.imageMode = builder.imageMode;
        this.onlineStatus = builder.onlineStatus;
        this.showStatusIndicator = builder.showStatusIndicator;
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's rank.
     *
     * @return The rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the user's level.
     *
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the minimum XP for the current level.
     *
     * @return The minimum XP
     */
    public int getMinXP() {
        return minXP;
    }

    /**
     * Gets the maximum XP for the current level.
     *
     * @return The maximum XP
     */
    public int getMaxXP() {
        return maxXP;
    }

    /**
     * Gets the user's current XP.
     *
     * @return The current XP
     */
    public int getCurrentXP() {
        return currentXP;
    }

    /**
     * Gets the avatar image as a byte array.
     * May be null if using a URL.
     *
     * @return The avatar image bytes or null
     */
    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    /**
     * Gets the avatar URL.
     * May be null if using local image bytes.
     *
     * @return The avatar URL or null
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Gets the image mode (LOCAL or DOWNLOAD).
     *
     * @return The image mode
     */
    public ImageMode getImageMode() {
        return imageMode;
    }

    /**
     * Gets the user's online status.
     *
     * @return The online status
     */
    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * Checks if the status indicator should be shown.
     *
     * @return true if the status indicator should be shown, false otherwise
     */
    public boolean isShowStatusIndicator() {
        return showStatusIndicator;
    }

    /**
     * Builder class for UserData.
     */
    public static class Builder {
        private String username;
        private int rank = 1;
        private int level = 1;
        private int minXP = 0;
        private int maxXP = 100;
        private int currentXP = 0;
        private byte[] avatarBytes;
        private String avatarUrl;
        private ImageMode imageMode = ImageMode.LOCAL;
        private OnlineStatus onlineStatus = OnlineStatus.ONLINE;
        private boolean showStatusIndicator = true;

        /**
         * Creates a new Builder with the required username.
         *
         * @param username The username (required)
         */
        public Builder(String username) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            this.username = username;
        }

        /**
         * Sets the user's rank.
         *
         * @param rank The rank
         * @return This builder for chaining
         */
        public Builder rank(int rank) {
            if (rank < 1) {
                throw new IllegalArgumentException("Rank must be positive");
            }
            this.rank = rank;
            return this;
        }

        /**
         * Sets the user's level.
         *
         * @param level The level
         * @return This builder for chaining
         */
        public Builder level(int level) {
            if (level < 1) {
                throw new IllegalArgumentException("Level must be positive");
            }
            this.level = level;
            return this;
        }

        /**
         * Sets the XP values.
         *
         * @param minXP The minimum XP for the current level
         * @param maxXP The maximum XP for the current level
         * @param currentXP The user's current XP
         * @return This builder for chaining
         */
        public Builder xp(int minXP, int maxXP, int currentXP) {
            if (minXP < 0 || maxXP <= minXP || currentXP < minXP || currentXP > maxXP) {
                throw new IllegalArgumentException(
                    "Invalid XP values: minXP must be >= 0, maxXP > minXP, and minXP <= currentXP <= maxXP");
            }
            this.minXP = minXP;
            this.maxXP = maxXP;
            this.currentXP = currentXP;
            return this;
        }

        /**
         * Sets the avatar image from a byte array.
         * This will set the image mode to LOCAL.
         *
         * @param avatarBytes The avatar image as a byte array
         * @return This builder for chaining
         */
        public Builder avatarBytes(byte[] avatarBytes) {
            this.avatarBytes = avatarBytes;
            this.imageMode = ImageMode.LOCAL;
            return this;
        }

        /**
         * Sets the avatar image URL.
         * This will set the image mode to DOWNLOAD.
         *
         * @param avatarUrl The URL of the avatar image
         * @return This builder for chaining
         */
        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            this.imageMode = ImageMode.DOWNLOAD;
            return this;
        }

        /**
         * Sets the online status.
         *
         * @param onlineStatus The online status
         * @return This builder for chaining
         */
        public Builder onlineStatus(OnlineStatus onlineStatus) {
            this.onlineStatus = onlineStatus;
            return this;
        }

        /**
         * Sets whether to show the status indicator.
         *
         * @param showStatusIndicator true to show the indicator, false to hide it
         * @return This builder for chaining
         */
        public Builder showStatusIndicator(boolean showStatusIndicator) {
            this.showStatusIndicator = showStatusIndicator;
            return this;
        }

        /**
         * Builds the UserData instance.
         *
         * @return A new UserData instance
         */
        public UserData build() {
            // Validate that we have either avatar bytes or URL
            if (imageMode == ImageMode.LOCAL && avatarBytes == null) {
                throw new IllegalStateException("Avatar bytes must be provided when using LOCAL image mode");
            }
            if (imageMode == ImageMode.DOWNLOAD && (avatarUrl == null || avatarUrl.isEmpty())) {
                throw new IllegalStateException("Avatar URL must be provided when using DOWNLOAD image mode");
            }
            
            return new UserData(this);
        }
    }
}