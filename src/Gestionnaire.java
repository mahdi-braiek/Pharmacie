import java.sql.*;

public class Gestionnaire {
    private int id_gestionnaire;
    private String nom;
    private String prenom;
    private String login;
    private String pwd;
    
    public Gestionnaire() {
    }
    
    public Gestionnaire(int id_gestionnaire, String nom, String prenom, String login, String pwd) {
        this.id_gestionnaire = id_gestionnaire;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.pwd = pwd;
    }
    
    public int getId_gestionnaire() { return id_gestionnaire; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getLogin() { return login; }
    public String getPwd() { return pwd; }
    
    public void setId_gestionnaire(int id) { this.id_gestionnaire = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setLogin(String login) { this.login = login; }
    public void setPwd(String pwd) { this.pwd = pwd; }
    
    public static Gestionnaire authentifier(String login, String pwd) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Gestionnaire WHERE login = ? AND pwd = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, pwd);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Gestionnaire gestionnaire = new Gestionnaire();
                gestionnaire.setId_gestionnaire(rs.getInt("id_gestionnaire"));
                gestionnaire.setNom(rs.getString("nom"));
                gestionnaire.setPrenom(rs.getString("prenom"));
                gestionnaire.setLogin(rs.getString("login"));
                gestionnaire.setPwd(rs.getString("pwd"));
                return gestionnaire;
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
        return "Gestionnaire {" +
                "id=" + id_gestionnaire +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
