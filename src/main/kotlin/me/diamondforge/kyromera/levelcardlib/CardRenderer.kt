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

        // Get avatar bytes (download if needed)
        val avatarBytes = getAvatarBytes(userData)

        // Create Skia surface and canvas
        val surface = createSurface(config.width, config.height)
        val canvas = surface.canvas

        // Draw card background
        drawCardBackground(canvas, config.width, config.height)

        // Draw avatar
        drawAvatar(canvas, avatarBytes, config.height, config.accentColor, userData)

        // Load fonts
        val typefacePair = loadFonts()

        // Draw text elements
        drawTextElements(canvas, userData, typefacePair.first, typefacePair.second)

        // Draw progress bar
        drawProgressBar(canvas, userData, config.width, config.accentColor)

        // Calculate and draw generation time if enabled for debugging
        val generationTime = System.currentTimeMillis() - startTime
        if (config.showGenerationTime) {
            drawGenerationTime(canvas, generationTime, typefacePair.second)
        }

        // Convert Skia image to BufferedImage
        return convertToBufferedImage(surface)
    }

    /**
     * Gets avatar bytes, downloading if necessary.
     */
    private fun getAvatarBytes(userData: UserData): ByteArray? {
        var avatarBytes = userData.avatarBytes
        if (userData.imageMode == ImageMode.DOWNLOAD && userData.avatarUrl != null) {
            try {
                avatarBytes = AvatarManager.downloadImage(userData.avatarUrl)
            } catch (e: IOException) {
                throw RuntimeException("Failed to download avatar from URL: ${userData.avatarUrl}", e)
            }
        }
        return avatarBytes
    }

    /**
     * Creates a Skia surface with the specified dimensions.
     */
    private fun createSurface(width: Int, height: Int): Surface {
        return Surface.makeRasterN32Premul(width, height)
    }

    /**
     * Draws the card background with shadow and rounded corners.
     */
    private fun drawCardBackground(canvas: Canvas, width: Int, height: Int) {
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
    }

    /**
     * Draws the avatar on the card.
     */
    private fun drawAvatar(canvas: Canvas, avatarBytes: ByteArray?, cardHeight: Int, accentColor: Int, userData: UserData) {
        AvatarManager.drawAvatar(
            canvas,
            avatarBytes,
            AVATAR_MARGIN.toFloat(),
            (cardHeight / 2 - AVATAR_SIZE / 2).toFloat(),
            AVATAR_SIZE.toFloat(),
            accentColor,
            userData.onlineStatus,
            userData.showStatusIndicator
        )
    }

    /**
     * Loads fonts for text rendering.
     * @return Pair of (boldTypeface, regularTypeface)
     */
    private fun loadFonts(): Pair<Typeface, Typeface> {
        val fontManager = FontMgr.default

        // Try to get Arial Bold, then Sans-Serif Bold, then default to a character-based match
        var boldTypeface = fontManager.matchFamilyStyle("Arial", FontStyle.BOLD)
        if (boldTypeface == null) {
            boldTypeface = fontManager.matchFamilyStyle("Sans-Serif", FontStyle.BOLD)
        }
        if (boldTypeface == null && fontManager.familiesCount > 0) {
            boldTypeface = fontManager.matchFamilyStyleCharacter(
                null, FontStyle.BOLD, null, 'A'.code
            )
        }
        // If still null, throw an exception as we need a font to render text
        if (boldTypeface == null) {
            throw RuntimeException("Failed to load any usable bold font")
        }

        // Try to get Arial Regular, then Sans-Serif Regular, then default to a character-based match
        var regularTypeface = fontManager.matchFamilyStyle("Arial", FontStyle.NORMAL)
        if (regularTypeface == null) {
            regularTypeface = fontManager.matchFamilyStyle("Sans-Serif", FontStyle.NORMAL)
        }
        if (regularTypeface == null && fontManager.familiesCount > 0) {
            regularTypeface = fontManager.matchFamilyStyleCharacter(
                null, FontStyle.NORMAL, null, 'A'.code
            )
        }
        // If still null, throw an exception as we need a font to render text
        if (regularTypeface == null) {
            throw RuntimeException("Failed to load any usable regular font")
        }

        return Pair(boldTypeface, regularTypeface)
    }

    /**
     * Draws all text elements on the card.
     */
    private fun drawTextElements(canvas: Canvas, userData: UserData, boldTypeface: Typeface, regularTypeface: Typeface) {
        // Draw username
        drawUsername(canvas, userData.username, boldTypeface)

        // Draw rank and level
        drawRankAndLevel(canvas, userData.rank, userData.level, regularTypeface)

        // Draw XP text
        drawXpText(canvas, userData.currentXP, userData.maxXpForCurrentLevel, regularTypeface)
    }

    /**
     * Draws the username on the card.
     */
    private fun drawUsername(canvas: Canvas, username: String, boldTypeface: Typeface) {
        val usernameFont = Font(boldTypeface, 47f) // Scaled from 28f (1.667x)
        val usernamePaint = Paint().apply {
            color = 0xFFFFFFFF.toInt()
            isAntiAlias = true
        }
        canvas.drawString(username, TEXT_MARGIN.toFloat(), USERNAME_Y_OFFSET.toFloat(), usernameFont, usernamePaint)
    }

    /**
     * Draws the rank and level information on the card.
     */
    private fun drawRankAndLevel(canvas: Canvas, rank: Int, level: Int, regularTypeface: Typeface) {
        val rankLevelFont = Font(regularTypeface, 33f) // Scaled from 20f (1.667x)
        val rankLevelPaint = Paint().apply {
            color = 0xFFCCCCCC.toInt()
            isAntiAlias = true
        }
        val rankLevelText = "Rank #$rank | Level $level"
        canvas.drawString(rankLevelText, TEXT_MARGIN.toFloat(), RANK_LEVEL_Y_OFFSET.toFloat(), rankLevelFont, rankLevelPaint)
    }

    /**
     * Draws the XP text on the card.
     */
    private fun drawXpText(canvas: Canvas, currentXP: Int, maxXP: Int, regularTypeface: Typeface) {
        val xpFont = Font(regularTypeface, 27f) // Scaled from 16f (1.667x)
        val xpPaint = Paint().apply {
            color = 0xFFAAAAAA.toInt()
            isAntiAlias = true
        }
        val xpText = "$currentXP / $maxXP XP"
        canvas.drawString(xpText, TEXT_MARGIN.toFloat(), XP_TEXT_Y_OFFSET.toFloat(), xpFont, xpPaint)
    }

    /**
     * Draws the progress bar on the card.
     */
    private fun drawProgressBar(canvas: Canvas, userData: UserData, cardWidth: Int, accentColor: Int) {
        // Draw progress bar background
        drawProgressBarBackground(canvas, cardWidth)

        // Draw progress bar fill
        drawProgressBarFill(canvas, userData, cardWidth, accentColor)
    }

    /**
     * Draws the background of the progress bar.
     */
    private fun drawProgressBarBackground(canvas: Canvas, cardWidth: Int) {
        val progressBgPaint = Paint().apply {
            color = 0xFF444444.toInt()
            isAntiAlias = true
        }
        canvas.drawRRect(
            RRect.makeXYWH(
                TEXT_MARGIN.toFloat(),
                PROGRESS_BAR_Y_OFFSET.toFloat(),
                (cardWidth - TEXT_MARGIN - PROGRESS_BAR_MARGIN).toFloat(),
                PROGRESS_BAR_HEIGHT.toFloat(),
                (PROGRESS_BAR_HEIGHT / 2).toFloat() // This is already scaled because PROGRESS_BAR_HEIGHT is scaled
            ),
            progressBgPaint
        )
    }

    /**
     * Draws the filled portion of the progress bar.
     */
    private fun drawProgressBarFill(canvas: Canvas, userData: UserData, cardWidth: Int, accentColor: Int) {
        val progressWidth = (userData.currentXP - userData.minXpForCurrentLevel).toFloat() /
                (userData.maxXpForCurrentLevel - userData.minXpForCurrentLevel).toFloat() *
                (cardWidth - TEXT_MARGIN - PROGRESS_BAR_MARGIN).toFloat()
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
    }

    /**
     * Draws the generation time text on the card.
     */
    private fun drawGenerationTime(canvas: Canvas, generationTime: Long, regularTypeface: Typeface) {
        val timeFont = Font(regularTypeface, 20f) // Scaled from 12f (1.667x)
        val timePaint = Paint().apply {
            color = 0xFF888888.toInt()
            isAntiAlias = true
        }
        val timeText = "Generated in ${generationTime}ms"
        canvas.drawString(timeText, TEXT_MARGIN.toFloat(), TIME_TEXT_Y_OFFSET.toFloat(), timeFont, timePaint)
    }

    /**
     * Converts a Skia surface to a Java BufferedImage.
     */
    private fun convertToBufferedImage(surface: Surface): BufferedImage {
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
