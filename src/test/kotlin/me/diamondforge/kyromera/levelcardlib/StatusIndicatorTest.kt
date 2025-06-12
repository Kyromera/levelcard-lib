package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.jetbrains.skia.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

class StatusIndicatorTest {
    
    private lateinit var testCanvas: Canvas
    private lateinit var testSurface: Surface
    
    @BeforeEach
    fun setUp() {
        // Create a test canvas
        testSurface = Surface.makeRasterN32Premul(200, 200)
        testCanvas = testSurface.canvas
        
        // Clear the canvas with a transparent background
        testCanvas.clear(Color.makeARGB(0, 0, 0, 0))
    }
    
    @Test
    fun `test drawStatusDot renders correctly`() {
        // Test parameters
        val x = 100f
        val y = 100f
        val size = 30f
        val status = OnlineStatus.ONLINE
        
        // Draw the status dot
        StatusIndicator.drawStatusDot(testCanvas, x, y, size, status)
        
        // Capture the image
        val image = testSurface.makeImageSnapshot()
        val data = image.encodeToData(EncodedImageFormat.PNG)
        assertNotNull(data, "Image encoding failed")
        
        val pngBytes = data!!.bytes
        val bufferedImage = ImageIO.read(ByteArrayInputStream(pngBytes))
        
        // Check that the center pixel has the correct color (should be the status color)
        val centerColor = bufferedImage.getRGB(x.toInt(), y.toInt())
        val statusColorARGB = status.color or 0xFF000000.toInt() // Add alpha channel
        
        assertEquals(statusColorARGB, centerColor and 0xFFFFFFFF.toInt(), 
            "Center pixel color should match status color")
        
        // Check that a pixel in the border area has white color
        val borderX = x.toInt()
        val borderY = (y - size / 2 - 1).toInt() // Just outside the status dot, in the border
        val borderColor = bufferedImage.getRGB(borderX, borderY)
        
        assertEquals(0xFFFFFFFF.toInt(), borderColor and 0xFFFFFFFF.toInt(), 
            "Border pixel should be white")
    }
    
    @Test
    fun `test drawStatusDot with all status types`() {
        // Test with each online status
        for (status in OnlineStatus.values()) {
            // Clear the canvas
            testCanvas.clear(Color.makeARGB(0, 0, 0, 0))
            
            // Draw the status dot
            StatusIndicator.drawStatusDot(testCanvas, 100f, 100f, 30f, status)
            
            // Capture the image
            val image = testSurface.makeImageSnapshot()
            val data = image.encodeToData(EncodedImageFormat.PNG)
            assertNotNull(data, "Image encoding failed for status: $status")
            
            val pngBytes = data!!.bytes
            val bufferedImage = ImageIO.read(ByteArrayInputStream(pngBytes))
            
            // Check that the center pixel has the correct color
            val centerColor = bufferedImage.getRGB(100, 100)
            val statusColorARGB = status.color or 0xFF000000.toInt() // Add alpha channel
            
            assertEquals(statusColorARGB, centerColor and 0xFFFFFFFF.toInt(), 
                "Center pixel color should match status color for status: $status")
        }
    }
    
    @Test
    fun `test calculateIndicatorPosition returns correct coordinates`() {
        val avatarX = 50f
        val avatarY = 50f
        val avatarSize = 100f
        val indicatorSize = 25f
        
        val position = StatusIndicator.calculateIndicatorPosition(
            avatarX, avatarY, avatarSize, indicatorSize
        )
        
        assertEquals(2, position.size, "Position array should have 2 elements")
        assertEquals(avatarX + avatarSize - indicatorSize / 2, position[0], 
            "X coordinate should be at the right edge of avatar minus half indicator size")
        assertEquals(avatarY + avatarSize - indicatorSize / 2, position[1], 
            "Y coordinate should be at the bottom edge of avatar minus half indicator size")
    }
    
    @Test
    fun `test anti-aliasing is applied to status dot`() {
        // Draw a large status dot to better observe anti-aliasing
        val size = 60f
        StatusIndicator.drawStatusDot(testCanvas, 100f, 100f, size, OnlineStatus.ONLINE)
        
        // Capture the image
        val image = testSurface.makeImageSnapshot()
        val data = image.encodeToData(EncodedImageFormat.PNG)
        assertNotNull(data, "Image encoding failed")
        
        val pngBytes = data!!.bytes
        val bufferedImage = ImageIO.read(ByteArrayInputStream(pngBytes))
        
        // Check pixels at the edge of the circle for anti-aliasing
        // Anti-aliasing creates a gradient of colors at the edge
        val edgeX = (100 + size / 2 * Math.cos(Math.PI / 4)).toInt()
        val edgeY = (100 + size / 2 * Math.sin(Math.PI / 4)).toInt()
        
        // Get colors of adjacent pixels near the edge
        val color1 = bufferedImage.getRGB(edgeX, edgeY)
        val color2 = bufferedImage.getRGB(edgeX + 1, edgeY + 1)
        
        // With anti-aliasing, adjacent pixels at the edge should have different colors
        assertNotEquals(color1, color2, "Adjacent pixels at the edge should have different colors due to anti-aliasing")
    }
}