package me.diamondforge.kyromera.levelcardlib

/**
 * Class that holds all user data needed to generate a level card.
 * Uses the builder pattern for flexible and readable instantiation.
 */
data class UserData private constructor(
    val username: String,
    val discriminator: String,
    val level: Int,
    val rank: Int,
    val currentXP: Int,
    val nextLevelXP: Int,
    val messagesCount: Int,
    val voiceTime: String,
    val streak: Int,
    val avatarBytes: ByteArray?,
    val avatarUrl: String?,
    val imageMode: ImageMode
) {
    // Override equals and hashCode because ByteArray uses reference equality by default
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserData

        if (username != other.username) return false
        if (discriminator != other.discriminator) return false
        if (level != other.level) return false
        if (rank != other.rank) return false
        if (currentXP != other.currentXP) return false
        if (nextLevelXP != other.nextLevelXP) return false
        if (messagesCount != other.messagesCount) return false
        if (voiceTime != other.voiceTime) return false
        if (streak != other.streak) return false
        if (avatarBytes != null) {
            if (other.avatarBytes == null) return false
            if (!avatarBytes.contentEquals(other.avatarBytes)) return false
        } else if (other.avatarBytes != null) return false
        if (avatarUrl != other.avatarUrl) return false
        if (imageMode != other.imageMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + discriminator.hashCode()
        result = 31 * result + level
        result = 31 * result + rank
        result = 31 * result + currentXP
        result = 31 * result + nextLevelXP
        result = 31 * result + messagesCount
        result = 31 * result + voiceTime.hashCode()
        result = 31 * result + streak
        result = 31 * result + (avatarBytes?.contentHashCode() ?: 0)
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + imageMode.hashCode()
        return result
    }

    /**
     * Builder class for UserData.
     */
    class Builder(private val username: String) {
        init {
            require(username.isNotEmpty()) { "Username cannot be null or empty" }
        }

        private var discriminator: String = ""
        private var rank: Int = 1
        private var level: Int = 1
        private var currentXP: Int = 0
        private var nextLevelXP: Int = 100
        private var messagesCount: Int = 0
        private var voiceTime: String = ""
        private var streak: Int = 0
        private var avatarBytes: ByteArray? = null
        private var avatarUrl: String? = null
        private var imageMode: ImageMode = ImageMode.LOCAL

        /**
         * Sets the user's discriminator (the 4-digit number after the username).
         *
         * @param discriminator The discriminator
         * @return This builder for chaining
         */
        fun discriminator(discriminator: String): Builder {
            this.discriminator = discriminator
            return this
        }

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
            require(level >= 0) { "Level must be positive" }
            this.level = level
            return this
        }

        /**
         * Sets the XP values.
         *
         * @param currentXP The user's current XP
         * @param nextLevelXP The XP required for the next level
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
                discriminator,
                level,
                rank,
                currentXP,
                nextLevelXP,
                messagesCount,
                voiceTime,
                streak,
                avatarBytes,
                avatarUrl,
                imageMode
            )
        }
    }
}
