/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;


/**
 * Modèle de données principal pour le système
 * Gère la liste des produits et fournit des données pour les simulations
 */
public class StockModel {
    private List<Produits> produits;

    public StockModel() {
        produits = new ArrayList<>();
        initialiserProduitsExemples();
    }

    /**
     * Initialise 7 produits d'exemple pour le système
     */
    private void initialiserProduitsExemples() {
        produits.add(new Produits("Laptop Dell", 45000, 65000, 25, 10, 500, 15));
        produits.add(new Produits("Souris Sans Fil", 1500, 2500, 50, 15, 20, 30));
        produits.add(new Produits("Clavier Mécanique", 3500, 5500, 30, 12, 50, 20));
        produits.add(new Produits("Écran 24\"", 18000, 28000, 15, 8, 300, 10));
        produits.add(new Produits("Casque Audio", 4000, 7000, 40, 18, 40, 25));
        produits.add(new Produits("Disque SSD 1TB", 8000, 12000, 20, 10, 80, 12));
        produits.add(new Produits("Webcam HD", 5500, 8500, 35, 14, 60, 18));
    }

    public List<Produits> getProduits() {
        return produits;
    }

    public void ajouterProduit(Produits p) {
        produits.add(p);
    }

    public void supprimerProduit(int index) {
        if (index >= 0 && index < produits.size()) {
            produits.remove(index);
        }
    }

    public Produits getProduit(int index) {
        if (index >= 0 && index < produits.size()) {
            return produits.get(index);
        }
        return null;
    }
    
    /**
     * Ajoute un nouveau produit avec tous les paramètres
     */
    public void ajouterProduit(String nom, int quantite, double prixAchat, double prixVente, 
                              int seuil, double prixStock, int demande) {
        Produits nouveauProduit = new Produits(nom, prixAchat, prixVente, quantite, seuil, prixStock, demande);
        produits.add(nouveauProduit);
    }
    
    /**
     * Modifie les paramètres d'un produit existant
     */
    public void modifierStock(int index, int nouvelleQuantite, int nouveauSeuil, int nouvelleDemande) {
        if (index >= 0 && index < produits.size()) {
            Produits p = produits.get(index);
            p.setQuantite(nouvelleQuantite);
            p.setSeuil(nouveauSeuil);
            p.setDemandeEstimee(nouvelleDemande);
        }
    }
    
    /**
     * Obtient le nombre total de produits
     */
    public int getNombreProduits() {
        return produits.size();
    }
    
    /**
     * Vérifie si un produit existe par son nom
     */
    public boolean produitExiste(String nom) {
        return produits.stream().anyMatch(p -> p.getNom().equalsIgnoreCase(nom));
    }
    
    /**
     * Trouve un produit par son nom
     */
    public Produits getProduitParNom(String nom) {
        return produits.stream()
                      .filter(p -> p.getNom().equalsIgnoreCase(nom))
                      .findFirst()
                      .orElse(null);
    }
}
