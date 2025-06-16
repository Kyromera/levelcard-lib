package me.diamondforge.kyromera.levelcardlib

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode

/**
 * Class responsible for drawing status indicators.
 */
object StatusIndicator {

    /**
     * Draws a status indicator dot on the canvas.
     *
     * @param canvas The canvas to draw on
     * @param x The x-coordinate of the center of the indicator
     * @param y The y-coordinate of the center of the indicator
     * @param size The diameter of the indicator
     * @param status The online status to display
     */
    fun drawStatusDot(canvas: Canvas, x: Float, y: Float, size: Float, status: OnlineStatus) {
        // Validate parameters
        if (canvas == null || size <= 0) {
            return
        }

        try {
            // Ensure size is positive and reasonable
            val safeSize = size.coerceIn(1f, 100f)

            // Draw white border around the status indicator
            val borderPaint = Paint().apply {
                color = 0xFFFFFFFF.toInt()
                mode = PaintMode.FILL
                isAntiAlias = true
            }
            canvas.drawCircle(x, y, safeSize / 2 + 2, borderPaint)

            // Draw the status indicator with the appropriate color
            val statusPaint = Paint().apply {
                color = status.color
                mode = PaintMode.FILL
                isAntiAlias = true
            }
            canvas.drawCircle(x, y, safeSize / 2, statusPaint)
        } catch (e: Exception) {
            // Silently ignore any drawing errors to prevent card generation failure
        }
    }

    /**
     * Calculates the position for a status indicator on an avatar.
     * Places the indicator in the bottom-right corner of the avatar.
     *
     * @param avatarX The x-coordinate of the top-left corner of the avatar
     * @param avatarY The y-coordinate of the top-left corner of the avatar
     * @param avatarSize The size (width and height) of the avatar
     * @param indicatorSize The size of the indicator
     * @return An array containing the x and y coordinates of the indicator center
     */
    fun calculateIndicatorPosition(avatarX: Float, avatarY: Float, avatarSize: Float, indicatorSize: Float): FloatArray {
        // Validate parameters and ensure they're reasonable
        val safeAvatarSize = avatarSize.coerceAtLeast(1f)
        val safeIndicatorSize = indicatorSize.coerceIn(1f, safeAvatarSize / 2)

        // Calculate position, ensuring the indicator stays within the avatar bounds
        val indicatorX = (avatarX + safeAvatarSize - safeIndicatorSize / 2).coerceAtLeast(avatarX)
        val indicatorY = (avatarY + safeAvatarSize - safeIndicatorSize / 2).coerceAtLeast(avatarY)

        return floatArrayOf(indicatorX, indicatorY)
    }
}
