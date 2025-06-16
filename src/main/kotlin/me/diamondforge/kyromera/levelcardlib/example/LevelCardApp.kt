package me.diamondforge.kyromera.levelcardlib.example

import me.diamondforge.kyromera.levelcardlib.LevelCardDrawer
import me.diamondforge.kyromera.levelcardlib.LayoutConfig
import me.diamondforge.kyromera.levelcardlib.OnlineStatus

import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.filechooser.FileNameExtensionFilter
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Example application to demonstrate the LevelCardDrawer library.
 */
class LevelCardApp : JFrame() {
    private val usernameField: JTextField
    private val rankSpinner: JSpinner
    private val levelSpinner: JSpinner
    private val minXPSpinner: JSpinner
    private val maxXPSpinner: JSpinner
    private val currentXPSpinner: JSpinner
    private val accentColorField: JTextField
    private val widthField: JTextField
    private val heightField: JTextField
    private val avatarUrlField: JTextField
    private val imagePreviewLabel: JLabel
    private val cardPreviewPanel: JPanel
    private val onlineStatusComboBox: JComboBox<OnlineStatus>
    private val showStatusIndicatorCheckBox: JCheckBox
    private val showGenerationTimeCheckBox: JCheckBox
    private val useCustomLayoutCheckBox: JCheckBox = JCheckBox()
    private val useOffsetsCheckBox: JCheckBox = JCheckBox()

    // Layout configuration fields
    private val avatarXField: JTextField = JTextField("50", 5)
    private val avatarYField: JTextField = JTextField("0", 5)
    private val usernameXField: JTextField = JTextField("270", 5)
    private val usernameYField: JTextField = JTextField("83", 5)
    private val rankLevelXField: JTextField = JTextField("270", 5)
    private val rankLevelYField: JTextField = JTextField("133", 5)
    private val xpTextXField: JTextField = JTextField("270", 5)
    private val xpTextYField: JTextField = JTextField("200", 5)
    private val timeTextXField: JTextField = JTextField("270", 5)
    private val timeTextYField: JTextField = JTextField("267", 5)
    private val progressBarXField: JTextField = JTextField("270", 5)
    private val progressBarYField: JTextField = JTextField("217", 5)
    private val progressBarRightMarginField: JTextField = JTextField("50", 5)

    private var avatarBytes: ByteArray? = null
    private var cardImage: BufferedImage? = null

    init {
        title = "Level Card Generator"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1600, 700)
        setLocationRelativeTo(null)

        // Create main panel with padding
        val mainPanel = JPanel(BorderLayout(10, 10))
        mainPanel.border = EmptyBorder(20, 20, 20, 20)
        contentPane = mainPanel

        // Create form panel
        val formPanel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.insets = Insets(5, 5, 5, 5)

