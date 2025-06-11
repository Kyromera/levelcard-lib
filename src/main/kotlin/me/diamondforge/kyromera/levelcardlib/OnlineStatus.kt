package me.diamondforge.kyromera.levelcardlib

/**
 * Enum representing different online statuses for the user profile.
 * This is used to display a status indicator on the user's avatar.
 */
enum class OnlineStatus(val color: Int) {
    /**
     * User is online and active.
     * Typically represented by a green indicator.
     */
    ONLINE(0xFF3BA55C.toInt()), // Green

    /**
     * User is online but idle/away.
     * Typically represented by a yellow indicator.
     */
    IDLE(0xFFFAA61A.toInt()), // Yellow

    /**
     * User is in Do Not Disturb mode.
     * Typically represented by a red indicator.
     */
    DND(0xFFED4245.toInt()), // Red

    /**
     * User is offline.
     * Typically represented by a gray indicator.
     */
    OFFLINE(0xFF747F8D.toInt()) // Gray
}
