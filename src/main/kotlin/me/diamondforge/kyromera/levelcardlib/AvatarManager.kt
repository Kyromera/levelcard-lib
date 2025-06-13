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
        val request = Request.Builder().url(url).build()
        HTTP_CLIENT.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Failed to download image: $response")
            }

            response.body?.byteStream()?.use { inputStream ->
                val outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                return outputStream.toByteArray()
            } ?: throw IOException("Response body is null")
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
    ): Boolean {
        if (avatarBytes == null) {
            return false
        }

        return try {
            val avatarImage = Image.makeFromEncoded(avatarBytes)

            // Create circular mask for avatar with anti-aliasing
            val avatarPaint = Paint().apply {
                isAntiAlias = true
            }

            // Draw avatar circle border with accent color
            val borderPaint = Paint().apply {
                color = borderColor
                strokeWidth = 7f // Scaled from 4f (1.667x)
                mode = PaintMode.STROKE
                isAntiAlias = true
            }
            canvas.drawCircle(x + size / 2, y + size / 2, size / 2 + 2, borderPaint)

            // Draw avatar in circular shape
            canvas.save()
            canvas.clipRRect(RRect.makeXYWH(x, y, size, size, size / 2))

            // Ensure anti-aliasing is enabled
            avatarPaint.apply {
                isAntiAlias = true
            }

            canvas.drawImageRect(
                avatarImage,
                Rect.makeXYWH(0f, 0f, avatarImage.width.toFloat(), avatarImage.height.toFloat()),
                Rect.makeXYWH(x, y, size, size),
                avatarPaint
            )
            canvas.restore()

            true
        } catch (e: Exception) {
            System.err.println("Failed to load avatar image: ${e.message}")

            // Draw placeholder avatar
            val placeholderPaint = Paint().apply {
                color = 0xFF555555.toInt()
            }
            canvas.drawCircle(x + size / 2, y + size / 2, size / 2, placeholderPaint)


            false
        }
    }
}
