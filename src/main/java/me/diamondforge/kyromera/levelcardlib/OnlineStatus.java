package me.diamondforge.kyromera.levelcardlib;

/**
 * Enum representing different online statuses for the user profile.
 * This is used to display a status indicator on the user's avatar.
 */
public enum OnlineStatus {
    /**
     * User is online and active.
     * Typically represented by a green indicator.
     */
    ONLINE(0xFF3BA55C), // Green

    /**
     * User is online but idle/away.
     * Typically represented by a yellow indicator.
     */
    IDLE(0xFFFAA61A), // Yellow

    /**
     * User is in Do Not Disturb mode.
     * Typically represented by a red indicator.
     */
    DND(0xFFED4245), // Red

    /**
     * User is offline.
     * Typically represented by a gray indicator.
     */
    OFFLINE(0xFF747F8D); // Gray

    private final int color;

    /**
     * Constructor for OnlineStatus.
     *
     * @param color The color associated with this status (ARGB format)
     */
    OnlineStatus(int color) {
        this.color = color;
    }

    /**
     * Gets the color associated with this status.
     *
     * @return The color in ARGB format
     */
    public int getColor() {
        return color;
    }
}