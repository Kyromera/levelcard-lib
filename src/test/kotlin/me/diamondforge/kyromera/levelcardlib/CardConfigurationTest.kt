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

        // Default values for new parameters
        assertEquals(200, config.avatarSize)
        assertEquals(50, config.avatarMargin)
        assertEquals(270, config.textMargin)
        assertEquals(83, config.usernameYOffset)
        assertEquals(133, config.rankLevelYOffset)
        assertEquals(200, config.xpTextYOffset)
        assertEquals(267, config.timeTextYOffset)
        assertEquals(33, config.progressBarHeight)
        assertEquals(217, config.progressBarYOffset)
        assertEquals(50, config.progressBarMargin)
        assertEquals(47f, config.usernameFontSize)
        assertEquals(33f, config.rankLevelFontSize)
        assertEquals(27f, config.xpFontSize)
        assertEquals(20f, config.timeFontSize)
        assertEquals(13f, config.shadowBlur)
        assertEquals(25f, config.cornerRadius)
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

    @Test
    fun `test avatar configuration`() {
        val config = CardConfiguration.Builder()
            .avatarConfig(150, 40)
            .build()

        assertEquals(150, config.avatarSize)
        assertEquals(40, config.avatarMargin)
    }

    @Test
    fun `test text margin configuration`() {
        val config = CardConfiguration.Builder()
            .textMargin(300)
            .build()

        assertEquals(300, config.textMargin)
    }

    @Test
    fun `test text offsets configuration`() {
        val config = CardConfiguration.Builder()
            .textOffsets(90, 140, 210, 280)
            .build()

        assertEquals(90, config.usernameYOffset)
        assertEquals(140, config.rankLevelYOffset)
        assertEquals(210, config.xpTextYOffset)
        assertEquals(280, config.timeTextYOffset)
    }

    @Test
    fun `test progress bar configuration`() {
        val config = CardConfiguration.Builder()
            .progressBarConfig(40, 220, 60)
            .build()

        assertEquals(40, config.progressBarHeight)
        assertEquals(220, config.progressBarYOffset)
        assertEquals(60, config.progressBarMargin)
    }

    @Test
    fun `test font sizes configuration`() {
        val config = CardConfiguration.Builder()
            .fontSizes(50f, 35f, 30f, 22f)
            .build()

        assertEquals(50f, config.usernameFontSize)
        assertEquals(35f, config.rankLevelFontSize)
        assertEquals(30f, config.xpFontSize)
        assertEquals(22f, config.timeFontSize)
    }

    @Test
    fun `test background configuration`() {
        val config = CardConfiguration.Builder()
            .backgroundConfig(15f, 30f)
            .build()

        assertEquals(15f, config.shadowBlur)
        assertEquals(30f, config.cornerRadius)
    }

    @Test
    fun `test comprehensive builder chaining`() {
        val customColor = 0xFF00FF00.toInt() // Green
        val config = CardConfiguration.Builder()
            .dimensions(600, 200)
            .accentColor(customColor)
            .showGenerationTime(true)
            .avatarConfig(180, 45)
            .textMargin(290)
            .textOffsets(85, 135, 205, 270)
            .progressBarConfig(35, 220, 55)
            .fontSizes(48f, 34f, 28f, 21f)
            .backgroundConfig(14f, 26f)
            .build()

        // Check all parameters
        assertEquals(600, config.width)
        assertEquals(200, config.height)
        assertEquals(customColor, config.accentColor)
        assertTrue(config.showGenerationTime)
        assertEquals(180, config.avatarSize)
        assertEquals(45, config.avatarMargin)
        assertEquals(290, config.textMargin)
        assertEquals(85, config.usernameYOffset)
        assertEquals(135, config.rankLevelYOffset)
        assertEquals(205, config.xpTextYOffset)
        assertEquals(270, config.timeTextYOffset)
        assertEquals(35, config.progressBarHeight)
        assertEquals(220, config.progressBarYOffset)
        assertEquals(55, config.progressBarMargin)
        assertEquals(48f, config.usernameFontSize)
        assertEquals(34f, config.rankLevelFontSize)
        assertEquals(28f, config.xpFontSize)
        assertEquals(21f, config.timeFontSize)
        assertEquals(14f, config.shadowBlur)
        assertEquals(26f, config.cornerRadius)
    }

    @Test
    fun `test invalid avatar config throws exception`() {
        val builder = CardConfiguration.Builder()

        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            builder.avatarConfig(-1, 50)
        }
        assertTrue(exception1.message!!.contains("Avatar size must be positive"))

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            builder.avatarConfig(200, -1)
        }
        assertTrue(exception2.message!!.contains("Avatar margin must be non-negative"))
    }

    @Test
    fun `test invalid text margin throws exception`() {
        val builder = CardConfiguration.Builder()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            builder.textMargin(-1)
        }
        assertTrue(exception.message!!.contains("Text margin must be non-negative"))
    }

    @Test
    fun `test invalid progress bar config throws exception`() {
        val builder = CardConfiguration.Builder()

        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            builder.progressBarConfig(-1, 217, 50)
        }
        assertTrue(exception1.message!!.contains("Progress bar height must be positive"))

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            builder.progressBarConfig(33, 217, -1)
        }
        assertTrue(exception2.message!!.contains("Progress bar margin must be non-negative"))
    }

    @Test
    fun `test invalid font sizes throws exception`() {
        val builder = CardConfiguration.Builder()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            builder.fontSizes(-1f, 33f, 27f, 20f)
        }
        assertTrue(exception.message!!.contains("Font sizes must be positive"))
    }

    @Test
    fun `test invalid background config throws exception`() {
        val builder = CardConfiguration.Builder()

        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            builder.backgroundConfig(-1f, 25f)
        }
        assertTrue(exception1.message!!.contains("Shadow blur must be non-negative"))

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            builder.backgroundConfig(13f, -1f)
        }
        assertTrue(exception2.message!!.contains("Corner radius must be non-negative"))
    }
}
