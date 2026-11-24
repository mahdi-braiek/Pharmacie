import java.sql.*;
import java.util.Vector;

public class Commande {
    private int id_commande;
    private int id_gestionnaire;
    private int id_medicament;
    private int quantite;
    private String date_commande;
    
    public Commande() {
    }
    
    public Commande(int id_commande, int id_gestionnaire, int id_medicament, int quantite, String date_commande) {
        this.id_commande = id_commande;
        this.id_gestionnaire = id_gestionnaire;
        this.id_medicament = id_medicament;
        this.quantite = quantite;
        this.date_commande = date_commande;
    }
    
    // Getters
    public int getId_commande() { return id_commande; }
    public int getId_gestionnaire() { return id_gestionnaire; }
    public int getId_medicament() { return id_medicament; }
    public int getQuantite() { return quantite; }
    public String getDate_commande() { return date_commande; }
    
    // Setters
    public void setId_commande(int id) { this.id_commande = id; }
    public void setId_gestionnaire(int id) { this.id_gestionnaire = id; }
    public void setId_medicament(int id) { this.id_medicament = id; }
    public void setQuantite(int q) { this.quantite = q; }
    public void setDate_commande(String d) { this.date_commande = d; }
    
    public static boolean creerCommande(int id_gestionnaire, int id_medicament, int quantite) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO Commande (id_gestionnaire, id_medicament, quantite, date_commande) VALUES (?, ?, ?, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_gestionnaire);
            stmt.setInt(2, id_medicament);
            stmt.setInt(3, quantite);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                Medicament med = Medicament.obtenirParId(id_medicament);
                int nouveau_stock = med.getStock() + quantite;
                Medicament.mettreAJourStock(id_medicament, nouveau_stock);
                StockHistorique.ajouterHistorique(id_medicament, nouveau_stock);
                System.out.println("✓ Commande créée et stock mis à jour!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la commande: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    /**
     * Récupère une commande par son ID
     * 
     * @param id_commande ID de la commande
     * @return Commande si trouvée, null sinon
     */
    public static Commande obtenirParId(int id_commande) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Commande WHERE id_commande = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_commande);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Commande commande = new Commande();
                commande.setId_commande(rs.getInt("id_commande"));
                commande.setId_gestionnaire(rs.getInt("id_gestionnaire"));
                commande.setId_medicament(rs.getInt("id_medicament"));
                commande.setQuantite(rs.getInt("quantite"));
                commande.setDate_commande(rs.getString("date_commande"));
                return commande;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la commande: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    public static Vector<Commande> obtenirTous() {
        Vector<Commande> commandes = new Vector<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Commande ORDER BY date_commande DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId_commande(rs.getInt("id_commande"));
                commande.setId_gestionnaire(rs.getInt("id_gestionnaire"));
                commande.setId_medicament(rs.getInt("id_medicament"));
                commande.setQuantite(rs.getInt("quantite"));
                commande.setDate_commande(rs.getString("date_commande"));
                commandes.add(commande);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return commandes;
    }
    
    @Override
    public String toString() {
        return "Commande {" +
                "id=" + id_commande +
                ", gestionnaire=" + id_gestionnaire +
                ", medicament=" + id_medicament +
                ", quantite=" + quantite +
                ", date='" + date_commande + '\'' +
                '}';
    }
}
