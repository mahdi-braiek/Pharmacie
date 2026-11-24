import java.sql.*;
import java.util.Vector;

public class Medicament {
    private int id_medicament;
    private String nom;
    private String dosage;
    private int stock;
    private double prix_unitaire;
    
    public Medicament() {
    }
    
    public Medicament(int id_medicament, String nom, String dosage, int stock, double prix_unitaire) {
        this.id_medicament = id_medicament;
        this.nom = nom;
        this.dosage = dosage;
        this.stock = stock;
        this.prix_unitaire = prix_unitaire;
    }
    
    public int getId_medicament() { return id_medicament; }
    public String getNom() { return nom; }
    public String getDosage() { return dosage; }
    public int getStock() { return stock; }
    public double getPrix_unitaire() { return prix_unitaire; }
    
    public void setId_medicament(int id) { this.id_medicament = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrix_unitaire(double prix) { this.prix_unitaire = prix; }
    
    public static boolean ajouter(String nom, String dosage, int stock, double prix_unitaire) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO Medicament (nom, dosage, stock, prix_unitaire) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, dosage);
            stmt.setInt(3, stock);
            stmt.setDouble(4, prix_unitaire);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du médicament: " + e.getMessage());
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
     * Récupère un médicament par son ID
     * 
     * @param id_medicament ID du médicament
     * @return Medicament si trouvé, null sinon
     */
    public static Medicament obtenirParId(int id_medicament) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Medicament WHERE id_medicament = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_medicament);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Medicament medicament = new Medicament();
                medicament.setId_medicament(rs.getInt("id_medicament"));
                medicament.setNom(rs.getString("nom"));
                medicament.setDosage(rs.getString("dosage"));
                medicament.setStock(rs.getInt("stock"));
                medicament.setPrix_unitaire(rs.getDouble("prix_unitaire"));
                return medicament;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du médicament: " + e.getMessage());
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
    
    public static Vector<Medicament> obtenirTous() {
        Vector<Medicament> medicaments = new Vector<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Medicament";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Medicament medicament = new Medicament();
                medicament.setId_medicament(rs.getInt("id_medicament"));
                medicament.setNom(rs.getString("nom"));
                medicament.setDosage(rs.getString("dosage"));
                medicament.setStock(rs.getInt("stock"));
                medicament.setPrix_unitaire(rs.getDouble("prix_unitaire"));
                medicaments.add(medicament);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des médicaments: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return medicaments;
    }
    
    public static boolean mettreAJourStock(int id_medicament, int nouvelle_quantite) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE Medicament SET stock = ? WHERE id_medicament = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nouvelle_quantite);
            stmt.setInt(2, id_medicament);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du stock: " + e.getMessage());
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
    
    public static Vector<Medicament> obtenirStockCritique() {
        Vector<Medicament> medicaments = new Vector<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Medicament WHERE stock < 5";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Medicament medicament = new Medicament();
                medicament.setId_medicament(rs.getInt("id_medicament"));
                medicament.setNom(rs.getString("nom"));
                medicament.setDosage(rs.getString("dosage"));
                medicament.setStock(rs.getInt("stock"));
                medicament.setPrix_unitaire(rs.getDouble("prix_unitaire"));
                medicaments.add(medicament);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des stocks critiques: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return medicaments;
    }
    
    @Override
    public String toString() {
        return "Medicament {" +
                "id=" + id_medicament +
                ", nom='" + nom + '\'' +
                ", dosage='" + dosage + '\'' +
                ", stock=" + stock +
                ", prix=" + prix_unitaire + "€" +
                '}';
    }
}
