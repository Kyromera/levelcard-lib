package me.diamondforge.kyromera.levelcardlib.example

import me.diamondforge.kyromera.levelcardlib.LevelCardDrawer
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

        // Generate button
        gbc.gridx = 0
        gbc.gridy = 15
        gbc.gridwidth = 2
        val generateButton = JButton("Generate Level Card")
        generateButton.addActionListener(::generateLevelCard)
        formPanel.add(generateButton, gbc)

        // Save button
        gbc.gridx = 0
        gbc.gridy = 16
        val saveButton = JButton("Save Level Card")
        saveButton.addActionListener(::saveLevelCard)
        formPanel.add(saveButton, gbc)

        // Add form panel to the left side
        mainPanel.add(formPanel, BorderLayout.WEST)

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

            // Generate the level card with new features
            cardImage = LevelCardDrawer.drawLevelCard(
                if (useUrl) null else avatarBytes,
                if (useUrl) avatarUrl else null,
                useUrl,
                username,
                rank,
                level,
                minXP,
                maxXP,
                currentXP,
                accentColor,
                width,
                height,
                onlineStatus,
                showStatusIndicator,
                showGenerationTime
            )

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
