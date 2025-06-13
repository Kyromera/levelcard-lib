package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class EnumTest {
    
    @Test
    fun `test ImageMode enum values`() {
        // Test that all expected values exist
        val modes = ImageMode.values()
        assertEquals(2, modes.size)
        
        // Test specific enum values
        assertTrue(modes.any { it.name == "LOCAL" })
        assertTrue(modes.any { it.name == "DOWNLOAD" })
    }

    
    @Test
    fun `test ImageMode valueOf`() {
        // Test valueOf method
        assertEquals(ImageMode.LOCAL, ImageMode.valueOf("LOCAL"))
        assertEquals(ImageMode.DOWNLOAD, ImageMode.valueOf("DOWNLOAD"))
    }
    

    
    @Test
    fun `test ImageMode valueOf with invalid value`() {
        // Test valueOf method with invalid value
        assertThrows(IllegalArgumentException::class.java) {
            ImageMode.valueOf("INVALID")
        }
    }
}