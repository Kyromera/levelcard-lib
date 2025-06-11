package me.diamondforge.kyromera.levelcardlib;

import java.awt.image.BufferedImage;

/**
 * A utility class for generating level cards with user information and avatar.
 * This is the main entry point for the library.
 */
public class LevelCardDrawer {
    // Default values
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 180;
    private static final int DEFAULT_ACCENT_COLOR = 0xFFEA397C; // Pink

    /**
     * Draws a level card with the specified parameters.
     *
     * @param avatarBytes Byte array of the avatar image, can be null if avatarUrl is provided
     * @param avatarUrl URL of the avatar image, can be null if avatarBytes is provided
     * @param downloadFromUrl Whether to download the avatar from the URL
     * @param username Username to display on the card
     * @param rank User's rank
     * @param level User's level
     * @param minXP Minimum XP for the current level
     * @param maxXP Maximum XP for the current level
     * @param currentXP Current XP of the user
     * @param accentColor Accent color for the card (ARGB format)
     * @param width Width of the card
     * @param height Height of the card
     * @return The generated level card as a BufferedImage
     */
    public static BufferedImage drawLevelCard(
            byte[] avatarBytes,
            String avatarUrl,
            boolean downloadFromUrl,
            String username,
            int rank,
            int level,
            int minXP,
            int maxXP,
            int currentXP,
            int accentColor,
            int width,
            int height
    ) {
        // Create UserData object
        UserData userData = createUserData(avatarBytes, avatarUrl, downloadFromUrl, username, rank, level, minXP, maxXP, currentXP);
        
        // Create CardConfiguration object
        CardConfiguration config = new CardConfiguration.Builder()
                .dimensions(width, height)
                .accentColor(accentColor)
                .build();
        
        // Render the card
        return CardRenderer.renderCard(userData, config);
    }
    
    /**
     * Draws a level card with the specified parameters and online status.
     *
     * @param avatarBytes Byte array of the avatar image, can be null if avatarUrl is provided
     * @param avatarUrl URL of the avatar image, can be null if avatarBytes is provided
     * @param downloadFromUrl Whether to download the avatar from the URL
     * @param username Username to display on the card
     * @param rank User's rank
     * @param level User's level
     * @param minXP Minimum XP for the current level
     * @param maxXP Maximum XP for the current level
     * @param currentXP Current XP of the user
     * @param accentColor Accent color for the card (ARGB format)
     * @param width Width of the card
     * @param height Height of the card
     * @param onlineStatus The online status to display
     * @param showStatusIndicator Whether to show the status indicator
     * @param showGenerationTime Whether to show generation time on the card
     * @return The generated level card as a BufferedImage
     */
    public static BufferedImage drawLevelCard(
            byte[] avatarBytes,
            String avatarUrl,
            boolean downloadFromUrl,
            String username,
            int rank,
            int level,
            int minXP,
            int maxXP,
            int currentXP,
            int accentColor,
            int width,
            int height,
            OnlineStatus onlineStatus,
            boolean showStatusIndicator,
            boolean showGenerationTime
    ) {
        // Create UserData object with online status
        UserData.Builder userDataBuilder = new UserData.Builder(username)
                .rank(rank)
                .level(level)
                .xp(minXP, maxXP, currentXP)
                .onlineStatus(onlineStatus)
                .showStatusIndicator(showStatusIndicator);
        
        // Set avatar source
        if (downloadFromUrl && avatarUrl != null && !avatarUrl.isEmpty()) {
            userDataBuilder.avatarUrl(avatarUrl);
        } else if (avatarBytes != null) {
            userDataBuilder.avatarBytes(avatarBytes);
        } else {
            throw new IllegalArgumentException("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true");
        }
        
        UserData userData = userDataBuilder.build();
        
        // Create CardConfiguration object
        CardConfiguration config = new CardConfiguration.Builder()
                .dimensions(width, height)
                .accentColor(accentColor)
                .showGenerationTime(showGenerationTime)
                .build();
        
        // Render the card
        return CardRenderer.renderCard(userData, config);
    }
    
    /**
     * Overloaded method with default width and height.
     */
    public static BufferedImage drawLevelCard(
            byte[] avatarBytes,
            String avatarUrl,
            boolean downloadFromUrl,
            String username,
            int rank,
            int level,
            int minXP,
            int maxXP,
            int currentXP,
            int accentColor
    ) {
        return drawLevelCard(
            avatarBytes, avatarUrl, downloadFromUrl, username, rank, level,
            minXP, maxXP, currentXP, accentColor, DEFAULT_WIDTH, DEFAULT_HEIGHT
        );
    }
    
    /**
     * Overloaded method with default accent color, width, and height.
     */
    public static BufferedImage drawLevelCard(
            byte[] avatarBytes,
            String avatarUrl,
            boolean downloadFromUrl,
            String username,
            int rank,
            int level,
            int minXP,
            int maxXP,
            int currentXP
    ) {
        return drawLevelCard(
            avatarBytes, avatarUrl, downloadFromUrl, username, rank, level,
            minXP, maxXP, currentXP, DEFAULT_ACCENT_COLOR, DEFAULT_WIDTH, DEFAULT_HEIGHT
        );
    }
    
    /**
     * Helper method to create a UserData object from the parameters.
     */
    private static UserData createUserData(
            byte[] avatarBytes,
            String avatarUrl,
            boolean downloadFromUrl,
            String username,
            int rank,
            int level,
            int minXP,
            int maxXP,
            int currentXP
    ) {
        UserData.Builder builder = new UserData.Builder(username)
                .rank(rank)
                .level(level)
                .xp(minXP, maxXP, currentXP);
        
        // Set avatar source
        if (downloadFromUrl && avatarUrl != null && !avatarUrl.isEmpty()) {
            builder.avatarUrl(avatarUrl);
        } else if (avatarBytes != null) {
            builder.avatarBytes(avatarBytes);
        } else {
            throw new IllegalArgumentException("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true");
        }
        
        return builder.build();
    }
}