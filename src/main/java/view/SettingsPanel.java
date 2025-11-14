package view;

import controller.StockController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

/**
 * Panneau de configuration du système DSS
 * Permet de configurer les paramètres globaux et les préférences utilisateur
 */
public class SettingsPanel extends JPanel {
    private StockController controller;
    private Preferences prefs;
    
    // Composants d'interface
    private JSpinner coutCommandeDefautSpinner;
    private JSpinner seuilAlerteSpinner;
    private JSpinner stockSecuriteSpinner;
    private JCheckBox alertesActivesCheckBox;
    private JCheckBox calculAutoEOQCheckBox;
    private JCheckBox sauvegardeAutoCheckBox;
    private JComboBox<String> deviseComboBox;
    private JSlider margeSecuriteSlider;
    private JTextArea rapportArea;

    public SettingsPanel(StockController controller) {
        this.controller = controller;
        this.prefs = Preferences.userNodeForPackage(SettingsPanel.class);
        
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadSettings();
    }

    private void initComponents() {
        // Panneau principal avec onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(45, 45, 45));
        tabbedPane.setForeground(Color.WHITE);
        
        // Onglet 1: Paramètres généraux
        tabbedPane.addTab("Général", createGeneralPanel());
        
        // Onglet 2: Calculs et algorithmes
        tabbedPane.addTab("Calculs", createCalculsPanel());
        
        // Onglet 3: Interface et préférences
        tabbedPane.addTab("Interface", createInterfacePanel());
        
        // Onglet 4: Rapports et export
        tabbedPane.addTab("Rapports", createRapportsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panneau de boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(30, 30, 30));
        
        JButton saveBtn = new JButton("Sauvegarder");
        saveBtn.setPreferredSize(new Dimension(120, 35));
        saveBtn.addActionListener(e -> saveSettings());
        
        JButton resetBtn = new JButton("Réinitialiser");
        resetBtn.setPreferredSize(new Dimension(120, 35));
        resetBtn.addActionListener(e -> resetSettings());
        
        JButton exportBtn = new JButton("Exporter Config");
        exportBtn.setPreferredSize(new Dimension(140, 35));
        exportBtn.addActionListener(e -> exportSettings());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(exportBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createGeneralPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Devise
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Devise par défaut:"), gbc);
        gbc.gridx = 1;
        deviseComboBox = new JComboBox<>(new String[]{"DA (Dinar Algérien)", "EUR (Euro)", "USD (Dollar)"});
        deviseComboBox.setPreferredSize(new Dimension(200, 25));
        panel.add(deviseComboBox, gbc);
        
        // Coût de commande par défaut
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createLabel("Coût de commande par défaut:"), gbc);
        gbc.gridx = 1;
        coutCommandeDefautSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 10.0));
        coutCommandeDefautSpinner.setPreferredSize(new Dimension(100, 25));
        panel.add(coutCommandeDefautSpinner, gbc);
        
        // Seuil d'alerte global
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createLabel("Seuil d'alerte global (%):"), gbc);
        gbc.gridx = 1;
        seuilAlerteSpinner = new JSpinner(new SpinnerNumberModel(20, 5, 50, 5));
        seuilAlerteSpinner.setPreferredSize(new Dimension(100, 25));
        panel.add(seuilAlerteSpinner, gbc);
        
        // Stock de sécurité par défaut
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(createLabel("Stock de sécurité (jours):"), gbc);
        gbc.gridx = 1;
        stockSecuriteSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));
        stockSecuriteSpinner.setPreferredSize(new Dimension(100, 25));
        panel.add(stockSecuriteSpinner, gbc);
        
        // Marge de sécurité
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(createLabel("Marge de sécurité (%):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        margeSecuriteSlider = new JSlider(0, 50, 15);
        margeSecuriteSlider.setMajorTickSpacing(10);
        margeSecuriteSlider.setMinorTickSpacing(5);
        margeSecuriteSlider.setPaintTicks(true);
        margeSecuriteSlider.setPaintLabels(true);
        margeSecuriteSlider.setBackground(new Color(45, 45, 45));
        margeSecuriteSlider.setForeground(Color.WHITE);
        panel.add(margeSecuriteSlider, gbc);
        
        return panel;
    }

    private JPanel createCalculsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Cases à cocher pour les fonctionnalités
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        calculAutoEOQCheckBox = new JCheckBox("Calcul automatique de l'EOQ");
        calculAutoEOQCheckBox.setBackground(new Color(45, 45, 45));
        calculAutoEOQCheckBox.setForeground(Color.WHITE);
        calculAutoEOQCheckBox.setSelected(true);
        panel.add(calculAutoEOQCheckBox, gbc);
        
        gbc.gridy = 1;
        alertesActivesCheckBox = new JCheckBox("Alertes automatiques activées");
        alertesActivesCheckBox.setBackground(new Color(45, 45, 45));
        alertesActivesCheckBox.setForeground(Color.WHITE);
        alertesActivesCheckBox.setSelected(true);
        panel.add(alertesActivesCheckBox, gbc);
        
        gbc.gridy = 2;
        sauvegardeAutoCheckBox = new JCheckBox("Sauvegarde automatique des données");
        sauvegardeAutoCheckBox.setBackground(new Color(45, 45, 45));
        sauvegardeAutoCheckBox.setForeground(Color.WHITE);
        sauvegardeAutoCheckBox.setSelected(false);
        panel.add(sauvegardeAutoCheckBox, gbc);
        
        // Informations sur les formules
        gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel formulesPanel = new JPanel(new BorderLayout());
        formulesPanel.setBackground(new Color(60, 60, 60));
        formulesPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Formules utilisées",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), Color.WHITE));
        
        JTextArea formulesArea = new JTextArea(6, 40);
        formulesArea.setEditable(false);
        formulesArea.setBackground(new Color(60, 60, 60));
        formulesArea.setForeground(Color.WHITE);
        formulesArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        formulesArea.setText(
            "Formules DSS utilisées:\n\n" +
            "• EOQ = √(2 × D × S / H)\n" +
            "  D = Demande annuelle, S = Coût de commande, H = Coût de stockage\n\n" +
            "• Point de commande = (D × L) + Stock de sécurité\n" +
            "  L = Délai de livraison\n\n" +
            "• Coût total = (D/Q × S) + (Q/2 × H)\n" +
            "  Q = Quantité commandée"
        );
        
        JScrollPane scrollPane = new JScrollPane(formulesArea);
        scrollPane.setPreferredSize(new Dimension(400, 120));
        formulesPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(formulesPanel, gbc);
        
        return panel;
    }

    private JPanel createInterfacePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Message d'information
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel infoLabel = createLabel("Configuration de l'interface utilisateur");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(infoLabel, gbc);
        
        gbc.gridy = 1;
        JTextArea infoText = new JTextArea(8, 50);
        infoText.setEditable(false);
        infoText.setBackground(new Color(60, 60, 60));
        infoText.setForeground(Color.WHITE);
        infoText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoText.setText(
            "Interface Configuration:\n\n" +
            "• Thème: Mode sombre professionnel activé\n" +
            "• Navigation: Barre latérale avec icônes\n" +
            "• Graphiques: Moteur de rendu personnalisé\n" +
            "• Tableaux: Tri et filtrage automatiques\n" +
            "• Notifications: Alertes en temps réel\n\n" +
            "L'interface utilise les principes du Material Design\n" +
            "pour une expérience utilisateur optimale.\n\n" +
            "Couleurs principales:\n" +
            "- Bleu: #64B5F6 (Actions principales)\n" +
            "- Rouge: #F44336 (Alertes critiques)\n" +
            "- Vert: #4CAF50 (États positifs)\n" +
            "- Orange: #FF9800 (Avertissements)"
        );
        
        panel.add(infoText, gbc);
        
        return panel;
    }

    private JPanel createRapportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Titre
        JLabel titleLabel = createLabel("Génération de Rapports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Zone de rapport
        rapportArea = new JTextArea();
        rapportArea.setEditable(false);
        rapportArea.setBackground(new Color(60, 60, 60));
        rapportArea.setForeground(Color.WHITE);
        rapportArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        rapportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(rapportArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Boutons de rapport
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(45, 45, 45));
        
        JButton rapportSystemeBtn = new JButton("Rapport Système");
        rapportSystemeBtn.setPreferredSize(new Dimension(140, 35));
        rapportSystemeBtn.addActionListener(e -> generateSystemReport());
        
        JButton rapportStockBtn = new JButton("Rapport Stock");
        rapportStockBtn.setPreferredSize(new Dimension(140, 35));
        rapportStockBtn.addActionListener(e -> generateStockReport());
        
        JButton clearBtn = new JButton("Effacer");
        clearBtn.setPreferredSize(new Dimension(100, 35));
        clearBtn.addActionListener(e -> rapportArea.setText(""));
        
        buttonPanel.add(rapportSystemeBtn);
        buttonPanel.add(rapportStockBtn);
        buttonPanel.add(clearBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }

    private void loadSettings() {
        // Charger les paramètres sauvegardés
        coutCommandeDefautSpinner.setValue(prefs.getDouble("coutCommande", 100.0));
        seuilAlerteSpinner.setValue(prefs.getInt("seuilAlerte", 20));
        stockSecuriteSpinner.setValue(prefs.getInt("stockSecurite", 7));
        margeSecuriteSlider.setValue(prefs.getInt("margeSecurite", 15));
        alertesActivesCheckBox.setSelected(prefs.getBoolean("alertesActives", true));
        calculAutoEOQCheckBox.setSelected(prefs.getBoolean("calculAutoEOQ", true));
        sauvegardeAutoCheckBox.setSelected(prefs.getBoolean("sauvegardeAuto", false));
        deviseComboBox.setSelectedIndex(prefs.getInt("devise", 0));
    }

    private void saveSettings() {
        // Sauvegarder les paramètres
        prefs.putDouble("coutCommande", (Double) coutCommandeDefautSpinner.getValue());
        prefs.putInt("seuilAlerte", (Integer) seuilAlerteSpinner.getValue());
        prefs.putInt("stockSecurite", (Integer) stockSecuriteSpinner.getValue());
        prefs.putInt("margeSecurite", margeSecuriteSlider.getValue());
        prefs.putBoolean("alertesActives", alertesActivesCheckBox.isSelected());
        prefs.putBoolean("calculAutoEOQ", calculAutoEOQCheckBox.isSelected());
        prefs.putBoolean("sauvegardeAuto", sauvegardeAutoCheckBox.isSelected());
        prefs.putInt("devise", deviseComboBox.getSelectedIndex());
        
        JOptionPane.showMessageDialog(this, "Paramètres sauvegardés avec succès!", 
                                      "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetSettings() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir réinitialiser tous les paramètres?", 
                "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Remettre les valeurs par défaut
            coutCommandeDefautSpinner.setValue(100.0);
            seuilAlerteSpinner.setValue(20);
            stockSecuriteSpinner.setValue(7);
            margeSecuriteSlider.setValue(15);
            alertesActivesCheckBox.setSelected(true);
            calculAutoEOQCheckBox.setSelected(true);
            sauvegardeAutoCheckBox.setSelected(false);
            deviseComboBox.setSelectedIndex(0);
            
            JOptionPane.showMessageDialog(this, "Paramètres réinitialisés!");
        }
    }

    private void exportSettings() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuration du Système DSS\n");
        sb.append("============================\n\n");
        sb.append("Paramètres généraux:\n");
        sb.append("- Devise: ").append(deviseComboBox.getSelectedItem()).append("\n");
        sb.append("- Coût commande: ").append(coutCommandeDefautSpinner.getValue()).append("\n");
        sb.append("- Seuil alerte: ").append(seuilAlerteSpinner.getValue()).append("%\n");
        sb.append("- Stock sécurité: ").append(stockSecuriteSpinner.getValue()).append(" jours\n");
        sb.append("- Marge sécurité: ").append(margeSecuriteSlider.getValue()).append("%\n\n");
        
        sb.append("Options activées:\n");
        sb.append("- Calcul auto EOQ: ").append(calculAutoEOQCheckBox.isSelected() ? "OUI" : "NON").append("\n");
        sb.append("- Alertes actives: ").append(alertesActivesCheckBox.isSelected() ? "OUI" : "NON").append("\n");
        sb.append("- Sauvegarde auto: ").append(sauvegardeAutoCheckBox.isSelected() ? "OUI" : "NON").append("\n");
        
        rapportArea.setText(sb.toString());
        JOptionPane.showMessageDialog(this, "Configuration exportée dans l'onglet Rapports");
    }

    private void generateSystemReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPPORT SYSTÈME DSS\n");
        sb.append("===================\n\n");
        sb.append("Date: ").append(new java.util.Date()).append("\n\n");
        
        sb.append("Informations système:\n");
        sb.append("- Version Java: ").append(System.getProperty("java.version")).append("\n");
        sb.append("- OS: ").append(System.getProperty("os.name")).append("\n");
        sb.append("- Mémoire libre: ").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append(" MB\n");
        sb.append("- Mémoire totale: ").append(Runtime.getRuntime().totalMemory() / 1024 / 1024).append(" MB\n\n");
        
        sb.append("Configuration DSS:\n");
        sb.append("- Nombre de produits: ").append(controller.getProduits().size()).append("\n");
        sb.append("- Alertes actives: ").append(alertesActivesCheckBox.isSelected() ? "OUI" : "NON").append("\n");
        sb.append("- Calcul EOQ: ").append(calculAutoEOQCheckBox.isSelected() ? "ACTIVÉ" : "DÉSACTIVÉ").append("\n");
        
        rapportArea.setText(sb.toString());
    }

    private void generateStockReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPPORT D'INVENTAIRE\n");
        sb.append("====================\n\n");
        sb.append("Date: ").append(new java.util.Date()).append("\n\n");
        
        var produits = controller.getProduits();
        if (produits.isEmpty()) {
            sb.append("Aucun produit en stock.\n");
        } else {
            int totalProduits = produits.size();
            int alertes = 0;
            double valeurTotale = 0;
            
            for (var p : produits) {
                if (p.getQuantite() <= p.getSeuil()) {
                    alertes++;
                }
                valeurTotale += p.getQuantite() * p.getPrixachat();
            }
            
            sb.append("Statistiques générales:\n");
            sb.append("- Total produits: ").append(totalProduits).append("\n");
            sb.append("- Produits en alerte: ").append(alertes).append("\n");
            sb.append("- Valeur totale stock: ").append(String.format("%.2f", valeurTotale)).append(" DA\n\n");
            
            sb.append("Détail des alertes:\n");
            if (alertes == 0) {
                sb.append("Aucune alerte active.\n");
            } else {
                for (var p : produits) {
                    if (p.getQuantite() <= p.getSeuil()) {
                        sb.append("- ").append(p.getNom()).append(": ").append(p.getQuantite())
                          .append("/").append((int)p.getSeuil()).append(" unités\n");
                    }
                }
            }
        }
        
        rapportArea.setText(sb.toString());
    }

    // Getters pour les paramètres
    public double getCoutCommandeDefaut() {
        return (Double) coutCommandeDefautSpinner.getValue();
    }

    public int getSeuilAlerte() {
        return (Integer) seuilAlerteSpinner.getValue();
    }

    public boolean isAlertesActives() {
        return alertesActivesCheckBox.isSelected();
    }

    public boolean isCalculAutoEOQ() {
        return calculAutoEOQCheckBox.isSelected();
    }
}