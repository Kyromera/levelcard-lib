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
    private const val DEFAULT_ACCENT_COLOR = 0xFFEA397C.toInt() // Pink

    /**
     * Draws a level card with the specified parameters.
     *
     * @param avatarBytes Byte array of the avatar image, can be null if avatarUrl is provided
     * @param avatarUrl URL of the avatar image, can be null if avatarBytes is provided
     * @param downloadFromUrl Whether to download the avatar from the URL
     * @param username Username to display on the card
     * @param rank User's rank
     * @param level User's level
     * @param minXP Minimum XP for the current level
     * @param maxXP Maximum XP for the current level
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
        minXP: Int,
        maxXP: Int,
        currentXP: Int,
        accentColor: Int,
        width: Int,
        height: Int
    ): BufferedImage {
        // Create UserData object
        val userData = createUserData(avatarBytes, avatarUrl, downloadFromUrl, username, rank, level, minXP, maxXP, currentXP)

        // Create CardConfiguration object
        val config = CardConfiguration.Builder()
            .dimensions(width, height)
            .accentColor(accentColor)
            .build()

        // Render the card
        return CardRenderer.renderCard(userData, config)
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
     * @param minXP Minimum XP for the current level
     * @param maxXP Maximum XP for the current level
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
        minXP: Int,
        maxXP: Int,
        currentXP: Int,
        accentColor: Int,
        width: Int,
        height: Int,
        onlineStatus: OnlineStatus,
        showStatusIndicator: Boolean,
        showGenerationTime: Boolean
    ): BufferedImage {
        // Create UserData object with online status
        val userDataBuilder = UserData.Builder(username)
            .rank(rank)
            .level(level)
            .xp(minXP, maxXP, currentXP)
            .onlineStatus(onlineStatus)
            .showStatusIndicator(showStatusIndicator)

        // Set avatar source
        if (downloadFromUrl && !avatarUrl.isNullOrEmpty()) {
            userDataBuilder.avatarUrl(avatarUrl)
        } else if (avatarBytes != null) {
            userDataBuilder.avatarBytes(avatarBytes)
        } else {
            throw IllegalArgumentException("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true")
        }

        val userData = userDataBuilder.build()

        // Create CardConfiguration object
        val config = CardConfiguration.Builder()
            .dimensions(width, height)
            .accentColor(accentColor)
            .showGenerationTime(showGenerationTime)
            .build()

        // Render the card
        return CardRenderer.renderCard(userData, config)
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
        minXP: Int,
        maxXP: Int,
        currentXP: Int,
        accentColor: Int
    ): BufferedImage {
        return drawLevelCard(
            avatarBytes, avatarUrl, downloadFromUrl, username, rank, level,
            minXP, maxXP, currentXP, accentColor, DEFAULT_WIDTH, DEFAULT_HEIGHT
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
        minXP: Int,
        maxXP: Int,
        currentXP: Int
    ): BufferedImage {
        return drawLevelCard(
            avatarBytes, avatarUrl, downloadFromUrl, username, rank, level,
            minXP, maxXP, currentXP, DEFAULT_ACCENT_COLOR, DEFAULT_WIDTH, DEFAULT_HEIGHT
        )
    }

    /**
     * Helper method to create a UserData object from the parameters.
     */
    private fun createUserData(
        avatarBytes: ByteArray?,
        avatarUrl: String?,
        downloadFromUrl: Boolean,
        username: String,
        rank: Int,
        level: Int,
        minXP: Int,
        maxXP: Int,
        currentXP: Int
    ): UserData {
        val builder = UserData.Builder(username)
            .rank(rank)
            .level(level)
            .xp(minXP, maxXP, currentXP)

        // Set avatar source
        if (downloadFromUrl && !avatarUrl.isNullOrEmpty()) {
            builder.avatarUrl(avatarUrl)
        } else if (avatarBytes != null) {
            builder.avatarBytes(avatarBytes)
        } else {
            throw IllegalArgumentException("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true")
        }

        return builder.build()
    }
}
