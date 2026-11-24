import java.sql.*;

public class Pharmacien {
    private int id_pharmacien;
    private String nom;
    private String prenom;
    private String login;
    private String pwd;
    
    public Pharmacien() {
    }
    
    public Pharmacien(int id_pharmacien, String nom, String prenom, String login, String pwd) {
        this.id_pharmacien = id_pharmacien;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.pwd = pwd;
    }
    
    public int getId_pharmacien() { return id_pharmacien; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getLogin() { return login; }
    public String getPwd() { return pwd; }
    
    public void setId_pharmacien(int id) { this.id_pharmacien = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setLogin(String login) { this.login = login; }
    public void setPwd(String pwd) { this.pwd = pwd; }
    
    public static Pharmacien authentifier(String login, String pwd) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Pharmacien WHERE login = ? AND pwd = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, pwd);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Pharmacien pharmacien = new Pharmacien();
                pharmacien.setId_pharmacien(rs.getInt("id_pharmacien"));
                pharmacien.setNom(rs.getString("nom"));
                pharmacien.setPrenom(rs.getString("prenom"));
                pharmacien.setLogin(rs.getString("login"));
                pharmacien.setPwd(rs.getString("pwd"));
                return pharmacien;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
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
    
    @Override
    public String toString() {
        return "Pharmacien {" +
                "id=" + id_pharmacien +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
