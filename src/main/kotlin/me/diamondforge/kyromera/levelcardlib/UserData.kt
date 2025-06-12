package me.diamondforge.kyromera.levelcardlib

/**
 * Class that holds all user data needed to generate a level card.
 * Uses the builder pattern for flexible and readable instantiation.
 */
data class UserData private constructor(
    val username: String,
    val rank: Int,
    val level: Int,
    val minXpForCurrentLevel: Int,
    val maxXpForCurrentLevel: Int,
    val currentXP: Int,
    val avatarBytes: ByteArray?,
    val avatarUrl: String?,
    val imageMode: ImageMode,
    val onlineStatus: OnlineStatus,
    val showStatusIndicator: Boolean
) {
    // Override equals and hashCode because ByteArray uses reference equality by default
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserData

        if (username != other.username) return false
        if (rank != other.rank) return false
        if (level != other.level) return false
        if (minXpForCurrentLevel != other.minXpForCurrentLevel) return false
        if (maxXpForCurrentLevel != other.maxXpForCurrentLevel) return false
        if (currentXP != other.currentXP) return false
        if (avatarBytes != null) {
            if (other.avatarBytes == null) return false
            if (!avatarBytes.contentEquals(other.avatarBytes)) return false
        } else if (other.avatarBytes != null) return false
        if (avatarUrl != other.avatarUrl) return false
        if (imageMode != other.imageMode) return false
        if (onlineStatus != other.onlineStatus) return false
        if (showStatusIndicator != other.showStatusIndicator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + rank
        result = 31 * result + level
        result = 31 * result + minXpForCurrentLevel
        result = 31 * result + maxXpForCurrentLevel
        result = 31 * result + currentXP
        result = 31 * result + (avatarBytes?.contentHashCode() ?: 0)
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + imageMode.hashCode()
        result = 31 * result + onlineStatus.hashCode()
        result = 31 * result + showStatusIndicator.hashCode()
        return result
    }

    /**
     * Builder class for UserData.
     */
    class Builder(private val username: String) {
        init {
            require(username.isNotEmpty()) { "Username cannot be null or empty" }
        }

        private var rank: Int = 1
        private var level: Int = 1
        private var minXpForCurrentLevel: Int = 0
        private var maxXpForCurrentLevel: Int = 100
        private var currentXP: Int = 0
        private var avatarBytes: ByteArray? = null
        private var avatarUrl: String? = null
        private var imageMode: ImageMode = ImageMode.LOCAL
        private var onlineStatus: OnlineStatus = OnlineStatus.ONLINE
        private var showStatusIndicator: Boolean = true

        /**
         * Sets the user's rank.
         *
         * @param rank The rank
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
         * @param level The level
         * @return This builder for chaining
         */
        fun level(level: Int): Builder {
            require(level > 0) { "Level must be positive" }
            this.level = level
            return this
        }

        /**
         * Sets the XP values.
         *
         * @param minXpForCurrentLevel The minimum XP for the current level
         * @param maxXpForCurrentLevel The maximum XP for the current level
         * @param currentXP The user's current XP
         * @return This builder for chaining
         */
        fun xp(minXpForCurrentLevel: Int, maxXpForCurrentLevel: Int, currentXP: Int): Builder {
            require(minXpForCurrentLevel >= 0 && maxXpForCurrentLevel > minXpForCurrentLevel && currentXP >= minXpForCurrentLevel && currentXP <= maxXpForCurrentLevel) {
                "Invalid XP values: minXpForCurrentLevel must be >= 0, maxXpForCurrentLevel > minXpForCurrentLevel, and minXpForCurrentLevel <= currentXP <= maxXpForCurrentLevel"
            }
            this.minXpForCurrentLevel = minXpForCurrentLevel
            this.maxXpForCurrentLevel = maxXpForCurrentLevel
            this.currentXP = currentXP
            return this
        }

        /**
         * Sets the avatar image from a byte array.
         * This will set the image mode to LOCAL.
         *
         * @param avatarBytes The avatar image as a byte array
         * @return This builder for chaining
         */
        fun avatarBytes(avatarBytes: ByteArray): Builder {
            this.avatarBytes = avatarBytes
            this.imageMode = ImageMode.LOCAL
            return this
        }

        /**
         * Sets the avatar image URL.
         * This will set the image mode to DOWNLOAD.
         *
         * @param avatarUrl The URL of the avatar image
         * @return This builder for chaining
         */
        fun avatarUrl(avatarUrl: String): Builder {
            this.avatarUrl = avatarUrl
            this.imageMode = ImageMode.DOWNLOAD
            return this
        }

        /**
         * Sets the online status.
         *
         * @param onlineStatus The online status
         * @return This builder for chaining
         */
        fun onlineStatus(onlineStatus: OnlineStatus): Builder {
            this.onlineStatus = onlineStatus
            return this
        }

        /**
         * Sets whether to show the status indicator.
         *
         * @param showStatusIndicator true to show the indicator, false to hide it
         * @return This builder for chaining
         */
        fun showStatusIndicator(showStatusIndicator: Boolean): Builder {
            this.showStatusIndicator = showStatusIndicator
            return this
        }

        /**
         * Builds the UserData instance.
         *
         * @return A new UserData instance
         */
        fun build(): UserData {
            // Validate that we have either avatar bytes or URL
            when (imageMode) {
                ImageMode.LOCAL -> require(avatarBytes != null) {
                    "Avatar bytes must be provided when using LOCAL image mode"
                }
                ImageMode.DOWNLOAD -> require(!avatarUrl.isNullOrEmpty()) {
                    "Avatar URL must be provided when using DOWNLOAD image mode"
                }
            }

            return UserData(
                username,
                rank,
                level,
                minXpForCurrentLevel,
                maxXpForCurrentLevel,
                currentXP,
                avatarBytes,
                avatarUrl,
                imageMode,
                onlineStatus,
                showStatusIndicator
            )
        }
    }
}
