/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import controller.StockController;
import model.Produits;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panneau de gestion des produits
 * Affiche la liste des produits et permet de voir les détails
 */
public class ProductPanel extends JPanel {
    private StockController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;

    public ProductPanel(StockController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Titre
        JLabel titleLabel = new JLabel("Liste des Produits en Stock");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Table des produits
        String[] columns = {"Nom", "Stock", "Prix Achat", "Prix Vente", "Seuil", "Coût Stock", "Demande Est."};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Listener pour afficher les détails
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showProductDetails();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        // Zone de détails
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails & Recommandation"));
        
        detailsArea = new JTextArea(10, 50);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);

        // Panneau de boutons
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        
        JButton recommandBtn = new JButton("Obtenir Recommandation");
        recommandBtn.setPreferredSize(new Dimension(180, 35));
        recommandBtn.addActionListener(e -> getRecommandation());
        
        JButton addBtn = new JButton("Ajouter Produit");
        addBtn.setPreferredSize(new Dimension(140, 35));
        addBtn.addActionListener(e -> addProduct());
        
        JButton editBtn = new JButton("Modifier Stock");
        editBtn.setPreferredSize(new Dimension(140, 35));
        editBtn.addActionListener(e -> editStock());
        
        JButton deleteBtn = new JButton("Supprimer");
        deleteBtn.setPreferredSize(new Dimension(120, 35));
        deleteBtn.addActionListener(e -> deleteProduct());
        
