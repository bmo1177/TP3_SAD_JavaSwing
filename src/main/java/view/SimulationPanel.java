/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import controller.StockController;
import model.Produits;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panneau de simulation "What-If"
 * Permet de tester différents scénarios de demande et de coûts
 */
public class SimulationPanel extends JPanel {
    private StockController controller;
    private JComboBox<String> productCombo;
    private JSpinner demandeSpinner;
    private JSpinner prixAchatSpinner;
    private JSpinner prixStockSpinner;
    private JTextArea resultArea;

    public SimulationPanel(StockController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
    }

    private void initComponents() {
        // Titre
        JLabel titleLabel = new JLabel("Simulation What-If: Testez différents scénarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Panneau de paramètres
        JPanel paramsPanel = new JPanel(new GridBagLayout());
        paramsPanel.setBorder(BorderFactory.createTitledBorder("Paramètres de Simulation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Sélection du produit
        gbc.gridx = 0; gbc.gridy = 0;
        paramsPanel.add(new JLabel("Produit:"), gbc);
        gbc.gridx = 1;
        productCombo = new JComboBox<>();
        productCombo.setPreferredSize(new Dimension(250, 25));
        productCombo.addActionListener(e -> updateSpinners());
        paramsPanel.add(productCombo, gbc);

        // Demande simulée
        gbc.gridx = 0; gbc.gridy = 1;
        paramsPanel.add(new JLabel("Demande simulée:"), gbc);
        gbc.gridx = 1;
        demandeSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 1000, 1));
        demandeSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(demandeSpinner, gbc);

        // Prix d'achat simulé
        gbc.gridx = 0; gbc.gridy = 2;
        paramsPanel.add(new JLabel("Prix d'achat (DA):"), gbc);
        gbc.gridx = 1;
        prixAchatSpinner = new JSpinner(new SpinnerNumberModel(1000.0, 0.0, 100000.0, 100.0));
        prixAchatSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(prixAchatSpinner, gbc);

        // Coût de stockage simulé
        gbc.gridx = 0; gbc.gridy = 3;
        paramsPanel.add(new JLabel("Coût stockage (DA/unité):"), gbc);
        gbc.gridx = 1;
        prixStockSpinner = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 1000.0, 1.0));
        prixStockSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(prixStockSpinner, gbc);

        // Boutons de simulation
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton simulateBtn = new JButton("Lancer Simulation");
        simulateBtn.setPreferredSize(new Dimension(150, 35));
        simulateBtn.addActionListener(e -> runSimulation());
        
        JButton compareBtn = new JButton("Comparer Scenarios");
        compareBtn.setPreferredSize(new Dimension(150, 35));
        compareBtn.addActionListener(e -> compareScenarios());
        
        JButton resetBtn = new JButton("Réinitialiser");
        resetBtn.setPreferredSize(new Dimension(120, 35));
        resetBtn.addActionListener(e -> resetSimulation());
        
        buttonPanel.add(simulateBtn);
        buttonPanel.add(compareBtn);
        buttonPanel.add(resetBtn);
        paramsPanel.add(buttonPanel, gbc);

        add(paramsPanel, BorderLayout.WEST);

        // Zone de résultats
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Résultats de Simulation"));
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        add(resultPanel, BorderLayout.CENTER);

