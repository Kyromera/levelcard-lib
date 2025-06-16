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
    // Public constants for layout configuration
    const val AVATAR_SIZE = 200 // Scaled from 120 (1.667x)
    const val AVATAR_MARGIN = 50 // Scaled from 30 (1.667x)
    const val TEXT_MARGIN = 270 // Scaled from 170 (1.583x) - Margin for text after avatar
    const val PROGRESS_BAR_HEIGHT = 33 // Scaled from 20 (1.667x)
    const val PROGRESS_BAR_Y_OFFSET = 217 // Scaled from 130 (1.667x)
    const val PROGRESS_BAR_MARGIN = 50 // Scaled from 30 (1.667x)
    const val USERNAME_Y_OFFSET = 83 // Scaled from 50 (1.667x)
    const val RANK_LEVEL_Y_OFFSET = 133 // Scaled from 80 (1.667x)
    const val XP_TEXT_Y_OFFSET = 200 // Scaled from 120 (1.667x)
    const val TIME_TEXT_Y_OFFSET = 267 // Scaled from 160 (1.667x)

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
        drawCardBackground(canvas, config.width, config.height, config)

        // Draw avatar
        drawAvatar(canvas, avatarBytes, config.height, config.accentColor, userData, config)

        // Load fonts
        val typefacePair = loadFonts()

        // Draw text elements
        drawTextElements(canvas, userData, typefacePair.first, typefacePair.second, config)

        // Draw progress bar
        drawProgressBar(canvas, userData, config.width, config.accentColor, config)

        // Calculate and draw generation time if enabled for debugging
        val generationTime = System.currentTimeMillis() - startTime
        if (config.showGenerationTime) {
            drawGenerationTime(canvas, generationTime, typefacePair.second, config)
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
    private fun drawCardBackground(canvas: Canvas, width: Int, height: Int, config: CardConfiguration) {
        // Draw shadow for the background
        val shadowPaint = Paint().apply {
            color = 0x40000000 // Semi-transparent black
            isAntiAlias = true
            maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, config.shadowBlur)
        }
        canvas.drawRRect(RRect.makeXYWH(7f, 7f, width.toFloat() - 14f, height.toFloat() - 14f, config.cornerRadius), shadowPaint)

        // Draw background (dark gray with rounded corners)
        val bgPaint = Paint().apply {
            color = 0xFF2A2A2A.toInt()
            isAntiAlias = true
        }
        canvas.drawRRect(RRect.makeXYWH(0f, 0f, width.toFloat(), height.toFloat(), config.cornerRadius), bgPaint)
    }

    /**
     * Draws the avatar on the card.
     */
    private fun drawAvatar(canvas: Canvas, avatarBytes: ByteArray?, cardHeight: Int, accentColor: Int, userData: UserData, config: CardConfiguration) {
        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.avatarXOffset?.toFloat() ?: config.avatarMargin.toFloat()
        val yOffset = if (config.layoutConfig?.avatarYOffset != 0) {
            config.layoutConfig?.avatarYOffset?.toFloat() ?: (cardHeight / 2 - config.avatarSize / 2).toFloat()
        } else {
            (cardHeight / 2 - config.avatarSize / 2).toFloat()
        }

        AvatarManager.drawAvatar(
            canvas,
            avatarBytes,
            xOffset,
            yOffset,
            config.avatarSize.toFloat(),
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
    private fun drawTextElements(canvas: Canvas, userData: UserData, boldTypeface: Typeface, regularTypeface: Typeface, config: CardConfiguration) {
        // Draw username
        drawUsername(canvas, userData.username, boldTypeface, config)

        // Draw rank and level
        drawRankAndLevel(canvas, userData.rank, userData.level, regularTypeface, config)

        // Draw XP text
        drawXpText(canvas, userData.currentXP, userData.maxXpForCurrentLevel, regularTypeface, config)
    }

    /**
     * Draws the username on the card.
     */
    private fun drawUsername(canvas: Canvas, username: String, boldTypeface: Typeface, config: CardConfiguration) {
        val usernameFont = Font(boldTypeface, config.usernameFontSize)
        val usernamePaint = Paint().apply {
            color = 0xFFFFFFFF.toInt()
            isAntiAlias = true
        }

        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.usernameXOffset ?: config.textMargin
        val yOffset = config.layoutConfig?.usernameYOffset ?: config.usernameYOffset

        canvas.drawString(username, xOffset.toFloat(), yOffset.toFloat(), usernameFont, usernamePaint)
    }

    /**
     * Draws the rank and level information on the card.
     */
    private fun drawRankAndLevel(canvas: Canvas, rank: Int, level: Int, regularTypeface: Typeface, config: CardConfiguration) {
        val rankLevelFont = Font(regularTypeface, config.rankLevelFontSize)
        val rankLevelPaint = Paint().apply {
            color = 0xFFCCCCCC.toInt()
            isAntiAlias = true
        }

        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.rankLevelXOffset ?: config.textMargin
        val yOffset = config.layoutConfig?.rankLevelYOffset ?: config.rankLevelYOffset

        val rankLevelText = "Rank #$rank | Level $level"
        canvas.drawString(rankLevelText, xOffset.toFloat(), yOffset.toFloat(), rankLevelFont, rankLevelPaint)
    }

    /**
     * Draws the XP text on the card.
     */
    private fun drawXpText(canvas: Canvas, currentXP: Int, maxXP: Int, regularTypeface: Typeface, config: CardConfiguration) {
        val xpFont = Font(regularTypeface, config.xpFontSize)
        val xpPaint = Paint().apply {
            color = 0xFFAAAAAA.toInt()
            isAntiAlias = true
        }

        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.xpTextXOffset ?: config.textMargin
        val yOffset = config.layoutConfig?.xpTextYOffset ?: config.xpTextYOffset

        val xpText = "$currentXP / $maxXP XP"
        canvas.drawString(xpText, xOffset.toFloat(), yOffset.toFloat(), xpFont, xpPaint)
    }

    /**
     * Draws the progress bar on the card.
     */
    private fun drawProgressBar(canvas: Canvas, userData: UserData, cardWidth: Int, accentColor: Int, config: CardConfiguration) {
        // Draw progress bar background
        drawProgressBarBackground(canvas, cardWidth, config)

        // Draw progress bar fill
        drawProgressBarFill(canvas, userData, cardWidth, accentColor, config)
    }

    /**
     * Draws the background of the progress bar.
     */
    private fun drawProgressBarBackground(canvas: Canvas, cardWidth: Int, config: CardConfiguration) {
        val progressBgPaint = Paint().apply {
            color = 0xFF444444.toInt()
            isAntiAlias = true
        }

        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.progressBarXOffset ?: config.textMargin
        val yOffset = config.layoutConfig?.progressBarYOffset ?: config.progressBarYOffset
        val rightMargin = config.layoutConfig?.progressBarRightMargin ?: config.progressBarMargin

        canvas.drawRRect(
            RRect.makeXYWH(
                xOffset.toFloat(),
                yOffset.toFloat(),
                (cardWidth - xOffset - rightMargin).toFloat(),
                config.progressBarHeight.toFloat(),
                (config.progressBarHeight / 2).toFloat()
            ),
            progressBgPaint
        )
    }

    /**
     * Draws the filled portion of the progress bar.
     */
    private fun drawProgressBarFill(canvas: Canvas, userData: UserData, cardWidth: Int, accentColor: Int, config: CardConfiguration) {
        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.progressBarXOffset ?: config.textMargin
        val yOffset = config.layoutConfig?.progressBarYOffset ?: config.progressBarYOffset
        val rightMargin = config.layoutConfig?.progressBarRightMargin ?: config.progressBarMargin

        val progressWidth = (userData.currentXP - userData.minXpForCurrentLevel).toFloat() /
                (userData.maxXpForCurrentLevel - userData.minXpForCurrentLevel).toFloat() *
                (cardWidth - xOffset - rightMargin).toFloat()
        val progressPaint = Paint().apply {
            color = accentColor
            isAntiAlias = true
        }
        canvas.drawRRect(
            RRect.makeXYWH(
                xOffset.toFloat(),
                yOffset.toFloat(),
                progressWidth,
                config.progressBarHeight.toFloat(),
                (config.progressBarHeight / 2).toFloat()
            ),
            progressPaint
        )
    }

    /**
     * Draws the generation time text on the card.
     */
    private fun drawGenerationTime(canvas: Canvas, generationTime: Long, regularTypeface: Typeface, config: CardConfiguration) {
        val timeFont = Font(regularTypeface, config.timeFontSize)
        val timePaint = Paint().apply {
            color = 0xFF888888.toInt()
            isAntiAlias = true
        }

        // Use layout config if available, otherwise use default config
        val xOffset = config.layoutConfig?.timeTextXOffset ?: config.textMargin
        val yOffset = config.layoutConfig?.timeTextYOffset ?: config.timeTextYOffset

        val timeText = "Generated in ${generationTime}ms"
        canvas.drawString(timeText, xOffset.toFloat(), yOffset.toFloat(), timeFont, timePaint)
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
