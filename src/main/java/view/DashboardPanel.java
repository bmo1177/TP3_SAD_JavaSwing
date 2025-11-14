package view;

import controller.StockController;
import model.Produits;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Professional Dashboard Panel with KPI cards and overview widgets
 * Provides real-time insights into inventory performance
 */
public class DashboardPanel extends JPanel {
    private StockController controller;
    private JPanel kpiPanel;
    private JPanel alertsPanel;
    private JPanel quickStatsPanel;
    private JPanel recentActivityPanel;

    public DashboardPanel(StockController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initComponents();
        updateDashboard();
    }

    private void initComponents() {
        // Main dashboard grid
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(30, 30, 30));

        // Top section - KPI Cards
        createKPIPanel();
        mainPanel.add(kpiPanel, BorderLayout.NORTH);

        // Middle section - Split between alerts and quick stats
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        middlePanel.setBackground(new Color(30, 30, 30));
        
        createAlertsPanel();
        createQuickStatsPanel();
        
        middlePanel.add(alertsPanel);
        middlePanel.add(quickStatsPanel);
        mainPanel.add(middlePanel, BorderLayout.CENTER);

        // Bottom section - Recent Activity
        createRecentActivityPanel();
        mainPanel.add(recentActivityPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void createKPIPanel() {
        kpiPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        kpiPanel.setBackground(new Color(30, 30, 30));
        kpiPanel.setPreferredSize(new Dimension(0, 120));

        // KPI Cards will be populated in updateDashboard()
    }

    private void createAlertsPanel() {
        alertsPanel = createDashboardCard("Alertes Stock", new Color(244, 67, 54));
        alertsPanel.setPreferredSize(new Dimension(0, 250));
    }

    private void createQuickStatsPanel() {
        quickStatsPanel = createDashboardCard("Statistiques Rapides", new Color(76, 175, 80));
        quickStatsPanel.setPreferredSize(new Dimension(0, 250));
    }

    private void createRecentActivityPanel() {
        recentActivityPanel = createDashboardCard("Activité Récente", new Color(63, 81, 181));
        recentActivityPanel.setPreferredSize(new Dimension(0, 200));
    }

    private JPanel createDashboardCard(String title, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background with rounded corners
                g2.setColor(new Color(45, 45, 45));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Accent border
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 12, 12));
                
