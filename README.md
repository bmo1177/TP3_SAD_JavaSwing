# Système d'Aide à la Décision (DSS) - Gestion Intelligente des Stocks

![Application Interface](screenshots/Screenshot%202025-11-14%20234828.png)

<div align="center">
  
### 🎯 Système d'Aide à la Décision Moderne pour la Gestion des Stocks

[![Java](https://img.shields.io/badge/Java-23-orange.svg)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/UI-Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Maven](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)
[![DSS](https://img.shields.io/badge/Type-DSS-green.svg)](https://en.wikipedia.org/wiki/Decision_support_system)

**Interface moderne** • **Algorithmes EOQ** • **Analyse What-If** • **Visualisations avancées**

</div>

---

## 📋 Résumé Exécutif

Ce projet implémente un **Système d'Aide à la Décision (DSS)** moderne pour la gestion optimisée des stocks, développé dans le cadre du cours "Systèmes d'aide à la décision" (L3 ISIL) à l'Université Ibn Khaldoun - Tiaret. Le système combine des algorithmes d'optimisation avancés (EOQ, Point de Commande) avec une interface utilisateur professionnelle pour fournir des recommandations intelligentes de réapprovisionnement.

## 🎯 Objectifs Scientifiques

### Problématique
La gestion des stocks représente un défi majeur pour les entreprises, nécessitant l'équilibre optimal entre :
- **Minimisation des coûts** (stockage, commande, rupture)
- **Maximisation du niveau de service** (satisfaction de la demande)
- **Optimisation des flux de trésorerie** (investissement en stock)

### Hypothèse de Recherche
*L'utilisation d'un système d'aide à la décision basé sur des algorithmes quantitatifs (EOQ, analyse What-If) améliore significativement l'efficacité de la gestion des stocks par rapport aux méthodes intuitives.*

## 🔬 Méthodologie

### 1. Modélisation Mathématique

#### Algorithme EOQ (Economic Order Quantity)
```mathematica
EOQ = √(2 × D × S / H)
```
Où :
- **D** = Demande annuelle (unités/période)
- **S** = Coût de commande (DA/commande)  
- **H** = Coût de stockage (DA/unité/période)

#### Point de Commande Dynamique
```mathematica
Point_Commande = max(75% × Demande, Seuil_Défini)
```

#### Coût Total d'Inventaire
```mathematica
CT = (D/Q × S) + (Q/2 × H) + (D × C)
```
Où **C** = Coût unitaire d'achat, **Q** = Quantité commandée

### 2. Architecture Logicielle (MVC)

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     MODEL       │    │   CONTROLLER    │    │      VIEW       │
│                 │    │                 │    │                 │
│ • StockModel    │◄───┤ StockController │───►│ • MainFrame     │
│ • Produits      │    │                 │    │ • DashboardPanel│
│ • Algorithms    │    │ • Business Logic│    │ • SimulationPanel│
└─────────────────┘    │ • Data Flow     │    │ • ChartPanel    │
                       └─────────────────┘    └─────────────────┘
```

## 📊 Fonctionnalités Implémentées

![Application Demo](screenshots/Screenshot%202025-11-14%20235039.png)

### 1. Dashboard Exécutif
- **KPIs en temps réel** : Nombre de produits, alertes, valeur totale, unités en stock
- **Système d'alertes visuelles** : Identification automatique des stocks faibles
- **Métriques de performance** : Prix moyen, demande totale, rotation des stocks

![Dashboard Overview](screenshots/Screenshot%202025-11-14%20234828.png)
*Vue d'ensemble du tableau de bord avec KPIs en temps réel*

### 2. Gestion Avancée de l'Inventaire
- **CRUD complet** : Création, lecture, mise à jour, suppression des produits
- **Calculs automatiques** : EOQ, point de commande, analyse des coûts
- **Recommandations intelligentes** : Suggestions basées sur les algorithmes DSS

![Inventory Management](screenshots/Screenshot%202025-11-14%20234836.png)
*Interface de gestion des produits avec détails et recommandations*

![Add Product](screenshots/Screenshot%202025-11-14%20234840.png)
*Dialogue d'ajout de nouveaux produits*

![Update Stock](screenshots/Screenshot%202025-11-14%20234924.png)
*Modification des niveaux de stock*

### 3. Simulation What-If
#### Fonctionnalité "Comparer Scenarios"
La fonction `compareScenarios()` implémente une analyse comparative de quatre scénarios :

```java
// Scénarios prédéfinis pour analyse comparative
int[] demandes = {
    (int)(demande_base * 0.5),  // Demande faible (-50%)
    demande_base,               // Demande normale (100%)
    (int)(demande_base * 1.5),  // Demande élevée (+50%)
    (int)(demande_base * 2.0)   // Demande très élevée (+100%)
};
```

**Avantages de cette approche :**
- **Analyse de sensibilité** : Impact des variations de demande
- **Planification stratégique** : Préparation aux différents scénarios
- **Gestion des risques** : Identification des situations critiques
- **Optimisation des coûts** : Comparaison des stratégies de réapprovisionnement

![Simulation What-If](screenshots/Screenshot%202025-11-14%20234945.png)
*Interface de simulation avec analyse comparative de scénarios*

### 4. Analytics et Visualisation
- **Graphiques personnalisés** : Utilisation de Java Graphics2D
- **Quatre types de visualisations** :
  - Analyse des niveaux de stock (barres)
  - Répartition de la valeur (camembert)
  - Tendances demande vs stock (lignes)
  - Statut des seuils (barres colorées)

![Analytics Dashboard](screenshots/Screenshot%202025-11-14%20235004.png)
*Tableau de bord analytique avec 4 types de visualisations*

### 5. Système d'Export et de Rapports
- **Export automatique** : Génération de graphiques PNG
- **Rapports textuels** : Fichiers de données structurées
- **Sauvegarde périodique** : Historique des analyses

![Export System](screenshots/Screenshot%202025-11-14%20235021.png)
*Système d'export avec fichiers générés automatiquement*

## 🧮 Algorithmes et Calculs

### Classe `Produits.recommanderReapprovisionnement(int demande)`

```java
public Recommendation recommanderReapprovisionnement(int demande) {
    // Calcul du seuil automatique
    int thresholdAuto = Math.max((int)(demande * 0.75), (int)this.seuil);
    
    // Algorithme EOQ
    double eoqValue = Math.sqrt((2.0 * demande * prixachat) / prixstock);
    int eoq = (int) Math.ceil(eoqValue);
    
    // Logique de décision
    if (demande > quantite) {
        // Cas critique : rupture de stock
        reorderQty = (demande - quantite) + eoq;
    } else if (quantite <= thresholdAuto) {
        // Cas préventif : stock faible
        reorderQty = eoq;
    }
    // ...
}
```

### Métriques Calculées
1. **ROI (Return On Investment)** : `(Profit Potentiel / Coût Commande) × 100`
2. **Taux de rotation** : `Demande Annuelle / Stock Moyen`
3. **Coût de possession** : `Stock × Coût Stockage`
4. **Niveau de service** : `Min(Stock, Demande) / Demande × 100`

## 🎨 Interface Utilisateur

![Settings Panel](screenshots/Screenshot%202025-11-14%20235030.png)
*Panneau de configuration avec paramètres avancés*

### Design Principles
- **Material Design** : Couleurs cohérentes, ombres subtiles
- **Dark Theme** : Interface professionnelle avec Nimbus LAF
- **Responsive Design** : Adaptation automatique aux tailles d'écran
- **Color Psychology** :
  - 🔵 Bleu (`#64B5F6`) : Actions principales
  - 🔴 Rouge (`#F44336`) : Alertes critiques  
  - 🟢 Vert (`#4CAF50`) : États positifs
  - 🟠 Orange (`#FF9800`) : Avertissements

## 📈 Résultats et Validation

### Tests de Performance
```bash
# Compilation et exécution
cd src/main/java
javac -cp . controller/*.java model/*.java view/*.java Main.java
java Main
```

### Scénarios de Test Validés
1. **Stock suffisant** : Demande < Stock → Pas de réapprovisionnement
2. **Stock faible** : Stock ≤ Seuil → Commande EOQ
3. **Rupture critique** : Demande > Stock → Commande (Pénurie + EOQ)
4. **Comparaison multi-scénarios** : Analyse de 4 niveaux de demande

### Métriques de Validation
- ✅ **Cohérence des calculs** : EOQ conforme à la formule théorique
- ✅ **Logique de décision** : Recommandations appropriées selon le contexte
- ✅ **Interface responsive** : Mise à jour en temps réel des KPIs
- ✅ **Gestion des erreurs** : Validation des entrées utilisateur

## 🛠 Technologies Utilisées

### Environnement de Développement
- **Langage** : Java 23 (LTS)
- **Build Tool** : Maven 3.11.0
- **IDE** : Compatible NetBeans, IntelliJ IDEA, Eclipse

### Dépendances Maven
```xml
<dependencies>
    <!-- Interface moderne -->
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf</artifactId>
        <version>3.2.5</version>
    </dependency>
    
    <!-- Graphiques avancés -->
    <dependency>
        <groupId>org.jfree</groupId>
        <artifactId>jfreechart</artifactId>
        <version>1.5.3</version>
    </dependency>
</dependencies>
```

## 📋 Structure du Projet

```
DSS-Stock-Management/
├── src/main/java/
│   ├── Main.java                    # Point d'entrée avec splash screen
│   ├── controller/
│   │   └── StockController.java     # Logique métier
│   ├── model/
│   │   ├── StockModel.java          # Gestion des données
│   │   └── Produits.java           # Entité produit + algorithmes
│   └── view/
│       ├── MainFrame.java          # Interface principale
│       ├── DashboardPanel.java     # Tableau de bord
│       ├── ProductPanel.java       # Gestion inventaire
│       ├── SimulationPanel.java    # Analyse What-If
│       └── ChartPanel.java         # Visualisations
├── pom.xml                         # Configuration Maven
├── README.md                       # Documentation scientifique
└── *.md                           # Documentation complémentaire
```

## 📱 Aperçu Visuel Complet

### Interface Principale
| Dashboard | Inventaire | Simulation |
|-----------|------------|------------|
| ![Dashboard](screenshots/Screenshot%202025-11-14%20234828.png) | ![Inventory](screenshots/Screenshot%202025-11-14%20234836.png) | ![Simulation](screenshots/Screenshot%202025-11-14%20234945.png) |
| Vue d'ensemble avec KPIs | Gestion des produits | Analyse What-If |

### Analytics et Configuration
| Analytics | Configuration | Exports |
|-----------|---------------|---------|
| ![Analytics](screenshots/Screenshot%202025-11-14%20235004.png) | ![Settings](screenshots/Screenshot%202025-11-14%20235030.png) | ![Exports](screenshots/Screenshot%202025-11-14%20235021.png) |
| Visualisations avancées | Paramètres système | Système d'export |

### Fonctionnalités CRUD
| Ajouter Produit | Modifier Stock | Gestion Détails |
|-----------------|----------------|------------------|
| ![Add](screenshots/Screenshot%202025-11-14%20234840.png) | ![Update](screenshots/Screenshot%202025-11-14%20234924.png) | ![Details](screenshots/Screenshot%202025-11-14%20234836.png) |
| Interface d'ajout | Modification stock | Informations détaillées |

## 🚀 Installation et Exécution

### Prérequis
```bash
Java 23+ (OpenJDK ou Oracle JDK)
Maven 3.8+
```

### Compilation et Lancement
```bash
# Cloner le projet
git clone [repository-url]
cd DSS-Stock-Management

# Compiler avec Maven
mvn clean compile

# Exécuter l'application
mvn exec:java -Dexec.mainClass="Main"

# Alternative : compilation manuelle
cd src/main/java
javac -cp . *.java controller/*.java model/*.java view/*.java
java Main
```

## 🔍 Analyse des Fonctionnalités DSS

### 1. Aide à la Décision Quantitative
- **Algorithmes d'optimisation** : EOQ pour minimiser les coûts totaux
- **Analyse multi-critères** : Équilibre coût/service/risque
- **Recommandations automatiques** : Suggestions basées sur des modèles mathématiques

### 2. Simulation et Modélisation
- **What-If Analysis** : Test de scénarios sans impact opérationnel
- **Analyse de sensibilité** : Impact des variations de paramètres
- **Comparaison stratégique** : Évaluation de différentes politiques de stock

### 3. Intelligence d'Affaires
- **Tableaux de bord exécutifs** : KPIs pour la prise de décision
- **Visualisation des tendances** : Graphiques pour l'analyse patterns
- **Alertes proactives** : Détection automatique des situations critiques

## 📚 Contributions Académiques

### Concepts SAD Démontrés
1. **Modélisation quantitative** : Application des mathématiques à la gestion
2. **Optimisation opérationnelle** : Minimisation des coûts sous contraintes
3. **Analyse décisionnelle** : Support à la prise de décision managériale
4. **Simulation d'entreprise** : Modélisation des processus métier

### Compétences Développées
- **Programmation orientée objet** avancée en Java
- **Patterns de conception** (MVC, Observer, Factory)
- **Interfaces utilisateur** professionnelles avec Swing
- **Algorithmes d'optimisation** appliqués à la gestion
- **Visualisation de données** et business intelligence

## 🔮 Extensions Possibles

### Court Terme
- [ ] **Export PDF/Excel** : Génération de rapports automatisés
- [ ] **Historique des décisions** : Traçabilité des recommandations
- [ ] **Paramétrage avancé** : Configuration fine des algorithmes

### Moyen Terme  
- [ ] **Base de données** : Persistance PostgreSQL/MySQL
- [ ] **API REST** : Accès mobile et web
- [ ] **Machine Learning** : Prédiction de demande avec IA
- [ ] **Multi-utilisateurs** : Gestion des rôles et permissions

### Long Terme
- [ ] **Supply Chain Integration** : Gestion multi-entrepôts
- [ ] **IoT Integration** : Capteurs automatiques de stock
- [ ] **Blockchain** : Traçabilité et audit automatisé
- [ ] **Cloud Deployment** : SaaS pour PME

## 📖 Conclusion Scientifique

Ce projet démontre l'application pratique des **Systèmes d'Aide à la Décision** dans un contexte industriel réel. L'implémentation combine :

1. **Rigueur scientifique** : Algorithmes mathématiques validés
2. **Pragmatisme opérationnel** : Interface utilisateur intuitive  
3. **Innovation technologique** : Architecture moderne et extensible
4. **Valeur pédagogique** : Illustration concrète des concepts SAD

Le système prouve que les **DSS modernes** peuvent transformer la prise de décision managériale en fournissant :
- Des **analyses quantitatives** objectives
- Des **simulations** pour l'aide à la planification
- Des **visualisations** pour la compréhension des tendances
- Des **recommandations automatisées** pour l'optimisation opérationnelle

---

## 📝 Métadonnées

**Auteur** : DAOUD Mohamed Amine  
**Université** : Ibn Khaldoun - Tiaret  
**Niveau** : L3 ISIL  
**Cours** : Systèmes d'aide à la décision (SAD)  
**Année académique** : 2024/2025  
**Langue** : Français  
**Technologie** : Java 18 + Swing   
**Licence** : Académique - Usage éducatif

**Mots-clés** : DSS, Gestion des Stocks, EOQ, Java, Aide à la Décision, Optimisation, Business Intelligence, What-If Analysis

---

*Ce document constitue un rapport scientifique complet sur l'implémentation d'un Système d'Aide à la Décision pour la gestion optimisée des stocks, démontrant l'application pratique des concepts théoriques dans un environnement de développement moderne.*