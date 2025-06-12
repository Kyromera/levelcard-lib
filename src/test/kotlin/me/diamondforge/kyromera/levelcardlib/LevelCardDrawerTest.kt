package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO

class LevelCardDrawerTest {

    private lateinit var testAvatarBytes: ByteArray

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
    }

    @Test
    fun `test drawLevelCard with avatar bytes`() {
        val result = LevelCardDrawer.drawLevelCard(
            avatarBytes = testAvatarBytes,
            avatarUrl = null,
            downloadFromUrl = false,
            username = "TestUser",
            rank = 5,
            level = 10,
            minXP = 100,
            maxXP = 200,
            currentXP = 150,
            accentColor = 0xFFFF0000.toInt(),
            width = 800,
            height = 250
        )

        // Basic assertions about the result
        assertNotNull(result)
        assertEquals(800, result.width)
        assertEquals(250, result.height)
        assertEquals(BufferedImage.TYPE_4BYTE_ABGR, result.type)
    }

    @Test
    fun `test drawLevelCard with online status`() {
        val result = LevelCardDrawer.drawLevelCard(
            avatarBytes = testAvatarBytes,
            avatarUrl = null,
            downloadFromUrl = false,
            username = "TestUser",
            rank = 5,
            level = 10,
            minXP = 100,
            maxXP = 200,
            currentXP = 150,
            accentColor = 0xFFFF0000.toInt(),
            width = 800,
            height = 250,
            onlineStatus = OnlineStatus.IDLE,
            showStatusIndicator = true,
            showGenerationTime = true
        )

        // Basic assertions about the result
        assertNotNull(result)
        assertEquals(800, result.width)
        assertEquals(250, result.height)
        assertEquals(BufferedImage.TYPE_4BYTE_ABGR, result.type)
    }

    @Test
    fun `test drawLevelCard with default dimensions`() {
        val result = LevelCardDrawer.drawLevelCard(
            avatarBytes = testAvatarBytes,
            avatarUrl = null,
            downloadFromUrl = false,
            username = "TestUser",
            rank = 5,
            level = 10,
            minXP = 100,
            maxXP = 200,
            currentXP = 150,
            accentColor = 0xFFFF0000.toInt()
        )

        // Basic assertions about the result
        assertNotNull(result)
        assertEquals(950, result.width) // Default width
        assertEquals(300, result.height) // Default height
        assertEquals(BufferedImage.TYPE_4BYTE_ABGR, result.type)
    }

    @Test
    fun `test drawLevelCard with default accent color and dimensions`() {
        val result = LevelCardDrawer.drawLevelCard(
            avatarBytes = testAvatarBytes,
            avatarUrl = null,
            downloadFromUrl = false,
            username = "TestUser",
            rank = 5,
            level = 10,
            minXP = 100,
            maxXP = 200,
            currentXP = 150
        )

        // Basic assertions about the result
        assertNotNull(result)
        assertEquals(950, result.width) // Default width
        assertEquals(300, result.height) // Default height
        assertEquals(BufferedImage.TYPE_4BYTE_ABGR, result.type)
    }

    @Test
    fun `test drawLevelCard throws exception with no avatar source`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            LevelCardDrawer.drawLevelCard(
                avatarBytes = null,
                avatarUrl = null,
                downloadFromUrl = false,
                username = "TestUser",
                rank = 5,
                level = 10,
                minXP = 100,
                maxXP = 200,
                currentXP = 150
            )
        }

        assertTrue(exception.message!!.contains("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true"))
    }

    @Test
    fun `test drawLevelCard with avatar URL but downloadFromUrl false throws exception`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            LevelCardDrawer.drawLevelCard(
                avatarBytes = null,
                avatarUrl = "https://example.com/avatar.png",
                downloadFromUrl = false, // Not downloading
                username = "TestUser",
                rank = 5,
                level = 10,
                minXP = 100,
                maxXP = 200,
                currentXP = 150
            )
        }

        assertTrue(exception.message!!.contains("Either avatarBytes must be provided or avatarUrl with downloadFromUrl=true"))
    }

    @Test
    fun `test saving generated image to file`(@TempDir tempDir: Path) {
        val result = LevelCardDrawer.drawLevelCard(
            avatarBytes = testAvatarBytes,
            avatarUrl = null,
            downloadFromUrl = false,
            username = "TestUser",
            rank = 5,
            level = 10,
            minXP = 100,
            maxXP = 200,
            currentXP = 150
        )

        // Save the image to a temporary file
        val outputFile = tempDir.resolve("test_card.png").toFile()
        ImageIO.write(result, "PNG", outputFile)

        // Verify the file exists and has content
        assertTrue(outputFile.exists())
        assertTrue(outputFile.length() > 0)

        // Verify we can read it back as an image
        val readImage = ImageIO.read(outputFile)
        assertNotNull(readImage)
        assertEquals(result.width, readImage.width)
        assertEquals(result.height, readImage.height)
    }
}
