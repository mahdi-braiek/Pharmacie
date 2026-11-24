import java.sql.*;
import java.util.Vector;

public class Vente {
    private int id_vente;
    private int id_pharmacien;
    private int id_client;
    private int id_medicament;
    private int quantite;
    private String date_vente;
    
    public Vente() {
    }
    
    public Vente(int id_vente, int id_pharmacien, int id_client, int id_medicament, int quantite, String date_vente) {
        this.id_vente = id_vente;
        this.id_pharmacien = id_pharmacien;
        this.id_client = id_client;
        this.id_medicament = id_medicament;
        this.quantite = quantite;
        this.date_vente = date_vente;
    }
    
    public int getId_vente() { return id_vente; }
    public int getId_pharmacien() { return id_pharmacien; }
    public int getId_client() { return id_client; }
    public int getId_medicament() { return id_medicament; }
    public int getQuantite() { return quantite; }
    public String getDate_vente() { return date_vente; }
    
    public void setId_vente(int id) { this.id_vente = id; }
    public void setId_pharmacien(int id) { this.id_pharmacien = id; }
    public void setId_client(int id) { this.id_client = id; }
    public void setId_medicament(int id) { this.id_medicament = id; }
    public void setQuantite(int q) { this.quantite = q; }
    public void setDate_vente(String d) { this.date_vente = d; }
    
    public static boolean enregistrerVente(int id_pharmacien, int id_client, int id_medicament, int quantite) {
        Medicament med = Medicament.obtenirParId(id_medicament);
        if (med == null || med.getStock() < quantite) {
            System.err.println("✗ Stock insuffisant! Stock actuel: " + (med != null ? med.getStock() : 0));
            return false;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO Vente (id_pharmacien, id_client, id_medicament, quantite, date_vente) VALUES (?, ?, ?, ?, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_pharmacien);
            stmt.setInt(2, id_client);
            stmt.setInt(3, id_medicament);
            stmt.setInt(4, quantite);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                int nouveau_stock = med.getStock() - quantite;
                Medicament.mettreAJourStock(id_medicament, nouveau_stock);
                StockHistorique.ajouterHistorique(id_medicament, nouveau_stock);
                System.out.println("✓ Vente enregistrée avec succès!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de la vente: " + e.getMessage());
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
    
    public static boolean annulerVente(int id_vente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sqlSelect = "SELECT id_medicament, quantite FROM Vente WHERE id_vente = ?";
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setInt(1, id_vente);
            rs = stmt.executeQuery();
            
            if (!rs.next()) {
                System.err.println("Vente non trouvée!");
                return false;
            }
            
            int id_medicament = rs.getInt("id_medicament");
            int quantite = rs.getInt("quantite");
            
            rs.close();
            stmt.close();
            
            String sqlDelete = "DELETE FROM Vente WHERE id_vente = ?";
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setInt(1, id_vente);
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                Medicament med = Medicament.obtenirParId(id_medicament);
                int nouveau_stock = med.getStock() + quantite;
                Medicament.mettreAJourStock(id_medicament, nouveau_stock);
                StockHistorique.ajouterHistorique(id_medicament, nouveau_stock);
                System.out.println("✓ Vente annulée et stock restauré!");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'annulation de la vente: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    public static Vente obtenirParId(int id_vente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Vente WHERE id_vente = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_vente);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Vente vente = new Vente();
                vente.setId_vente(rs.getInt("id_vente"));
                vente.setId_pharmacien(rs.getInt("id_pharmacien"));
                vente.setId_client(rs.getInt("id_client"));
                vente.setId_medicament(rs.getInt("id_medicament"));
                vente.setQuantite(rs.getInt("quantite"));
                vente.setDate_vente(rs.getString("date_vente"));
                return vente;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la vente: " + e.getMessage());
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
    
    public static Vector<Vente> obtenirTous() {
        Vector<Vente> ventes = new Vector<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Vente ORDER BY date_vente DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Vente vente = new Vente();
                vente.setId_vente(rs.getInt("id_vente"));
                vente.setId_pharmacien(rs.getInt("id_pharmacien"));
                vente.setId_client(rs.getInt("id_client"));
                vente.setId_medicament(rs.getInt("id_medicament"));
                vente.setQuantite(rs.getInt("quantite"));
                vente.setDate_vente(rs.getString("date_vente"));
                ventes.add(vente);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return ventes;
    }
    
    @Override
    public String toString() {
        return "Vente {" +
                "id=" + id_vente +
                ", pharmacien=" + id_pharmacien +
                ", client=" + id_client +
                ", medicament=" + id_medicament +
                ", quantite=" + quantite +
                ", date='" + date_vente + '\'' +
                '}';
    }
}
