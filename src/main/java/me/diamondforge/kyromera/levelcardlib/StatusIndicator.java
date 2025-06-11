package me.diamondforge.kyromera.levelcardlib;

import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.Paint;

/**
 * Class responsible for drawing status indicators.
 */
public class StatusIndicator {
    
    /**
     * Draws a status indicator dot on the canvas.
     *
     * @param canvas The canvas to draw on
     * @param x The x-coordinate of the center of the indicator
     * @param y The y-coordinate of the center of the indicator
     * @param size The diameter of the indicator
     * @param status The online status to display
     */
    public static void drawStatusDot(Canvas canvas, float x, float y, float size, OnlineStatus status) {
        // Draw white border around the status indicator
        Paint borderPaint = new Paint().setColor(0xFFFFFFFF).setAntiAlias(true);
        canvas.drawCircle(x, y, size / 2 + 2, borderPaint);
        
        // Draw the status indicator with the appropriate color
        Paint statusPaint = new Paint().setColor(status.getColor()).setAntiAlias(true);
        canvas.drawCircle(x, y, size / 2, statusPaint);
        
        // Clean up resources
        borderPaint.close();
        statusPaint.close();
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
    public static float[] calculateIndicatorPosition(float avatarX, float avatarY, float avatarSize, float indicatorSize) {
        float indicatorX = avatarX + avatarSize - indicatorSize / 2;
        float indicatorY = avatarY + avatarSize - indicatorSize / 2;
        return new float[] { indicatorX, indicatorY };
    }
}