import java.sql.*;
import java.util.Vector;

public class StockHistorique {
    private int id_historique;
    private int id_medicament;
    private int quantite;
    private String date_historique;
    
    public StockHistorique() {
    }
    
    public StockHistorique(int id_historique, int id_medicament, int quantite, String date_historique) {
        this.id_historique = id_historique;
        this.id_medicament = id_medicament;
        this.quantite = quantite;
        this.date_historique = date_historique;
    }
    
    public int getId_historique() { return id_historique; }
    public int getId_medicament() { return id_medicament; }
    public int getQuantite() { return quantite; }
    public String getDate_historique() { return date_historique; }
    
    public void setId_historique(int id) { this.id_historique = id; }
    public void setId_medicament(int id) { this.id_medicament = id; }
    public void setQuantite(int q) { this.quantite = q; }
    public void setDate_historique(String d) { this.date_historique = d; }
    
    /**
     * Ajoute une entrée dans l'historique du stock
     * Appelée automatiquement lors d'une vente ou d'une commande
     * 
     * @param id_medicament ID du médicament
     * @param quantite quantité en stock après modification
     * @return true si ajout réussi, false sinon
     */
    public static boolean ajouterHistorique(int id_medicament, int quantite) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO StockHistorique (id_medicament, quantite, date_historique) VALUES (?, ?, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_medicament);
            stmt.setInt(2, quantite);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout à l'historique: " + e.getMessage());
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
    
    public static Vector<StockHistorique> obtenirHistoriqueMedicament(int id_medicament) {
        Vector<StockHistorique> historique = new Vector<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM StockHistorique WHERE id_medicament = ? ORDER BY date_historique DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_medicament);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                StockHistorique sh = new StockHistorique();
                sh.setId_historique(rs.getInt("id_historique"));
                sh.setId_medicament(rs.getInt("id_medicament"));
                sh.setQuantite(rs.getInt("quantite"));
                sh.setDate_historique(rs.getString("date_historique"));
                historique.add(sh);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return historique;
    }
    
    public static Vector<StockHistorique> obtenirTout() {
        Vector<StockHistorique> historique = new Vector<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM StockHistorique ORDER BY date_historique DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                StockHistorique sh = new StockHistorique();
                sh.setId_historique(rs.getInt("id_historique"));
                sh.setId_medicament(rs.getInt("id_medicament"));
                sh.setQuantite(rs.getInt("quantite"));
                sh.setDate_historique(rs.getString("date_historique"));
                historique.add(sh);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return historique;
    }
    
    @Override
    public String toString() {
        return "StockHistorique {" +
                "id=" + id_historique +
                ", medicament=" + id_medicament +
                ", quantite=" + quantite +
                ", date='" + date_historique + '\'' +
                '}';
    }
}
