package me.diamondforge.kyromera.levelcardlib

import org.jetbrains.skiko.*
import org.jetbrains.skia.*
import java.io.File
import kotlin.math.min

/**
 * Main class for rendering level cards with user information and statistics.
 * This class handles all the drawing and styling of the level card.
 */
class LevelCard(
    private val userData: UserData = UserData.Builder("User").build(),
    private val colorConfig: ColorConfig = ColorConfig()
) {
    // Calculate progress percentage
    private val progressPercentage: Float
        get() = min(100f, (userData.currentXP.toFloat() / userData.nextLevelXP.toFloat()) * 100f)

    // Default card dimensions
    companion object {
        const val DEFAULT_WIDTH = 500
        const val DEFAULT_HEIGHT = 280
    }

    // Helper function to create a font with a typeface from loadFonts()
    private fun createFont(size: Float, bold: Boolean = false): Font {
        // Get typefaces from loadFonts()
        val (boldTypeface, regularTypeface) = loadFonts()

        var font = if (bold) {
            Font(boldTypeface, size)
        } else {
            Font(regularTypeface, size)
        }
        // Ensure the font is not null
        if (font == null) {
            throw RuntimeException("Failed to create font with size $size and bold $bold")
        }
        return font
    }

    /**
     * Draw the level card on the provided canvas.
     *
     * @param canvas The canvas to draw on
     * @param width The width of the canvas
     * @param height The height of the canvas
     */
    fun draw(canvas: Canvas, width: Int, height: Int) {
        // Draw background
        drawBackground(canvas, width, height)

        // Draw header with user info
        drawHeader(canvas, width, height)

        // Draw XP progress bar
        drawProgressBar(canvas, width, height)

        // Draw stats
        drawStats(canvas, width, height)
    }

    /**
     * Save the level card as an image file.
     *
     * @param filePath The path to save the image to
     * @param width The width of the image (default: DEFAULT_WIDTH)
     * @param height The height of the image (default: DEFAULT_HEIGHT)
     */
    fun saveAsImage(filePath: String, width: Int = DEFAULT_WIDTH, height: Int = DEFAULT_HEIGHT) {
        // Create a surface to render to
        val surface = Surface.makeRasterN32Premul(width, height)
        val canvas = surface.canvas

        // Draw the level card on the surface
        draw(canvas, width, height)

        // Get the image from the surface
        val image = surface.makeImageSnapshot()

        // Encode the image as PNG
        val pngData = image.encodeToData(EncodedImageFormat.PNG)

        // Save the image to a file
        if (pngData != null) {
            val bytes = pngData.bytes
            File(filePath).writeBytes(bytes)
            println("Level card saved to $filePath")
        } else {
            println("Failed to encode image")
        }

        // Clean up
        surface.close()
    }

    private fun drawBackground(canvas: Canvas, width: Int, height: Int) {
        // Create a paint for the background with gradient
        val bgPaint = Paint()

        // Create a gradient from primary color to background color
        bgPaint.shader = Shader.makeLinearGradient(
            0f, 0f,
            width.toFloat(), height.toFloat(),
            intArrayOf(colorConfig.backgroundColor, adjustColor(colorConfig.backgroundColor, 0.8f)),
            floatArrayOf(0f, 1f)
        )

        // Draw the card background with rounded corners
        val rect = Rect(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRect(rect, bgPaint)

        // Add mica effect (semi-transparent overlay with blur)
        val micaPaint = Paint()
        micaPaint.color = 0x10FFFFFF  // Very light white for the mica effect
        micaPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 20f)

        // Draw some blurred shapes for the mica effect
        for (i in 0 until 5) {
            val x = (Math.random() * width).toFloat()
            val y = (Math.random() * height).toFloat()
            val size = (20 + Math.random() * 50).toFloat()

            canvas.drawCircle(x, y, size, micaPaint)
        }

        // Add decorative elements in the corners
        val decorPaint = Paint()
        decorPaint.color = 0x0A0099FF  // Very light blue
        decorPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 40f)

        // Draw some blurred circles for decoration
        canvas.drawCircle(-20f, -20f, 100f, decorPaint)
        canvas.drawCircle(width.toFloat() + 20f, -20f, 100f, decorPaint)
        canvas.drawCircle(-20f, height.toFloat() + 20f, 100f, decorPaint)
        canvas.drawCircle(width.toFloat() + 20f, height.toFloat() + 20f, 100f, decorPaint)
    }

    private fun drawHeader(canvas: Canvas, width: Int, height: Int) {
        val padding = 24f
        val avatarSize = 90f  // Increased avatar size
        val avatarX = padding
        val avatarY = padding
        val avatarCenterX = avatarX + avatarSize / 2
        val avatarCenterY = avatarY + avatarSize / 2

        // Draw avatar background with a subtle glow
        val avatarBgPaint = Paint()
        avatarBgPaint.color = 0xFF2A2A2A.toInt()  // Dark gray background for avatar

        // Add a subtle glow behind the avatar
        val glowPaint = Paint()
        glowPaint.color = 0x30009FFF.toInt()  // Very light blue glow
        glowPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 15f)
        canvas.drawCircle(avatarCenterX, avatarCenterY, avatarSize / 2 + 5f, glowPaint)

        // Draw avatar circle
        canvas.drawCircle(avatarCenterX, avatarCenterY, avatarSize / 2, avatarBgPaint)

        // Draw avatar if available
        if (userData.avatarBytes != null || userData.avatarUrl != null) {
            drawAvatar(canvas, avatarCenterX, avatarCenterY, avatarSize / 2)
        }

        // Draw avatar border with animated gradient effect
        val avatarBorderPaint = Paint()
        val gradientColors = intArrayOf(
            colorConfig.primaryColor,
            colorConfig.secondaryColor,
            adjustColor(colorConfig.primaryColor, 1.2f),
            colorConfig.secondaryColor
        )

        // Create a more dynamic gradient for the avatar border
        avatarBorderPaint.shader = Shader.makeLinearGradient(
            avatarX, avatarY,
            avatarX + avatarSize, avatarY + avatarSize,
            gradientColors,
            floatArrayOf(0f, 0.3f, 0.6f, 1f)
        )
        avatarBorderPaint.setStroke(true)
        avatarBorderPaint.strokeWidth = 4f  // Thicker border

        canvas.drawCircle(avatarCenterX, avatarCenterY, avatarSize / 2, avatarBorderPaint)

        // Draw username and discriminator with improved styling
        val usernameX = avatarX + avatarSize + 20f
        val usernameY = avatarY + 25f

        // Add a subtle text shadow for the username
        val usernameShadowPaint = Paint()
        usernameShadowPaint.color = 0x80000000.toInt()  // Semi-transparent black

        val usernamePaint = Paint()
        usernamePaint.color = colorConfig.textColor

        val discriminatorPaint = Paint()
        discriminatorPaint.color = adjustColor(colorConfig.textColor, 0.7f)

        // Larger username font
        val usernameFont = createFont(24f)
        // Note: Skiko Font doesn't support weight directly

        val discriminatorFont = createFont(16f)

        // Draw username with shadow effect
        canvas.drawString(userData.username, usernameX + 1f, usernameY + 1f, usernameFont, usernameShadowPaint)
        canvas.drawString(userData.username, usernameX, usernameY, usernameFont, usernamePaint)

        // Draw discriminator with better positioning
        // Position the discriminator a fixed distance from the username
        val discX = usernameX + 130f
        if (userData.discriminator.isNotEmpty()) {
            canvas.drawString("#${userData.discriminator}", discX, usernameY, discriminatorFont, discriminatorPaint)
        }

        // Draw rank with improved styling
        val rankY = usernameY + 30f
        val rankPaint = Paint()
        rankPaint.color = adjustColor(colorConfig.textColor, 0.8f)

        val rankValuePaint = Paint()
        rankValuePaint.color = colorConfig.primaryColor

        val rankFont = createFont(16f)

        val rankValueFont = createFont(16f)
        // Note: Skiko Font doesn't support weight directly

        canvas.drawString("Server Rank: ", usernameX, rankY, rankFont, rankPaint)
        canvas.drawString("#${userData.rank}", usernameX + 100f, rankY, rankValueFont, rankValuePaint)

        // Draw level badge with enhanced gradient and glow effect
        val badgeSize = 34f  // Slightly larger badge
        val badgeX = avatarX + avatarSize - badgeSize / 2
        val badgeY = avatarY + avatarSize - badgeSize / 2

        // Add glow effect to the badge
        val badgeGlowPaint = Paint()
        badgeGlowPaint.color = 0x40009FFF.toInt()  // Light blue glow
        badgeGlowPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 8f)
        canvas.drawCircle(badgeX, badgeY, badgeSize / 2 + 3f, badgeGlowPaint)

        // Enhanced gradient for the badge
        val badgePaint = Paint()
        badgePaint.shader = Shader.makeLinearGradient(
            badgeX - badgeSize / 2, badgeY - badgeSize / 2,
            badgeX + badgeSize / 2, badgeY + badgeSize / 2,
            intArrayOf(
                adjustColor(colorConfig.primaryColor, 1.2f),
                colorConfig.primaryColor,
                colorConfig.secondaryColor
            ),
            floatArrayOf(0f, 0.5f, 1f)
        )

        val badgeBorderPaint = Paint()
        badgeBorderPaint.color = 0xFFFFFFFF.toInt()  // White border for better contrast
        badgeBorderPaint.setStroke(true)
        badgeBorderPaint.strokeWidth = 2f

        canvas.drawCircle(badgeX, badgeY, badgeSize / 2, badgePaint)
        canvas.drawCircle(badgeX, badgeY, badgeSize / 2, badgeBorderPaint)

        // Draw level number with improved styling
        val levelTextPaint = Paint()
        levelTextPaint.color = 0xFFFFFFFF.toInt()

        val levelTextFont = createFont(16f)
        // Note: Skiko Font doesn't support weight directly

        // Better centering of the level number
        val levelText = userData.level.toString()
        // Use a fixed offset for centering based on the level number length
        val levelX = if (levelText.length > 1) badgeX - 8f else badgeX - 4f
        canvas.drawString(levelText, levelX, badgeY + 6f, levelTextFont, levelTextPaint)

    }

    /**
     * Draw the avatar image if available.
     */
    private fun drawAvatar(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        try {
            val avatarImage = when {
                userData.avatarBytes != null -> {
                    // Use local avatar bytes
                    Image.makeFromEncoded(userData.avatarBytes)
                }
                userData.avatarUrl != null && userData.imageMode == ImageMode.DOWNLOAD -> {
                    // Download avatar from URL
                    try {
                        val bytes = AvatarManager.downloadImage(userData.avatarUrl)
                        Image.makeFromEncoded(bytes)
                    } catch (e: Exception) {
                        null
                    }
                }
                else -> null
            }

            avatarImage?.let { image ->
                // Create a circular clip for the avatar
                canvas.save()
                canvas.clipRRect(RRect.makeXYWH(
                    centerX - radius, centerY - radius,
                    radius * 2, radius * 2,
                    radius, radius, radius, radius
                ))

                // Scale and center the avatar
                val srcRect = Rect(0f, 0f, image.width.toFloat(), image.height.toFloat())
                val dstRect = Rect(
                    centerX - radius, centerY - radius,
                    centerX + radius, centerY + radius
                )

                canvas.drawImageRect(image, srcRect, dstRect, Paint())
                canvas.restore()
            }
        } catch (e: Exception) {
            // If avatar drawing fails, just continue without it
            println("Failed to draw avatar: ${e.message}")
        }
    }


    // Helper function to format numbers with commas
    private fun formatNumber(number: Int): String {
        return String.format("%,d", number)
    }

    private fun drawProgressBar(canvas: Canvas, width: Int, height: Int) {
        // Get typefaces from loadFonts()
        val (boldTypeface, regularTypeface) = loadFonts()
        val padding = 24f
        val progressY = 150f  // Moved down slightly for better spacing
        val progressHeight = 14f  // Slightly taller progress bar
        val progressWidth = width.toFloat() - (padding * 2)

        // Draw progress text with improved styling
        val textY = progressY - 12f
        val xpFont = createFont(15f)  // Slightly larger font

        // Add a subtle glow to the XP text
        val xpGlowPaint = Paint()
        xpGlowPaint.color = 0x20FFFFFF.toInt()  // Very light white glow
        xpGlowPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 2f)

        val currentXPPaint = Paint()
        currentXPPaint.color = adjustColor(colorConfig.textColor, 0.9f)  // Brighter text

        val nextLevelXPPaint = Paint()
        nextLevelXPPaint.color = adjustColor(colorConfig.textColor, 0.9f)  // Brighter text

        // Format XP numbers with commas for better readability
        val currentXPText = "${formatNumber(userData.currentXP)} XP"
        val nextLevelXPText = "${formatNumber(userData.nextLevelXP)} XP"

        // Draw XP text with subtle glow effect
        canvas.drawString(currentXPText, padding + 1f, textY + 1f, xpFont, xpGlowPaint)
        canvas.drawString(currentXPText, padding, textY, xpFont, currentXPPaint)

        canvas.drawString(nextLevelXPText, width.toFloat() - padding - 70f + 1f, textY + 1f, xpFont, xpGlowPaint)
        canvas.drawString(nextLevelXPText, width.toFloat() - padding - 70f, textY, xpFont, nextLevelXPPaint)

        // Draw percentage in the middle
        val percentText = "${progressPercentage.toInt()}%"
        val percentFont = createFont(12f)

        val percentPaint = Paint()
        percentPaint.color = 0xFFFFFFFF.toInt()

        // Center the percentage text
        val percentX = padding + (progressWidth / 2) - 15f
        canvas.drawString(percentText, percentX, textY, percentFont, percentPaint)

        // Draw progress bar background with enhanced blur effect
        val progressBgPaint = Paint()
        progressBgPaint.color = 0x25FFFFFF.toInt()  // Semi-transparent white
        progressBgPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 2f)

        // Draw progress bar background
        val progressBgRect = Rect(padding, progressY, width.toFloat() - padding, progressY + progressHeight)
        canvas.drawRect(progressBgRect, progressBgPaint)

        // Draw progress bar with enhanced gradient
        val progress = progressPercentage / 100f

        // Create a rectangle for the progress bar
        val progressRect = Rect(
            padding, progressY,
            padding + (progressWidth * progress), progressY + progressHeight
        )

        // Enhanced gradient with more colors for a more dynamic look
        val progressPaint = Paint()
        progressPaint.shader = Shader.makeLinearGradient(
            padding, progressY,
            width.toFloat() - padding, progressY,
            intArrayOf(
                adjustColor(colorConfig.primaryColor, 1.2f),
                colorConfig.primaryColor,
                colorConfig.secondaryColor,
                adjustColor(colorConfig.secondaryColor, 1.1f)
            ),
            floatArrayOf(0f, 0.3f, 0.7f, 1f)
        )

        canvas.drawRect(progressRect, progressPaint)

        // Add a subtle glow to the progress bar
        val progressGlowPaint = Paint()
        progressGlowPaint.color = 0x30FFFFFF.toInt()  // Semi-transparent white
        progressGlowPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 3f)

        // Draw a thin line on top of the progress bar for a glow effect
        val glowRect = Rect(
            padding, progressY,
            padding + (progressWidth * progress), progressY + 2f
        )
        canvas.drawRect(glowRect, progressGlowPaint)

        // Draw enhanced progress crystals
        for (i in 1..5) {
            val crystalX = padding + (progressWidth * (i.toFloat() / 5f))
            val crystalY = progressY + (progressHeight / 2)
            val isActive = i * 20 <= progressPercentage

            // Larger crystals for better visibility
            val crystalSize = if (isActive) 9f else 7f

            // Draw crystal glow for active crystals
            if (isActive) {
                val crystalGlowPaint = Paint()
                crystalGlowPaint.color = 0x60FFFFFF.toInt()  // Semi-transparent white
                crystalGlowPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 6f)
                canvas.drawCircle(crystalX, crystalY, crystalSize + 3f, crystalGlowPaint)
            }

            // Draw crystal with gradient for active ones
            val crystalPaint = Paint()
            if (isActive) {
                crystalPaint.shader = Shader.makeLinearGradient(
                    crystalX - crystalSize, crystalY - crystalSize,
                    crystalX + crystalSize, crystalY + crystalSize,
                    intArrayOf(0xFFFFFFFF.toInt(), 0xFFCCEEFF.toInt()),
                    floatArrayOf(0f, 1f)
                )
            } else {
                crystalPaint.color = 0x33FFFFFF.toInt()
            }

            canvas.drawCircle(crystalX, crystalY, crystalSize, crystalPaint)
        }
    }

    private fun drawStats(canvas: Canvas, width: Int, height: Int) {
        val padding = 24f
        val statsY = 190f  // Moved down to accommodate the enhanced progress bar
        val statWidth = (width.toFloat() - (padding * 2) - 20f) / 3f
        val statHeight = 75f  // Slightly taller stat boxes

        // Define stats with formatted values
        val stats = listOf(
            Triple("Messages", formatNumber(userData.messagesCount), colorConfig.primaryColor),
            Triple("Voice Time", userData.voiceTime, adjustColor(colorConfig.primaryColor, 1.1f)),
            Triple("Day Streak", userData.streak.toString(), colorConfig.accentColor)
        )

        // Draw stats with enhanced styling
        for (i in stats.indices) {
            val statX = padding + (i.toFloat() * (statWidth + 10f))

            // Create a rectangle for the stat background
            val statRect = Rect(statX, statsY, statX + statWidth, statsY + statHeight)

            // Draw stat background with a subtle gradient and glow
            val statBgPaint = Paint()
            statBgPaint.shader = Shader.makeLinearGradient(
                statX, statsY,
                statX, statsY + statHeight,
                intArrayOf(0x15FFFFFF.toInt(), 0x08FFFFFF.toInt()),
                floatArrayOf(0f, 1f)
            )
            statBgPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 1f)

            canvas.drawRect(statRect, statBgPaint)

            // Add a subtle border to the stat box
            val borderPaint = Paint()
            borderPaint.color = 0x10FFFFFF.toInt()  // Very light white
            borderPaint.setStroke(true)
            borderPaint.strokeWidth = 1f
            canvas.drawRect(statRect, borderPaint)

            // Draw stat value with a bold, prominent style and glow effect
            val valueY = statsY + 30f
            val valueFont = createFont(22f)  // Larger value font

            // Add a subtle glow to the value text
            val valueGlowPaint = Paint()
            valueGlowPaint.color = 0x30FFFFFF.toInt()  // Semi-transparent white
            valueGlowPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 2f)

            val valuePaint = Paint()
            valuePaint.color = stats[i].third

            // Center the value text better
            val valueText = stats[i].second
            val valueX = statX + (statWidth / 2) - (valueText.length * 5f)  // Better centering approximation

            // Draw value with glow effect
            canvas.drawString(valueText, valueX + 1f, valueY + 1f, valueFont, valueGlowPaint)
            canvas.drawString(valueText, valueX, valueY, valueFont, valuePaint)

            // Draw stat label with an improved style
            val labelY = statsY + 58f
            val labelFont = createFont(15f)  // Slightly larger label font

            val labelPaint = Paint()
            labelPaint.color = adjustColor(colorConfig.textColor, 0.8f)  // Brighter label text

            // Center the label text better
            val labelText = stats[i].first
            val labelX = statX + (statWidth / 2) - (labelText.length * 3.5f)  // Better centering approximation
            canvas.drawString(labelText, labelX, labelY, labelFont, labelPaint)

            // Add a more prominent accent line at the top of each stat box
            val accentPaint = Paint()
            accentPaint.shader = Shader.makeLinearGradient(
                statX, statsY,
                statX + statWidth, statsY,
                intArrayOf(
                    0x00FFFFFF.toInt(),
                    stats[i].third,
                    stats[i].third,
                    0x00FFFFFF.toInt()
                ),
                floatArrayOf(0f, 0.2f, 0.8f, 1f)
            )

            canvas.drawRect(Rect(statX, statsY, statX + statWidth, statsY + 3f), accentPaint)

            // Add a subtle icon or indicator for each stat
            val iconSize = 12f
            val iconX = statX + 10f
            val iconY = statsY + 15f

            val iconPaint = Paint()
            iconPaint.color = stats[i].third

            when (i) {
                0 -> { // Messages icon (simple chat bubble)
                    val bubblePaint = Paint()
                    bubblePaint.color = stats[i].third
                    canvas.drawRect(Rect(iconX, iconY, iconX + iconSize, iconY + iconSize), bubblePaint)
                }
                1 -> { // Voice time icon (simple microphone)
                    val micPaint = Paint()
                    micPaint.color = stats[i].third
                    canvas.drawCircle(iconX + iconSize/2, iconY + iconSize/2, iconSize/2, micPaint)
                }
                2 -> { // Streak icon (simple flame)
                    val flamePaint = Paint()
                    flamePaint.color = stats[i].third
                    canvas.drawRect(Rect(iconX, iconY, iconX + iconSize, iconY + iconSize), flamePaint)
                }
            }
        }
    }

    // Helper function to adjust color brightness
    private fun adjustColor(color: Int, factor: Float): Int {
        val a = color and 0xFF000000.toInt()
        val r = ((color and 0x00FF0000) shr 16) * factor
        val g = ((color and 0x0000FF00) shr 8) * factor
        val b = (color and 0x000000FF) * factor

        return a or ((r.toInt() shl 16) and 0x00FF0000) or ((g.toInt() shl 8) and 0x0000FF00) or (b.toInt() and 0x000000FF)
    }
}

/**
 * Helper function to load fonts for text rendering.
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
