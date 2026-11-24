import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;

public class PharmacieTheme {

    public static final Color PRIMARY = new Color(45, 136, 255);
    public static final Color PRIMARY_DARK = new Color(25, 100, 200);
    public static final Color PRIMARY_LIGHT = new Color(100, 170, 255);
    public static final Color SECONDARY = new Color(108, 99, 255);
    public static final Color ACCENT = new Color(0, 210, 180);
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color WARNING = new Color(255, 193, 7);
    public static final Color DANGER = new Color(231, 76, 60);

    public static final Color BG_DARK = new Color(15, 23, 42);
    public static final Color BG_MEDIUM = new Color(30, 41, 59);
    public static final Color BG_LIGHT = new Color(51, 65, 85);
    public static final Color BG_CARD = new Color(45, 55, 72);

    public static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    public static void setup() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.put("OptionPane.background", BG_MEDIUM);
        UIManager.put("Panel.background", BG_MEDIUM);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", BG_LIGHT);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", PRIMARY);
        UIManager.put("ComboBox.selectionForeground", TEXT_PRIMARY);
        UIManager.put("List.background", BG_LIGHT);
        UIManager.put("List.foreground", TEXT_PRIMARY);
        UIManager.put("Table.background", BG_CARD);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("TableHeader.background", BG_MEDIUM);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
    }

    public static class GradientPanel extends JPanel {
        private Color startColor;
        private Color endColor;
        private boolean vertical;

        public GradientPanel(Color startColor, Color endColor) {
            this(startColor, endColor, true);
        }

        public GradientPanel(Color startColor, Color endColor, boolean vertical) {
            this.startColor = startColor;
            this.endColor = endColor;
            this.vertical = vertical;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient;
            if (vertical) {
                gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
            } else {
                gradient = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
            }
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    public static class GlassPanel extends JPanel {
        private int cornerRadius = 20;
        private float opacity = 0.15f;

        public GlassPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }

        public GlassPanel(int cornerRadius, float opacity) {
            this.cornerRadius = cornerRadius;
            this.opacity = opacity;
            setOpaque(false);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(255, 255, 255, (int) (opacity * 255)));
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2d.setColor(new Color(255, 255, 255, 50));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius));
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    public static class ModernButton extends JButton {
        private Color bgColor;
        private Color hoverColor;
        private Color pressedColor;
        private boolean isHovered = false;
        private boolean isPressed = false;
        private int cornerRadius = 12;

        public ModernButton(String text) {
            this(text, PRIMARY, PRIMARY_DARK, PRIMARY_LIGHT);
        }

        public ModernButton(String text, Color bgColor) {
            this(text, bgColor, darken(bgColor, 0.15f), lighten(bgColor, 0.15f));
        }

        public ModernButton(String text, Color bgColor, Color pressedColor, Color hoverColor) {
            super(text);
            this.bgColor = bgColor;
            this.hoverColor = hoverColor;
            this.pressedColor = pressedColor;
            setFont(FONT_BUTTON);
            setForeground(TEXT_PRIMARY);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 45));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered = false;
                    isPressed = false;
                    repaint();
                }

                public void mousePressed(java.awt.event.MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                public void mouseReleased(java.awt.event.MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color currentBg = bgColor;
            if (isPressed) {
                currentBg = pressedColor;
            } else if (isHovered) {
                currentBg = hoverColor;
            }
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fill(new RoundRectangle2D.Float(2, 4, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius));
            g2d.setColor(currentBg);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 2, getHeight() - 4, cornerRadius, cornerRadius));
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 6, (getHeight() - 8) / 2, cornerRadius - 2,
                    cornerRadius - 2));
            g2d.dispose();
            super.paintComponent(g);
        }

        public void setCornerRadius(int radius) {
            this.cornerRadius = radius;
            repaint();
        }
    }

    public static class ModernTextField extends JTextField {
        private String placeholder;
        private int cornerRadius = 10;

        public ModernTextField(String placeholder) {
            this(placeholder, 20);
        }

        public ModernTextField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setCaretColor(PRIMARY);
            setOpaque(false);
            setBorder(new EmptyBorder(12, 15, 12, 15));
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }

                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(BG_LIGHT);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            if (hasFocus()) {
                g2d.setColor(PRIMARY);
                g2d.setStroke(new BasicStroke(2f));
            } else {
                g2d.setColor(new Color(100, 116, 139));
                g2d.setStroke(new BasicStroke(1f));
            }
            g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius));
            g2d.dispose();
            super.paintComponent(g);
            if (getText().isEmpty() && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    public static class ModernPasswordField extends JPasswordField {
        private String placeholder;
        private int cornerRadius = 10;

        public ModernPasswordField(String placeholder) {
            this(placeholder, 20);
        }

        public ModernPasswordField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setCaretColor(PRIMARY);
            setOpaque(false);
            setBorder(new EmptyBorder(12, 15, 12, 15));
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }

                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(BG_LIGHT);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            if (hasFocus()) {
                g2d.setColor(PRIMARY);
                g2d.setStroke(new BasicStroke(2f));
            } else {
                g2d.setColor(new Color(100, 116, 139));
                g2d.setStroke(new BasicStroke(1f));
            }
            g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius));
            g2d.dispose();
            super.paintComponent(g);
            if (getPassword().length == 0 && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    public static class ModernTable extends JTable {
        public ModernTable() {
            setup();
        }

        public ModernTable(Object[][] data, Object[] columns) {
            super(data, columns);
            setup();
        }

        public ModernTable(TableModel model) {
            super(model);
            setup();
        }

        private void setup() {
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setBackground(BG_CARD);
            setGridColor(BG_LIGHT);
            setRowHeight(45);
            setShowGrid(true);
            setIntercellSpacing(new Dimension(0, 1));
            setSelectionBackground(PRIMARY);
            setSelectionForeground(TEXT_PRIMARY);
            JTableHeader header = getTableHeader();
            header.setFont(FONT_HEADING);
            header.setForeground(TEXT_PRIMARY);
            header.setBackground(BG_MEDIUM);
            header.setOpaque(true);
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel(value != null ? value.toString() : "");
                    label.setOpaque(true);
                    label.setBackground(BG_MEDIUM);
                    label.setForeground(TEXT_PRIMARY);
                    label.setFont(FONT_HEADING);
                    label.setHorizontalAlignment(JLabel.LEFT);
                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 1, PRIMARY),
                            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
                    return label;
                }
            });
            setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (isSelected) {
                        c.setBackground(PRIMARY);
                        c.setForeground(TEXT_PRIMARY);
                    } else {
                        c.setBackground(row % 2 == 0 ? BG_CARD : BG_MEDIUM);
                        c.setForeground(TEXT_PRIMARY);
                    }
                    ((JLabel) c).setBorder(new EmptyBorder(0, 15, 0, 15));
                    return c;
                }
            });
        }
    }

    public static class CardPanel extends JPanel {
        private Color accentColor;
        private int cornerRadius = 16;

        public CardPanel(String title, Color accentColor) {
            this.accentColor = accentColor;
            setOpaque(false);
            setLayout(new BorderLayout(10, 10));
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius));
            g2d.setColor(BG_CARD);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius));
            g2d.setColor(accentColor);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, 4, 2, 2));
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    public static class StyledLabel extends JLabel {
        public StyledLabel(String text) {
            this(text, FONT_BODY, TEXT_PRIMARY);
        }

        public StyledLabel(String text, Font font) {
            this(text, font, TEXT_PRIMARY);
        }

        public StyledLabel(String text, Font font, Color color) {
            super(text);
            setFont(font);
            setForeground(color);
        }
    }

    public static class ModernScrollPane extends JScrollPane {
        public ModernScrollPane(Component view) {
            super(view);
            setBorder(null);
            setBackground(BG_MEDIUM);
            getViewport().setBackground(BG_MEDIUM);
            getVerticalScrollBar().setUI(new ModernScrollBarUI());
            getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        }
    }

    public static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = PRIMARY;
            this.trackColor = BG_DARK;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createEmptyButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createEmptyButton();
        }

        private JButton createEmptyButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(thumbColor);
            g2d.fill(new RoundRectangle2D.Float(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 8, 8));
            g2d.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(trackColor);
            g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2d.dispose();
        }
    }

    public static class ModernComboBox<E> extends JComboBox<E> {
        public ModernComboBox(E[] items) {
            super(items);
            setFont(FONT_BODY);
            setBackground(BG_LIGHT);
            setForeground(TEXT_PRIMARY);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = new JLabel(value != null ? value.toString() : "");
                    label.setOpaque(true);
                    label.setBackground(isSelected ? PRIMARY : BG_LIGHT);
                    label.setForeground(TEXT_PRIMARY);
                    label.setFont(FONT_BODY);
                    label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    return label;
                }
            });
        }
    }

    public static Color darken(Color color, float factor) {
        return new Color(
                Math.max(0, (int) (color.getRed() * (1 - factor))),
                Math.max(0, (int) (color.getGreen() * (1 - factor))),
                Math.max(0, (int) (color.getBlue() * (1 - factor))),
                color.getAlpha());
    }

    public static Color lighten(Color color, float factor) {
        return new Color(
                Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor)),
                Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor)),
                Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor)),
                color.getAlpha());
    }

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Succes", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}
