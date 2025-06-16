package me.diamondforge.kyromera.levelcardlib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class UserDataTest {

    @Test
    fun `test basic user data creation`() {
        val userData = UserData.Builder("testUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarBytes(ByteArray(10))
            .build()

        assertEquals("testUser", userData.username)
        assertEquals(5, userData.rank)
        assertEquals(10, userData.level)
        assertEquals(100, userData.minXpForCurrentLevel)
        assertEquals(200, userData.maxXpForCurrentLevel)
        assertEquals(150, userData.currentXP)
        assertEquals(ImageMode.LOCAL, userData.imageMode)
        assertEquals(OnlineStatus.ONLINE, userData.onlineStatus)
        assertFalse(userData.showStatusIndicator)
        assertNotNull(userData.avatarBytes)
        assertNull(userData.avatarUrl)
    }

    @Test
    fun `test user data with avatar URL`() {
        val userData = UserData.Builder("testUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarUrl("https://example.com/avatar.png")
            .build()

        assertEquals("testUser", userData.username)
        assertEquals(ImageMode.DOWNLOAD, userData.imageMode)
        assertNull(userData.avatarBytes)
        assertEquals("https://example.com/avatar.png", userData.avatarUrl)
    }

    @Test
    fun `test user data with custom online status`() {
        val userData = UserData.Builder("testUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarBytes(ByteArray(10))
            .onlineStatus(OnlineStatus.IDLE)
            .build()

        assertEquals(OnlineStatus.IDLE, userData.onlineStatus)
    }

    @Test
    fun `test user data with status indicator disabled`() {
        val userData = UserData.Builder("testUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarBytes(ByteArray(10))
            .showStatusIndicator(false)
            .build()

        assertFalse(userData.showStatusIndicator)
    }

    @Test
    fun `test invalid rank throws exception`() {
        val builder = UserData.Builder("testUser")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            builder.rank(-1)
        }

        assertTrue(exception.message!!.contains("Rank must be positive"))
    }

    @Test
    fun `test invalid level throws exception`() {
        val builder = UserData.Builder("testUser")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            builder.level(-1)
        }

        assertTrue(exception.message!!.contains("Level must be positive"))
    }

    @Test
    fun `test invalid XP values throw exception`() {
        val builder = UserData.Builder("testUser")

        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            builder.xp(-1, 100, 50)
        }
        assertTrue(exception1.message!!.contains("Invalid XP values"))

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            builder.xp(100, 50, 75)
        }
        assertTrue(exception2.message!!.contains("Invalid XP values"))

        val exception3 = assertThrows(IllegalArgumentException::class.java) {
            builder.xp(50, 100, 150)
        }
        assertTrue(exception3.message!!.contains("Invalid XP values"))
    }

    @Test
    fun `test equals and hashCode`() {
        val userData1 = UserData.Builder("testUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarBytes(ByteArray(10))
            .build()

        val userData2 = UserData.Builder("testUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarBytes(ByteArray(10))
            .build()

        val userData3 = UserData.Builder("differentUser")
            .rank(5)
            .level(10)
            .xp(100, 200, 150)
            .avatarBytes(ByteArray(10))
            .build()

        // Test equals
        assertEquals(userData1, userData1) // Same object
        assertNotEquals(userData1, userData3) // Different username

        // Test hashCode
        assertEquals(userData1.hashCode(), userData1.hashCode()) // Same object
        assertNotEquals(userData1.hashCode(), userData3.hashCode()) // Different username
    }
}
