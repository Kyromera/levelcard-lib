package me.diamondforge.kyromera.levelcardlib

/**
 * Data class to hold color configuration for the level card.
 * This class defines the color scheme used for rendering the level card.
 */
data class ColorConfig(
    /**
     * Primary color used for main elements like the progress bar and username.
     * Default: Light Kryo blue
     */
    val primaryColor: Int = 0xFF00A9FF.toInt(),
    
    /**
     * Secondary color used for gradient effects and less prominent elements.
     * Default: Even lighter blue
     */
    val secondaryColor: Int = 0xFF80CFFF.toInt(),
    
    /**
     * Background color for the card.
     * Default: Dark blue-gray
     */
    val backgroundColor: Int = 0xFF0F1729.toInt(),
    
    /**
     * Text color for most text elements.
     * Default: White
     */
    val textColor: Int = 0xFFFFFFFF.toInt(),
    
    /**
     * Accent color used for highlighting and special elements.
     * Default: Same as primary color
     */
    val accentColor: Int = 0xFF00A9FF.toInt()
)