        buttonsPanel.add(recommandBtn);
        buttonsPanel.add(addBtn);
        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);
        
        detailsPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, detailsPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Produits> produits = controller.getProduits();
        
        for (Produits p : produits) {
            Object[] row = {
                p.getNom(),
                p.getQuantite(),
                String.format("%.2f DA", p.getPrixachat()),
                String.format("%.2f DA", p.getPrixvente()),
                (int)p.getSeuil(),
                String.format("%.2f DA", p.coutstock()),
                p.getDemandeEstimee()
            };
            tableModel.addRow(row);
        }
    }

    private void showProductDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Produits p = controller.getProduit(selectedRow);
            if (p != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("═══════════════════════════════════════════\n");
                sb.append("         DÉTAILS DU PRODUIT\n");
                sb.append("═══════════════════════════════════════════\n\n");
                sb.append(String.format("Nom:              %s\n", p.getNom()));
                sb.append(String.format("Stock actuel:     %d unités\n", p.getQuantite()));
                sb.append(String.format("Prix d'achat:     %.2f DA\n", p.getPrixachat()));
                sb.append(String.format("Prix de vente:    %.2f DA\n", p.getPrixvente()));
                sb.append(String.format("Marge unitaire:   %.2f DA\n", (p.getPrixvente() - p.getPrixachat())));
                sb.append(String.format("Seuil défini:     %.0f unités\n", p.getSeuil()));
                sb.append(String.format("Coût de stockage: %.2f DA/unité\n", p.getPrixstock()));
                sb.append(String.format("Coût stock total: %.2f DA\n", p.coutstock()));
                sb.append(String.format("Demande estimée:  %d unités/période\n\n", p.getDemandeEstimee()));
                sb.append("Cliquez sur 'Obtenir Recommandation' pour analyser.");
                
                detailsArea.setText(sb.toString());
            }
        }
    }

    private void getRecommandation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un produit dans la table.", 
                "Aucun produit sélectionné", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produits p = controller.getProduit(selectedRow);
        String demandeStr = JOptionPane.showInputDialog(this, 
            "Entrez la demande attendue pour " + p.getNom() + ":", 
            p.getDemandeEstimee());
        
        if (demandeStr != null && !demandeStr.isEmpty()) {
            try {
                int demande = Integer.parseInt(demandeStr);
                Produits.Recommendation rec = controller.getRecommandation(selectedRow, demande);
                
                if (rec != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("═══════════════════════════════════════════\n");
                    sb.append("    ANALYSE DE RÉAPPROVISIONNEMENT\n");
                    sb.append("═══════════════════════════════════════════\n\n");
                    sb.append(rec.message).append("\n\n");
                    sb.append("───────────────────────────────────────────\n");
                    sb.append("DÉTAILS DE L'ANALYSE:\n");
                    sb.append("───────────────────────────────────────────\n");
                    sb.append(String.format("Seuil automatique:  %d unités\n", rec.thresholdAuto));
                    sb.append(String.format("EOQ calculé:        %d unités\n", rec.eoq));
                    sb.append(String.format("Coût commande:      %.2f DA\n", 
                        controller.calculerCoutReappro(selectedRow, rec.reorderQty)));
                    sb.append(String.format("Profit potentiel:   %.2f DA\n", 
                        controller.calculerProfitPotentiel(selectedRow, rec.sold)));
                    
                    detailsArea.setText(sb.toString());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Veuillez entrer un nombre valide.", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addProduct() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField nomField = new JTextField(20);
        JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        JSpinner prixAchatSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 100.0));
        JSpinner prixVenteSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 100.0));
        JSpinner seuilSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        JSpinner prixStockSpinner = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 1000.0, 1.0));
        JSpinner demandeSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 1000, 1));
        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nom du produit:"), gbc);
        gbc.gridx = 1; panel.add(nomField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Quantité initiale:"), gbc);
        gbc.gridx = 1; panel.add(quantiteSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Prix d'achat (DA):"), gbc);
        gbc.gridx = 1; panel.add(prixAchatSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Prix de vente (DA):"), gbc);
        gbc.gridx = 1; panel.add(prixVenteSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Seuil de réappro:"), gbc);
        gbc.gridx = 1; panel.add(seuilSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Coût stockage (DA/unité):"), gbc);
        gbc.gridx = 1; panel.add(prixStockSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Demande estimée:"), gbc);
        gbc.gridx = 1; panel.add(demandeSpinner, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un nouveau produit", 
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom du produit ne peut pas être vide.", 
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int quantite = (Integer) quantiteSpinner.getValue();
                double prixAchat = (Double) prixAchatSpinner.getValue();
                double prixVente = (Double) prixVenteSpinner.getValue();
                int seuil = (Integer) seuilSpinner.getValue();
                double prixStock = (Double) prixStockSpinner.getValue();
                int demande = (Integer) demandeSpinner.getValue();
                
                if (prixVente <= prixAchat) {
                    JOptionPane.showMessageDialog(this, "Le prix de vente doit être supérieur au prix d'achat.", 
                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                controller.ajouterProduit(nom, quantite, prixAchat, prixVente, seuil, prixStock, demande);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès!");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + e.getMessage(), 
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editStock() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit.", 
                                        "Aucun produit sélectionné", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Produits p = controller.getProduit(selectedRow);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(p.getQuantite(), 0, 10000, 1));
        JSpinner seuilSpinner = new JSpinner(new SpinnerNumberModel((int)p.getSeuil(), 1, 1000, 1));
        JSpinner demandeSpinner = new JSpinner(new SpinnerNumberModel(p.getDemandeEstimee(), 0, 1000, 1));
        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Produit: " + p.getNom()), gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nouvelle quantité:"), gbc);
        gbc.gridx = 1; panel.add(quantiteSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Nouveau seuil:"), gbc);
        gbc.gridx = 1; panel.add(seuilSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Demande estimée:"), gbc);
        gbc.gridx = 1; panel.add(demandeSpinner, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Modifier le stock", 
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int nouvelleQuantite = (Integer) quantiteSpinner.getValue();
            int nouveauSeuil = (Integer) seuilSpinner.getValue();
            int nouvelleDemande = (Integer) demandeSpinner.getValue();
            
            controller.modifierStock(selectedRow, nouvelleQuantite, nouveauSeuil, nouvelleDemande);
            refreshTable();
            showProductDetails();
            JOptionPane.showMessageDialog(this, "Stock mis à jour avec succès!");
        }
    }
    
    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit.", 
                                        "Aucun produit sélectionné", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Produits p = controller.getProduit(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer le produit '" + p.getNom() + "' ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.supprimerProduit(selectedRow);
            refreshTable();
            detailsArea.setText("Produit supprimé. Sélectionnez un autre produit pour voir les détails.");
            JOptionPane.showMessageDialog(this, "Produit supprimé avec succès!");
        }
    }
}
