package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.jetbrains.skia.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.awt.Color

class CardRendererTest {

    private lateinit var testUserData: UserData
    private lateinit var testConfig: CardConfiguration
    private lateinit var testAvatarBytes: ByteArray

    @BeforeEach
    fun setUp() {
        // Create test avatar image
        val avatarImage = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        val graphics = avatarImage.createGraphics()
        graphics.color = Color.BLUE
        graphics.fillRect(0, 0, 100, 100)
        graphics.dispose()

        // Convert to byte array
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(avatarImage, "PNG", outputStream)
        testAvatarBytes = outputStream.toByteArray()

        // Create test user data
        testUserData = UserData.Builder("TestUser")
            .level(10)
            .rank(5)
            .xp(0, 2000, 1500)
            .avatarBytes(testAvatarBytes)
            .onlineStatus(OnlineStatus.ONLINE)
            .build()

        // Create test configuration
        testConfig = CardConfiguration.Builder()
            .dimensions(800, 300)
            .accentColor(0xFFFF0000.toInt()) // Red
            .showGenerationTime(true)
            .build()
    }

    @Test
    fun `test renderCard produces valid BufferedImage`() {
        // Render the card
        val renderedCard = CardRenderer.renderCard(testUserData, testConfig)

        // Verify the result is a valid BufferedImage with the expected dimensions
        assertNotNull(renderedCard, "Rendered card should not be null")
        assertEquals(testConfig.width, renderedCard.width, "Rendered card width should match configuration")
        assertEquals(testConfig.height, renderedCard.height, "Rendered card height should match configuration")
    }

    @Test
    fun `test renderCard with different configurations`() {
        // Test with different dimensions
        val configs = listOf(
            CardConfiguration.Builder().dimensions(600, 200).build(),
            CardConfiguration.Builder().dimensions(1000, 400).build(),
            CardConfiguration.Builder().dimensions(500, 500).build()
        )

        for (config in configs) {
            val renderedCard = CardRenderer.renderCard(testUserData, config)

            assertNotNull(renderedCard, "Rendered card should not be null")
            assertEquals(config.width, renderedCard.width, "Rendered card width should match configuration")
            assertEquals(config.height, renderedCard.height, "Rendered card height should match configuration")
        }
    }

    @Test
    fun `test renderCard with different accent colors`() {
        // Test with different accent colors
        val colors = listOf(
            0xFF00FF00.toInt(), // Green
            0xFF0000FF.toInt(), // Blue
            0xFFFFFF00.toInt()  // Yellow
        )

        for (color in colors) {
            val config = CardConfiguration.Builder()
                .dimensions(800, 300)
                .accentColor(color)
                .build()

            val renderedCard = CardRenderer.renderCard(testUserData, config)
            assertNotNull(renderedCard, "Rendered card should not be null")

            // We could check pixels to verify the accent color is used correctly,
            // but that would require knowledge of exact pixel positions where the
            // accent color is applied, which might be brittle to changes.
        }
    }

    // Note: We're not testing the download mode directly
    // as it requires network access. In a real-world scenario,
    // you would use a mocking framework to mock the HTTP client.

    @Test
    fun `test renderCard with different progress values`() {
        // Test with different XP progress values
        val progressValues = listOf(
            Triple(0, 1000, 0.0),      // 0%
            Triple(500, 1000, 0.5),    // 50%
            Triple(1000, 1000, 1.0)    // 100%
        )

        for ((current, max, expectedRatio) in progressValues) {
            val userData = UserData.Builder("TestUser")
                .level(10)
                .rank(5)
                .xp(0, max, current)
                .avatarBytes(testAvatarBytes)
                .build()

            val renderedCard = CardRenderer.renderCard(userData, testConfig)
            assertNotNull(renderedCard, "Rendered card should not be null")

            // We could check pixels to verify the progress bar width,
            // but that would require knowledge of exact pixel positions.
        }
    }

    @Test
    fun `test renderCard with different online statuses`() {
        // Test with each online status
        for (status in OnlineStatus.values()) {
            val userData = UserData.Builder("TestUser")
                .level(10)
                .rank(5)
                .xp(0, 2000, 1500)
                .avatarBytes(testAvatarBytes)
                .onlineStatus(status)
                .showStatusIndicator(true)
                .build()

            val renderedCard = CardRenderer.renderCard(userData, testConfig)
            assertNotNull(renderedCard, "Rendered card should not be null with status: $status")
        }
    }

    @Test
    fun `test renderCard with very long username`() {
        // Test with a very long username to ensure text rendering handles it
        val longUsername = "ThisIsAVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongUsername"
        val userData = UserData.Builder(longUsername)
            .level(10)
            .rank(5)
            .xp(0, 2000, 1500)
            .avatarBytes(testAvatarBytes)
            .build()

        val renderedCard = CardRenderer.renderCard(userData, testConfig)
        assertNotNull(renderedCard, "Rendered card should not be null with long username")
    }

    @Test
    fun `test renderCard with extreme values`() {
        // Test with extreme values to ensure robustness
        val userData = UserData.Builder("Extreme Test")
            .level(Integer.MAX_VALUE)
            .rank(Integer.MAX_VALUE)
            .xp(0, Integer.MAX_VALUE, Integer.MAX_VALUE - 1)
            .avatarBytes(testAvatarBytes)
            .build()

        val renderedCard = CardRenderer.renderCard(userData, testConfig)
        assertNotNull(renderedCard, "Rendered card should not be null with extreme values")
    }

    @Test
    fun `test renderCard with invalid avatar bytes`() {
        // Create invalid image data
        val invalidImageBytes = "Not an image".toByteArray()

        val userData = UserData.Builder("TestUser")
            .level(10)
            .rank(5)
            .xp(0, 2000, 1500)
            .avatarBytes(invalidImageBytes)
            .build()

        // Should handle invalid image data gracefully
        val renderedCard = CardRenderer.renderCard(userData, testConfig)
        assertNotNull(renderedCard, "Rendered card should not be null even with invalid avatar bytes")
    }

    @Test
    fun `test renderCard with status indicator disabled`() {
        // Create user data with status indicator disabled
        val userData = UserData.Builder("TestUser")
            .level(10)
            .rank(5)
            .xp(0, 2000, 1500)
            .avatarBytes(testAvatarBytes)
            .onlineStatus(OnlineStatus.ONLINE)
            .showStatusIndicator(false)
            .build()

        // Should handle disabled status indicator
        val renderedCard = CardRenderer.renderCard(userData, testConfig)
        assertNotNull(renderedCard, "Rendered card should not be null with status indicator disabled")
    }
}
