package me.diamondforge.kyromera.levelcardlib.wrapper.example

import me.diamondforge.kyromera.levelcardlib.OnlineStatus
import me.diamondforge.kyromera.levelcardlib.wrapper.JDALevelCard
import me.diamondforge.kyromera.levelcardlib.wrapper.createLevelCard
import me.diamondforge.kyromera.levelcardlib.wrapper.sendLevelCard
import me.diamondforge.kyromera.levelcardlib.wrapper.sendToChannel
import me.diamondforge.kyromera.levelcardlib.wrapper.toByteArray
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.FileUpload
import java.io.File
import javax.imageio.ImageIO

/**
 * Example class demonstrating how to use the JDA Level Card wrapper in a Discord bot.
 * This is a simple bot that responds to slash commands to generate level cards.
 * 
 * Note: This is just an example and not meant to be run directly.
 * You would need to provide your own Discord bot token and implement proper
 * level tracking in a real application.
 */
class JDALevelCardExample {

    /**
     * Example of how to set up a JDA bot with level card commands
     */
    fun setupBot(token: String) {
        // Create JDA instance with necessary intents
        val jda = JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
            .addEventListeners(LevelCardCommandListener())
            .build()

        // Register slash commands
        jda.updateCommands().addCommands(
            Commands.slash("level", "Show your level card"),
            Commands.slash("leveluser", "Show level card for a user")
                .addOption(OptionType.USER, "user", "The user to show the level card for", true)
        ).queue()
    }

    /**
     * Example listener for level card commands
     */
    private class LevelCardCommandListener : ListenerAdapter() {

        // Mock database for user levels (in a real app, this would be a database)
        private val userLevels = mutableMapOf<String, UserLevelData>()

        override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
            when (event.name) {
                "level" -> handleLevelCommand(event)
                "leveluser" -> handleLevelUserCommand(event)
            }
        }

        private fun handleLevelCommand(event: SlashCommandInteractionEvent) {
            event.deferReply().queue()

            val member = event.member ?: run {
                event.hook.sendMessage("This command can only be used in a server.").queue()
                return
            }

            // Get or create level data for the user
            val levelData = getUserLevelData(member.id)

            // Method 1: Using the extension function directly
            member.sendLevelCard(
                channel = event.hook.interaction.messageChannel,
                rank = levelData.rank,
                level = levelData.level,
                currentXP = levelData.currentXP,
                maxXpForCurrentLevel = levelData.maxXP,
                content = "Here's your level card, ${member.effectiveName}!"
            )
        }

        private fun handleLevelUserCommand(event: SlashCommandInteractionEvent) {
            event.deferReply().queue()

            val targetUser = event.getOption("user")?.asUser
            if (targetUser == null) {
                event.hook.sendMessage("User not found.").queue()
                return
            }

            val targetMember = event.getOption("user")?.asMember

            // Get or create level data for the target user
            val levelData = getUserLevelData(targetUser.id)

            // Method 2: Using the builder pattern
            val levelCardBuilder = if (targetMember != null) {
                // If we have a Member object, use it to get nickname and role color
                targetMember.createLevelCard()
            } else {
                // Otherwise, just use the User object
                targetUser.createLevelCard()
            }

            // Configure and build the level card
            val levelCard = levelCardBuilder
                .rank(levelData.rank)
                .level(levelData.level)
                .xp(0, levelData.maxXP, levelData.currentXP)
                .showGenerationTime(true)
                .build()

            // Convert to bytes and send
            val imageBytes = levelCard.toByteArray()
            val fileUpload = FileUpload.fromData(imageBytes, "level-card.png")

            // In JDA 5.6.1, we can use sendMessage with files
            event.hook.sendMessage("Level card for ${targetUser.name}")
                .addFiles(fileUpload)
                .queue()
        }

        /**
         * Example method to save a level card to a file
         */
        private fun saveExampleCard(user: User) {
            // Create a level card
            val levelCard = JDALevelCard.builder(user)
                .rank(5)
                .level(10)
                .xp(100, 500, 250)
                .onlineStatus(OnlineStatus.ONLINE)
                .showGenerationTime(true)
                .build()

            // Save to file
            ImageIO.write(levelCard, "png", File("level-card.png"))
        }

        /**
         * Example method demonstrating advanced customization with a custom CardConfiguration
         */
        private fun saveCustomConfigCard(user: User) {
            // Create a custom configuration
            val customConfig = me.diamondforge.kyromera.levelcardlib.CardConfiguration.Builder()
                .dimensions(1000, 350)
                .accentColor(0xFF00FF00.toInt()) // Green
                .avatarConfig(180, 45)
                .textMargin(290)
                .textOffsets(85, 135, 205, 270)
                .progressBarConfig(35, 220, 55)
                .fontSizes(48f, 34f, 28f, 21f)
                .backgroundConfig(14f, 26f)
                .build()

            // Create a level card with the custom configuration
            val levelCard = JDALevelCard.builder(user)
                .rank(5)
                .level(10)
                .xp(100, 500, 250)
                .customConfig(customConfig)
                .build()

            // Save to file
            ImageIO.write(levelCard, "png", File("custom-level-card.png"))
        }

        /**
         * Mock method to get user level data (in a real app, this would query a database)
         */
        private fun getUserLevelData(userId: String): UserLevelData {
            return userLevels.getOrPut(userId) {
                // Generate random level data for demonstration
                val level = (1..20).random()
                val maxXP = level * 100
                val currentXP = (0..maxXP).random()
                val rank = (1..100).random()

                UserLevelData(level, rank, currentXP, maxXP)
            }
        }
    }

    /**
     * Simple data class to hold user level information
     */
    private data class UserLevelData(
        val level: Int,
        val rank: Int,
        val currentXP: Int,
        val maxXP: Int
    )
}
