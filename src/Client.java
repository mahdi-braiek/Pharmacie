import java.sql.*;
import java.util.Vector;

public class Client {
    private int id_client;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    
    public Client() {
    }
    
    public Client(int id_client, String nom, String prenom, String email, String adresse) {
        this.id_client = id_client;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
    }
    
    public int getId_client() { return id_client; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    
    public void setId_client(int id) { this.id_client = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public static boolean ajouter(String nom, String prenom, String email, String adresse) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO Client (nom, prenom, email, adresse) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, adresse);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
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
    
    public static Client obtenirParId(int id_client) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Client WHERE id_client = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_client);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Client client = new Client();
                client.setId_client(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                client.setAdresse(rs.getString("adresse"));
                return client;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client: " + e.getMessage());
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
    
    public static Vector<Client> obtenirTous() {
        Vector<Client> clients = new Vector<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM Client";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Client client = new Client();
                client.setId_client(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                client.setAdresse(rs.getString("adresse"));
                clients.add(client);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return clients;
    }
    
    @Override
    public String toString() {
        return "Client {" +
                "id=" + id_client +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
