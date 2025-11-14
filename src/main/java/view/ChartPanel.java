/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.StockController;
import model.Produits;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
// Using built-in Java graphics for charts

/**
 * Enhanced Chart Panel with professional JFreeChart visualizations
 * Provides comprehensive analytics and insights
 */
public class ChartPanel extends JPanel {
    private StockController controller;
    private JPanel chartContainer;
    private JPanel controlPanel;

    public ChartPanel(StockController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        updateChart();
    }

    private void initComponents() {
        // Header with controls
        createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Main chart container with grid layout
        chartContainer = new JPanel(new GridLayout(2, 2, 15, 15));
        chartContainer.setBackground(new Color(30, 30, 30));
        
        JScrollPane scrollPane = new JScrollPane(chartContainer);
        scrollPane.setBackground(new Color(30, 30, 30));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createControlPanel() {
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(30, 30, 30));
        controlPanel.setPreferredSize(new Dimension(0, 80));

        // Title section
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(30, 30, 30));
        
        JLabel titleLabel = new JLabel("Analytics - Visualisations Avancées");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Controls section
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(new Color(30, 30, 30));

        JButton refreshBtn = createStyledButton("Actualiser", new Color(100, 181, 246));
        refreshBtn.addActionListener(e -> updateChart());

        JButton exportBtn = createStyledButton("Exporter", new Color(76, 175, 80));
        exportBtn.addActionListener(e -> exportCharts());

        buttonsPanel.add(refreshBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(exportBtn);

        controlPanel.add(titlePanel, BorderLayout.WEST);
        controlPanel.add(buttonsPanel, BorderLayout.EAST);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    public void updateChart() {
        chartContainer.removeAll();
        
        List<Produits> produits = controller.getProduits();
        
        if (produits.isEmpty()) {
            showNoDataMessage();
            return;
        }
        
        // Create four different chart types
        chartContainer.add(createStockBarChart(produits));
        chartContainer.add(createValuePieChart(produits));
        chartContainer.add(createDemandAnalysisChart(produits));
        chartContainer.add(createThresholdComparisonChart(produits));
        
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private JPanel createStockBarChart(List<Produits> produits) {
        return createCustomChart("Analyse des Niveaux de Stock", produits, "STOCK_BAR");
    }

    private JPanel createValuePieChart(List<Produits> produits) {
        return createCustomChart("Répartition de la Valeur du Stock", produits, "PIE");
    }

    private JPanel createDemandAnalysisChart(List<Produits> produits) {
        return createCustomChart("Analyse Demande vs Stock", produits, "LINE");
    }

    private JPanel createThresholdComparisonChart(List<Produits> produits) {
        return createCustomChart("Status des Stocks (% du Seuil)", produits, "THRESHOLD");
    }
    
    private JPanel createCustomChart(String title, List<Produits> produits, String chartType) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(new Color(45, 45, 45));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Title
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                g2.drawString(title, titleX, 30);
                
                // Draw chart based on type
                switch (chartType) {
                    case "STOCK_BAR":
                        drawStockBarChart(g2, produits);
                        break;
                    case "PIE":
                        drawPieChart(g2, produits);
                        break;
                    case "LINE":
                        drawLineChart(g2, produits);
                        break;
                    case "THRESHOLD":
                        drawThresholdChart(g2, produits);
                        break;
                }
                
                g2.dispose();
            }
        };
        
        chartPanel.setBackground(new Color(45, 45, 45));
        chartPanel.setPreferredSize(new Dimension(400, 300));
        
