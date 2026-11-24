import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Vector;

public class MainGUI extends JFrame {

    private Pharmacien pharmacien;
    private Gestionnaire gestionnaire;
    private String userRole;
    private String userName;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    private JLabel userInfoLabel;

    private static final String VIEW_DASHBOARD = "dashboard";
    private static final String VIEW_MEDICAMENTS = "medicaments";
    private static final String VIEW_VENTES = "ventes";
    private static final String VIEW_COMMANDES = "commandes";
    private static final String VIEW_CLIENTS = "clients";
    private static final String VIEW_RAPPORTS = "rapports";

    public MainGUI(Pharmacien pharmacien, Gestionnaire gestionnaire) {
        this.pharmacien = pharmacien;
        this.gestionnaire = gestionnaire;
        if (pharmacien != null) {
            this.userRole = "Pharmacien";
            this.userName = pharmacien.getPrenom() + " " + pharmacien.getNom();
        } else {
            this.userRole = "Gestionnaire";
            this.userName = gestionnaire.getPrenom() + " " + gestionnaire.getNom();
        }
        PharmacieTheme.setup();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Pharmacie App - " + userRole);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 700));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(PharmacieTheme.BG_DARK);
        setContentPane(mainPanel);

        sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(PharmacieTheme.BG_DARK);

        JPanel headerPanel = createHeader();
        contentWrapper.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(PharmacieTheme.BG_DARK);

        contentPanel.add(createDashboardPanel(), VIEW_DASHBOARD);
        contentPanel.add(createMedicamentsPanel(), VIEW_MEDICAMENTS);
        contentPanel.add(createVentesPanel(), VIEW_VENTES);
        contentPanel.add(createCommandesPanel(), VIEW_COMMANDES);
        contentPanel.add(createClientsPanel(), VIEW_CLIENTS);
        contentPanel.add(createRapportsPanel(), VIEW_RAPPORTS);

        contentWrapper.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);
        cardLayout.show(contentPanel, VIEW_DASHBOARD);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(PharmacieTheme.BG_MEDIUM);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, PharmacieTheme.BG_LIGHT));

        JPanel logoPanel = createLogoSection();
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(20));

        String[][] menuItems = {
                { "Dashboard", VIEW_DASHBOARD, "D" },
                { "Medicaments", VIEW_MEDICAMENTS, "M" },
                { "Ventes", VIEW_VENTES, "V" },
                { "Commandes", VIEW_COMMANDES, "C" },
                { "Clients", VIEW_CLIENTS, "L" },
                { "Rapports", VIEW_RAPPORTS, "R" }
        };

        for (String[] item : menuItems) {
            JButton menuButton = createMenuButton(item[0], item[1], item[2]);
            if (gestionnaire != null && (item[1].equals(VIEW_VENTES))) {
                menuButton.setEnabled(false);
                menuButton.setToolTipText("Reserve aux pharmaciens");
            }
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(5));
        }

        sidebar.add(Box.createVerticalGlue());

        JPanel userSection = createUserSection();
        sidebar.add(userSection);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setOpaque(false);
        logoutPanel.setMaximumSize(new Dimension(250, 60));

        PharmacieTheme.ModernButton logoutButton = new PharmacieTheme.ModernButton("Deconnexion",
                PharmacieTheme.DANGER);
        logoutButton.setPreferredSize(new Dimension(200, 40));
        logoutButton.addActionListener(e -> handleLogout());
        logoutPanel.add(logoutButton);

        sidebar.add(logoutPanel);
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    private JPanel createLogoSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(250, 120));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 50;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                GradientPaint gradient = new GradientPaint(
                        x, y, PharmacieTheme.PRIMARY,
                        x + size, y + size, PharmacieTheme.SECONDARY);
                g2d.setPaint(gradient);
                g2d.fill(new Ellipse2D.Float(x, y, size, size));
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = x + size / 2;
                int cy = y + size / 2;
                g2d.drawLine(cx - 12, cy, cx + 12, cy);
                g2d.drawLine(cx, cy - 12, cx, cy + 12);
                g2d.dispose();
            }
        };
        logoIcon.setOpaque(false);
        logoIcon.setPreferredSize(new Dimension(60, 60));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoIcon);
        panel.add(Box.createVerticalStrut(10));

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Pharmacie App", PharmacieTheme.FONT_SUBTITLE, PharmacieTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        return panel;
    }

    private JButton createMenuButton(String text, String viewName, String shortcut) {
        JButton button = new JButton("[" + shortcut + "] " + text) {
            private boolean isHovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isHovered || getModel().isPressed()) {
                    g2d.setColor(PharmacieTheme.BG_LIGHT);
                    g2d.fill(new RoundRectangle2D.Float(10, 0, getWidth() - 20, getHeight(), 10, 10));
                    g2d.setColor(PharmacieTheme.PRIMARY);
                    g2d.fill(new RoundRectangle2D.Float(0, 5, 4, getHeight() - 10, 2, 2));
                }
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(PharmacieTheme.FONT_BODY);
        button.setForeground(PharmacieTheme.TEXT_PRIMARY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(250, 45));
        button.setPreferredSize(new Dimension(250, 45));
        button.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        button.addActionListener(e -> cardLayout.show(contentPanel, viewName));

        return button;
    }

    private JPanel createUserSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(250, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JSeparator sep = new JSeparator();
        sep.setForeground(PharmacieTheme.BG_LIGHT);
        sep.setMaximumSize(new Dimension(210, 1));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(15));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoPanel.setOpaque(false);

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(PharmacieTheme.PRIMARY);
                g2d.fill(new Ellipse2D.Float(0, 0, 40, 40));
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String initial = userName.substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (40 - fm.stringWidth(initial)) / 2;
                int y = (40 + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(initial, x, y);
                g2d.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(40, 40));
        infoPanel.add(avatar);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        PharmacieTheme.StyledLabel nameLabel = new PharmacieTheme.StyledLabel(userName, PharmacieTheme.FONT_BODY);
        PharmacieTheme.StyledLabel roleLabel = new PharmacieTheme.StyledLabel(
                userRole, PharmacieTheme.FONT_SMALL, PharmacieTheme.TEXT_SECONDARY);

        textPanel.add(nameLabel);
        textPanel.add(roleLabel);
        infoPanel.add(textPanel);
        panel.add(infoPanel);

        return panel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PharmacieTheme.BG_DARK);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, PharmacieTheme.BG_LIGHT),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));

        userInfoLabel = new JLabel("Dashboard");
        userInfoLabel.setFont(PharmacieTheme.FONT_SUBTITLE);
        userInfoLabel.setForeground(PharmacieTheme.TEXT_PRIMARY);
        header.add(userInfoLabel, BorderLayout.WEST);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);

        Vector<Medicament> stockCritique = Medicament.obtenirStockCritique();
        if (!stockCritique.isEmpty()) {
            JButton alertButton = new JButton("! " + stockCritique.size() + " stock(s) critique(s)");
            alertButton.setFont(PharmacieTheme.FONT_SMALL);
            alertButton.setForeground(PharmacieTheme.WARNING);
            alertButton.setBackground(PharmacieTheme.BG_MEDIUM);
            alertButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PharmacieTheme.WARNING, 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
            alertButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            alertButton.addActionListener(e -> {
                cardLayout.show(contentPanel, VIEW_MEDICAMENTS);
                showStockCritiqueDialog();
            });
            actionsPanel.add(alertButton);
        }

        header.add(actionsPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(PharmacieTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Tableau de bord", PharmacieTheme.FONT_TITLE);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setOpaque(false);

        Vector<Medicament> medicaments = Medicament.obtenirTous();
        Vector<Vente> ventes = Vente.obtenirTous();
        Vector<Client> clients = Client.obtenirTous();
        Vector<Commande> commandes = Commande.obtenirTous();
        Vector<Medicament> stockCritique = Medicament.obtenirStockCritique();

        double totalVentes = 0;
        for (Vente v : ventes) {
            Medicament m = Medicament.obtenirParId(v.getId_medicament());
            if (m != null) {
                totalVentes += v.getQuantite() * m.getPrix_unitaire();
            }
        }

        cardsPanel.add(createStatCard("Medicaments", String.valueOf(medicaments.size()),
                "Total en stock", PharmacieTheme.PRIMARY));
        cardsPanel.add(createStatCard("Ventes", String.valueOf(ventes.size()),
                String.format("%.2f EUR", totalVentes), PharmacieTheme.SUCCESS));
        cardsPanel.add(createStatCard("Clients", String.valueOf(clients.size()),
                "Clients enregistres", PharmacieTheme.SECONDARY));
        cardsPanel.add(createStatCard("Commandes", String.valueOf(commandes.size()),
                "Commandes passees", PharmacieTheme.ACCENT));
        cardsPanel.add(createStatCard("Stock Critique", String.valueOf(stockCritique.size()),
                "Medicaments < 5 unites", PharmacieTheme.WARNING));

        double valeurStock = 0;
        for (Medicament m : medicaments) {
            valeurStock += m.getStock() * m.getPrix_unitaire();
        }
        cardsPanel.add(createStatCard("Valeur Stock", String.format("%.2f EUR", valeurStock),
                "Valeur totale", PharmacieTheme.PRIMARY_LIGHT));

        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        PharmacieTheme.CardPanel card = new PharmacieTheme.CardPanel(title, accentColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                title, PharmacieTheme.FONT_BODY, PharmacieTheme.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));

        PharmacieTheme.StyledLabel valueLabel = new PharmacieTheme.StyledLabel(
                value, PharmacieTheme.FONT_TITLE, accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));

        PharmacieTheme.StyledLabel subtitleLabel = new PharmacieTheme.StyledLabel(
                subtitle, PharmacieTheme.FONT_SMALL, PharmacieTheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(subtitleLabel);

        return card;
    }

    private JPanel createMedicamentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(PharmacieTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Gestion des Medicaments", PharmacieTheme.FONT_TITLE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton addButton = new PharmacieTheme.ModernButton("+ Ajouter", PharmacieTheme.SUCCESS);
        addButton.addActionListener(e -> showAddMedicamentDialog());
        buttonsPanel.add(addButton);

        PharmacieTheme.ModernButton refreshButton = new PharmacieTheme.ModernButton("Actualiser",
                PharmacieTheme.PRIMARY);
        refreshButton.addActionListener(e -> refreshMedicamentsTable());
        buttonsPanel.add(refreshButton);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = createMedicamentsTable();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMedicamentsTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        String[] columns = { "ID", "Nom", "Dosage", "Stock", "Prix (EUR)" };
        Vector<Medicament> medicaments = Medicament.obtenirTous();

        Object[][] data = new Object[medicaments.size()][5];
        for (int i = 0; i < medicaments.size(); i++) {
            Medicament m = medicaments.get(i);
            data[i] = new Object[] {
                    m.getId_medicament(),
                    m.getNom(),
                    m.getDosage(),
                    m.getStock(),
                    String.format("%.2f", m.getPrix_unitaire())
            };
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        PharmacieTheme.ModernTable table = new PharmacieTheme.ModernTable(model);
        table.setName("medicamentsTable");

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int stock = Integer.parseInt(value.toString());
                if (stock < 5) {
                    c.setForeground(PharmacieTheme.DANGER);
                    ((JLabel) c).setText("! " + stock);
                } else if (!isSelected) {
                    c.setForeground(PharmacieTheme.TEXT_PRIMARY);
                }
                c.setBackground(isSelected ? PharmacieTheme.PRIMARY
                        : (row % 2 == 0 ? PharmacieTheme.BG_CARD : PharmacieTheme.BG_MEDIUM));
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                return c;
            }
        });

        PharmacieTheme.ModernScrollPane scrollPane = new PharmacieTheme.ModernScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createVentesPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(PharmacieTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Gestion des Ventes", PharmacieTheme.FONT_TITLE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton addButton = new PharmacieTheme.ModernButton("+ Nouvelle Vente",
                PharmacieTheme.SUCCESS);
        addButton.addActionListener(e -> showNouvelleVenteDialog());
        buttonsPanel.add(addButton);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = createVentesTable();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createVentesTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        String[] columns = { "ID", "Client", "Medicament", "Quantite", "Total (EUR)", "Date" };
        Vector<Vente> ventes = Vente.obtenirTous();

        Object[][] data = new Object[ventes.size()][6];
        for (int i = 0; i < ventes.size(); i++) {
            Vente v = ventes.get(i);
            Client c = Client.obtenirParId(v.getId_client());
            Medicament m = Medicament.obtenirParId(v.getId_medicament());
            String clientName = c != null ? c.getPrenom() + " " + c.getNom() : "Inconnu";
            String medName = m != null ? m.getNom() : "Inconnu";
            double total = m != null ? v.getQuantite() * m.getPrix_unitaire() : 0;
            data[i] = new Object[] {
                    v.getId_vente(),
                    clientName,
                    medName,
                    v.getQuantite(),
                    String.format("%.2f", total),
                    v.getDate_vente()
            };
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        PharmacieTheme.ModernTable table = new PharmacieTheme.ModernTable(model);
        PharmacieTheme.ModernScrollPane scrollPane = new PharmacieTheme.ModernScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createCommandesPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(PharmacieTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Gestion des Commandes", PharmacieTheme.FONT_TITLE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        if (gestionnaire != null) {
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonsPanel.setOpaque(false);
            PharmacieTheme.ModernButton addButton = new PharmacieTheme.ModernButton(
                    "+ Nouvelle Commande", PharmacieTheme.SUCCESS);
            addButton.addActionListener(e -> showNouvelleCommandeDialog());
            buttonsPanel.add(addButton);
            headerPanel.add(buttonsPanel, BorderLayout.EAST);
        }

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = createCommandesTable();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCommandesTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        String[] columns = { "ID", "Medicament", "Quantite", "Date" };
        Vector<Commande> commandes = Commande.obtenirTous();

        Object[][] data = new Object[commandes.size()][4];
        for (int i = 0; i < commandes.size(); i++) {
            Commande c = commandes.get(i);
            Medicament m = Medicament.obtenirParId(c.getId_medicament());
            data[i] = new Object[] {
                    c.getId_commande(),
                    m != null ? m.getNom() : "Inconnu",
                    c.getQuantite(),
                    c.getDate_commande()
            };
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        PharmacieTheme.ModernTable table = new PharmacieTheme.ModernTable(model);
        PharmacieTheme.ModernScrollPane scrollPane = new PharmacieTheme.ModernScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(PharmacieTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Gestion des Clients", PharmacieTheme.FONT_TITLE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton addButton = new PharmacieTheme.ModernButton("+ Ajouter Client",
                PharmacieTheme.SUCCESS);
        addButton.addActionListener(e -> showAddClientDialog());
        buttonsPanel.add(addButton);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = createClientsTable();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createClientsTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        String[] columns = { "ID", "Nom", "Prenom", "Email", "Adresse" };
        Vector<Client> clients = Client.obtenirTous();

        Object[][] data = new Object[clients.size()][5];
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            data[i] = new Object[] {
                    c.getId_client(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getEmail(),
                    c.getAdresse()
            };
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        PharmacieTheme.ModernTable table = new PharmacieTheme.ModernTable(model);
        PharmacieTheme.ModernScrollPane scrollPane = new PharmacieTheme.ModernScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createRapportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(PharmacieTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel("Rapports", PharmacieTheme.FONT_TITLE);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel rapportsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        rapportsGrid.setOpaque(false);

        JButton rapportVentesBtn = createRapportButton("Rapport des Ventes",
                "Voir le resume de toutes les ventes", PharmacieTheme.SUCCESS);
        rapportVentesBtn.addActionListener(e -> showRapportVentes());
        rapportsGrid.add(rapportVentesBtn);

        JButton rapportStocksBtn = createRapportButton("Rapport des Stocks",
                "Etat actuel de l'inventaire", PharmacieTheme.PRIMARY);
        rapportStocksBtn.addActionListener(e -> showRapportStocks());
        rapportsGrid.add(rapportStocksBtn);

        JButton rapportCritiquesBtn = createRapportButton("Stocks Critiques",
                "Medicaments a commander", PharmacieTheme.WARNING);
        rapportCritiquesBtn.addActionListener(e -> showRapportCritiques());
        rapportsGrid.add(rapportCritiquesBtn);

        JButton rapportClientsBtn = createRapportButton("Rapport Clients",
                "Liste des clients", PharmacieTheme.SECONDARY);
        rapportClientsBtn.addActionListener(e -> showRapportClients());
        rapportsGrid.add(rapportClientsBtn);

        panel.add(rapportsGrid, BorderLayout.CENTER);

        return panel;
    }

    private JButton createRapportButton(String title, String description, Color accentColor) {
        JButton button = new JButton() {
            private boolean isHovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isHovered ? PharmacieTheme.BG_LIGHT : PharmacieTheme.BG_CARD);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2d.setColor(accentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, 4, getHeight(), 2, 2));
                g2d.setFont(PharmacieTheme.FONT_HEADING);
                g2d.setColor(PharmacieTheme.TEXT_PRIMARY);
                g2d.drawString(title, 30, 40);
                g2d.setFont(PharmacieTheme.FONT_SMALL);
                g2d.setColor(PharmacieTheme.TEXT_SECONDARY);
                g2d.drawString(description, 30, 65);
                g2d.dispose();
            }
        };

        button.setPreferredSize(new Dimension(300, 100));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void showAddMedicamentDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Medicament", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PharmacieTheme.BG_MEDIUM);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        PharmacieTheme.ModernTextField nomField = new PharmacieTheme.ModernTextField("Nom du medicament");
        PharmacieTheme.ModernTextField dosageField = new PharmacieTheme.ModernTextField("Dosage (ex: 500mg)");
        PharmacieTheme.ModernTextField stockField = new PharmacieTheme.ModernTextField("Stock initial");
        PharmacieTheme.ModernTextField prixField = new PharmacieTheme.ModernTextField("Prix unitaire");

        panel.add(createFormField("Nom:", nomField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Dosage:", dosageField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Stock:", stockField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Prix:", prixField));
        panel.add(Box.createVerticalStrut(25));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton saveButton = new PharmacieTheme.ModernButton("Enregistrer", PharmacieTheme.SUCCESS);
        saveButton.addActionListener(e -> {
            try {
                String nom = nomField.getText().trim();
                String dosage = dosageField.getText().trim();
                int stock = Integer.parseInt(stockField.getText().trim());
                double prix = Double.parseDouble(prixField.getText().trim().replace(",", "."));
                if (nom.isEmpty()) {
                    PharmacieTheme.showError(dialog, "Le nom est obligatoire!");
                    return;
                }
                if (Medicament.ajouter(nom, dosage, stock, prix)) {
                    PharmacieTheme.showSuccess(dialog, "Medicament ajoute avec succes!");
                    dialog.dispose();
                    refreshMedicamentsTable();
                } else {
                    PharmacieTheme.showError(dialog, "Erreur lors de l'ajout!");
                }
            } catch (NumberFormatException ex) {
                PharmacieTheme.showError(dialog, "Veuillez entrer des valeurs numeriques valides!");
            }
        });

        PharmacieTheme.ModernButton cancelButton = new PharmacieTheme.ModernButton("Annuler", PharmacieTheme.BG_LIGHT);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showNouvelleVenteDialog() {
        if (pharmacien == null) {
            PharmacieTheme.showError(this, "Seuls les pharmaciens peuvent enregistrer des ventes!");
            return;
        }

        JDialog dialog = new JDialog(this, "Nouvelle Vente", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PharmacieTheme.BG_MEDIUM);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        Vector<Client> clients = Client.obtenirTous();
        String[] clientItems = new String[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            clientItems[i] = c.getId_client() + " - " + c.getPrenom() + " " + c.getNom();
        }
        PharmacieTheme.ModernComboBox<String> clientCombo = new PharmacieTheme.ModernComboBox<>(clientItems);

        Vector<Medicament> medicaments = Medicament.obtenirTous();
        String[] medItems = new String[medicaments.size()];
        for (int i = 0; i < medicaments.size(); i++) {
            Medicament m = medicaments.get(i);
            medItems[i] = m.getId_medicament() + " - " + m.getNom() + " (" + m.getStock() + " en stock)";
        }
        PharmacieTheme.ModernComboBox<String> medCombo = new PharmacieTheme.ModernComboBox<>(medItems);

        PharmacieTheme.ModernTextField quantiteField = new PharmacieTheme.ModernTextField("Quantite");

        panel.add(createFormField("Client:", clientCombo));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Medicament:", medCombo));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Quantite:", quantiteField));
        panel.add(Box.createVerticalStrut(25));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton saveButton = new PharmacieTheme.ModernButton("Enregistrer", PharmacieTheme.SUCCESS);
        saveButton.addActionListener(e -> {
            try {
                String clientStr = (String) clientCombo.getSelectedItem();
                String medStr = (String) medCombo.getSelectedItem();
                int quantite = Integer.parseInt(quantiteField.getText().trim());
                int idClient = Integer.parseInt(clientStr.split(" - ")[0]);
                int idMed = Integer.parseInt(medStr.split(" - ")[0]);
                if (Vente.enregistrerVente(pharmacien.getId_pharmacien(), idClient, idMed, quantite)) {
                    PharmacieTheme.showSuccess(dialog, "Vente enregistree avec succes!");
                    dialog.dispose();
                    refreshContentPanel();
                } else {
                    PharmacieTheme.showError(dialog, "Erreur: Stock insuffisant ou probleme!");
                }
            } catch (Exception ex) {
                PharmacieTheme.showError(dialog, "Erreur: " + ex.getMessage());
            }
        });

        PharmacieTheme.ModernButton cancelButton = new PharmacieTheme.ModernButton("Annuler", PharmacieTheme.BG_LIGHT);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showNouvelleCommandeDialog() {
        if (gestionnaire == null) {
            PharmacieTheme.showError(this, "Seuls les gestionnaires peuvent passer des commandes!");
            return;
        }

        JDialog dialog = new JDialog(this, "Nouvelle Commande", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PharmacieTheme.BG_MEDIUM);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        Vector<Medicament> medicaments = Medicament.obtenirTous();
        String[] medItems = new String[medicaments.size()];
        for (int i = 0; i < medicaments.size(); i++) {
            Medicament m = medicaments.get(i);
            medItems[i] = m.getId_medicament() + " - " + m.getNom() + " (" + m.getStock() + " en stock)";
        }
        PharmacieTheme.ModernComboBox<String> medCombo = new PharmacieTheme.ModernComboBox<>(medItems);

        PharmacieTheme.ModernTextField quantiteField = new PharmacieTheme.ModernTextField("Quantite a commander");

        panel.add(createFormField("Medicament:", medCombo));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Quantite:", quantiteField));
        panel.add(Box.createVerticalStrut(25));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton saveButton = new PharmacieTheme.ModernButton("Commander", PharmacieTheme.SUCCESS);
        saveButton.addActionListener(e -> {
            try {
                String medStr = (String) medCombo.getSelectedItem();
                int quantite = Integer.parseInt(quantiteField.getText().trim());
                int idMed = Integer.parseInt(medStr.split(" - ")[0]);
                if (Commande.creerCommande(gestionnaire.getId_gestionnaire(), idMed, quantite)) {
                    PharmacieTheme.showSuccess(dialog, "Commande passee avec succes!");
                    dialog.dispose();
                    refreshContentPanel();
                } else {
                    PharmacieTheme.showError(dialog, "Erreur lors de la commande!");
                }
            } catch (Exception ex) {
                PharmacieTheme.showError(dialog, "Erreur: " + ex.getMessage());
            }
        });

        PharmacieTheme.ModernButton cancelButton = new PharmacieTheme.ModernButton("Annuler", PharmacieTheme.BG_LIGHT);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showAddClientDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Client", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PharmacieTheme.BG_MEDIUM);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        PharmacieTheme.ModernTextField nomField = new PharmacieTheme.ModernTextField("Nom");
        PharmacieTheme.ModernTextField prenomField = new PharmacieTheme.ModernTextField("Prenom");
        PharmacieTheme.ModernTextField emailField = new PharmacieTheme.ModernTextField("Email");
        PharmacieTheme.ModernTextField adresseField = new PharmacieTheme.ModernTextField("Adresse");

        panel.add(createFormField("Nom:", nomField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Prenom:", prenomField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Email:", emailField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormField("Adresse:", adresseField));
        panel.add(Box.createVerticalStrut(25));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);

        PharmacieTheme.ModernButton saveButton = new PharmacieTheme.ModernButton("Enregistrer", PharmacieTheme.SUCCESS);
        saveButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();
            if (nom.isEmpty() || prenom.isEmpty()) {
                PharmacieTheme.showError(dialog, "Nom et prenom sont obligatoires!");
                return;
            }
            if (Client.ajouter(nom, prenom, email, adresse)) {
                PharmacieTheme.showSuccess(dialog, "Client ajoute avec succes!");
                dialog.dispose();
                refreshContentPanel();
            } else {
                PharmacieTheme.showError(dialog, "Erreur lors de l'ajout!");
            }
        });

        PharmacieTheme.ModernButton cancelButton = new PharmacieTheme.ModernButton("Annuler", PharmacieTheme.BG_LIGHT);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showStockCritiqueDialog() {
        Vector<Medicament> stockCritique = Medicament.obtenirStockCritique();
        StringBuilder message = new StringBuilder("Medicaments avec stock critique (<5):\n\n");
        for (Medicament m : stockCritique) {
            message.append("- ").append(m.getNom())
                    .append(" (").append(m.getDosage()).append(")")
                    .append(" - Stock: ").append(m.getStock()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Stocks Critiques", JOptionPane.WARNING_MESSAGE);
    }

    private void showRapportVentes() {
        Vector<Vente> ventes = Vente.obtenirTous();
        double total = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPPORT DES VENTES ===\n\n");
        sb.append("Nombre total de ventes: ").append(ventes.size()).append("\n\n");
        for (Vente v : ventes) {
            Medicament m = Medicament.obtenirParId(v.getId_medicament());
            if (m != null) {
                double montant = v.getQuantite() * m.getPrix_unitaire();
                total += montant;
            }
        }
        sb.append("Montant total: ").append(String.format("%.2f EUR", total));
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(PharmacieTheme.FONT_BODY);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Rapport des Ventes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRapportStocks() {
        Vector<Medicament> medicaments = Medicament.obtenirTous();
        int totalUnites = 0;
        double valeur = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPPORT DES STOCKS ===\n\n");
        for (Medicament m : medicaments) {
            totalUnites += m.getStock();
            valeur += m.getStock() * m.getPrix_unitaire();
            sb.append(m.getNom()).append(": ").append(m.getStock())
                    .append(" unites (").append(String.format("%.2f EUR", m.getStock() * m.getPrix_unitaire()))
                    .append(")\n");
        }
        sb.append("\n-----------------------\n");
        sb.append("Total unites: ").append(totalUnites).append("\n");
        sb.append("Valeur totale: ").append(String.format("%.2f EUR", valeur));
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(PharmacieTheme.FONT_BODY);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "Rapport des Stocks", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRapportCritiques() {
        showStockCritiqueDialog();
    }

    private void showRapportClients() {
        Vector<Client> clients = Client.obtenirTous();
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTE DES CLIENTS ===\n\n");
        sb.append("Nombre total: ").append(clients.size()).append("\n\n");
        for (Client c : clients) {
            sb.append("- ").append(c.getPrenom()).append(" ").append(c.getNom())
                    .append("\n  Email: ").append(c.getEmail())
                    .append("\n  Adresse: ").append(c.getAdresse()).append("\n\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(PharmacieTheme.FONT_BODY);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "Rapport des Clients", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(350, 70));
        PharmacieTheme.StyledLabel labelComponent = new PharmacieTheme.StyledLabel(
                label, PharmacieTheme.FONT_BODY, PharmacieTheme.TEXT_SECONDARY);
        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void refreshMedicamentsTable() {
        for (Component c : contentPanel.getComponents()) {
            if (VIEW_MEDICAMENTS.equals(c.getName())) {
                contentPanel.remove(c);
                break;
            }
        }
        contentPanel.add(createMedicamentsPanel(), VIEW_MEDICAMENTS);
        cardLayout.show(contentPanel, VIEW_MEDICAMENTS);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshContentPanel() {
        String currentView = VIEW_DASHBOARD;
        contentPanel.removeAll();
        contentPanel.add(createDashboardPanel(), VIEW_DASHBOARD);
        contentPanel.add(createMedicamentsPanel(), VIEW_MEDICAMENTS);
        contentPanel.add(createVentesPanel(), VIEW_VENTES);
        contentPanel.add(createCommandesPanel(), VIEW_COMMANDES);
        contentPanel.add(createClientsPanel(), VIEW_CLIENTS);
        contentPanel.add(createRapportsPanel(), VIEW_RAPPORTS);
        cardLayout.show(contentPanel, currentView);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleLogout() {
        if (PharmacieTheme.showConfirm(this, "Voulez-vous vraiment vous deconnecter?")) {
            dispose();
            new LoginGUI().setVisible(true);
        }
    }
}
