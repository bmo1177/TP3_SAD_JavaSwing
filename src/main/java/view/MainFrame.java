/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.StockController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
// Using built-in Look and Feel features

/**
 * Enhanced main frame with professional dashboard design
 * Features modern UI/UX with sidebar navigation and real-time updates
 */
public class MainFrame extends JFrame {
    private StockController controller;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JLabel timeLabel;
    private JButton dashboardBtn, productsBtn, simulationBtn, chartsBtn, settingsBtn;
    private ProductPanel productPanel;
    private SimulationPanel simulationPanel;
    private ChartPanel chartPanel;
    private DashboardPanel dashboardPanel;
    private SettingsPanel settingsPanel;
    private Timer clockTimer;
    private boolean isDarkTheme = true;

    public MainFrame(StockController controller) {
        this.controller = controller;
        
        setTitle("DSS - Système d'Aide à la Décision | Gestion Intelligente des Stocks");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Set application icon
        setIconImage(createAppIcon());

        initComponents();
        setupClock();
        showDashboard(); // Start with dashboard view
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Create sidebar navigation
        createSidebar();
        
        // Create main content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(30, 30, 30));
        
        // Create header bar
        createHeaderBar();
        
        // Initialize panels
        dashboardPanel = new DashboardPanel(controller);
        productPanel = new ProductPanel(controller);
        simulationPanel = new SimulationPanel(controller);
        chartPanel = new ChartPanel(controller);
        settingsPanel = new SettingsPanel(controller);
        
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));
        sidebarPanel.setBackground(new Color(25, 25, 25));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 60, 60)));
        
        // Logo and title area
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(35, 35, 35));
        logoPanel.setPreferredSize(new Dimension(250, 80));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel logoLabel = new JLabel("DSS Stock Manager");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(new Color(100, 181, 246));
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Navigation buttons
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(25, 25, 25));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Create navigation buttons
        dashboardBtn = createNavButton("Dashboard", true);
        productsBtn = createNavButton("Inventaire", false);
        simulationBtn = createNavButton("Simulation", false);
        chartsBtn = createNavButton("Analytics", false);
        settingsBtn = createNavButton("Parametres", false);
        
        // Add button listeners
        dashboardBtn.addActionListener(e -> showDashboard());
        productsBtn.addActionListener(e -> showProducts());
        simulationBtn.addActionListener(e -> showSimulation());
        chartsBtn.addActionListener(e -> showCharts());
        settingsBtn.addActionListener(e -> showSettings());
        
        navPanel.add(dashboardBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        navPanel.add(productsBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        navPanel.add(simulationBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        navPanel.add(chartsBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        navPanel.add(settingsBtn);
        navPanel.add(Box.createVerticalGlue());
        
        // University info at bottom
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(25, 25, 25));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JLabel uniLabel = new JLabel("<html><center>Université Ibn Khaldoun<br>Tiaret - L3 ISIL<br>Module: SAD</center></html>");
        uniLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        uniLabel.setForeground(new Color(120, 120, 120));
        uniLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(uniLabel, BorderLayout.CENTER);
        
        sidebarPanel.add(logoPanel, BorderLayout.NORTH);
        sidebarPanel.add(navPanel, BorderLayout.CENTER);
        sidebarPanel.add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createNavButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(230, 45));
        button.setMaximumSize(new Dimension(230, 45));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        
        if (selected) {
            button.setBackground(new Color(100, 181, 246, 40));
            button.setForeground(new Color(100, 181, 246));
        } else {
            button.setBackground(new Color(25, 25, 25));
            button.setForeground(new Color(180, 180, 180));
        }
        
        return button;
    }
    
    private void createHeaderBar() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(35, 35, 35));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));
        
        // Page title (will be updated based on current view)
        JLabel titleLabel = new JLabel("Dashboard - Vue d'ensemble");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        
        // Header controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setBackground(new Color(35, 35, 35));
        
        // Theme toggle button
        JButton themeBtn = new JButton(isDarkTheme ? "Light" : "Dark");
        themeBtn.setToolTipText("Changer le thème");
        themeBtn.setPreferredSize(new Dimension(60, 35));
        themeBtn.setFocusPainted(false);
        themeBtn.addActionListener(e -> toggleTheme());
        
        // Refresh button
        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.setToolTipText("Actualiser les données");
        refreshBtn.setPreferredSize(new Dimension(80, 35));
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshAllPanels());
        
        // Time display
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(180, 180, 180));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        controlsPanel.add(themeBtn);
        controlsPanel.add(refreshBtn);
        controlsPanel.add(timeLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void setupClock() {
        clockTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                timeLabel.setText(sdf.format(new Date()));
            }
        });
        clockTimer.start();
        
        // Initial time display
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        timeLabel.setText(sdf.format(new Date()));
    }
    
    private void showDashboard() {
        setActiveButton(dashboardBtn);
        updateTitle("Dashboard - Vue d'ensemble");
        showPanel(dashboardPanel);
    }
    
    private void showProducts() {
        setActiveButton(productsBtn);
        updateTitle("Inventaire - Gestion des produits");
        showPanel(productPanel);
    }
    
    private void showSimulation() {
        setActiveButton(simulationBtn);
        updateTitle("Simulation - Analyse What-If");
        showPanel(simulationPanel);
    }
    
    private void showCharts() {
        setActiveButton(chartsBtn);
        updateTitle("Analytics - Visualisations avancées");
        showPanel(chartPanel);
    }
    
    private void showSettings() {
        setActiveButton(settingsBtn);
        updateTitle("Paramètres - Configuration système");
        showPanel(settingsPanel);
    }
    
    private void setActiveButton(JButton activeBtn) {
        // Reset all buttons with theme-appropriate colors
        JButton[] buttons = {dashboardBtn, productsBtn, simulationBtn, chartsBtn, settingsBtn};
        Color inactiveBg = isDarkTheme ? new Color(25, 25, 25) : new Color(248, 248, 248);
        Color inactiveText = isDarkTheme ? new Color(180, 180, 180) : new Color(80, 80, 80);
        Color activeBg = isDarkTheme ? new Color(100, 181, 246, 40) : new Color(33, 150, 243, 60);
        Color activeText = isDarkTheme ? new Color(100, 181, 246) : new Color(33, 150, 243);
        
        for (JButton btn : buttons) {
            btn.setBackground(inactiveBg);
            btn.setForeground(inactiveText);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        }
        
        // Set active button with theme-appropriate colors
        activeBtn.setBackground(activeBg);
        activeBtn.setForeground(activeText);
    }
    
    private void updateTitle(String title) {
        Component[] components = ((JPanel) contentPanel.getComponent(0)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel) components[0]).setText(title);
        }
    }
    
    private void showPanel(JPanel panel) {
        // Remove current panel if exists
        if (contentPanel.getComponentCount() > 1) {
            contentPanel.remove(1);
        }
        
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void toggleTheme() {
        try {
            if (isDarkTheme) {
                // Switch to light theme
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                isDarkTheme = false;
                applyLightTheme();
            } else {
                // Switch back to dark theme
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        isDarkTheme = true;
                        applyDarkTheme();
                        break;
                    }
                }
            }
            
            SwingUtilities.updateComponentTreeUI(this);
            updateThemeButton();
            updateCustomPanelThemes();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void applyDarkTheme() {
        // Configure Nimbus for dark theme
        UIManager.put("control", new Color(45, 45, 45));
        UIManager.put("info", new Color(45, 45, 45));
        UIManager.put("nimbusBase", new Color(30, 30, 30));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(120, 120, 120));
        UIManager.put("nimbusFocus", new Color(100, 181, 246));
        UIManager.put("nimbusGreen", new Color(76, 175, 80));
        UIManager.put("nimbusInfoBlue", new Color(100, 181, 246));
        UIManager.put("nimbusLightBackground", new Color(45, 45, 45));
        UIManager.put("nimbusOrange", new Color(255, 152, 0));
        UIManager.put("nimbusRed", new Color(244, 67, 54));
        UIManager.put("nimbusSelectedText", Color.WHITE);
        UIManager.put("nimbusSelectionBackground", new Color(100, 181, 246));
        UIManager.put("text", Color.WHITE);
        UIManager.put("Table.background", new Color(45, 45, 45));
        UIManager.put("Table.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(45, 45, 45));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextArea.background", new Color(45, 45, 45));
        UIManager.put("TextArea.foreground", Color.WHITE);
        
        // Apply dark theme to custom panels
        applyDarkCustomColors();
    }
    
    private void applyLightTheme() {
        // Configure Nimbus for light theme with custom values
        UIManager.put("control", new Color(248, 248, 248));
        UIManager.put("info", Color.WHITE);
        UIManager.put("nimbusBase", new Color(220, 220, 220));
        UIManager.put("nimbusAlertYellow", new Color(255, 235, 59));
        UIManager.put("nimbusDisabledText", new Color(142, 142, 142));
        UIManager.put("nimbusFocus", new Color(33, 150, 243));
        UIManager.put("nimbusGreen", new Color(76, 175, 80));
        UIManager.put("nimbusInfoBlue", new Color(33, 150, 243));
        UIManager.put("nimbusLightBackground", Color.WHITE);
        UIManager.put("nimbusOrange", new Color(255, 152, 0));
        UIManager.put("nimbusRed", new Color(244, 67, 54));
        UIManager.put("nimbusSelectedText", Color.BLACK);
        UIManager.put("nimbusSelectionBackground", new Color(33, 150, 243));
        UIManager.put("text", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("TextArea.foreground", Color.BLACK);
        
        // Apply light theme to custom panels
        applyLightCustomColors();
    }
    
    private void applyDarkCustomColors() {
        // Sidebar dark theme
        sidebarPanel.setBackground(new Color(25, 25, 25));
        updateSidebarComponents(new Color(25, 25, 25), new Color(35, 35, 35), 
                               new Color(100, 181, 246), new Color(120, 120, 120));
        
        // Content area dark theme
        contentPanel.setBackground(new Color(30, 30, 30));
        updateHeaderColors(new Color(35, 35, 35), Color.WHITE);
        
        // Update all child panels
        updatePanelBackgrounds(true);
    }
    
    private void applyLightCustomColors() {
        // Sidebar light theme
        sidebarPanel.setBackground(new Color(248, 248, 248));
        updateSidebarComponents(new Color(248, 248, 248), new Color(240, 240, 240), 
                               new Color(33, 150, 243), new Color(100, 100, 100));
        
        // Content area light theme
        contentPanel.setBackground(Color.WHITE);
        updateHeaderColors(new Color(250, 250, 250), Color.BLACK);
        
        // Update all child panels
        updatePanelBackgrounds(false);
    }
    
    private void updateSidebarComponents(Color sidebarBg, Color logoBg, Color logoText, Color uniText) {
        sidebarPanel.setBackground(sidebarBg);
        
        // Update logo panel
        Component[] sidebarComponents = sidebarPanel.getComponents();
        for (Component comp : sidebarComponents) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JLabel) {
                    // This is likely the logo panel
                    panel.setBackground(logoBg);
                    ((JLabel) panel.getComponent(0)).setForeground(logoText);
                } else {
                    // Other panels (navigation, footer)
                    panel.setBackground(sidebarBg);
                    updateSidebarPanelComponents(panel, uniText);
                }
            }
        }
    }
    
    private void updateSidebarPanelComponents(JPanel panel, Color uniTextColor) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().contains("Université")) {
                    label.setForeground(uniTextColor);
                }
            } else if (comp instanceof JPanel) {
                ((JPanel) comp).setBackground(panel.getBackground());
                updateSidebarPanelComponents((JPanel) comp, uniTextColor);
            }
        }
    }
    
    private void updatePanelBackgrounds(boolean isDark) {
        Color bgColor = isDark ? new Color(30, 30, 30) : Color.WHITE;
        Color cardColor = isDark ? new Color(45, 45, 45) : new Color(248, 248, 248);
        
        // Update dashboard panel if it exists
        if (dashboardPanel != null) {
            updateDashboardColors(dashboardPanel, bgColor, cardColor, isDark);
        }
        
        // Update other panels
        if (productPanel != null) {
            updateGenericPanelColors(productPanel, bgColor, isDark);
        }
        
        if (simulationPanel != null) {
            updateGenericPanelColors(simulationPanel, bgColor, isDark);
        }
        
        if (chartPanel != null) {
            updateGenericPanelColors(chartPanel, bgColor, isDark);
        }
        
        if (settingsPanel != null) {
            updateGenericPanelColors(settingsPanel, bgColor, isDark);
        }
    }
    
    private void updateDashboardColors(JPanel panel, Color bgColor, Color cardColor, boolean isDark) {
        panel.setBackground(bgColor);
        updatePanelColorsRecursive(panel, bgColor, cardColor, isDark);
    }
    
    private void updateGenericPanelColors(JPanel panel, Color bgColor, boolean isDark) {
        panel.setBackground(bgColor);
        Color cardColor = isDark ? new Color(45, 45, 45) : new Color(248, 248, 248);
        updatePanelColorsRecursive(panel, bgColor, cardColor, isDark);
    }
    
    private void updatePanelColorsRecursive(Container container, Color bgColor, Color cardColor, boolean isDark) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Only update background if it's one of our custom colors
                Color currentBg = panel.getBackground();
                if (isDark && (currentBg.equals(Color.WHITE) || currentBg.equals(new Color(248, 248, 248)) || 
                              currentBg.equals(new Color(240, 240, 240)))) {
                    panel.setBackground(bgColor);
                } else if (!isDark && (currentBg.equals(new Color(30, 30, 30)) || 
                          currentBg.equals(new Color(45, 45, 45)) || currentBg.equals(new Color(25, 25, 25)))) {
                    panel.setBackground(bgColor);
                }
                updatePanelColorsRecursive(panel, bgColor, cardColor, isDark);
            } else if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                scrollPane.setBackground(bgColor);
                scrollPane.getViewport().setBackground(bgColor);
                if (scrollPane.getViewport().getView() instanceof JComponent) {
                    ((JComponent) scrollPane.getViewport().getView()).setBackground(bgColor);
                }
            }
        }
    }
    
    private void updateHeaderColors(Color headerBg, Color textColor) {
        Component headerPanel = contentPanel.getComponent(0);
        if (headerPanel instanceof JPanel) {
            ((JPanel) headerPanel).setBackground(headerBg);
            updatePanelTextColors((JPanel) headerPanel, textColor);
        }
    }
    
    private void updatePanelTextColors(JPanel panel, Color textColor) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(textColor);
            } else if (comp instanceof JPanel) {
                updatePanelTextColors((JPanel) comp, textColor);
            }
        }
    }
    
    private void updateThemeButton() {
        // Find and update the theme button text
        Component headerPanel = contentPanel.getComponent(0);
        if (headerPanel instanceof JPanel) {
            updateButtonText((JPanel) headerPanel);
        }
    }
    
    private void updateButtonText(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                updateButtonText((JPanel) comp);
            } else if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("Light") || btn.getText().equals("Dark")) {
                    btn.setText(isDarkTheme ? "Light" : "Dark");
                }
            }
        }
    }
    
    private void updateCustomPanelThemes() {
        // Update navigation buttons
        updateNavButtonThemes();
        
        // Force repaint and revalidate all panels for smooth transition
        SwingUtilities.invokeLater(() -> {
            // Update and repaint all panels
            updatePanelThemeColors();
            
            // Revalidate and repaint the entire frame
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
            contentPanel.revalidate();
            contentPanel.repaint();
            
            if (dashboardPanel != null) {
                dashboardPanel.revalidate();
                dashboardPanel.repaint();
            }
            if (productPanel != null) {
                productPanel.revalidate();
                productPanel.repaint();
            }
            if (simulationPanel != null) {
                simulationPanel.revalidate();
                simulationPanel.repaint();
            }
            if (chartPanel != null) {
                chartPanel.revalidate();
                chartPanel.repaint();
            }
            if (settingsPanel != null) {
                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
            
            // Final frame update
            revalidate();
            repaint();
        });
    }
    
    private void updatePanelThemeColors() {
        // Create theme-appropriate colors
        ThemeColors colors = isDarkTheme ? createDarkThemeColors() : createLightThemeColors();
        
        // Apply to all panels
        applyThemeToPanel(dashboardPanel, colors);
        applyThemeToPanel(productPanel, colors);
        applyThemeToPanel(simulationPanel, colors);
        applyThemeToPanel(chartPanel, colors);
        applyThemeToPanel(settingsPanel, colors);
    }
    
    private static class ThemeColors {
        Color background;
        Color cardBackground;
        Color textPrimary;
        Color textSecondary;
        Color accent;
        Color border;
        
        ThemeColors(Color bg, Color cardBg, Color textPri, Color textSec, Color acc, Color bor) {
            background = bg;
            cardBackground = cardBg;
            textPrimary = textPri;
            textSecondary = textSec;
            accent = acc;
            border = bor;
        }
    }
    
    private ThemeColors createDarkThemeColors() {
        return new ThemeColors(
            new Color(30, 30, 30),      // background
            new Color(45, 45, 45),      // cardBackground
            Color.WHITE,                 // textPrimary
            new Color(180, 180, 180),   // textSecondary
            new Color(100, 181, 246),   // accent
            new Color(60, 60, 60)       // border
        );
    }
    
    private ThemeColors createLightThemeColors() {
        return new ThemeColors(
            Color.WHITE,                 // background
            new Color(248, 248, 248),   // cardBackground
            Color.BLACK,                 // textPrimary
            new Color(80, 80, 80),      // textSecondary
            new Color(33, 150, 243),    // accent
            new Color(200, 200, 200)    // border
        );
    }
    
    private void applyThemeToPanel(JPanel panel, ThemeColors colors) {
        if (panel == null) return;
        
        // Apply background color
        panel.setBackground(colors.background);
        
        // Recursively apply theme to child components
        applyThemeRecursively(panel, colors);
    }
    
    private void applyThemeRecursively(Container container, ThemeColors colors) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Color currentBg = panel.getBackground();
                
                // Update background based on current color
                if (shouldUpdateBackground(currentBg)) {
                    if (isCardPanel(currentBg)) {
                        panel.setBackground(colors.cardBackground);
                    } else {
                        panel.setBackground(colors.background);
                    }
                }
                
                applyThemeRecursively(panel, colors);
            } else if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                scrollPane.setBackground(colors.background);
                scrollPane.getViewport().setBackground(colors.background);
                
                if (scrollPane.getViewport().getView() instanceof JComponent) {
                    JComponent viewComponent = (JComponent) scrollPane.getViewport().getView();
                    viewComponent.setBackground(colors.background);
                }
            } else if (comp instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) comp;
                tabbedPane.setBackground(colors.cardBackground);
                tabbedPane.setForeground(colors.textPrimary);
                
                // Update tab content
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    Component tabComponent = tabbedPane.getComponentAt(i);
                    if (tabComponent instanceof Container) {
                        applyThemeRecursively((Container) tabComponent, colors);
                    }
                }
            }
        }
    }
    
    private boolean shouldUpdateBackground(Color currentBg) {
        // Check if this is one of our theme colors that should be updated
        return currentBg.equals(new Color(30, 30, 30)) ||     // Dark background
               currentBg.equals(new Color(45, 45, 45)) ||     // Dark card
               currentBg.equals(new Color(25, 25, 25)) ||     // Dark sidebar
               currentBg.equals(Color.WHITE) ||               // Light background
               currentBg.equals(new Color(248, 248, 248)) ||  // Light card
               currentBg.equals(new Color(240, 240, 240));    // Light sidebar
    }
    
    private boolean isCardPanel(Color currentBg) {
        // Determine if this should be a card (elevated) background
        return currentBg.equals(new Color(45, 45, 45)) ||     // Dark card
               currentBg.equals(new Color(248, 248, 248)) ||  // Light card
               currentBg.equals(new Color(35, 35, 35)) ||     // Dark header
               currentBg.equals(new Color(250, 250, 250));    // Light header
    }
    
    private void updateNavButtonThemes() {
        JButton[] buttons = {dashboardBtn, productsBtn, simulationBtn, chartsBtn, settingsBtn};
        Color bgColor = isDarkTheme ? new Color(25, 25, 25) : new Color(248, 248, 248);
        Color textColor = isDarkTheme ? new Color(180, 180, 180) : new Color(80, 80, 80);
        Color activeBg = isDarkTheme ? new Color(100, 181, 246, 40) : new Color(33, 150, 243, 60);
        Color activeText = isDarkTheme ? new Color(100, 181, 246) : new Color(33, 150, 243);
        
        // Track which button is currently active
        JButton activeButton = null;
        for (JButton btn : buttons) {
            Color btnBg = btn.getBackground();
            if (btnBg.equals(new Color(100, 181, 246, 40)) || 
                btnBg.equals(new Color(33, 150, 243, 60)) ||
                btnBg.equals(new Color(100, 181, 246, 60))) {
                activeButton = btn;
                break;
            }
        }
        
        // Update all buttons
        for (JButton btn : buttons) {
            if (btn == activeButton) {
                btn.setBackground(activeBg);
                btn.setForeground(activeText);
            } else {
                btn.setBackground(bgColor);
                btn.setForeground(textColor);
            }
            
            // Ensure proper styling
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        }
        
        // Update university label color
        updateUniversityLabelTheme();
    }
    
    private void updateUniversityLabelTheme() {
        Color uniColor = isDarkTheme ? new Color(120, 120, 120) : new Color(100, 100, 100);
        Component[] sidebarComponents = sidebarPanel.getComponents();
        for (Component comp : sidebarComponents) {
            if (comp instanceof JPanel) {
                updateUniversityLabelInPanel((JPanel) comp, uniColor);
            }
        }
    }
    
    private void updateUniversityLabelInPanel(JPanel panel, Color color) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().contains("Université")) {
                ((JLabel) comp).setForeground(color);
            } else if (comp instanceof JPanel) {
                updateUniversityLabelInPanel((JPanel) comp, color);
            }
        }
    }
    
    private Image createAppIcon() {
        // Create a simple app icon
        int size = 32;
        java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = icon.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(new Color(100, 181, 246));
        g2.fillRoundRect(0, 0, size, size, 8, 8);
        
        // Icon content - DSS letters
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        String text = "DSS";
        int x = (size - fm.stringWidth(text)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, x, y);
        
        g2.dispose();
        return icon;
    }

    public void refreshAllPanels() {
        productPanel.refreshTable();
        simulationPanel.refreshProductList();
        chartPanel.updateChart();
        if (dashboardPanel != null) {
            dashboardPanel.updateDashboard();
        }
    }
    
    @Override
    public void dispose() {
        if (clockTimer != null) {
            clockTimer.stop();
        }
        super.dispose();
    }
}