        return wrapChartInPanel(chartPanel);
    }
    
    private void drawStockBarChart(Graphics2D g2, List<Produits> produits) {
        if (produits.isEmpty()) return;
        
        int margin = 50;
        int panelWidth = 400;
        int panelHeight = 300;
        int chartWidth = panelWidth - 2 * margin;
        int chartHeight = panelHeight - 100;
        int startY = 50;
        
        // Find max value
        int maxValue = 0;
        for (Produits p : produits) {
            maxValue = Math.max(maxValue, Math.max(p.getQuantite(), (int) p.getSeuil()));
        }
        maxValue = (int) (maxValue * 1.2); // More space at top
        
        // Calculate bar dimensions for better readability
        int numProducts = Math.min(produits.size(), 5); // Limit to 5 for readability
        int totalBars = numProducts * 2;
        int barWidth = Math.max(15, chartWidth / (totalBars + numProducts + 2)); // Minimum width 15
        int groupSpacing = barWidth / 2;
        
        // Draw grid lines for better readability
        g2.setColor(new Color(80, 80, 80));
        g2.setStroke(new BasicStroke(1));
        for (int i = 1; i <= 5; i++) {
            int y = startY + (chartHeight * i) / 5;
            g2.drawLine(margin, y, margin + chartWidth, y);
            
            // Y-axis labels
            g2.setColor(new Color(200, 200, 200));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String label = String.valueOf((maxValue * (5 - i)) / 5);
            g2.drawString(label, margin - 30, y + 4);
        }
        
        // Draw bars
        int x = margin + groupSpacing;
        
        for (int i = 0; i < numProducts; i++) {
            Produits p = produits.get(i);
            
            // Stock bar (blue) - wider and more visible
            int stockHeight = Math.max(3, (int) ((double) p.getQuantite() / maxValue * chartHeight));
            g2.setColor(new Color(100, 181, 246));
            g2.fillRect(x, startY + chartHeight - stockHeight, barWidth, stockHeight);
            
            // Add white border for better visibility
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(x, startY + chartHeight - stockHeight, barWidth, stockHeight);
            
            // Value label on bar
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            String stockValue = String.valueOf(p.getQuantite());
            FontMetrics fm = g2.getFontMetrics();
            int labelX = x + (barWidth - fm.stringWidth(stockValue)) / 2;
            g2.drawString(stockValue, labelX, startY + chartHeight - stockHeight - 5);
            
            x += barWidth + 5; // Small gap between stock and threshold
            
            // Threshold bar (red)
            int thresholdHeight = Math.max(3, (int) ((double) p.getSeuil() / maxValue * chartHeight));
            g2.setColor(new Color(244, 67, 54));
            g2.fillRect(x, startY + chartHeight - thresholdHeight, barWidth, thresholdHeight);
            
            // Add white border
            g2.setColor(Color.WHITE);
            g2.drawRect(x, startY + chartHeight - thresholdHeight, barWidth, thresholdHeight);
            
            // Value label on bar
            String thresholdValue = String.valueOf((int) p.getSeuil());
            int thresholdLabelX = x + (barWidth - fm.stringWidth(thresholdValue)) / 2;
            g2.drawString(thresholdValue, thresholdLabelX, startY + chartHeight - thresholdHeight - 5);
            
            // Product name - better positioned and readable
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String name = p.getNom().length() > 10 ? p.getNom().substring(0, 10) + "..." : p.getNom();
            FontMetrics nameFm = g2.getFontMetrics();
            int nameX = x - barWidth - 5 + (barWidth * 2 + 5 - nameFm.stringWidth(name)) / 2;
            g2.drawString(name, nameX, startY + chartHeight + 15);
            
            x += barWidth + groupSpacing; // Space between product groups
        }
        
        // Enhanced Legend
        drawEnhancedLegend(g2, new String[]{"Stock Actuel", "Seuil Critique"}, 
                          new Color[]{new Color(100, 181, 246), new Color(244, 67, 54)}, 
                          panelWidth, panelHeight);
    }
    
    private void drawPieChart(Graphics2D g2, List<Produits> produits) {
        if (produits.isEmpty()) return;
        
        int panelWidth = 400;
        int panelHeight = 300;
        int centerX = panelWidth / 2;
        int centerY = panelHeight / 2 - 10; // Move up slightly for legend space
        int radius = Math.min(centerX, centerY) - 60; // Larger radius
        
        // Calculate total value
        double totalValue = 0;
        for (Produits p : produits) {
            totalValue += p.getQuantite() * p.getPrixachat();
        }
        
        if (totalValue == 0) return; // Avoid division by zero
        
        // Enhanced colors with better contrast
        Color[] colors = {
            new Color(100, 181, 246), new Color(244, 67, 54),
            new Color(76, 175, 80), new Color(255, 193, 7),
            new Color(156, 39, 176), new Color(255, 87, 34),
            new Color(63, 81, 181), new Color(139, 69, 19)
        };
        
        // Draw pie slices with enhanced visibility
        int startAngle = 0;
        for (int i = 0; i < Math.min(produits.size(), colors.length); i++) {
            Produits p = produits.get(i);
            double value = p.getQuantite() * p.getPrixachat();
            int arcAngle = Math.max(5, (int) (360 * value / totalValue)); // Minimum 5 degrees
            
            // Fill slice
            g2.setColor(colors[i % colors.length]);
            g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcAngle);
            
            // Add white border for better separation
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcAngle);
            
            // Draw percentage label on slice if large enough
            if (arcAngle > 20) { // Only show percentage if slice is large enough
                double percentage = (value / totalValue) * 100;
                double midAngle = Math.toRadians(startAngle + arcAngle / 2);
                int labelRadius = radius / 2;
                int labelX = centerX + (int) (labelRadius * Math.cos(midAngle));
                int labelY = centerY - (int) (labelRadius * Math.sin(midAngle));
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String percentText = String.format("%.1f%%", percentage);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(percentText, 
                             labelX - fm.stringWidth(percentText) / 2, 
                             labelY + fm.getAscent() / 2);
            }
            
            startAngle += arcAngle;
        }
        
        // Enhanced legend with values and percentages
        drawPieChartLegend(g2, produits, colors, totalValue, panelWidth, panelHeight);
    }
    
    private void drawPieChartLegend(Graphics2D g2, List<Produits> produits, Color[] colors, 
                                   double totalValue, int panelWidth, int panelHeight) {
        int legendStartX = 10;
        int legendStartY = panelHeight - 80;
        int itemHeight = 15;
        int maxItems = Math.min(produits.size(), 4); // Show max 4 items in legend
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        
        for (int i = 0; i < maxItems; i++) {
            Produits p = produits.get(i);
            double value = p.getQuantite() * p.getPrixachat();
            double percentage = (value / totalValue) * 100;
            
            int y = legendStartY + i * itemHeight;
            
            // Color box
            g2.setColor(colors[i % colors.length]);
            g2.fillRect(legendStartX, y, 12, 12);
            g2.setColor(Color.WHITE);
            g2.drawRect(legendStartX, y, 12, 12);
            
            // Text
            g2.setColor(Color.WHITE);
            String legendText = String.format("%s: %.1f%% (%.0f DA)", 
                               p.getNom().length() > 12 ? p.getNom().substring(0, 12) + "..." : p.getNom(),
                               percentage, value);
            g2.drawString(legendText, legendStartX + 18, y + 10);
        }
        
        // Show "Others" if there are more items
        if (produits.size() > maxItems) {
            double othersValue = 0;
            for (int i = maxItems; i < produits.size(); i++) {
                othersValue += produits.get(i).getQuantite() * produits.get(i).getPrixachat();
            }
            double othersPercentage = (othersValue / totalValue) * 100;
            
            int y = legendStartY + maxItems * itemHeight;
            g2.setColor(new Color(120, 120, 120));
            g2.fillRect(legendStartX, y, 12, 12);
            g2.setColor(Color.WHITE);
            g2.drawRect(legendStartX, y, 12, 12);
            
            String othersText = String.format("Autres: %.1f%% (%.0f DA)", othersPercentage, othersValue);
            g2.drawString(othersText, legendStartX + 18, y + 10);
        }
    }
    
    private void drawLineChart(Graphics2D g2, List<Produits> produits) {
        if (produits.isEmpty()) return;
        
        int margin = 50;
        int panelWidth = 400;
        int panelHeight = 300;
        int chartWidth = panelWidth - 2 * margin;
        int chartHeight = panelHeight - 90;
        int startY = 45;
        
        // Find max value
        int maxValue = 0;
        for (Produits p : produits) {
            maxValue = Math.max(maxValue, Math.max(p.getQuantite(), p.getDemandeEstimee()));
        }
        maxValue = (int) (maxValue * 1.2);
        
        // Draw enhanced grid with Y-axis labels
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i <= 5; i++) {
            int y = startY + (chartHeight * i) / 5;
            
            // Grid lines
            g2.setColor(new Color(80, 80, 80));
            g2.drawLine(margin, y, margin + chartWidth, y);
            
            // Y-axis labels
            g2.setColor(new Color(200, 200, 200));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String label = String.valueOf((maxValue * (5 - i)) / 5);
            g2.drawString(label, margin - 25, y + 4);
        }
        
        // Draw X-axis labels (product names)
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        int stepX = chartWidth / Math.max(1, Math.min(produits.size(), 6) - 1);
        for (int i = 0; i < Math.min(produits.size(), 6); i++) {
            int x = margin + i * stepX;
            String name = produits.get(i).getNom();
            if (name.length() > 6) name = name.substring(0, 6) + "...";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(name, x - fm.stringWidth(name) / 2, startY + chartHeight + 15);
            
            // Vertical grid lines
            g2.setColor(new Color(60, 60, 60));
            g2.drawLine(x, startY, x, startY + chartHeight);
        }
        
        // Draw enhanced lines with better visibility
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int numPoints = Math.min(produits.size(), 6);
        stepX = chartWidth / Math.max(1, numPoints - 1);
        
        // Stock line (green) with data points
        g2.setColor(new Color(76, 175, 80));
        for (int i = 0; i < numPoints - 1; i++) {
            int x1 = margin + i * stepX;
            int y1 = startY + chartHeight - (int) ((double) produits.get(i).getQuantite() / maxValue * chartHeight);
            int x2 = margin + (i + 1) * stepX;
            int y2 = startY + chartHeight - (int) ((double) produits.get(i + 1).getQuantite() / maxValue * chartHeight);
            g2.drawLine(x1, y1, x2, y2);
        }
        
        // Draw data points for stock with values
        g2.setColor(new Color(76, 175, 80));
        for (int i = 0; i < numPoints; i++) {
            int x = margin + i * stepX;
            int y = startY + chartHeight - (int) ((double) produits.get(i).getQuantite() / maxValue * chartHeight);
            
            // Point circle
            g2.fillOval(x - 6, y - 6, 12, 12);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - 6, y - 6, 12, 12);
            
            // Value label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
            String value = String.valueOf(produits.get(i).getQuantite());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(value, x - fm.stringWidth(value) / 2, y - 10);
            
            g2.setColor(new Color(76, 175, 80));
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        }
        
        // Demand line (orange) with data points
        g2.setColor(new Color(255, 152, 0));
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < numPoints - 1; i++) {
            int x1 = margin + i * stepX;
            int y1 = startY + chartHeight - (int) ((double) produits.get(i).getDemandeEstimee() / maxValue * chartHeight);
            int x2 = margin + (i + 1) * stepX;
            int y2 = startY + chartHeight - (int) ((double) produits.get(i + 1).getDemandeEstimee() / maxValue * chartHeight);
            g2.drawLine(x1, y1, x2, y2);
        }
        
        // Draw data points for demand with values
        g2.setColor(new Color(255, 152, 0));
        for (int i = 0; i < numPoints; i++) {
            int x = margin + i * stepX;
            int y = startY + chartHeight - (int) ((double) produits.get(i).getDemandeEstimee() / maxValue * chartHeight);
            
            // Point circle
            g2.fillOval(x - 6, y - 6, 12, 12);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - 6, y - 6, 12, 12);
            
            // Value label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
            String value = String.valueOf(produits.get(i).getDemandeEstimee());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(value, x - fm.stringWidth(value) / 2, y + 20);
            
            g2.setColor(new Color(255, 152, 0));
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        }
        
        drawEnhancedLegend(g2, new String[]{"Stock Disponible", "Demande Prévue"}, 
                          new Color[]{new Color(76, 175, 80), new Color(255, 152, 0)}, 
                          panelWidth, panelHeight);
    }
    
    private void drawThresholdChart(Graphics2D g2, List<Produits> produits) {
        if (produits.isEmpty()) return;
        
        int margin = 50;
        int panelWidth = 400;
        int panelHeight = 300;
        int chartWidth = panelWidth - 2 * margin;
        int chartHeight = panelHeight - 100;
        int startY = 50;
        
        // Limit to 6 products for better readability
        int numProducts = Math.min(produits.size(), 6);
        int barWidth = Math.max(25, (chartWidth - (numProducts - 1) * 10) / numProducts);
        int spacing = 10;
        int totalWidth = numProducts * barWidth + (numProducts - 1) * spacing;
        int startX = margin + (chartWidth - totalWidth) / 2;
        
        // Draw horizontal reference lines
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(80, 80, 80));
        
        // 50% line (critical)
        int criticalY = startY + (int)(chartHeight * 0.75);
        g2.drawLine(margin, criticalY, margin + chartWidth, criticalY);
        
        // 100% line (threshold)
        int thresholdY = startY + (int)(chartHeight * 0.5);
        g2.drawLine(margin, thresholdY, margin + chartWidth, thresholdY);
        
        // 200% line (normal)
        int normalY = startY + (int)(chartHeight * 0.25);
        g2.drawLine(margin, normalY, margin + chartWidth, normalY);
        
        // Add percentage labels
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.drawString("200%", margin - 30, normalY + 4);
        g2.drawString("100%", margin - 30, thresholdY + 4);
        g2.drawString("50%", margin - 30, criticalY + 4);
        g2.drawString("0%", margin - 20, startY + chartHeight + 4);
        
        int x = startX;
        
        for (int i = 0; i < numProducts; i++) {
            Produits p = produits.get(i);
            double ratio = p.getQuantite() / Math.max(p.getSeuil(), 1);
            int barHeight = Math.max(5, (int) (Math.min(ratio, 3.0) * chartHeight / 3.0)); // Cap at 300%
            
            // Enhanced color coding with gradients
            Color barColor;
            String statusText;
            if (ratio <= 0.5) {
                barColor = new Color(244, 67, 54); // Red - Critical
                statusText = "CRITIQUE";
            } else if (ratio <= 1.0) {
                barColor = new Color(255, 152, 0); // Orange - Low
                statusText = "FAIBLE";
            } else if (ratio <= 2.0) {
                barColor = new Color(76, 175, 80); // Green - Normal
                statusText = "NORMAL";
            } else {
                barColor = new Color(33, 150, 243); // Blue - High
                statusText = "ÉLEVÉ";
            }
            
            // Draw bar with gradient effect
            g2.setColor(barColor);
            g2.fillRect(x, startY + chartHeight - barHeight, barWidth, barHeight);
            
            // Add border for better visibility
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, startY + chartHeight - barHeight, barWidth, barHeight);
            
            // Percentage label on top of bar
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String percentText = String.format("%.0f%%", ratio * 100);
            FontMetrics fm = g2.getFontMetrics();
            int labelX = x + (barWidth - fm.stringWidth(percentText)) / 2;
            g2.drawString(percentText, labelX, startY + chartHeight - barHeight - 8);
            
            // Status text inside bar (if space allows)
            if (barHeight > 30) {
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                FontMetrics statusFm = g2.getFontMetrics();
                int statusX = x + (barWidth - statusFm.stringWidth(statusText)) / 2;
                int statusY = startY + chartHeight - barHeight / 2 + statusFm.getAscent() / 2;
                g2.drawString(statusText, statusX, statusY);
            }
            
            // Product name below bar
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            String name = p.getNom().length() > 8 ? p.getNom().substring(0, 8) + "..." : p.getNom();
            FontMetrics nameFm = g2.getFontMetrics();
            int nameX = x + (barWidth - nameFm.stringWidth(name)) / 2;
            g2.drawString(name, nameX, startY + chartHeight + 15);
            
            // Stock vs Threshold info
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2.setColor(new Color(200, 200, 200));
            String stockInfo = String.format("%d/%d", p.getQuantite(), (int)p.getSeuil());
            FontMetrics stockFm = g2.getFontMetrics();
            int stockX = x + (barWidth - stockFm.stringWidth(stockInfo)) / 2;
            g2.drawString(stockInfo, stockX, startY + chartHeight + 28);
            
            x += barWidth + spacing;
        }
        
        // Enhanced legend for threshold chart
        drawThresholdLegend(g2, panelWidth, panelHeight);
    }
    
    private void drawThresholdLegend(Graphics2D g2, int panelWidth, int panelHeight) {
        String[] labels = {"Critique (<50%)", "Faible (50-100%)", "Normal (100-200%)", "Élevé (>200%)"};
        Color[] colors = {
            new Color(244, 67, 54), new Color(255, 152, 0), 
            new Color(76, 175, 80), new Color(33, 150, 243)
        };
        
        int legendStartY = panelHeight - 25;
        int itemWidth = 80;
        int startX = (panelWidth - itemWidth * colors.length) / 2;
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
        
        for (int i = 0; i < colors.length; i++) {
            int x = startX + i * itemWidth;
            
            // Color box
            g2.setColor(colors[i]);
            g2.fillRect(x, legendStartY, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawRect(x, legendStartY, 10, 10);
            
            // Label
            g2.setColor(Color.WHITE);
            g2.drawString(labels[i], x + 12, legendStartY + 8);
        }
    }
    
    private void drawEnhancedLegend(Graphics2D g2, String[] labels, Color[] colors, 
                                   int panelWidth, int panelHeight) {
        int legendStartY = panelHeight - 25;
        int itemWidth = panelWidth / labels.length;
        
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        for (int i = 0; i < labels.length; i++) {
            int x = 20 + i * itemWidth;
            
            // Color box with border
            g2.setColor(colors[i]);
            g2.fillRect(x, legendStartY, 15, 12);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(x, legendStartY, 15, 12);
            
            // Label text
            g2.setColor(Color.WHITE);
            g2.drawString(labels[i], x + 20, legendStartY + 10);
        }
    }
    
    
    private JPanel wrapChartInPanel(JPanel chartPanel) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rounded border
                g2.setColor(new Color(60, 60, 60));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                
                g2.dispose();
            }
        };
        
        wrapper.setBackground(new Color(45, 45, 45));
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wrapper.add(chartPanel, BorderLayout.CENTER);
        
        return wrapper;
    }

    private void showNoDataMessage() {
        JPanel noDataPanel = new JPanel(new BorderLayout());
        noDataPanel.setBackground(new Color(30, 30, 30));
        
        JLabel messageLabel = new JLabel("Aucune donnée disponible pour l'affichage", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        messageLabel.setForeground(new Color(150, 150, 150));
        
        noDataPanel.add(messageLabel, BorderLayout.CENTER);
        chartContainer.add(noDataPanel);
        
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private void exportCharts() {
        try {
            // Create export directory
            File exportDir = new File("exports");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            // Generate timestamp for unique filenames
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            
            List<Produits> produits = controller.getProduits();
            if (produits.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucune donnée à exporter.", 
                    "Export impossible", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Export each chart type
            exportSingleChart("stock_analysis_" + timestamp + ".png", 
                            "Analyse des Niveaux de Stock", produits, "STOCK_BAR");
            
            exportSingleChart("value_distribution_" + timestamp + ".png", 
                            "Répartition de la Valeur du Stock", produits, "PIE");
            
            exportSingleChart("demand_analysis_" + timestamp + ".png", 
                            "Analyse Demande vs Stock", produits, "LINE");
            
            exportSingleChart("threshold_status_" + timestamp + ".png", 
                            "Status des Stocks", produits, "THRESHOLD");
            
            // Create summary report
            createExportReport(timestamp, produits);
            
            // Show success message
            int result = JOptionPane.showConfirmDialog(this,
                "Export terminé avec succès!\n" +
                "4 graphiques et 1 rapport générés dans le dossier 'exports'.\n\n" +
                "Voulez-vous ouvrir le dossier d'export?",
                "Export Réussi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(exportDir);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Impossible d'ouvrir le dossier: " + exportDir.getAbsolutePath(),
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'export: " + e.getMessage(),
                "Erreur d'Export",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportSingleChart(String filename, String title, List<Produits> produits, String chartType) 
            throws IOException {
        
        int width = 800;
        int height = 600;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        // Enable antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Background
        g2.setColor(new Color(45, 45, 45));
        g2.fillRect(0, 0, width, height);
        
        // Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (width - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, 40);
        
        // Export timestamp
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String exportInfo = "Exporté le: " + new SimpleDateFormat("dd/MM/yyyy à HH:mm").format(new Date());
        g2.drawString(exportInfo, 20, height - 20);
        
        // DSS info
        g2.drawString("DSS - Système d'Aide à la Décision | Université Ibn Khaldoun - Tiaret", 
                     20, height - 40);
        
        // Create a temporary graphics context for chart drawing
        Graphics2D chartG2 = (Graphics2D) g2.create(0, 60, width, height - 120);
        
        // Draw chart based on type
        switch (chartType) {
            case "STOCK_BAR":
                drawStockBarChartExport(chartG2, produits, width, height - 120);
                break;
            case "PIE":
                drawPieChartExport(chartG2, produits, width, height - 120);
                break;
            case "LINE":
                drawLineChartExport(chartG2, produits, width, height - 120);
                break;
            case "THRESHOLD":
                drawThresholdChartExport(chartG2, produits, width, height - 120);
                break;
        }
        
        chartG2.dispose();
        g2.dispose();
        
        // Save image
        File outputFile = new File("exports", filename);
        ImageIO.write(image, "PNG", outputFile);
    }
    
    private void drawStockBarChartExport(Graphics2D g2, List<Produits> produits, int width, int height) {
        if (produits.isEmpty()) return;
        
        int margin = 80;
        int chartWidth = width - 2 * margin;
        int chartHeight = height - 120;
        int startY = 60;
        
        // Find max value
        int maxValue = 0;
        for (Produits p : produits) {
            maxValue = Math.max(maxValue, Math.max(p.getQuantite(), (int) p.getSeuil()));
        }
        maxValue = (int) (maxValue * 1.1);
        
        // Draw bars
        int barWidth = chartWidth / (produits.size() * 2 + 1);
        int x = margin;
        
        for (int i = 0; i < produits.size(); i++) {
            Produits p = produits.get(i);
            
            // Stock bar (blue)
            int stockHeight = (int) ((double) p.getQuantite() / maxValue * chartHeight);
            g2.setColor(new Color(100, 181, 246));
            g2.fillRect(x, startY + chartHeight - stockHeight, barWidth, stockHeight);
            
            // Add value label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString(String.valueOf(p.getQuantite()), 
                         x + barWidth/2 - 10, startY + chartHeight - stockHeight - 5);
            
            x += barWidth;
            
            // Threshold bar (red)
            int thresholdHeight = (int) ((double) p.getSeuil() / maxValue * chartHeight);
            g2.setColor(new Color(244, 67, 54));
            g2.fillRect(x, startY + chartHeight - thresholdHeight, barWidth, thresholdHeight);
            
            // Add value label
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf((int)p.getSeuil()), 
                         x + barWidth/2 - 10, startY + chartHeight - thresholdHeight - 5);
            
            // Product name
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            String name = p.getNom().length() > 10 ? p.getNom().substring(0, 10) + "..." : p.getNom();
            FontMetrics fm = g2.getFontMetrics();
            int nameX = x - barWidth / 2 - fm.stringWidth(name) / 2;
            g2.drawString(name, nameX, startY + chartHeight + 25);
            
            x += barWidth;
        }
        
        // Legend
        drawLegendExport(g2, new String[]{"Stock Actuel", "Seuil Critique"}, 
                        new Color[]{new Color(100, 181, 246), new Color(244, 67, 54)},
                        height);
    }
    
    private void drawPieChartExport(Graphics2D g2, List<Produits> produits, int width, int height) {
        if (produits.isEmpty()) return;
        
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY) - 100;
        
        // Calculate total value
        double totalValue = 0;
        for (Produits p : produits) {
            totalValue += p.getQuantite() * p.getPrixachat();
        }
        
        // Colors for pie slices
        Color[] colors = {
            new Color(100, 181, 246), new Color(244, 67, 54),
            new Color(76, 175, 80), new Color(255, 152, 0),
            new Color(156, 39, 176), new Color(96, 125, 139),
            new Color(121, 85, 72)
        };
        
        int startAngle = 0;
        for (int i = 0; i < produits.size(); i++) {
            Produits p = produits.get(i);
            double value = p.getQuantite() * p.getPrixachat();
            int arcAngle = (int) (360 * value / totalValue);
            
            g2.setColor(colors[i % colors.length]);
            g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcAngle);
            
            startAngle += arcAngle;
        }
        
        // Draw labels with values
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        int legendY = centerY + radius + 40;
        for (int i = 0; i < Math.min(produits.size(), colors.length); i++) {
            Produits p = produits.get(i);
            double value = p.getQuantite() * p.getPrixachat();
            double percentage = (value / totalValue) * 100;
            
            g2.setColor(colors[i % colors.length]);
            g2.fillRect(centerX - 200, legendY + i * 25, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawString(String.format("%s: %.1f%% (%.0f DA)", 
                         p.getNom(), percentage, value), 
                         centerX - 180, legendY + i * 25 + 12);
        }
    }
    
    private void drawLineChartExport(Graphics2D g2, List<Produits> produits, int width, int height) {
        if (produits.isEmpty()) return;
        
        int margin = 80;
        int chartWidth = width - 2 * margin;
        int chartHeight = height - 120;
        int startY = 60;
        
        // Find max value
        int maxValue = 0;
        for (Produits p : produits) {
            maxValue = Math.max(maxValue, Math.max(p.getQuantite(), p.getDemandeEstimee()));
        }
        maxValue = (int) (maxValue * 1.1);
        
        // Draw grid
        g2.setColor(new Color(80, 80, 80));
        g2.setStroke(new BasicStroke(1));
        for (int i = 1; i < 5; i++) {
            int y = startY + (chartHeight * i) / 5;
            g2.drawLine(margin, y, margin + chartWidth, y);
        }
        
        // Draw lines
        g2.setStroke(new BasicStroke(3));
        int stepX = chartWidth / Math.max(1, produits.size() - 1);
        
        // Stock line (green)
        g2.setColor(new Color(76, 175, 80));
        for (int i = 0; i < produits.size() - 1; i++) {
            int x1 = margin + i * stepX;
            int y1 = startY + chartHeight - (int) ((double) produits.get(i).getQuantite() / maxValue * chartHeight);
            int x2 = margin + (i + 1) * stepX;
            int y2 = startY + chartHeight - (int) ((double) produits.get(i + 1).getQuantite() / maxValue * chartHeight);
            g2.drawLine(x1, y1, x2, y2);
            g2.fillOval(x1 - 4, y1 - 4, 8, 8);
        }
        
        // Demand line (orange)
        g2.setColor(new Color(255, 152, 0));
        for (int i = 0; i < produits.size() - 1; i++) {
            int x1 = margin + i * stepX;
            int y1 = startY + chartHeight - (int) ((double) produits.get(i).getDemandeEstimee() / maxValue * chartHeight);
            int x2 = margin + (i + 1) * stepX;
            int y2 = startY + chartHeight - (int) ((double) produits.get(i + 1).getDemandeEstimee() / maxValue * chartHeight);
            g2.drawLine(x1, y1, x2, y2);
            g2.fillOval(x1 - 4, y1 - 4, 8, 8);
        }
        
        drawLegendExport(g2, new String[]{"Stock Disponible", "Demande Prévue"}, 
                        new Color[]{new Color(76, 175, 80), new Color(255, 152, 0)},
                        height);
    }
    
    private void drawThresholdChartExport(Graphics2D g2, List<Produits> produits, int width, int height) {
        if (produits.isEmpty()) return;
        
        int margin = 80;
        int chartWidth = width - 2 * margin;
        int chartHeight = height - 120;
        int startY = 60;
        
        int barWidth = chartWidth / produits.size() - 10;
        int x = margin + 5;
        
        for (Produits p : produits) {
            double ratio = p.getQuantite() / Math.max(p.getSeuil(), 1);
            int barHeight = (int) (ratio * chartHeight / 3); // Scale for visibility
            
            // Color based on ratio
            Color barColor;
            if (ratio <= 0.5) {
                barColor = new Color(244, 67, 54); // Red - Critical
            } else if (ratio <= 1.0) {
                barColor = new Color(255, 152, 0); // Orange - Low
            } else if (ratio <= 2.0) {
                barColor = new Color(76, 175, 80); // Green - Normal
            } else {
                barColor = new Color(33, 150, 243); // Blue - High
            }
            
            g2.setColor(barColor);
            g2.fillRect(x, startY + chartHeight - barHeight, barWidth, barHeight);
            
            // Add percentage label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString(String.format("%.0f%%", ratio * 100), 
                         x + barWidth/2 - 15, startY + chartHeight - barHeight - 5);
            
            // Product name
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            String name = p.getNom().length() > 8 ? p.getNom().substring(0, 8) + "..." : p.getNom();
            FontMetrics fm = g2.getFontMetrics();
            int nameX = x + (barWidth - fm.stringWidth(name)) / 2;
            g2.drawString(name, nameX, startY + chartHeight + 25);
            
            x += barWidth + 10;
        }
        
        // Legend for colors
        String[] labels = {"Critique (<50%)", "Faible (50-100%)", "Normal (100-200%)", "Élevé (>200%)"};
        Color[] colors = {
            new Color(244, 67, 54), new Color(255, 152, 0), 
            new Color(76, 175, 80), new Color(33, 150, 243)
        };
        drawLegendExport(g2, labels, colors, height);
    }
    
    private void drawLegendExport(Graphics2D g2, String[] labels, Color[] colors, int height) {
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        int legendX = 50;
        int legendY = height - 50;
        
        for (int i = 0; i < labels.length; i++) {
            g2.setColor(colors[i]);
            g2.fillRect(legendX, legendY, 20, 15);
            g2.setColor(Color.WHITE);
            g2.drawString(labels[i], legendX + 25, legendY + 12);
            legendX += 150;
            if (legendX > 600) { // Wrap to next line if needed
                legendX = 50;
                legendY += 25;
            }
        }
    }
    
    private void createExportReport(String timestamp, List<Produits> produits) throws IOException {
        StringBuilder report = new StringBuilder();
        
        report.append("RAPPORT D'EXPORT - ANALYTICS DSS\n");
        report.append("================================\n\n");
        report.append("Date d'export: ").append(new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss").format(new Date())).append("\n");
        report.append("Système: DSS - Gestion Intelligente des Stocks\n");
        report.append("Université: Ibn Khaldoun - Tiaret | Module: SAD | L3 ISIL\n\n");
        
        report.append("RÉSUMÉ DES DONNÉES\n");
        report.append("==================\n");
        report.append("Nombre total de produits: ").append(produits.size()).append("\n");
        
        // Calculate summary stats
        int alertes = 0;
        double valeurTotale = 0;
        int stockTotal = 0;
        
        for (Produits p : produits) {
            if (p.getQuantite() <= p.getSeuil()) {
                alertes++;
            }
            valeurTotale += p.getQuantite() * p.getPrixachat();
            stockTotal += p.getQuantite();
        }
        
        report.append("Produits en alerte: ").append(alertes).append("\n");
        report.append("Valeur totale du stock: ").append(String.format("%.2f", valeurTotale)).append(" DA\n");
        report.append("Stock total (unités): ").append(stockTotal).append("\n\n");
        
        report.append("DÉTAIL DES PRODUITS\n");
        report.append("===================\n");
        for (Produits p : produits) {
            report.append(String.format("• %s: %d unités (seuil: %.0f) - Valeur: %.2f DA\n",
                p.getNom(), p.getQuantite(), p.getSeuil(), p.getQuantite() * p.getPrixachat()));
        }
        
        report.append("\n\nFICHIERS EXPORTÉS\n");
        report.append("=================\n");
        report.append("1. stock_analysis_").append(timestamp).append(".png - Analyse des niveaux de stock\n");
        report.append("2. value_distribution_").append(timestamp).append(".png - Répartition de la valeur\n");
        report.append("3. demand_analysis_").append(timestamp).append(".png - Analyse demande vs stock\n");
        report.append("4. threshold_status_").append(timestamp).append(".png - Status des seuils\n");
        report.append("5. export_report_").append(timestamp).append(".txt - Ce rapport\n");
        
        // Write report to file
        File reportFile = new File("exports", "export_report_" + timestamp + ".txt");
        try (java.io.FileWriter writer = new java.io.FileWriter(reportFile)) {
            writer.write(report.toString());
        }
    }
}