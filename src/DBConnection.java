import java.sql.*;

public class DBConnection {

    private static final String URL = getEnvironment("DB_URL",
            "jdbc:mysql://localhost:3306/gestion_pharmacie");
    private static final String USER = getEnvironment("DB_USER", "root");
    private static final String PASSWORD = getEnvironment("DB_PASSWORD", "");

    private static String getEnvironment(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Driver MySQL non trouvé!");
            System.err.println("Assurez-vous que mysql-connector-j.jar est dans le dossier lib/");
            throw new SQLException("Driver MySQL non chargé", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("[OK] Connexion a la base de donnees reussie!");
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("[Error] Erreur de connexion: " + e.getMessage());
        }
    }
}
