package me.diamondforge.kyromera.levelcardlib

import org.jetbrains.skia.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Class responsible for rendering the level card.
 */
object CardRenderer {
    private const val AVATAR_SIZE = 200 // Scaled from 120 (1.667x)
    private const val AVATAR_MARGIN = 50 // Scaled from 30 (1.667x)
    private const val TEXT_MARGIN = 270 // Scaled from 170 (1.583x) - Margin for text after avatar
    private const val PROGRESS_BAR_HEIGHT = 33 // Scaled from 20 (1.667x)
    private const val PROGRESS_BAR_Y_OFFSET = 217 // Scaled from 130 (1.667x)
    private const val PROGRESS_BAR_MARGIN = 50 // Scaled from 30 (1.667x)
    private const val USERNAME_Y_OFFSET = 83 // Scaled from 50 (1.667x)
    private const val RANK_LEVEL_Y_OFFSET = 133 // Scaled from 80 (1.667x)
    private const val XP_TEXT_Y_OFFSET = 200 // Scaled from 120 (1.667x)
    private const val TIME_TEXT_Y_OFFSET = 267 // Scaled from 160 (1.667x)

    /**
     * Renders a level card with the specified parameters.
     *
     * @param userData The user data to display on the card
     * @param config The card configuration
     * @return The rendered level card as a BufferedImage
     */
    fun renderCard(
        userData: UserData,
        config: CardConfiguration
    ): BufferedImage {
        // Start timing the generation
        val startTime = System.currentTimeMillis()

        // Get configuration values
        val accentColor = config.accentColor
        val width = config.width
        val height = config.height
        val showGenerationTime = config.showGenerationTime

        // Get avatar bytes (download if needed)
        var avatarBytes = userData.avatarBytes
        if (userData.imageMode == ImageMode.DOWNLOAD && userData.avatarUrl != null) {
            try {
                avatarBytes = AvatarManager.downloadImage(userData.avatarUrl)
            } catch (e: IOException) {
                throw RuntimeException("Failed to download avatar from URL: ${userData.avatarUrl}", e)
            }
        }

        // Create Skia surface
        val surface = Surface.makeRasterN32Premul(width, height)
        val canvas = surface.canvas

        // Draw shadow for the background
        val shadowPaint = Paint().apply {
            color = 0x40000000 // Semi-transparent black
            isAntiAlias = true
            maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 13f) // Scaled from 8f (1.667x)
        }
        canvas.drawRRect(RRect.makeXYWH(7f, 7f, width.toFloat() - 14f, height.toFloat() - 14f, 25f), shadowPaint) // Scaled values (1.667x)

        // Draw background (dark gray with rounded corners)
        val bgPaint = Paint().apply {
            color = 0xFF2A2A2A.toInt()
            isAntiAlias = true
        }
        canvas.drawRRect(RRect.makeXYWH(0f, 0f, width.toFloat(), height.toFloat(), 25f), bgPaint) // Scaled corner radius from 15f to 25f

        // Draw avatar if available
        AvatarManager.drawAvatar(
            canvas,
            avatarBytes,
            AVATAR_MARGIN.toFloat(),
            (height / 2 - AVATAR_SIZE / 2).toFloat(),
            AVATAR_SIZE.toFloat(),
            accentColor,
            userData.onlineStatus,
            userData.showStatusIndicator
        )

        // Load fonts
        val fontManager = FontMgr.default

        // Use a modern font for bold text if available, otherwise use Arial
        val boldTypeface = fontManager.matchFamilyStyle("Arial", FontStyle.BOLD)

        // Use a modern font for regular text if available, otherwise use Arial
        val regularTypeface = fontManager.matchFamilyStyle("Arial", FontStyle.NORMAL)

