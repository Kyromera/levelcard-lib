package me.diamondforge.kyromera.levelcardlib

import org.jetbrains.skia.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Class responsible for handling avatar images, including downloading and processing.
 */
object AvatarManager {
    private val HTTP_CLIENT = OkHttpClient()

    /**
     * Downloads an image from a URL and returns it as a byte array.
     *
     * @param url The URL to download the image from
     * @return The image as a byte array
     * @throws IOException If the download fails
     */
    @Throws(IOException::class)
    fun downloadImage(url: String): ByteArray {
        try {
            // Create a client with timeouts
            val client = OkHttpClient.Builder()
                .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "LevelCardLib/1.0")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Failed to download image: ${response.code} ${response.message}")
                }

                response.body?.byteStream()?.use { inputStream ->
                    val outputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesRead = 0
                    val maxBytes = 10 * 1024 * 1024 // 10MB max

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead

                        // Check if we've exceeded the maximum size
                        if (totalBytesRead > maxBytes) {
                            throw IOException("Image too large (exceeds 10MB)")
                        }
                    }

                    val bytes = outputStream.toByteArray()
                    if (bytes.isEmpty()) {
                        throw IOException("Downloaded image is empty")
                    }

                    return bytes
                } ?: throw IOException("Response body is null")
            }
        } catch (e: Exception) {
            // Convert any exception to IOException with a descriptive message
            when (e) {
                is IOException -> throw e
                is IllegalArgumentException -> throw IOException("Invalid URL: $url", e)
                else -> throw IOException("Failed to download image: ${e.message}", e)
            }
        }
    }

    /**
     * Draws the avatar on the canvas with a circular mask.
     *
     * @param canvas The canvas to draw on
     * @param avatarBytes The avatar image as a byte array
     * @param x The x-coordinate of the top-left corner of the avatar
     * @param y The y-coordinate of the top-left corner of the avatar
     * @param size The size (width and height) of the avatar
     * @param borderColor The color of the border (ARGB format)
     * @param onlineStatus The online status to display, or null to hide the status indicator
     * @param showStatusIndicator Whether to show the status indicator
     * @return True if the avatar was drawn successfully, false otherwise
     */
    fun drawAvatar(
        canvas: Canvas,
        avatarBytes: ByteArray?,
        x: Float,
        y: Float,
        size: Float,
        borderColor: Int,
        onlineStatus: OnlineStatus?,
        showStatusIndicator: Boolean
    ): Boolean {
        if (avatarBytes == null || avatarBytes.isEmpty()) {
            drawPlaceholderAvatar(canvas, x, y, size, borderColor, onlineStatus, showStatusIndicator)
            return false
        }

        // Ensure size is positive
        val safeSize = size.coerceAtLeast(1f)

        return try {
            val avatarImage = Image.makeFromEncoded(avatarBytes)

            // Check if image was loaded successfully
            if (avatarImage == null || avatarImage.width <= 0 || avatarImage.height <= 0) {
                drawPlaceholderAvatar(canvas, x, y, safeSize, borderColor, onlineStatus, showStatusIndicator)
                return false
            }

            // Create circular mask for avatar with anti-aliasing
            val avatarPaint = Paint().apply {
                isAntiAlias = true
            }

            try {
                // Draw avatar circle border with accent color
                val borderPaint = Paint().apply {
                    color = borderColor
                    strokeWidth = 7f // Scaled from 4f (1.667x)
                    mode = PaintMode.STROKE
                    isAntiAlias = true
                }
                canvas.drawCircle(x + safeSize / 2, y + safeSize / 2, safeSize / 2 + 2, borderPaint)

                // Draw avatar in circular shape
                canvas.save()
                canvas.clipRRect(RRect.makeXYWH(x, y, safeSize, safeSize, safeSize / 2))

                // Ensure anti-aliasing is enabled
                avatarPaint.apply {
                    isAntiAlias = true
                }

                canvas.drawImageRect(
                    avatarImage,
                    Rect.makeXYWH(0f, 0f, avatarImage.width.toFloat(), avatarImage.height.toFloat()),
                    Rect.makeXYWH(x, y, safeSize, safeSize),
                    avatarPaint
                )
                canvas.restore()

                // Draw online status indicator if enabled
                if (onlineStatus != null && showStatusIndicator) {
                    drawStatusIndicator(canvas, x, y, safeSize, onlineStatus)
                }

                true
            } catch (e: Exception) {
                // If any drawing operation fails, try to recover by drawing a placeholder
                drawPlaceholderAvatar(canvas, x, y, safeSize, borderColor, onlineStatus, showStatusIndicator)
                false
            }
        } catch (e: Exception) {
            // If image loading fails, draw a placeholder
            drawPlaceholderAvatar(canvas, x, y, safeSize, borderColor, onlineStatus, showStatusIndicator)
            false
        }
    }

    /**
     * Draws a placeholder avatar when the actual avatar cannot be loaded.
     */
    private fun drawPlaceholderAvatar(
        canvas: Canvas,
        x: Float,
        y: Float,
        size: Float,
        borderColor: Int,
        onlineStatus: OnlineStatus?,
        showStatusIndicator: Boolean
    ) {
        try {
            // Draw placeholder circle with border
            val borderPaint = Paint().apply {
                color = borderColor
                strokeWidth = 7f
                mode = PaintMode.STROKE
                isAntiAlias = true
            }
            canvas.drawCircle(x + size / 2, y + size / 2, size / 2 + 2, borderPaint)

            // Draw placeholder avatar
            val placeholderPaint = Paint().apply {
                color = 0xFF555555.toInt()
                isAntiAlias = true
            }
            canvas.drawCircle(x + size / 2, y + size / 2, size / 2, placeholderPaint)

            // Draw online status indicator on placeholder if enabled
            if (onlineStatus != null && showStatusIndicator) {
                drawStatusIndicator(canvas, x, y, size, onlineStatus)
            }
        } catch (e: Exception) {
            // Silently ignore any errors in the placeholder drawing to prevent cascading failures
        }
    }

    /**
     * Draws the status indicator dot on the avatar.
     *
     * @param canvas The canvas to draw on
     * @param avatarX The x-coordinate of the top-left corner of the avatar
     * @param avatarY The y-coordinate of the top-left corner of the avatar
     * @param avatarSize The size (width and height) of the avatar
     * @param status The online status to display
     */
    private fun drawStatusIndicator(canvas: Canvas, avatarX: Float, avatarY: Float, avatarSize: Float, status: OnlineStatus) {
        val indicatorSize = avatarSize * 0.25f // 25% of avatar size
        val position = StatusIndicator.calculateIndicatorPosition(avatarX, avatarY, avatarSize, indicatorSize)
        StatusIndicator.drawStatusDot(canvas, position[0], position[1], indicatorSize, status)
    }
}
