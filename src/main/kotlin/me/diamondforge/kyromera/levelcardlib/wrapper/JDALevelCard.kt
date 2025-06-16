package me.diamondforge.kyromera.levelcardlib.wrapper

import me.diamondforge.kyromera.levelcardlib.CardConfiguration
import me.diamondforge.kyromera.levelcardlib.LayoutConfig
import me.diamondforge.kyromera.levelcardlib.LevelCardDrawer
import me.diamondforge.kyromera.levelcardlib.OnlineStatus
import me.diamondforge.kyromera.levelcardlib.UserData
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

/**
 * A wrapper class for the LevelCardDrawer that provides easy integration with JDA.
 * This class simplifies the process of creating level cards for Discord users.
 */
object JDALevelCard {

    /**
     * Creates a new builder for creating level cards for JDA users.
     *
     * @param user The JDA User to create a level card for
     * @return A new Builder instance
     */
    fun builder(user: User): Builder {
        return Builder(user)
    }

    /**
     * Creates a new builder for creating level cards for JDA guild members.
     * This method uses the member's nickname if available, otherwise falls back to the username.
     *
     * @param member The JDA Member to create a level card for
     * @return A new Builder instance
     */
    fun builder(member: Member): Builder {
        return Builder(member)
    }

    /**
     * Builder class for creating level cards for JDA users.
     */
    class Builder {
        private val user: User
        private val username: String
        private var member: Member? = null
        private var rank: Int = 1
        private var level: Int = 1
        private var minXpForCurrentLevel: Int = 0
        private var maxXpForCurrentLevel: Int = 100
        private var currentXP: Int = 0
        private var accentColor: Int? = null
        private var width: Int = 950
        private var height: Int = 300
        private var showStatusIndicator: Boolean = true
        private var showGenerationTime: Boolean = false
        private var downloadAvatar: Boolean = true
        private var customOnlineStatus: OnlineStatus? = null
        private var customConfig: CardConfiguration? = null
        private var layoutConfig: LayoutConfig? = null

        /**
         * Creates a builder for a JDA User.
         *
         * @param user The JDA User to create a level card for
         */
        constructor(user: User) {
            this.user = user
            this.username = user.name
        }

        /**
         * Creates a builder for a JDA Member.
         * Uses the member's nickname if available, otherwise falls back to the username.
         *
         * @param member The JDA Member to create a level card for
         */
        constructor(member: Member) {
            this.user = member.user
            this.username = member.effectiveName
            this.member = member

            // If the member has a color, use it as the accent color
            if (member.colorRaw != 0) {
                this.accentColor = member.colorRaw
            }
        }

        /**
         * Sets the user's rank.
         *
         * @param rank User's rank
         * @return This builder for chaining
         */
        fun rank(rank: Int): Builder {
            require(rank > 0) { "Rank must be positive" }
            this.rank = rank
            return this
        }

        /**
         * Sets the user's level.
         *
         * @param level User's level
         * @return This builder for chaining
         */
        fun level(level: Int): Builder {
            require(level >= 0) { "Level must be positive" }
            this.level = level
            return this
        }

        /**
         * Sets the XP values.
         *
         * @param minXpForCurrentLevel Minimum XP for the current level
         * @param maxXpForCurrentLevel Maximum XP for the current level
         * @param currentXP Current XP of the user
         * @return This builder for chaining
         */
        fun xp(minXpForCurrentLevel: Int, maxXpForCurrentLevel: Int, currentXP: Int): Builder {
            require(minXpForCurrentLevel >= 0 && maxXpForCurrentLevel > minXpForCurrentLevel && currentXP >= minXpForCurrentLevel && currentXP <= maxXpForCurrentLevel) {
                "Invalid XP values: minXpForCurrentLevel must be >= 0, maxXpForCurrentLevel > minXpForCurrentLevel, and minXpForCurrentLevel <= currentXP <= maxXpForCurrentLevel"
            }
            this.minXpForCurrentLevel = minXpForCurrentLevel
            this.maxXpForCurrentLevel = maxXpForCurrentLevel
            this.currentXP = currentXP
            return this
        }

        /**
         * Sets the accent color.
         *
         * @param accentColor Accent color for the card (ARGB format)
         * @return This builder for chaining
         */
        fun accentColor(accentColor: Int): Builder {
            this.accentColor = accentColor
            return this
        }

