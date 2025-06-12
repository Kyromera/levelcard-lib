package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CardConfigurationTest {

    @Test
    fun `test default configuration`() {
        val config = CardConfiguration.getDefault()
        
        // Default values from the CardConfiguration class
        assertEquals(950, config.width)
        assertEquals(300, config.height)
        assertEquals(0xFF00A8E8.toInt(), config.accentColor)
        assertFalse(config.showGenerationTime)
    }
    
    @Test
    fun `test custom dimensions`() {
        val config = CardConfiguration.Builder()
            .dimensions(800, 400)
            .build()
            
        assertEquals(800, config.width)
        assertEquals(400, config.height)
    }
    
    @Test
    fun `test custom accent color`() {
        val customColor = 0xFFFF0000.toInt() // Red
        val config = CardConfiguration.Builder()
            .accentColor(customColor)
            .build()
            
        assertEquals(customColor, config.accentColor)
    }
    
    @Test
    fun `test show generation time`() {
        val config = CardConfiguration.Builder()
            .showGenerationTime(true)
            .build()
            
        assertTrue(config.showGenerationTime)
    }
    
    @Test
    fun `test builder chaining`() {
        val customColor = 0xFF00FF00.toInt() // Green
        val config = CardConfiguration.Builder()
            .dimensions(600, 200)
            .accentColor(customColor)
            .showGenerationTime(true)
            .build()
            
        assertEquals(600, config.width)
        assertEquals(200, config.height)
        assertEquals(customColor, config.accentColor)
        assertTrue(config.showGenerationTime)
    }
    
    @Test
    fun `test invalid dimensions throws exception`() {
        val builder = CardConfiguration.Builder()
        
        val exception = assertThrows(IllegalArgumentException::class.java) {
            builder.dimensions(-1, 300)
        }
        
        assertTrue(exception.message!!.contains("Width and height must be positive"))
    }
}