package me.diamondforge.kyromera.levelcardlib;

import org.jetbrains.skija.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class responsible for handling avatar images, including downloading and processing.
 */
public class AvatarManager {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    /**
     * Downloads an image from a URL and returns it as a byte array.
     *
     * @param url The URL to download the image from
     * @return The image as a byte array
     * @throws IOException If the download fails
     */
    public static byte[] downloadImage(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download image: " + response);
            }

            try (InputStream inputStream = Objects.requireNonNull(response.body()).byteStream()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return outputStream.toByteArray();
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
    public static boolean drawAvatar(
            Canvas canvas,
            byte[] avatarBytes,
            float x,
            float y,
            float size,
            int borderColor,
            OnlineStatus onlineStatus,
            boolean showStatusIndicator
    ) {
        if (avatarBytes == null) {
            return false;
        }

        try {
            Image avatarImage = Image.makeFromEncoded(avatarBytes);

            // Create circular mask for avatar
            Paint avatarPaint = new Paint().setAntiAlias(true);

            // Draw avatar circle border with accent color
            Paint borderPaint = new Paint().setColor(borderColor).setStrokeWidth(4).setMode(PaintMode.STROKE);
            canvas.drawCircle(x + size / 2, y + size / 2, size / 2 + 2, borderPaint);

            // Draw avatar in circular shape
            canvas.save();
            canvas.clipRRect(RRect.makeXYWH(x, y, size, size, size / 2));
            canvas.drawImageRect(
                avatarImage,
                Rect.makeXYWH(0, 0, avatarImage.getWidth(), avatarImage.getHeight()),
                Rect.makeXYWH(x, y, size, size),
                avatarPaint
            );
            canvas.restore();

            // Draw online status indicator if enabled
            if (onlineStatus != null && showStatusIndicator) {
                drawStatusIndicator(canvas, x, y, size, onlineStatus);
            }

            // Clean up resources
            avatarPaint.close();
            borderPaint.close();
            avatarImage.close();

            return true;
        } catch (Exception e) {
            System.err.println("Failed to load avatar image: " + e.getMessage());

            // Draw placeholder avatar
            Paint placeholderPaint = new Paint().setColor(0xFF555555);
            canvas.drawCircle(x + size / 2, y + size / 2, size / 2, placeholderPaint);
            placeholderPaint.close();

            // Draw online status indicator on placeholder if enabled
            if (onlineStatus != null && showStatusIndicator) {
                drawStatusIndicator(canvas, x, y, size, onlineStatus);
            }

            return false;
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
    private static void drawStatusIndicator(Canvas canvas, float avatarX, float avatarY, float avatarSize, OnlineStatus status) {
        float indicatorSize = avatarSize * 0.25f; // 25% of avatar size
        float[] position = StatusIndicator.calculateIndicatorPosition(avatarX, avatarY, avatarSize, indicatorSize);
        StatusIndicator.drawStatusDot(canvas, position[0], position[1], indicatorSize, status);
    }
}