        /**
         * Sets the card dimensions.
         *
         * @param width Width of the card
         * @param height Height of the card
         * @return This builder for chaining
         */
        fun dimensions(width: Int, height: Int): Builder {
            require(width > 0 && height > 0) { "Width and height must be positive" }
            this.width = width
            this.height = height
            return this
        }

        /**
         * Sets whether to show the status indicator.
         *
         * @param showStatusIndicator Whether to show the status indicator
         * @return This builder for chaining
         */
        fun showStatusIndicator(showStatusIndicator: Boolean): Builder {
            this.showStatusIndicator = showStatusIndicator
            return this
        }

        /**
         * Sets whether to show generation time on the card.
         *
         * @param showGenerationTime Whether to show generation time
         * @return This builder for chaining
         */
        fun showGenerationTime(showGenerationTime: Boolean): Builder {
            this.showGenerationTime = showGenerationTime
            return this
        }

        /**
         * Sets a custom card configuration.
         * This allows for more detailed customization of the card appearance.
         * If provided, this configuration will be used instead of creating a default one.
         * Note that dimensions, accent color, and showGenerationTime settings from this builder
         * will still be applied to the custom configuration if they were set.
         *
         * @param config The custom card configuration
         * @return This builder for chaining
         */
        fun customConfig(config: CardConfiguration): Builder {
            this.customConfig = config
            return this
        }

        /**
         * Sets a detailed layout configuration for the card.
         * This allows for independent positioning of all elements on the card.
         * If a custom configuration is also provided via customConfig(), this layout
         * will be applied to that configuration.
         *
         * @param layoutConfig The layout configuration
         * @return This builder for chaining
         */
        fun layoutConfig(layoutConfig: LayoutConfig): Builder {
            this.layoutConfig = layoutConfig
            return this
        }

        /**
         * Sets whether to download the avatar from Discord.
         * If set to false, the avatar URL will be used directly.
         *
         * @param downloadAvatar Whether to download the avatar
         * @return This builder for chaining
         */
        fun downloadAvatar(downloadAvatar: Boolean): Builder {
            this.downloadAvatar = downloadAvatar
            return this
        }

        /**
         * Sets a custom online status for the user.
         * This overrides the status that would be determined from the Member object.
         *
         * @param status The online status to use
         * @return This builder for chaining
         */
        fun onlineStatus(status: OnlineStatus): Builder {
            this.customOnlineStatus = status
            return this
        }

        /**
         * Builds and returns the level card.
         *
         * @return The generated level card as a BufferedImage
         */
        fun build(): BufferedImage {
            // Determine online status from available sources
            val onlineStatus = determineOnlineStatus()

            // Get the effective avatar URL (with size parameter for better quality)
            val avatarUrl = user.effectiveAvatarUrl + "?size=256"

            // Create or use the card configuration
            val config = if (customConfig != null) {
                // If we have a custom config, apply any explicitly set values from the builder
                val configBuilder = CardConfiguration.Builder()

                // Start with the custom config
                val tempConfig = customConfig!!

                // Apply dimensions if they were explicitly set
                if (width != 950 || height != 300) {
                    configBuilder.dimensions(width, height)
                } else {
                    configBuilder.dimensions(tempConfig.width, tempConfig.height)
                }

                // Apply accent color if it was explicitly set
                if (accentColor != null) {
                    configBuilder.accentColor(accentColor!!)
                } else {
                    configBuilder.accentColor(tempConfig.accentColor)
                }

                // Apply showGenerationTime
                configBuilder.showGenerationTime(showGenerationTime)

                // Copy all other properties from the custom config
                configBuilder
                    .avatarConfig(tempConfig.avatarSize, tempConfig.avatarMargin)
                    .textMargin(tempConfig.textMargin)
                    .textOffsets(
                        tempConfig.usernameYOffset,
                        tempConfig.rankLevelYOffset,
                        tempConfig.xpTextYOffset,
                        tempConfig.timeTextYOffset
                    )
                    .progressBarConfig(
                        tempConfig.progressBarHeight,
                        tempConfig.progressBarYOffset,
                        tempConfig.progressBarMargin
                    )
                    .fontSizes(
                        tempConfig.usernameFontSize,
                        tempConfig.rankLevelFontSize,
                        tempConfig.xpFontSize,
                        tempConfig.timeFontSize
                    )
                    .backgroundConfig(tempConfig.shadowBlur, tempConfig.cornerRadius)

                if (layoutConfig != null) {
                    configBuilder.layoutConfig(layoutConfig!!)
                } else if (tempConfig.layoutConfig != null) {
                    configBuilder.layoutConfig(tempConfig.layoutConfig)
                }

                configBuilder.build()
            } else {
                val configBuilder = CardConfiguration.Builder()
                    .dimensions(width, height)
                    .accentColor(accentColor ?: 0xFF2CBCC9.toInt()) // Use provided color or default
                    .showGenerationTime(showGenerationTime)

                if (layoutConfig != null) {
                    configBuilder.layoutConfig(layoutConfig!!)
                }

                configBuilder.build()
            }

            // Create the user data
            val userDataBuilder = UserData.Builder(username)
                .rank(rank)
                .level(level)
                .xp(minXpForCurrentLevel, maxXpForCurrentLevel, currentXP)
                .onlineStatus(onlineStatus)
                .showStatusIndicator(showStatusIndicator)

            // Handle avatar
            if (downloadAvatar) {
                try {
                    // Download avatar and convert to byte array
                    val avatarBytes = downloadAvatarBytes(avatarUrl)
                    userDataBuilder.avatarBytes(avatarBytes)
                } catch (e: IOException) {
                    // Fallback to URL if download fails
                    userDataBuilder.avatarUrl(avatarUrl)
                }
            } else {
                // Use URL directly
                userDataBuilder.avatarUrl(avatarUrl)
            }

            val userData = userDataBuilder.build()

            // Use the LevelCardDrawer to create the card
            return LevelCardDrawer.builder(username)
                .avatarSource(
                    userData.avatarBytes,
                    userData.avatarUrl,
                    userData.imageMode == me.diamondforge.kyromera.levelcardlib.ImageMode.DOWNLOAD
                )
                .rank(userData.rank)
                .level(userData.level)
                .xp(userData.minXpForCurrentLevel, userData.maxXpForCurrentLevel, userData.currentXP)
                .accentColor(config.accentColor)
                .dimensions(config.width, config.height)
                .onlineStatus(userData.onlineStatus)
                .showStatusIndicator(userData.showStatusIndicator)
                .showGenerationTime(config.showGenerationTime)
                .build()
        }

