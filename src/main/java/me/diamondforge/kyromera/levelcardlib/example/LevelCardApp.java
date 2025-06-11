package me.diamondforge.kyromera.levelcardlib.example;

import me.diamondforge.kyromera.levelcardlib.LevelCardDrawer;
import me.diamondforge.kyromera.levelcardlib.OnlineStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Example application to demonstrate the LevelCardDrawer library.
 */
public class LevelCardApp extends JFrame {
    private JTextField usernameField;
    private JSpinner rankSpinner;
    private JSpinner levelSpinner;
    private JSpinner minXPSpinner;
    private JSpinner maxXPSpinner;
    private JSpinner currentXPSpinner;
    private JTextField accentColorField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField avatarUrlField;
    private JLabel imagePreviewLabel;
    private JPanel cardPreviewPanel;
    private JComboBox<OnlineStatus> onlineStatusComboBox;
    private JCheckBox showStatusIndicatorCheckBox;
    private JCheckBox showGenerationTimeCheckBox;

    private byte[] avatarBytes;
    private BufferedImage cardImage;

    public LevelCardApp() {
        setTitle("Level Card Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField("fabichan", 15);
        formPanel.add(usernameField, gbc);

        // Rank
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Rank:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        rankSpinner = new JSpinner(new SpinnerNumberModel(44, 1, 9999, 1));
        formPanel.add(rankSpinner, gbc);

        // Level
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Level:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        levelSpinner = new JSpinner(new SpinnerNumberModel(12, 1, 9999, 1));
        formPanel.add(levelSpinner, gbc);

        // Min XP
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Min XP:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        minXPSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        formPanel.add(minXPSpinner, gbc);

        // Max XP
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Max XP:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        maxXPSpinner = new JSpinner(new SpinnerNumberModel(1337, 1, 999999, 1));
        formPanel.add(maxXPSpinner, gbc);

        // Current XP
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Current XP:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        currentXPSpinner = new JSpinner(new SpinnerNumberModel(429, 0, 999999, 1));
        formPanel.add(currentXPSpinner, gbc);

        // Accent Color
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Accent Color (hex):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        accentColorField = new JTextField("FFEA397C", 10);
        formPanel.add(accentColorField, gbc);

        JButton colorPickerButton = new JButton("Pick Color");
        colorPickerButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Choose Accent Color", Color.PINK);
            if (color != null) {
                int rgb = color.getRGB();
                accentColorField.setText(Integer.toHexString(rgb & 0xFFFFFFFF).toUpperCase());
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 6;
        formPanel.add(colorPickerButton, gbc);

        // Width
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Width:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        widthField = new JTextField("600", 5);
        formPanel.add(widthField, gbc);

        // Height
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Height:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        heightField = new JTextField("180", 5);
        formPanel.add(heightField, gbc);

        // Avatar URL
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("Avatar URL:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        avatarUrlField = new JTextField("", 25);
        formPanel.add(avatarUrlField, gbc);

        // Avatar file selection
        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("Avatar File:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        JButton selectFileButton = new JButton("Select Image File");
        selectFileButton.addActionListener(this::selectAvatarFile);
        formPanel.add(selectFileButton, gbc);

        // Image preview
        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("Avatar Preview:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        imagePreviewLabel = new JLabel("No image selected");
        imagePreviewLabel.setPreferredSize(new Dimension(100, 100));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(imagePreviewLabel, gbc);

        // Online Status
        gbc.gridx = 0;
        gbc.gridy = 12;
        formPanel.add(new JLabel("Online Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 12;
        onlineStatusComboBox = new JComboBox<>(OnlineStatus.values());
        formPanel.add(onlineStatusComboBox, gbc);

        // Show Status Indicator
        gbc.gridx = 0;
        gbc.gridy = 13;
        formPanel.add(new JLabel("Show Status Indicator:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 13;
        showStatusIndicatorCheckBox = new JCheckBox();
        showStatusIndicatorCheckBox.setSelected(true);
        formPanel.add(showStatusIndicatorCheckBox, gbc);

        // Show Generation Time
        gbc.gridx = 0;
        gbc.gridy = 14;
        formPanel.add(new JLabel("Show Generation Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 14;
        showGenerationTimeCheckBox = new JCheckBox();
        showGenerationTimeCheckBox.setSelected(false);
        formPanel.add(showGenerationTimeCheckBox, gbc);

        // Generate button
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        JButton generateButton = new JButton("Generate Level Card");
        generateButton.addActionListener(this::generateLevelCard);
        formPanel.add(generateButton, gbc);

        // Save button
        gbc.gridx = 0;
        gbc.gridy = 16;
        JButton saveButton = new JButton("Save Level Card");
        saveButton.addActionListener(this::saveLevelCard);
        formPanel.add(saveButton, gbc);

        // Add form panel to the left side
        mainPanel.add(formPanel, BorderLayout.WEST);

        // Card preview panel
        cardPreviewPanel = new JPanel(new BorderLayout());
        cardPreviewPanel.setBorder(BorderFactory.createTitledBorder("Card Preview"));
        cardPreviewPanel.setPreferredSize(new Dimension(600, 180));
        mainPanel.add(cardPreviewPanel, BorderLayout.CENTER);

        // Set visible
        setVisible(true);
    }

    private void selectAvatarFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Avatar Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                avatarBytes = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));

                // Update preview
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagePreviewLabel.setIcon(new ImageIcon(img));
                imagePreviewLabel.setText("");

                // Clear URL field
                avatarUrlField.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading image: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateLevelCard(ActionEvent e) {
        try {
            String username = usernameField.getText();
            int rank = (Integer) rankSpinner.getValue();
            int level = (Integer) levelSpinner.getValue();
            int minXP = (Integer) minXPSpinner.getValue();
            int maxXP = (Integer) maxXPSpinner.getValue();
            int currentXP = (Integer) currentXPSpinner.getValue();

            // Parse accent color
            String colorText = accentColorField.getText().replace("#", "");
            int accentColor;
            try {
                accentColor = (int) Long.parseLong(colorText, 16);
                // Ensure alpha channel is set
                if ((accentColor & 0xFF000000) == 0) {
                    accentColor |= 0xFF000000;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid color format. Please use hex format (e.g., FFEA397C)", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            String avatarUrl = avatarUrlField.getText().trim();
            boolean useUrl = !avatarUrl.isEmpty();

            // Get online status settings
            OnlineStatus onlineStatus = (OnlineStatus) onlineStatusComboBox.getSelectedItem();
            boolean showStatusIndicator = showStatusIndicatorCheckBox.isSelected();
            boolean showGenerationTime = showGenerationTimeCheckBox.isSelected();

            // Generate the level card with new features
            cardImage = LevelCardDrawer.drawLevelCard(
                useUrl ? null : avatarBytes,
                useUrl ? avatarUrl : null,
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
            );

            // Display the card
            JLabel cardLabel = new JLabel(new ImageIcon(cardImage));
            cardPreviewPanel.removeAll();
            cardPreviewPanel.add(cardLabel, BorderLayout.CENTER);
            cardPreviewPanel.revalidate();
            cardPreviewPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error generating level card: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void saveLevelCard(ActionEvent e) {
        if (cardImage == null) {
            JOptionPane.showMessageDialog(this, 
                "Please generate a level card first", 
                "No Card Generated", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Level Card");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
        fileChooser.setSelectedFile(new File("levelcard.png"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Add .png extension if not present
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

            try {
                ImageIO.write(cardImage, "png", file);
                JOptionPane.showMessageDialog(this, 
                    "Level card saved successfully to: " + file.getAbsolutePath(), 
                    "Save Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving level card: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the application
        SwingUtilities.invokeLater(LevelCardApp::new);
    }
}
