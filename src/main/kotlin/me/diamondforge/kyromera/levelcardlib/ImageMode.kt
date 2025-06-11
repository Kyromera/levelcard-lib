package me.diamondforge.kyromera.levelcardlib

/**
 * Enum representing different modes for handling avatar images.
 * This determines how the avatar image is sourced for the level card.
 */
enum class ImageMode {
    /**
     * Use a local image provided as a byte array.
     * No download will be attempted.
     */
    LOCAL,

    /**
     * Download the image from a provided URL.
     * If the download fails, a placeholder will be used.
     */
    DOWNLOAD
}