        /**
         * Determines the online status to use for the level card.
         * Priority order:
         * 1. Custom status set via onlineStatus()
         * 2. Status from Member object if available
         * 3. Default to ONLINE
         *
         * @return The determined OnlineStatus
         */
        private fun determineOnlineStatus(): OnlineStatus {
            // If custom status is set, use it
            customOnlineStatus?.let { return it }

            // If member is available, get status from it
            member?.let {
                return mapJDAStatus(it.onlineStatus)
            }

            // Default to ONLINE
            return OnlineStatus.ONLINE
        }

        /**
         * Maps JDA's online status to the library's OnlineStatus enum.
         *
         * @param jdaStatus JDA's online status
         * @return Corresponding library OnlineStatus
         */
        private fun mapJDAStatus(jdaStatus: net.dv8tion.jda.api.OnlineStatus): OnlineStatus {
            return when (jdaStatus) {
                net.dv8tion.jda.api.OnlineStatus.ONLINE -> OnlineStatus.ONLINE
                net.dv8tion.jda.api.OnlineStatus.IDLE -> OnlineStatus.IDLE
                net.dv8tion.jda.api.OnlineStatus.DO_NOT_DISTURB -> OnlineStatus.DND
                else -> OnlineStatus.OFFLINE
            }
        }

        /**
         * Downloads an avatar image from a URL and converts it to a byte array.
         *
         * @param url The URL to download from
         * @return The image as a byte array
         * @throws IOException If the download fails
         */
        private fun downloadAvatarBytes(url: String): ByteArray {
            if (url.isBlank()) {
                throw IOException("Avatar URL cannot be blank")
            }

            try {
                val connection = URL(url).openConnection()
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.setRequestProperty("User-Agent", "JDALevelCard/1.0")

                val image = ImageIO.read(connection.getInputStream())
                if (image == null) {
                    throw IOException("Failed to read image from URL: $url")
                }

                if (image.width <= 0 || image.height <= 0) {
                    throw IOException("Invalid image dimensions: ${image.width}x${image.height}")
                }

                val outputStream = ByteArrayOutputStream()
                if (!ImageIO.write(image, "png", outputStream)) {
                    throw IOException("Failed to encode image as PNG")
                }

                val bytes = outputStream.toByteArray()
                if (bytes.isEmpty()) {
                    throw IOException("Generated image is empty")
                }

                return bytes
            } catch (e: IOException) {
                throw e
            } catch (e: Exception) {
                throw IOException("Failed to download avatar: ${e.message}", e)
            }
        }
    }
}
