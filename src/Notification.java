import java.util.Vector;

public class Notification {

    public static void afficherBienvenue(String nom, String prenom, String type) {
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|         BIENVENUE A LA PHARMACIE APP             |");
        System.out.println("+-------------------------------------------------+");
        System.out.println("|  Connecte en tant que: " + type);
        System.out.println("|  Utilisateur: " + prenom + " " + nom);
        System.out.println("+-------------------------------------------------+\n");
    }

    public static void verifierStockCritique() {
        Vector<Medicament> stockCritique = Medicament.obtenirStockCritique();

        if (!stockCritique.isEmpty()) {
            System.out.println("\n+-------------------------------------------------+");
            System.out.println("|           [!]  ALERTE STOCK CRITIQUE [!]        |");
            System.out.println("+-------------------------------------------------+");

            for (Medicament med : stockCritique) {
                System.out.println("|  - " + med.getNom() + " (" + med.getDosage() + ")");
                System.out.println("|    Stock: " + med.getStock() + " unites");
            }

            System.out.println("+-------------------------------------------------+");
            System.out.println("|  > Merci de passer une commande de reappro.     |");
            System.out.println("+-------------------------------------------------+\n");
        }
    }

    public static void afficherMenu() {
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|              MENU PRINCIPAL                     |");
        System.out.println("+-------------------------------------------------+");
        System.out.println("|  1. Gerer le stock des medicaments              |");
        System.out.println("|  2. Enregistrer une vente                       |");
        System.out.println("|  3. Annuler une vente                           |");
        System.out.println("|  4. Consulter les ventes                        |");
        System.out.println("|  5. Gerer les commandes                         |");
        System.out.println("|  6. Consulter les commandes                     |");
        System.out.println("|  7. Consulter l'historique du stock             |");
        System.out.println("|  8. Afficher les rapports                       |");
        System.out.println("|  0. Quitter                                     |");
        System.out.println("+-------------------------------------------------+");
        System.out.print("> Votre choix: ");
    }

    /**
     * Affiche les options de gestion du stock
     */
    public static void afficherMenuStock() {
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|          GESTION DU STOCK                       |");
        System.out.println("+-------------------------------------------------+");
        System.out.println("|  1. Ajouter un nouveau medicament               |");
        System.out.println("|  2. Consulter tous les medicaments              |");
        System.out.println("|  3. Consulter un medicament                     |");
        System.out.println("|  4. Consulter les stocks critiques              |");
        System.out.println("|  0. Retour                                      |");
        System.out.println("+-------------------------------------------------+");
        System.out.print("> Votre choix: ");
    }

    /**
     * Affiche les options des rapports
     */
    public static void afficherMenuRapports() {
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|              RAPPORTS                           |");
        System.out.println("+-------------------------------------------------+");
        System.out.println("|  1. Rapport des ventes totales                  |");
        System.out.println("|  2. Rapport des stocks actuels                  |");
        System.out.println("|  3. Rapport des stocks critiques                |");
        System.out.println("|  0. Retour                                      |");
        System.out.println("+-------------------------------------------------+");
        System.out.print("> Votre choix: ");
    }

    public static void succes(String message) {
        System.out.println("[OK] " + message);
    }

    public static void erreur(String message) {
        System.err.println("[Error] " + message);
    }

    public static void info(String message) {
        System.out.println("[Info] " + message);
    }

    public static void afficherSeparateur() {
        System.out.println("-------------------------------------------------");
    }
}
