package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class EnumTest {

    @Test
    fun `test OnlineStatus enum values`() {
        // Test that all expected values exist
        val statuses = OnlineStatus.values()
        assertEquals(4, statuses.size)
        
        // Test specific enum values
        assertTrue(statuses.any { it.name == "ONLINE" })
        assertTrue(statuses.any { it.name == "IDLE" })
        assertTrue(statuses.any { it.name == "DND" })
        assertTrue(statuses.any { it.name == "OFFLINE" })
        
        // Test color values
        assertEquals(0xFF3BA55C.toInt(), OnlineStatus.ONLINE.color)
        assertEquals(0xFFFAA61A.toInt(), OnlineStatus.IDLE.color)
        assertEquals(0xFFED4245.toInt(), OnlineStatus.DND.color)
        assertEquals(0xFF747F8D.toInt(), OnlineStatus.OFFLINE.color)
    }
    
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
    fun `test OnlineStatus valueOf`() {
        // Test valueOf method
        assertEquals(OnlineStatus.ONLINE, OnlineStatus.valueOf("ONLINE"))
        assertEquals(OnlineStatus.IDLE, OnlineStatus.valueOf("IDLE"))
        assertEquals(OnlineStatus.DND, OnlineStatus.valueOf("DND"))
        assertEquals(OnlineStatus.OFFLINE, OnlineStatus.valueOf("OFFLINE"))
    }
    
    @Test
    fun `test ImageMode valueOf`() {
        // Test valueOf method
        assertEquals(ImageMode.LOCAL, ImageMode.valueOf("LOCAL"))
        assertEquals(ImageMode.DOWNLOAD, ImageMode.valueOf("DOWNLOAD"))
    }
    
    @Test
    fun `test OnlineStatus valueOf with invalid value`() {
        // Test valueOf method with invalid value
        assertThrows(IllegalArgumentException::class.java) {
            OnlineStatus.valueOf("INVALID")
        }
    }
    
    @Test
    fun `test ImageMode valueOf with invalid value`() {
        // Test valueOf method with invalid value
        assertThrows(IllegalArgumentException::class.java) {
            ImageMode.valueOf("INVALID")
        }
    }
}