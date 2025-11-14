/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import controller.StockController;
import model.StockModel;
import view.MainFrame;
import javax.swing.*;
// Modern Look and Feel using built-in Nimbus
import java.awt.*;

/**
 * Classe principale - Point d'entrée de l'application
 * Enhanced with modern UI/UX and professional dashboard capabilities
 * 
 * @author DAOUD Mohamed Amine
 * @course Systèmes d'aide à la décision (SAD)
 * @level L3 ISIL
 * @university Université Ibn Khaldoun - Tiaret
 * @year 2024/2025
 */
public class Main {
    public static void main(String[] args) {
        // Configure system properties for better rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        System.setProperty("sun.java2d.d3d", "false");
        
        // Set modern Look and Feel with Nimbus (built-in)
        try {
            // Use Nimbus for modern appearance
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    
                    // Configure Nimbus for dark theme
                    UIManager.put("control", new Color(45, 45, 45));
                    UIManager.put("info", new Color(45, 45, 45));
                    UIManager.put("nimbusBase", new Color(30, 30, 30));
                    UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
                    UIManager.put("nimbusDisabledText", new Color(120, 120, 120));
                    UIManager.put("nimbusFocus", new Color(100, 181, 246));
                    UIManager.put("nimbusGreen", new Color(176, 179, 50));
                    UIManager.put("nimbusInfoBlue", new Color(100, 181, 246));
                    UIManager.put("nimbusLightBackground", new Color(45, 45, 45));
                    UIManager.put("nimbusOrange", new Color(191, 98, 4));
                    UIManager.put("nimbusRed", new Color(169, 46, 34));
                    UIManager.put("nimbusSelectedText", Color.WHITE);
                    UIManager.put("nimbusSelectionBackground", new Color(100, 181, 246));
                    UIManager.put("text", Color.WHITE);
                    break;
                }
            }
            
        } catch (Exception e) {
            // Fallback to system Look and Feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Launch application with splash screen effect
        SwingUtilities.invokeLater(() -> {
            // Show splash screen
            showSplashScreen();
            
            // Create model
            StockModel model = new StockModel();
            
            // Create controller
            StockController controller = new StockController(model);
            
            // Create and show main frame
            MainFrame frame = new MainFrame(controller);
            
            // Center and show
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(400, 250);
        splash.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        
        // Title
        JLabel titleLabel = new JLabel("Système d'Aide à la Décision", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 20, 10, 20));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Gestion Intelligente des Stocks", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        
        // University info
        JLabel uniLabel = new JLabel("Université Ibn Khaldoun - Tiaret | L3 ISIL", JLabel.CENTER);
        uniLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        uniLabel.setForeground(new Color(140, 140, 140));
        uniLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Chargement...");
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(45, 45, 45));
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(uniLabel, BorderLayout.SOUTH);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        splash.add(panel);
        splash.setVisible(true);
        
        // Show for 2 seconds
        Timer timer = new Timer(2000, e -> splash.dispose());
        timer.setRepeats(false);
        timer.start();
        
        try {
            Thread.sleep(2100); // Wait for splash to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}