                g2.dispose();
            }
        };
        
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Card header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        card.add(headerPanel, BorderLayout.NORTH);
        
        return card;
    }

    private JPanel createKPICard(String title, String value, String subtitle, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, color.darker().darker(),
                    getWidth(), getHeight(), color
                );
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                
                g2.dispose();
            }
        };
        
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(iconLabel, BorderLayout.EAST);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(240, 240, 240));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subtitleLabel.setForeground(new Color(220, 220, 220));

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        contentPanel.add(subtitleLabel, BorderLayout.SOUTH);

        card.add(contentPanel, BorderLayout.WEST);

        return card;
    }

    public void updateDashboard() {
        updateKPIs();
        updateAlerts();
        updateQuickStats();
        updateRecentActivity();
    }

    private void updateKPIs() {
        kpiPanel.removeAll();
        
        List<Produits> produits = controller.getProduits();
        
        // Calculate KPIs
        int totalProducts = produits.size();
        int lowStockItems = 0;
        double totalValue = 0;
        int totalStock = 0;
        
        for (Produits p : produits) {
            if (p.getQuantite() <= p.getSeuil()) {
                lowStockItems++;
            }
            totalValue += p.getQuantite() * p.getPrixachat();
            totalStock += p.getQuantite();
        }

        // Create KPI cards
        kpiPanel.add(createKPICard("Produits Total", 
            String.valueOf(totalProducts), 
            "articles en stock", 
            new Color(33, 150, 243), "[P]"));

        kpiPanel.add(createKPICard("Stock Faible", 
            String.valueOf(lowStockItems), 
            "alertes actives", 
            new Color(255, 152, 0), "[!]"));

        kpiPanel.add(createKPICard("Valeur Totale", 
            String.format("%.0f", totalValue) + " DA", 
            "valeur inventaire", 
            new Color(76, 175, 80), "[$]"));

        kpiPanel.add(createKPICard("Unités Stock", 
            String.valueOf(totalStock), 
            "total unités", 
            new Color(156, 39, 176), "[#]"));

        kpiPanel.revalidate();
        kpiPanel.repaint();
    }

    private void updateAlerts() {
        // Remove existing content
        Component[] components = alertsPanel.getComponents();
        for (int i = 1; i < components.length; i++) {
            alertsPanel.remove(components[i]);
        }

        List<Produits> produits = controller.getProduits();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        boolean hasAlerts = false;
        for (Produits p : produits) {
            if (p.getQuantite() <= p.getSeuil()) {
                hasAlerts = true;
                JPanel alertItem = createAlertItem(p);
                contentPanel.add(alertItem);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        if (!hasAlerts) {
            JLabel noAlertsLabel = new JLabel("[OK] Aucune alerte active");
            noAlertsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noAlertsLabel.setForeground(new Color(150, 150, 150));
            noAlertsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(Box.createVerticalGlue());
            contentPanel.add(noAlertsLabel);
            contentPanel.add(Box.createVerticalGlue());
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        alertsPanel.add(scrollPane, BorderLayout.CENTER);
        alertsPanel.revalidate();
        alertsPanel.repaint();
    }

    private JPanel createAlertItem(Produits produit) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel nameLabel = new JLabel(produit.getNom());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(Color.WHITE);

        JLabel stockLabel = new JLabel(produit.getQuantite() + "/" + (int)produit.getSeuil());
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        stockLabel.setForeground(new Color(255, 183, 77));

        item.add(nameLabel, BorderLayout.WEST);
        item.add(stockLabel, BorderLayout.EAST);

        return item;
    }

    private void updateQuickStats() {
        // Remove existing content
        Component[] components = quickStatsPanel.getComponents();
        for (int i = 1; i < components.length; i++) {
            quickStatsPanel.remove(components[i]);
        }

        List<Produits> produits = controller.getProduits();
        
        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        contentPanel.setOpaque(false);

        // Calculate stats
        double avgPrice = produits.stream().mapToDouble(Produits::getPrixachat).average().orElse(0);
        double totalDemand = produits.stream().mapToInt(Produits::getDemandeEstimee).sum();
        double avgStock = produits.stream().mapToInt(Produits::getQuantite).average().orElse(0);
        long criticalItems = produits.stream().filter(p -> p.getQuantite() <= p.getSeuil() * 0.5).count();

        contentPanel.add(createStatItem("Prix Moyen:", String.format("%.2f DA", avgPrice)));
        contentPanel.add(createStatItem("Demande Totale:", String.format("%.0f unités", totalDemand)));
        contentPanel.add(createStatItem("Stock Moyen:", String.format("%.1f unités", avgStock)));
        contentPanel.add(createStatItem("Critique:", criticalItems + " produits"));

        quickStatsPanel.add(contentPanel, BorderLayout.CENTER);
        quickStatsPanel.revalidate();
        quickStatsPanel.repaint();
    }

    private JPanel createStatItem(String label, String value) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelComponent.setForeground(new Color(180, 180, 180));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueComponent.setForeground(Color.WHITE);

        item.add(labelComponent, BorderLayout.WEST);
        item.add(valueComponent, BorderLayout.EAST);

        return item;
    }

    private void updateRecentActivity() {
        // Remove existing content
        Component[] components = recentActivityPanel.getComponents();
        for (int i = 1; i < components.length; i++) {
            recentActivityPanel.remove(components[i]);
        }

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Simulate recent activities
        String[] activities = {
            "[UPD] Stock mis à jour pour: Laptop Dell",
            "[ANA] Analyse de demande effectuée",
            "[ALR] Alerte générée: Souris Sans Fil",
            "[RPT] Rapport de performance généré",
            "[CFG] Paramètres système modifiés"
        };

        for (String activity : activities) {
            JLabel activityLabel = new JLabel(activity);
            activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            activityLabel.setForeground(new Color(200, 200, 200));
            activityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            contentPanel.add(activityLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        recentActivityPanel.add(contentPanel, BorderLayout.CENTER);
        recentActivityPanel.revalidate();
        recentActivityPanel.repaint();
    }
}