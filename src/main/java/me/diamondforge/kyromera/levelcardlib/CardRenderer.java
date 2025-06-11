package me.diamondforge.kyromera.levelcardlib;

import org.jetbrains.skija.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class responsible for rendering the level card.
 */
public class CardRenderer {
    private static final int AVATAR_SIZE = 120;
    private static final int AVATAR_MARGIN = 30;
    private static final int TEXT_MARGIN = 170; // Margin for text after avatar
    private static final int PROGRESS_BAR_HEIGHT = 20;
    private static final int PROGRESS_BAR_Y_OFFSET = 130;
    private static final int PROGRESS_BAR_MARGIN = 30;
    private static final int USERNAME_Y_OFFSET = 50;
    private static final int RANK_LEVEL_Y_OFFSET = 80;
    private static final int XP_TEXT_Y_OFFSET = 120;
    private static final int TIME_TEXT_Y_OFFSET = 160;

    /**
     * Renders a level card with the specified parameters.
     *
     * @param userData The user data to display on the card
     * @param config The card configuration
     * @return The rendered level card as a BufferedImage
     */
    public static BufferedImage renderCard(
            UserData userData,
            CardConfiguration config
    ) {
        // Start timing the generation
        long startTime = System.currentTimeMillis();

        // Get configuration values
        int accentColor = config.getAccentColor();
        int width = config.getWidth();
        int height = config.getHeight();
        boolean showGenerationTime = config.isShowGenerationTime();
        // Get avatar bytes (download if needed)
        byte[] avatarBytes = userData.getAvatarBytes();
        if (userData.getImageMode() == ImageMode.DOWNLOAD && userData.getAvatarUrl() != null) {
            try {
                avatarBytes = AvatarManager.downloadImage(userData.getAvatarUrl());
            } catch (IOException e) {
                throw new RuntimeException("Failed to download avatar from URL: " + userData.getAvatarUrl(), e);
            }
        }

        // Create Skija surface
        Surface surface = Surface.makeRasterN32Premul(width, height);
        Canvas canvas = surface.getCanvas();

        // Draw background (dark gray with rounded corners)
        Paint bgPaint = new Paint().setColor(0xFF2A2A2A);
        canvas.drawRRect(RRect.makeXYWH(0, 0, width, height, 15), bgPaint);

        // Draw avatar if available
        AvatarManager.drawAvatar(
            canvas,
            avatarBytes,
            AVATAR_MARGIN,
            height / 2 - AVATAR_SIZE / 2,
            AVATAR_SIZE,
            accentColor,
            userData.getOnlineStatus(),
            userData.isShowStatusIndicator()
        );

        // Load fonts
        Typeface boldTypeface = Typeface.makeFromName("Arial", FontStyle.BOLD);
        Typeface regularTypeface = Typeface.makeFromName("Arial", FontStyle.NORMAL);

        // Draw username
        Font usernameFont = new Font(boldTypeface, 28);
        Paint usernamePaint = new Paint().setColor(0xFFFFFFFF);
        canvas.drawString(userData.getUsername(), TEXT_MARGIN, USERNAME_Y_OFFSET, usernameFont, usernamePaint);

        // Draw rank and level
        Font rankLevelFont = new Font(regularTypeface, 20);
        Paint rankLevelPaint = new Paint().setColor(0xFFCCCCCC);
        String rankLevelText = "Rank #" + userData.getRank() + " | Level " + userData.getLevel();
        canvas.drawString(rankLevelText, TEXT_MARGIN, RANK_LEVEL_Y_OFFSET, rankLevelFont, rankLevelPaint);

        // Draw XP text
        Font xpFont = new Font(regularTypeface, 16);
        Paint xpPaint = new Paint().setColor(0xFFAAAAAA);
        String xpText = userData.getCurrentXP() + " / " + userData.getMaxXP() + " XP";
        canvas.drawString(xpText, TEXT_MARGIN, XP_TEXT_Y_OFFSET, xpFont, xpPaint);

        // Draw progress bar background
        Paint progressBgPaint = new Paint().setColor(0xFF444444);
        canvas.drawRRect(RRect.makeXYWH(
            TEXT_MARGIN, 
            PROGRESS_BAR_Y_OFFSET, 
            width - TEXT_MARGIN - PROGRESS_BAR_MARGIN, 
            PROGRESS_BAR_HEIGHT, 
            PROGRESS_BAR_HEIGHT / 2
        ), progressBgPaint);

        // Draw progress bar
        float progressWidth = (float) (userData.getCurrentXP() - userData.getMinXP()) / 
                             (userData.getMaxXP() - userData.getMinXP()) * 
                             (width - TEXT_MARGIN - PROGRESS_BAR_MARGIN);
        Paint progressPaint = new Paint().setColor(accentColor);
        canvas.drawRRect(RRect.makeXYWH(
            TEXT_MARGIN, 
            PROGRESS_BAR_Y_OFFSET, 
            progressWidth, 
            PROGRESS_BAR_HEIGHT, 
            PROGRESS_BAR_HEIGHT / 2
        ), progressPaint);

        // Calculate generation time
        long endTime = System.currentTimeMillis();
        long generationTime = endTime - startTime;

        // Draw generation time if enabled
        if (showGenerationTime) {
            Font timeFont = new Font(regularTypeface, 12);
            Paint timePaint = new Paint().setColor(0xFF888888);
            String timeText = "Generated in " + generationTime + "ms";
            canvas.drawString(timeText, TEXT_MARGIN, TIME_TEXT_Y_OFFSET, timeFont, timePaint);
            timeFont.close();
            timePaint.close();
        }

        // Convert Skija image to BufferedImage
        Image skiaImage = surface.makeImageSnapshot();
        Data imageData = skiaImage.encodeToData(EncodedImageFormat.PNG);
        byte[] pngBytes = imageData.getBytes();

        try {
            java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(pngBytes);
            BufferedImage bufferedImage = javax.imageio.ImageIO.read(inputStream);
            return bufferedImage;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Skija image to BufferedImage", e);
        } finally {
            // Clean up resources
            imageData.close();
            skiaImage.close();
            surface.close();
            bgPaint.close();
            usernamePaint.close();
            rankLevelPaint.close();
            xpPaint.close();
            progressBgPaint.close();
            progressPaint.close();
            usernameFont.close();
            rankLevelFont.close();
            xpFont.close();
        }
    }
}
