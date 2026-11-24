import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginGUI extends JFrame {

    private PharmacieTheme.ModernTextField loginField;
    private PharmacieTheme.ModernPasswordField passwordField;
    private PharmacieTheme.ModernComboBox<String> userTypeCombo;
    private PharmacieTheme.ModernButton loginButton;
    private JLabel statusLabel;
    private Pharmacien pharmacienConnecte = null;
    private Gestionnaire gestionnaireConnecte = null;

    public LoginGUI() {
        PharmacieTheme.setup();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Pharmacie App - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        PharmacieTheme.GradientPanel mainPanel = new PharmacieTheme.GradientPanel(
                PharmacieTheme.BG_DARK, new Color(20, 30, 48));
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        PharmacieTheme.GlassPanel loginContainer = new PharmacieTheme.GlassPanel(24, 0.08f);
        loginContainer.setLayout(new BoxLayout(loginContainer, BoxLayout.Y_AXIS));
        loginContainer.setPreferredSize(new Dimension(380, 520));

        JPanel logoPanel = createLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginContainer.add(logoPanel);
        loginContainer.add(Box.createVerticalStrut(20));

        PharmacieTheme.StyledLabel titleLabel = new PharmacieTheme.StyledLabel(
                "Bienvenue", PharmacieTheme.FONT_TITLE, PharmacieTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginContainer.add(titleLabel);

        PharmacieTheme.StyledLabel subtitleLabel = new PharmacieTheme.StyledLabel(
                "Connectez-vous pour continuer", PharmacieTheme.FONT_BODY, PharmacieTheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginContainer.add(subtitleLabel);
        loginContainer.add(Box.createVerticalStrut(30));

        JPanel typePanel = new JPanel();
        typePanel.setOpaque(false);
        typePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        typePanel.setMaximumSize(new Dimension(320, 60));

        PharmacieTheme.StyledLabel typeLabel = new PharmacieTheme.StyledLabel("Type: ", PharmacieTheme.FONT_BODY);
        String[] userTypes = { "Pharmacien", "Gestionnaire" };
        userTypeCombo = new PharmacieTheme.ModernComboBox<>(userTypes);
        userTypeCombo.setPreferredSize(new Dimension(200, 40));

        typePanel.add(typeLabel);
        typePanel.add(userTypeCombo);
        loginContainer.add(typePanel);
        loginContainer.add(Box.createVerticalStrut(15));

        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setMaximumSize(new Dimension(320, 50));
        loginField = new PharmacieTheme.ModernTextField("Identifiant", 22);
        loginPanel.add(loginField);
        loginContainer.add(loginPanel);
        loginContainer.add(Box.createVerticalStrut(15));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(320, 50));
        passwordField = new PharmacieTheme.ModernPasswordField("Mot de passe", 22);
        passwordPanel.add(passwordField);
        loginContainer.add(passwordPanel);
        loginContainer.add(Box.createVerticalStrut(25));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(320, 55));
        loginButton = new PharmacieTheme.ModernButton("Se connecter");
        loginButton.setPreferredSize(new Dimension(280, 50));
        loginButton.addActionListener(e -> handleLogin());
        buttonPanel.add(loginButton);
        loginContainer.add(buttonPanel);
        loginContainer.add(Box.createVerticalStrut(15));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(PharmacieTheme.FONT_SMALL);
        statusLabel.setForeground(PharmacieTheme.DANGER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginContainer.add(statusLabel);
        loginContainer.add(Box.createVerticalStrut(20));

        PharmacieTheme.StyledLabel footerLabel = new PharmacieTheme.StyledLabel(
                "2026 Pharmacie App", PharmacieTheme.FONT_SMALL, PharmacieTheme.TEXT_MUTED);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginContainer.add(footerLabel);

        mainPanel.add(loginContainer);

        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private JPanel createLogoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 80;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                GradientPaint gradient = new GradientPaint(
                        x, y, PharmacieTheme.PRIMARY,
                        x + size, y + size, PharmacieTheme.SECONDARY);
                g2d.setPaint(gradient);
                g2d.fill(new Ellipse2D.Float(x, y, size, size));
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = x + size / 2;
                int cy = y + size / 2;
                int armLength = 20;
                g2d.drawLine(cx - armLength, cy, cx + armLength, cy);
                g2d.drawLine(cx, cy - armLength, cx, cy + armLength);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        Dimension logoSize = new Dimension(100, 100);
        panel.setPreferredSize(logoSize);
        panel.setMinimumSize(logoSize);
        panel.setMaximumSize(logoSize);
        return panel;
    }

    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String userType = (String) userTypeCombo.getSelectedItem();

        if (login.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs!");
            return;
        }

        statusLabel.setForeground(PharmacieTheme.TEXT_SECONDARY);
        statusLabel.setText("Connexion en cours...");
        loginButton.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Thread.sleep(500);
                if ("Pharmacien".equals(userType)) {
                    pharmacienConnecte = Pharmacien.authentifier(login, password);
                    return pharmacienConnecte != null;
                } else {
                    gestionnaireConnecte = Gestionnaire.authentifier(login, password);
                    return gestionnaireConnecte != null;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        statusLabel.setForeground(PharmacieTheme.SUCCESS);
                        statusLabel.setText("Connexion reussie!");
                        SwingUtilities.invokeLater(() -> {
                            dispose();
                            if (pharmacienConnecte != null) {
                                new MainGUI(pharmacienConnecte, null).setVisible(true);
                            } else {
                                new MainGUI(null, gestionnaireConnecte).setVisible(true);
                            }
                        });
                    } else {
                        showError("Identifiants incorrects!");
                        loginButton.setEnabled(true);
                    }
                } catch (Exception ex) {
                    showError("Erreur de connexion!");
                    loginButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void showError(String message) {
        statusLabel.setForeground(PharmacieTheme.DANGER);
        statusLabel.setText(message);
        Timer timer = new Timer(50, null);
        final int[] count = { 0 };
        final int originalX = getX();
        timer.addActionListener(e -> {
            count[0]++;
            if (count[0] <= 6) {
                int offset = (count[0] % 2 == 0) ? 5 : -5;
                setLocation(originalX + offset, getY());
            } else {
                setLocation(originalX, getY());
                timer.stop();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DBConnection.testConnection();
            } catch (Exception e) {
                System.err.println("Attention: Probleme de connexion a la base de donnees");
            }
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}