        refreshProductList();
    }

    public void refreshProductList() {
        productCombo.removeAllItems();
        List<Produits> produits = controller.getProduits();
        for (Produits p : produits) {
            productCombo.addItem(p.getNom());
        }
        if (produits.size() > 0) {
            updateSpinners();
        }
    }

    private void updateSpinners() {
        int index = productCombo.getSelectedIndex();
        if (index >= 0) {
            Produits p = controller.getProduit(index);
            if (p != null) {
                demandeSpinner.setValue(p.getDemandeEstimee());
                prixAchatSpinner.setValue(p.getPrixachat());
                prixStockSpinner.setValue(p.getPrixstock());
            }
        }
    }

    private void runSimulation() {
        int index = productCombo.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produits p = controller.getProduit(index);
        int demandeSim = (Integer) demandeSpinner.getValue();
        double prixAchatSim = (Double) prixAchatSpinner.getValue();
        double prixStockSim = (Double) prixStockSpinner.getValue();

        // Sauvegarder valeurs originales
        double oldPrixAchat = p.getPrixachat();
        double oldPrixStock = p.getPrixstock();

        // Appliquer valeurs simulées
        p.setPrixachat(prixAchatSim);
        p.setPrixstock(prixStockSim);

        // Obtenir recommandation
        Produits.Recommendation rec = p.recommanderReapprovisionnement(demandeSim);

        // Restaurer valeurs originales
        p.setPrixachat(oldPrixAchat);
        p.setPrixstock(oldPrixStock);

        // Afficher résultats
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════════════╗\n");
        sb.append("║          RÉSULTATS DE LA SIMULATION                    ║\n");
        sb.append("╚════════════════════════════════════════════════════════╝\n\n");
        sb.append("Produit: ").append(p.getNom()).append("\n");
        sb.append("Stock actuel: ").append(p.getQuantite()).append(" unités\n\n");
        
        sb.append("┌─ PARAMÈTRES DE SIMULATION ─────────────────────────┐\n");
        sb.append(String.format("│ Demande simulée:        %d unités\n", demandeSim));
        sb.append(String.format("│ Prix d'achat simulé:    %.2f DA\n", prixAchatSim));
        sb.append(String.format("│ Coût stockage simulé:   %.2f DA/unité\n", prixStockSim));
        sb.append("└────────────────────────────────────────────────────┘\n\n");
        
        sb.append("┌─ RECOMMANDATION ───────────────────────────────────┐\n");
        sb.append("│ ").append(rec.message.replace("\n", "\n│ ")).append("\n");
        sb.append("└────────────────────────────────────────────────────┘\n\n");
        
        sb.append("┌─ ANALYSE FINANCIÈRE ───────────────────────────────┐\n");
        double coutCommande = prixAchatSim * rec.reorderQty;
        double coutStockageTotal = prixStockSim * (p.getQuantite() + rec.reorderQty);
        double profitPotentiel = (p.getPrixvente() - prixAchatSim) * rec.sold;
        
        sb.append(String.format("│ Coût de commande:       %.2f DA\n", coutCommande));
        sb.append(String.format("│ Coût stockage après:    %.2f DA\n", coutStockageTotal));
        sb.append(String.format("│ Profit potentiel:       %.2f DA\n", profitPotentiel));
        sb.append(String.format("│ ROI estimé:             %.1f%%\n", 
            coutCommande > 0 ? (profitPotentiel / coutCommande * 100) : 0));
        sb.append("└────────────────────────────────────────────────────┘\n\n");
        
        sb.append("┌─ INDICATEURS CLÉS ─────────────────────────────────┐\n");
        sb.append(String.format("│ EOQ (Economic Order Qty): %d unités\n", rec.eoq));
        sb.append(String.format("│ Seuil auto calculé:       %d unités\n", rec.thresholdAuto));
        sb.append(String.format("│ Besoin réappro:           %s\n", rec.needReorder ? "OUI (ALERTE)" : "NON (OK)"));
        sb.append("└────────────────────────────────────────────────────┘\n");

        resultArea.setText(sb.toString());
    }
    
    private void compareScenarios() {
        int index = productCombo.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Produits p = controller.getProduit(index);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Produit non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Scenarios prédéfinis
        int[] demandes = {
            (int)(p.getDemandeEstimee() * 0.5),  // Demande faible (-50%)
            p.getDemandeEstimee(),               // Demande normale
            (int)(p.getDemandeEstimee() * 1.5),  // Demande élevée (+50%)
            (int)(p.getDemandeEstimee() * 2.0)   // Demande très élevée (+100%)
        };
        
        String[] scenarios = {"Demande Faible", "Demande Normale", "Demande Élevée", "Demande Très Élevée"};
        
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════════════╗\n");
        sb.append("║          COMPARAISON DE SCÉNARIOS                      ║\n");
        sb.append("╚════════════════════════════════════════════════════════╝\n\n");
        sb.append("Produit: ").append(p.getNom()).append("\n");
        sb.append("Stock actuel: ").append(p.getQuantite()).append(" unités\n\n");
        
        for (int i = 0; i < scenarios.length; i++) {
            Produits.Recommendation rec = p.recommanderReapprovisionnement(demandes[i]);
            
            sb.append("┌─ ").append(scenarios[i].toUpperCase()).append(" (").append(demandes[i]).append(" unités) ─────────────\n");
            sb.append(String.format("│ EOQ recommandé:     %d unités\n", rec.eoq));
            sb.append(String.format("│ Quantité commande:  %d unités\n", rec.reorderQty));
            sb.append(String.format("│ Coût commande:      %.2f DA\n", p.getPrixachat() * rec.reorderQty));
            sb.append(String.format("│ Profit potentiel:   %.2f DA\n", (p.getPrixvente() - p.getPrixachat()) * rec.sold));
            sb.append(String.format("│ Statut:             %s\n", rec.needReorder ? "RÉAPPRO NÉCESSAIRE" : "STOCK SUFFISANT"));
            sb.append("└─────────────────────────────────────────────────────\n\n");
        }
        
        // Recommandation globale
        sb.append("┌─ RECOMMANDATION STRATÉGIQUE ───────────────────────\n");
        sb.append("│ Conseil: Surveillez particulièrement les scénarios\n");
        sb.append("│ de demande élevée pour éviter les ruptures de stock.\n");
        sb.append("│ Considérez un stock de sécurité supplémentaire si\n");
        sb.append("│ la variabilité de la demande est importante.\n");
        sb.append("└─────────────────────────────────────────────────────\n");
        
        resultArea.setText(sb.toString());
    }
    
    private void resetSimulation() {
        int index = productCombo.getSelectedIndex();
        if (index >= 0) {
            updateSpinners();  // Remet les valeurs d'origine
        }
        
        resultArea.setText("Paramètres réinitialisés. Configurez vos paramètres et lancez une simulation.\n\n" +
                          "Fonctionnalités disponibles:\n" +
                          "- Simulation simple: Testez un scénario spécifique\n" +
                          "- Comparaison: Analysez plusieurs scénarios simultanément\n" +
                          "- Chaque simulation inclut l'analyse EOQ et les recommandations");
    }
    
    // Méthode pour analyser les risques
    private String analyseRisques(Produits p, int demande, Produits.Recommendation rec) {
        StringBuilder risques = new StringBuilder();
        
        // Analyse du niveau de stock après commande
        int stockApresCommande = p.getQuantite() + rec.reorderQty;
        double ratioStock = (double) stockApresCommande / demande;
        
        if (ratioStock < 0.5) {
            risques.append("RISQUE ÉLEVÉ: Stock insuffisant même après réappro\n");
        } else if (ratioStock < 1.0) {
            risques.append("RISQUE MODÉRÉ: Stock juste suffisant\n");
        } else if (ratioStock > 3.0) {
            risques.append("RISQUE DE SURSTOCKAGE: Stock excessif\n");
        }
        
        // Analyse du coût
        double coutTotal = p.getPrixachat() * rec.reorderQty + p.getPrixstock() * stockApresCommande;
        double coutParUnite = coutTotal / Math.max(rec.sold, 1);
        
        if (coutParUnite > p.getPrixvente() * 0.8) {
            risques.append("ATTENTION: Coûts élevés réduisent la marge\n");
        }
        
        return risques.toString();
    }
}