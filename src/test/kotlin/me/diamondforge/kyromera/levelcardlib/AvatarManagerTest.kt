package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Surface
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class AvatarManagerTest {
    
    private lateinit var testAvatarBytes: ByteArray
    private lateinit var testCanvas: Canvas
    private lateinit var testSurface: Surface
    
    @BeforeEach
    fun setUp() {
        // Create a simple test avatar image
        val avatarImage = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        val graphics = avatarImage.createGraphics()
        graphics.color = java.awt.Color.BLUE
        graphics.fillRect(0, 0, 100, 100)
        graphics.dispose()
        
        // Convert to byte array
        val outputStream = java.io.ByteArrayOutputStream()
        ImageIO.write(avatarImage, "PNG", outputStream)
        testAvatarBytes = outputStream.toByteArray()
        
        // Create a test canvas
        testSurface = Surface.makeRasterN32Premul(500, 500)
        testCanvas = testSurface.canvas
    }
    
    @Test
    fun `test drawAvatar with valid avatar bytes`() {
        val result = AvatarManager.drawAvatar(
            canvas = testCanvas,
            avatarBytes = testAvatarBytes,
            x = 50f,
            y = 50f,
            size = 100f,
            borderColor = 0xFFFF0000.toInt(), // Red
            onlineStatus = OnlineStatus.ONLINE,
            showStatusIndicator = true
        )
        
        // Should return true for successful drawing
        assertTrue(result)
    }
    
    @Test
    fun `test drawAvatar with null avatar bytes`() {
        val result = AvatarManager.drawAvatar(
            canvas = testCanvas,
            avatarBytes = null,
            x = 50f,
            y = 50f,
            size = 100f,
            borderColor = 0xFFFF0000.toInt(), // Red
            onlineStatus = OnlineStatus.ONLINE,
            showStatusIndicator = true
        )
        
        // Should return false for null avatar bytes
        assertFalse(result)
    }
    
    @Test
    fun `test drawAvatar with status indicator disabled`() {
        val result = AvatarManager.drawAvatar(
            canvas = testCanvas,
            avatarBytes = testAvatarBytes,
            x = 50f,
            y = 50f,
            size = 100f,
            borderColor = 0xFFFF0000.toInt(), // Red
            onlineStatus = OnlineStatus.ONLINE,
            showStatusIndicator = false
        )
        
        // Should return true for successful drawing
        assertTrue(result)
    }
    
    @Test
    fun `test drawAvatar with null online status`() {
        val result = AvatarManager.drawAvatar(
            canvas = testCanvas,
            avatarBytes = testAvatarBytes,
            x = 50f,
            y = 50f,
            size = 100f,
            borderColor = 0xFFFF0000.toInt(), // Red
            onlineStatus = null,
            showStatusIndicator = true
        )
        
        // Should return true for successful drawing
        assertTrue(result)
    }
    
    @Test
    fun `test drawAvatar with different online statuses`() {
        // Test with each online status
        for (status in OnlineStatus.values()) {
            val result = AvatarManager.drawAvatar(
                canvas = testCanvas,
                avatarBytes = testAvatarBytes,
                x = 50f,
                y = 50f,
                size = 100f,
                borderColor = 0xFFFF0000.toInt(), // Red
                onlineStatus = status,
                showStatusIndicator = true
            )
            
            // Should return true for successful drawing
            assertTrue(result, "Failed to draw avatar with status: $status")
        }
    }
    
    @Test
    fun `test drawAvatar with invalid image data`() {
        // Create invalid image data
        val invalidImageBytes = "Not an image".toByteArray()
        
        val result = AvatarManager.drawAvatar(
            canvas = testCanvas,
            avatarBytes = invalidImageBytes,
            x = 50f,
            y = 50f,
            size = 100f,
            borderColor = 0xFFFF0000.toInt(), // Red
            onlineStatus = OnlineStatus.ONLINE,
            showStatusIndicator = true
        )
        
        // Should return false for invalid image data
        // But the method should not throw an exception
        assertFalse(result)
    }
    
    // Note: We're not testing the downloadImage method directly
    // as it requires network access. In a real-world scenario,
    // you would use a mocking framework to mock the HTTP client.
}