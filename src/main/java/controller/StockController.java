/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import model.Produits;
import model.StockModel;
import java.util.List;

/**
 * Contrôleur principal - gère la logique métier et la communication
 * entre le modèle et la vue (pattern MVC)
 */
public class StockController {
    private StockModel model;

    public StockController(StockModel model) {
        this.model = model;
    }

    public List<Produits> getProduits() {
        return model.getProduits();
    }

    public Produits getProduit(int index) {
        return model.getProduit(index);
    }

    /**
     * Calcule une recommandation pour un produit donné
     */
    public Produits.Recommendation getRecommandation(int productIndex, int demande) {
        Produits p = model.getProduit(productIndex);
        if (p != null) {
            return p.recommanderReapprovisionnement(demande);
        }
        return null;
    }

    /**
     * Simule l'application d'une recommandation (mise à jour du stock)
     */
    public void appliquerReapprovisionnement(int productIndex, int quantiteCommande) {
        Produits p = model.getProduit(productIndex);
        if (p != null) {
            p.setQuantite(p.getQuantite() + quantiteCommande);
        }
    }

    /**
     * Calcule le coût total de réapprovisionnement
     */
    public double calculerCoutReappro(int productIndex, int quantiteCommande) {
        Produits p = model.getProduit(productIndex);
        if (p != null) {
            return p.getPrixachat() * quantiteCommande;
        }
        return 0;
    }

    /**
     * Calcule le profit potentiel
     */
    public double calculerProfitPotentiel(int productIndex, int quantiteVendue) {
        Produits p = model.getProduit(productIndex);
        if (p != null) {
            return (p.getPrixvente() - p.getPrixachat()) * quantiteVendue;
        }
        return 0;
    }
    
    /**
     * Ajoute un nouveau produit
     */
    public void ajouterProduit(String nom, int quantite, double prixAchat, double prixVente, 
                              int seuil, double prixStock, int demande) {
        model.ajouterProduit(nom, quantite, prixAchat, prixVente, seuil, prixStock, demande);
    }
    
    /**
     * Modifie le stock d'un produit
     */
    public void modifierStock(int index, int nouvelleQuantite, int nouveauSeuil, int nouvelleDemande) {
        model.modifierStock(index, nouvelleQuantite, nouveauSeuil, nouvelleDemande);
    }
    
    /**
     * Supprime un produit
     */
    public void supprimerProduit(int index) {
        model.supprimerProduit(index);
    }
    
    /**
     * Obtient le nombre total de produits
     */
    public int getNombreProduits() {
        return model.getProduits().size();
    }
    
    /**
     * Calcule la valeur totale du stock
     */
    public double getValeurTotaleStock() {
        double total = 0;
        for (Produits p : model.getProduits()) {
            total += p.getQuantite() * p.getPrixachat();
        }
        return total;
    }
    
    /**
     * Compte le nombre de produits en alerte
     */
    public int getNombreAlertes() {
        int alertes = 0;
        for (Produits p : model.getProduits()) {
            if (p.getQuantite() <= p.getSeuil()) {
                alertes++;
            }
        }
        return alertes;
    }
    
    /**
     * Obtient les produits en alerte
     */
    public List<Produits> getProduitsEnAlerte() {
        return model.getProduits().stream()
                   .filter(p -> p.getQuantite() <= p.getSeuil())
                   .collect(java.util.stream.Collectors.toList());
    }
}
