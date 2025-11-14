/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Classe représentant un produit dans le système de gestion des stocks
 * Contient toutes les informations nécessaires pour calculer les recommandations
 */
public class Produits {
    private String nom;
    private double prixachat;      // Prix d'achat unitaire
    private double prixvente;      // Prix de vente unitaire
    private int quantite;          // Stock actuel
    private double seuil;          // Seuil de réapprovisionnement défini
    private double prixstock;      // Coût de stockage par unité
    private int demandeEstimee;    // Demande estimée (hebdomadaire/mensuelle)

    public Produits(String nom, double prixachat, double prixvente, int quantite, 
                    double seuil, double prixstock, int demandeEstimee) {
        this.nom = nom;
        this.prixachat = prixachat;
        this.prixvente = prixvente;
        this.quantite = quantite;
        this.seuil = seuil;
        this.prixstock = prixstock;
        this.demandeEstimee = demandeEstimee;
    }

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public double getPrixachat() { return prixachat; }
    public void setPrixachat(double prixachat) { this.prixachat = prixachat; }
    
    public double getPrixvente() { return prixvente; }
    public void setPrixvente(double prixvente) { this.prixvente = prixvente; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public double getSeuil() { return seuil; }
    public void setSeuil(double seuil) { this.seuil = seuil; }
    
    public double getPrixstock() { return prixstock; }
    public void setPrixstock(double prixstock) { this.prixstock = prixstock; }
    
    public int getDemandeEstimee() { return demandeEstimee; }
    public void setDemandeEstimee(int demandeEstimee) { this.demandeEstimee = demandeEstimee; }

    /**
     * Calcule le coût total de stockage
     * @return coût de stockage total
     */
    public double coutstock() {
        return prixstock * quantite;
    }

    /**
     * Classe interne représentant une recommandation de réapprovisionnement
     */
    public static class Recommendation {
        public int sold;              // Quantité vendue
        public int shortage;          // Pénurie (demande non satisfaite)
        public boolean needReorder;   // Besoin de réapprovisionner?
        public int reorderQty;        // Quantité recommandée à commander
        public int thresholdAuto;     // Seuil automatique calculé
        public String message;        // Message explicatif
        public int eoq;               // Quantité économique de commande

        public Recommendation(int sold, int shortage, boolean needReorder,
                int reorderQty, int thresholdAuto, String message, int eoq) {
            this.sold = sold;
            this.shortage = shortage;
            this.needReorder = needReorder;
            this.reorderQty = reorderQty;
            this.thresholdAuto = thresholdAuto;
            this.message = message;
            this.eoq = eoq;
        }
    }

    /**
     * Recommande un réapprovisionnement basé sur la demande
     * Utilise le modèle EOQ (Economic Order Quantity)
     * @param demande demande attendue
     * @return objet Recommendation avec tous les détails
     */
    public Recommendation recommanderReapprovisionnement(int demande) {
        // Calculer le seuil automatique (75% de la demande ou seuil défini)
        int thresholdAuto = Math.max((int) Math.ceil(demande * 0.75), (int) Math.ceil(this.seuil));
        
        int sold = 0;
        int shortage = 0;
        boolean needReorder = false;
        int reorderQty = 0;
        int eoq = 0;
        String message;

        // Cas 1: La demande dépasse le stock actuel
        if (demande > this.quantite) {
            sold = this.quantite;
            shortage = demande - this.quantite;
            needReorder = true;

            // Calculer EOQ (Economic Order Quantity)
            double storageCostPerUnit = (this.prixstock <= 0) ? 1.0 : this.prixstock;
            double replenishmentCost = (this.prixachat <= 0) ? 1.0 : this.prixachat;
            eoq = (int) Math.ceil(Math.sqrt((2.0 * Math.max(demande, 1) * replenishmentCost) / storageCostPerUnit));
            reorderQty = shortage + eoq;

            message = "⚠️ ALERTE: Demande > Stock actuel\n" +
                      "Vente possible: " + sold + " unités\n" +
                      "Pénurie: " + shortage + " unités\n" +
                      "Commande recommandée: " + reorderQty + " unités (Pénurie + EOQ=" + eoq + ")";
        } 
        // Cas 2: Stock suffisant pour la demande
        else {
            sold = demande;
            shortage = 0;
            
            // Vérifier si le stock restant est sous le seuil
            if (this.quantite <= thresholdAuto) {
                needReorder = true;
                double storageCostPerUnit = (this.prixstock <= 0) ? 1.0 : this.prixstock;
                double replenishmentCost = (this.prixachat <= 0) ? 1.0 : this.prixachat;
                eoq = (int) Math.ceil(Math.sqrt((2.0 * Math.max(demande, 1) * replenishmentCost) / storageCostPerUnit));
                reorderQty = eoq;
                
                message = "⚠️ Stock faible détecté\n" +
                          "Vente: " + sold + " unités\n" +
                          "Stock actuel: " + this.quantite + " ≤ Seuil: " + thresholdAuto + "\n" +
                          "Commande recommandée: " + eoq + " unités (EOQ)";
            } else {
                needReorder = false;
                reorderQty = 0;
                message = "✓ Stock suffisant\n" +
                          "Vente: " + sold + " unités\n" +
                          "Stock restant: " + (this.quantite - sold) + " unités\n" +
                          "Pas de réapprovisionnement nécessaire";
            }
        }

        return new Recommendation(sold, shortage, needReorder, reorderQty, thresholdAuto, message, eoq);
    }
}