        // Username
        gbc.gridx = 0
        gbc.gridy = 0
        formPanel.add(JLabel("Username:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 0
        usernameField = JTextField("fabichan", 15)
        formPanel.add(usernameField, gbc)

        // Rank
        gbc.gridx = 0
        gbc.gridy = 1
        formPanel.add(JLabel("Rank:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 1
        rankSpinner = JSpinner(SpinnerNumberModel(44, 1, 9999, 1))
        formPanel.add(rankSpinner, gbc)

        // Level
        gbc.gridx = 0
        gbc.gridy = 2
        formPanel.add(JLabel("Level:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 2
        levelSpinner = JSpinner(SpinnerNumberModel(12, 1, 9999, 1))
        formPanel.add(levelSpinner, gbc)

        // Min XP
        gbc.gridx = 0
        gbc.gridy = 3
        formPanel.add(JLabel("Min XP:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 3
        minXPSpinner = JSpinner(SpinnerNumberModel(0, 0, 999999, 1))
        formPanel.add(minXPSpinner, gbc)

        // Max XP
        gbc.gridx = 0
        gbc.gridy = 4
        formPanel.add(JLabel("Max XP:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 4
        maxXPSpinner = JSpinner(SpinnerNumberModel(1337, 1, 999999, 1))
        formPanel.add(maxXPSpinner, gbc)

        // Current XP
        gbc.gridx = 0
        gbc.gridy = 5
        formPanel.add(JLabel("Current XP:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 5
        currentXPSpinner = JSpinner(SpinnerNumberModel(429, 0, 999999, 1))
        formPanel.add(currentXPSpinner, gbc)

        // Accent Color
        gbc.gridx = 0
        gbc.gridy = 6
        formPanel.add(JLabel("Accent Color (hex):"), gbc)

        gbc.gridx = 1
        gbc.gridy = 6
        accentColorField = JTextField("FF00A8E8", 10)
        formPanel.add(accentColorField, gbc)

        val colorPickerButton = JButton("Pick Color")
        colorPickerButton.addActionListener { e ->
            val color = JColorChooser.showDialog(this, "Choose Accent Color", Color(0, 168, 232))
            if (color != null) {
                val rgb = color.rgb
                accentColorField.text = Integer.toHexString(rgb and 0xFFFFFFFF.toInt()).uppercase(Locale.getDefault())
            }
        }
        gbc.gridx = 2
        gbc.gridy = 6
        formPanel.add(colorPickerButton, gbc)

        // Width
        gbc.gridx = 0
        gbc.gridy = 7
        formPanel.add(JLabel("Width:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 7
        widthField = JTextField("950", 5)
        formPanel.add(widthField, gbc)

        // Height
        gbc.gridx = 0
        gbc.gridy = 8
        formPanel.add(JLabel("Height:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 8
        heightField = JTextField("300", 5)
        formPanel.add(heightField, gbc)

        // Avatar URL
        gbc.gridx = 0
        gbc.gridy = 9
        formPanel.add(JLabel("Avatar URL:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 9
        avatarUrlField = JTextField("", 25)
        formPanel.add(avatarUrlField, gbc)

        // Avatar file selection
        gbc.gridx = 0
        gbc.gridy = 10
        formPanel.add(JLabel("Avatar File:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 10
        val selectFileButton = JButton("Select Image File")
        selectFileButton.addActionListener(::selectAvatarFile)
        formPanel.add(selectFileButton, gbc)

        // Image preview
        gbc.gridx = 0
        gbc.gridy = 11
        formPanel.add(JLabel("Avatar Preview:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 11
        imagePreviewLabel = JLabel("No image selected")
        imagePreviewLabel.preferredSize = Dimension(100, 100)
        imagePreviewLabel.border = BorderFactory.createLineBorder(Color.GRAY)
        formPanel.add(imagePreviewLabel, gbc)

        // Online Status
        gbc.gridx = 0
        gbc.gridy = 12
        formPanel.add(JLabel("Online Status:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 12
        onlineStatusComboBox = JComboBox(OnlineStatus.values())
        formPanel.add(onlineStatusComboBox, gbc)

        // Show Status Indicator
        gbc.gridx = 0
        gbc.gridy = 13
        formPanel.add(JLabel("Show Status Indicator:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 13
        showStatusIndicatorCheckBox = JCheckBox()
        showStatusIndicatorCheckBox.isSelected = true
        formPanel.add(showStatusIndicatorCheckBox, gbc)

        // Show Generation Time
        gbc.gridx = 0
        gbc.gridy = 14
        formPanel.add(JLabel("Show Generation Time:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 14
        showGenerationTimeCheckBox = JCheckBox()
        showGenerationTimeCheckBox.isSelected = false
        formPanel.add(showGenerationTimeCheckBox, gbc)

        // Custom Layout
        gbc.gridx = 0
        gbc.gridy = 15
        formPanel.add(JLabel("Use Custom Layout:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 15
        useCustomLayoutCheckBox.isSelected = false
        formPanel.add(useCustomLayoutCheckBox, gbc)

        // Use Offsets
        gbc.gridx = 0
        gbc.gridy = 16
        formPanel.add(JLabel("Use Offsets:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 16
        useOffsetsCheckBox.isSelected = false
        useOffsetsCheckBox.addActionListener { e ->
            val useOffsets = useOffsetsCheckBox.isSelected
            // Update field labels to indicate whether values are offsets or absolute positions
            val labelSuffix = if (useOffsets) " Offset:" else ":"

            // Update all field labels
            updateFieldLabels(formPanel, labelSuffix)

            // Set default values for offset mode
            if (useOffsets) {
                if (avatarXField.text == "50") avatarXField.text = "0"
                if (avatarYField.text == "0") avatarYField.text = "0"
                if (usernameXField.text == "270") usernameXField.text = "0"
                if (usernameYField.text == "83") usernameYField.text = "0"
                if (rankLevelXField.text == "270") rankLevelXField.text = "0"
                if (rankLevelYField.text == "133") rankLevelYField.text = "0"
                if (xpTextXField.text == "270") xpTextXField.text = "0"
                if (xpTextYField.text == "200") xpTextYField.text = "0"
                if (timeTextXField.text == "270") timeTextXField.text = "0"
                if (timeTextYField.text == "267") timeTextYField.text = "0"
                if (progressBarXField.text == "270") progressBarXField.text = "0"
                if (progressBarYField.text == "217") progressBarYField.text = "0"
                if (progressBarRightMarginField.text == "50") progressBarRightMarginField.text = "0"
            } else {
                // Revert to default absolute positions when offset mode is disabled
                if (avatarXField.text == "0") avatarXField.text = "50"
                if (avatarYField.text == "0") avatarYField.text = "0"
                if (usernameXField.text == "0") usernameXField.text = "270"
                if (usernameYField.text == "0") usernameYField.text = "83"
                if (rankLevelXField.text == "0") rankLevelXField.text = "270"
                if (rankLevelYField.text == "0") rankLevelYField.text = "133"
                if (xpTextXField.text == "0") xpTextXField.text = "270"
                if (xpTextYField.text == "0") xpTextYField.text = "200"
                if (timeTextXField.text == "0") timeTextXField.text = "270"
                if (timeTextYField.text == "0") timeTextYField.text = "267"
                if (progressBarXField.text == "0") progressBarXField.text = "270"
                if (progressBarYField.text == "0") progressBarYField.text = "217"
                if (progressBarRightMarginField.text == "0") progressBarRightMarginField.text = "50"
            }
        }
        formPanel.add(useOffsetsCheckBox, gbc)

        // Layout Configuration Section
        gbc.gridx = 0
        gbc.gridy = 17
        gbc.gridwidth = 2
        formPanel.add(JLabel("--- Layout Configuration ---"), gbc)
        gbc.gridwidth = 1

        // Avatar Position
        gbc.gridx = 0
        gbc.gridy = 18
        formPanel.add(JLabel("Avatar X:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 18
        formPanel.add(avatarXField, gbc)

        gbc.gridx = 0
        gbc.gridy = 19
        formPanel.add(JLabel("Avatar Y:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 19
        formPanel.add(avatarYField, gbc)

        // Username Position
        gbc.gridx = 0
        gbc.gridy = 20
        formPanel.add(JLabel("Username X:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 20
        formPanel.add(usernameXField, gbc)

        gbc.gridx = 0
        gbc.gridy = 21
        formPanel.add(JLabel("Username Y:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 21
        formPanel.add(usernameYField, gbc)

        // Rank/Level Position
        gbc.gridx = 0
        gbc.gridy = 22
        formPanel.add(JLabel("Rank/Level X:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 22
        formPanel.add(rankLevelXField, gbc)

        gbc.gridx = 0
        gbc.gridy = 23
        formPanel.add(JLabel("Rank/Level Y:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 23
        formPanel.add(rankLevelYField, gbc)

        // XP Text Position
        gbc.gridx = 0
        gbc.gridy = 24
        formPanel.add(JLabel("XP Text X:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 24
        formPanel.add(xpTextXField, gbc)

        gbc.gridx = 0
        gbc.gridy = 25
        formPanel.add(JLabel("XP Text Y:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 25
        formPanel.add(xpTextYField, gbc)

        // Time Text Position
        gbc.gridx = 0
        gbc.gridy = 26
        formPanel.add(JLabel("Time Text X:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 26
        formPanel.add(timeTextXField, gbc)

        gbc.gridx = 0
        gbc.gridy = 27
        formPanel.add(JLabel("Time Text Y:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 27
        formPanel.add(timeTextYField, gbc)

        // Progress Bar Position
        gbc.gridx = 0
        gbc.gridy = 28
        formPanel.add(JLabel("Progress Bar X:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 28
        formPanel.add(progressBarXField, gbc)

        gbc.gridx = 0
        gbc.gridy = 29
        formPanel.add(JLabel("Progress Bar Y:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 29
        formPanel.add(progressBarYField, gbc)

        gbc.gridx = 0
        gbc.gridy = 30
        formPanel.add(JLabel("Progress Bar Right Margin:"), gbc)

        gbc.gridx = 1
        gbc.gridy = 30
        formPanel.add(progressBarRightMarginField, gbc)

        // Generate button
        gbc.gridx = 0
        gbc.gridy = 31
        gbc.gridwidth = 2
        val generateButton = JButton("Generate Level Card")
        generateButton.addActionListener(::generateLevelCard)
        formPanel.add(generateButton, gbc)

        // Save button
        gbc.gridx = 0
        gbc.gridy = 32
        val saveButton = JButton("Save Level Card")
        saveButton.addActionListener(::saveLevelCard)
        formPanel.add(saveButton, gbc)

        val scrollPane = JScrollPane(formPanel)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPane.border = null  

        scrollPane.preferredSize = Dimension(400, 500)
        mainPanel.add(scrollPane, BorderLayout.WEST)

        // Card preview panel
        cardPreviewPanel = JPanel(BorderLayout())
        cardPreviewPanel.border = BorderFactory.createTitledBorder("Card Preview")
        cardPreviewPanel.preferredSize = Dimension(950, 300)
        mainPanel.add(cardPreviewPanel, BorderLayout.CENTER)

        // Set visible
        isVisible = true
    }

    private fun selectAvatarFile(e: ActionEvent) {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select Avatar Image"
        fileChooser.fileFilter = FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif")

        val result = fileChooser.showOpenDialog(this)
        if (result == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            try {
                avatarBytes = Files.readAllBytes(Paths.get(selectedFile.absolutePath))

                // Update preview
                val icon = ImageIcon(selectedFile.absolutePath)
                val img = icon.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH)
                imagePreviewLabel.icon = ImageIcon(img)
                imagePreviewLabel.text = ""

                // Clear URL field
                avatarUrlField.text = ""
            } catch (ex: IOException) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error loading image: ${ex.message}",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }

    private fun generateLevelCard(e: ActionEvent) {
        try {
            val username = usernameField.text
            val rank = rankSpinner.value as Int
            val level = levelSpinner.value as Int
            val minXP = minXPSpinner.value as Int
            val maxXP = maxXPSpinner.value as Int
            val currentXP = currentXPSpinner.value as Int

            // Parse accent color
            val colorText = accentColorField.text.replace("#", "")
            val accentColor: Int
            try {
                accentColor = colorText.toLong(16).toInt()
                // Ensure alpha channel is set
                if ((accentColor and 0xFF000000.toInt()) == 0) {
                    accentColor or 0xFF000000.toInt()
                }
            } catch (ex: NumberFormatException) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid color format. Please use hex format (e.g., FFEA397C)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                )
                return
            }

            val width = widthField.text.toInt()
            val height = heightField.text.toInt()

            val avatarUrl = avatarUrlField.text.trim()
            val useUrl = avatarUrl.isNotEmpty()

            // Get online status settings
            val onlineStatus = onlineStatusComboBox.selectedItem as OnlineStatus
            val showStatusIndicator = showStatusIndicatorCheckBox.isSelected
            val showGenerationTime = showGenerationTimeCheckBox.isSelected

            // Create the level card builder
            val cardBuilder = LevelCardDrawer.builder(username)
                .avatarSource(
                    if (useUrl) null else avatarBytes,
                    if (useUrl) avatarUrl else null,
                    useUrl
                )
                .rank(rank)
                .level(level)
                .xp(minXP, maxXP, currentXP)
                .accentColor(accentColor)
                .dimensions(width, height)
                .onlineStatus(onlineStatus)
                .showStatusIndicator(showStatusIndicator)
                .showGenerationTime(showGenerationTime)

            // Add custom layout configuration if enabled
            if (useCustomLayoutCheckBox.isSelected) {
                try {
                    // Parse layout configuration values
                    val avatarX = avatarXField.text.toInt()
                    val avatarY = avatarYField.text.toInt()
                    val usernameX = usernameXField.text.toInt()
                    val usernameY = usernameYField.text.toInt()
                    val rankLevelX = rankLevelXField.text.toInt()
                    val rankLevelY = rankLevelYField.text.toInt()
                    val xpTextX = xpTextXField.text.toInt()
                    val xpTextY = xpTextYField.text.toInt()
                    val timeTextX = timeTextXField.text.toInt()
                    val timeTextY = timeTextYField.text.toInt()
                    val progressBarX = progressBarXField.text.toInt()
                    val progressBarY = progressBarYField.text.toInt()
                    val progressBarRightMargin = progressBarRightMarginField.text.toInt()

                    // Create layout configuration
                    val layoutConfigBuilder = LayoutConfig.Builder()

                    // Use offset methods if "Use Offsets" is checked, otherwise use absolute position methods
                    if (useOffsetsCheckBox.isSelected) {
                        layoutConfigBuilder
                            .avatarOffset(avatarX, avatarY)
                            .usernameOffset(usernameX, usernameY)
                            .rankLevelOffset(rankLevelX, rankLevelY)
                            .xpTextOffset(xpTextX, xpTextY)
                            .timeTextOffset(timeTextX, timeTextY)
                            .progressBarOffset(progressBarX, progressBarY, progressBarRightMargin)
                    } else {
                        layoutConfigBuilder
                            .avatarPosition(avatarX, avatarY)
                            .usernamePosition(usernameX, usernameY)
                            .rankLevelPosition(rankLevelX, rankLevelY)
                            .xpTextPosition(xpTextX, xpTextY)
                            .timeTextPosition(timeTextX, timeTextY)
                            .progressBarPosition(progressBarX, progressBarY, progressBarRightMargin)
                    }

                    val layoutConfig = layoutConfigBuilder.build()

                    // Add layout configuration to card builder
                    cardBuilder.layoutConfig(layoutConfig)
                } catch (ex: NumberFormatException) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid layout configuration values. Please enter valid integers.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    )
                    return
                }
            }

            // Build the level card
            cardImage = cardBuilder.build()

            // Display the card
            val cardLabel = JLabel(ImageIcon(cardImage))
            cardPreviewPanel.removeAll()
            cardPreviewPanel.add(cardLabel, BorderLayout.CENTER)
            cardPreviewPanel.revalidate()
            cardPreviewPanel.repaint()

        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(
                this,
                "Error generating level card: ${ex.message}",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
            ex.printStackTrace()
        }
    }

    private fun saveLevelCard(e: ActionEvent) {
        if (cardImage == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please generate a level card first",
                "No Card Generated",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }

        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Save Level Card"
        fileChooser.fileFilter = FileNameExtensionFilter("PNG Image", "png")
        fileChooser.selectedFile = File("levelcard.png")

        val result = fileChooser.showSaveDialog(this)
        if (result == JFileChooser.APPROVE_OPTION) {
            var file = fileChooser.selectedFile
            // Add .png extension if not present
            if (!file.name.lowercase(Locale.getDefault()).endsWith(".png")) {
                file = File(file.absolutePath + ".png")
            }

            try {
                ImageIO.write(cardImage, "png", file)
                JOptionPane.showMessageDialog(
                    this,
                    "Level card saved successfully to: ${file.absolutePath}",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } catch (ex: IOException) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error saving level card: ${ex.message}",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }

    /**
     * Updates the field labels in the form panel to indicate whether values are offsets or absolute positions.
     *
     * @param formPanel The form panel containing the labels
     * @param labelSuffix The suffix to append to the label text (e.g., ":" or " Offset:")
     */
    private fun updateFieldLabels(formPanel: JPanel, labelSuffix: String) {
        // Get all components in the form panel
        val components = formPanel.components

        // Update labels for position fields
        for (component in components) {
            if (component is JLabel) {
                val text = component.text
                // Extract the base name without any suffix
                when {
                    text.contains("Avatar X") -> component.text = "Avatar X$labelSuffix"
                    text.contains("Avatar Y") -> component.text = "Avatar Y$labelSuffix"
                    text.contains("Username X") -> component.text = "Username X$labelSuffix"
                    text.contains("Username Y") -> component.text = "Username Y$labelSuffix"
                    text.contains("Rank/Level X") -> component.text = "Rank/Level X$labelSuffix"
                    text.contains("Rank/Level Y") -> component.text = "Rank/Level Y$labelSuffix"
                    text.contains("XP Text X") -> component.text = "XP Text X$labelSuffix"
                    text.contains("XP Text Y") -> component.text = "XP Text Y$labelSuffix"
                    text.contains("Time Text X") -> component.text = "Time Text X$labelSuffix"
                    text.contains("Time Text Y") -> component.text = "Time Text Y$labelSuffix"
                    text.contains("Progress Bar X") -> component.text = "Progress Bar X$labelSuffix"
                    text.contains("Progress Bar Y") -> component.text = "Progress Bar Y$labelSuffix"
                    text.contains("Right Margin") -> component.text = "Right Margin$labelSuffix"
                }
            }
        }

        // Refresh the panel
        formPanel.revalidate()
        formPanel.repaint()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Set look and feel to system default
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Start the application
            SwingUtilities.invokeLater { LevelCardApp() }
        }
    }
}