        // Draw username
        val usernameFont = Font(boldTypeface, 47f) // Scaled from 28f (1.667x)
        val usernamePaint = Paint().apply {
            color = 0xFFFFFFFF.toInt()
            isAntiAlias = true
        }
        canvas.drawString(userData.username, TEXT_MARGIN.toFloat(), USERNAME_Y_OFFSET.toFloat(), usernameFont, usernamePaint)

        // Draw rank and level
        val rankLevelFont = Font(regularTypeface, 33f) // Scaled from 20f (1.667x)
        val rankLevelPaint = Paint().apply {
            color = 0xFFCCCCCC.toInt()
            isAntiAlias = true
        }
        val rankLevelText = "Rank #${userData.rank} | Level ${userData.level}"
        canvas.drawString(rankLevelText, TEXT_MARGIN.toFloat(), RANK_LEVEL_Y_OFFSET.toFloat(), rankLevelFont, rankLevelPaint)

        // Draw XP text
        val xpFont = Font(regularTypeface, 27f) // Scaled from 16f (1.667x)
        val xpPaint = Paint().apply {
            color = 0xFFAAAAAA.toInt()
            isAntiAlias = true
        }
        val xpText = "${userData.currentXP} / ${userData.maxXP} XP"
        canvas.drawString(xpText, TEXT_MARGIN.toFloat(), XP_TEXT_Y_OFFSET.toFloat(), xpFont, xpPaint)

        // Draw progress bar background
        val progressBgPaint = Paint().apply {
            color = 0xFF444444.toInt()
            isAntiAlias = true
        }
        canvas.drawRRect(
            RRect.makeXYWH(
                TEXT_MARGIN.toFloat(),
                PROGRESS_BAR_Y_OFFSET.toFloat(),
                (width - TEXT_MARGIN - PROGRESS_BAR_MARGIN).toFloat(),
                PROGRESS_BAR_HEIGHT.toFloat(),
                (PROGRESS_BAR_HEIGHT / 2).toFloat() // This is already scaled because PROGRESS_BAR_HEIGHT is scaled
            ),
            progressBgPaint
        )

        // Draw progress bar
        val progressWidth = (userData.currentXP - userData.minXP).toFloat() /
                (userData.maxXP - userData.minXP).toFloat() *
                (width - TEXT_MARGIN - PROGRESS_BAR_MARGIN).toFloat()
        val progressPaint = Paint().apply {
            color = accentColor
            isAntiAlias = true
        }
        canvas.drawRRect(
            RRect.makeXYWH(
                TEXT_MARGIN.toFloat(),
                PROGRESS_BAR_Y_OFFSET.toFloat(),
                progressWidth,
                PROGRESS_BAR_HEIGHT.toFloat(),
                (PROGRESS_BAR_HEIGHT / 2).toFloat() // This is already scaled because PROGRESS_BAR_HEIGHT is scaled
            ),
            progressPaint
        )

        // Calculate generation time
        val endTime = System.currentTimeMillis()
        val generationTime = endTime - startTime

        // Draw generation time if enabled
        if (showGenerationTime) {
            val timeFont = Font(regularTypeface, 20f) // Scaled from 12f (1.667x)
            val timePaint = Paint().apply {
                color = 0xFF888888.toInt()
                isAntiAlias = true
            }
            val timeText = "Generated in ${generationTime}ms"
            canvas.drawString(timeText, TEXT_MARGIN.toFloat(), TIME_TEXT_Y_OFFSET.toFloat(), timeFont, timePaint)
        }

        // Convert Skia image to BufferedImage
        val skiaImage = surface.makeImageSnapshot()
        val imageData = skiaImage.encodeToData(EncodedImageFormat.PNG)
            ?: throw RuntimeException("Failed to encode image to PNG")
        val pngBytes = imageData.bytes

        try {
            val inputStream = ByteArrayInputStream(pngBytes)
            return ImageIO.read(inputStream)
        } catch (e: IOException) {
            throw RuntimeException("Failed to convert Skia image to BufferedImage", e)
        }
    }
}
