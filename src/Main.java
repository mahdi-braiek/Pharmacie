import java.util.Scanner;
import java.util.Vector;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Pharmacien pharmacienConnecte = null;
    private static Gestionnaire gestionnaireConnecte = null;

    public static void main(String[] args) {

        System.out.println("+-------------------------------------------------+");
        System.out.println("|      BIENVENUE A LA PHARMACIE APP               |");
        System.out.println("|          Gestion de Pharmacie en Java           |");
        System.out.println("+-------------------------------------------------+");

        DBConnection.testConnection();

        boolean appliExecutee = true;

        while (appliExecutee) {
            afficherMenuAuthentification();
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    connecterPharmacien();
                    if (pharmacienConnecte != null) {
                        menuPharmacien();
                    }
                    break;
                case "2":
                    connecterGestionnaire();
                    if (gestionnaireConnecte != null) {
                        menuGestionnaire();
                    }
                    break;
                case "0":
                    System.out.println("\n[OK] Merci d'avoir utilise la Pharmacie App. A bientot!");
                    appliExecutee = false;
                    break;
                default:
                    Notification.erreur("Choix invalide!");
            }
        }

        scanner.close();
    }

    /**
     * Affiche le menu d'authentification initial
     */
    private static void afficherMenuAuthentification() {
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|         AUTHENTIFICATION                        |");
        System.out.println("+-------------------------------------------------+");
        System.out.println("|  1. Se connecter en tant que Pharmacien         |");
        System.out.println("|  2. Se connecter en tant que Gestionnaire       |");
        System.out.println("|  0. Quitter                                     |");
        System.out.println("+-------------------------------------------------+");
        System.out.print("> Votre choix: ");
    }

    private static void connecterPharmacien() {
        System.out.print("\n> Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("> Mot de passe: ");
        String pwd = scanner.nextLine().trim();

        pharmacienConnecte = Pharmacien.authentifier(login, pwd);

        if (pharmacienConnecte != null) {
            Notification.afficherBienvenue(
                    pharmacienConnecte.getNom(),
                    pharmacienConnecte.getPrenom(),
                    "PHARMACIEN");
            Notification.verifierStockCritique();
        } else {
            Notification.erreur("Identifiants invalides!");
            pharmacienConnecte = null;
        }
    }

    private static void connecterGestionnaire() {
        System.out.print("\n> Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("> Mot de passe: ");
        String pwd = scanner.nextLine().trim();

        gestionnaireConnecte = Gestionnaire.authentifier(login, pwd);

        if (gestionnaireConnecte != null) {
            Notification.afficherBienvenue(
                    gestionnaireConnecte.getNom(),
                    gestionnaireConnecte.getPrenom(),
                    "GESTIONNAIRE");
            Notification.verifierStockCritique();
        } else {
            Notification.erreur("Identifiants invalides!");
            gestionnaireConnecte = null;
        }
    }

    private static void menuPharmacien() {
        boolean menuExecute = true;

        while (menuExecute) {
            Notification.afficherMenu();
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    gererStock();
                    break;
                case "2":
                    enregistrerVente();
                    break;
                case "3":
                    annulerVente();
                    break;
                case "4":
                    consulterVentes();
                    break;
                case "5":
                    gererCommandes();
                    break;
                case "6":
                    consulterCommandes();
                    break;
                case "7":
                    consulterHistoriqueStock();
                    break;
                case "8":
                    afficherRapports();
                    break;
                case "0":
                    System.out.println("\n[OK] Deconnexion effectuee.");
                    pharmacienConnecte = null;
                    menuExecute = false;
                    break;
                default:
                    Notification.erreur("Choix invalide!");
            }
        }
    }

    /**
     * Menu principal pour un gestionnaire connecté
     */
    private static void menuGestionnaire() {
        boolean menuExecute = true;

        while (menuExecute) {
            Notification.afficherMenu();
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    gererStock();
                    break;
                case "2":
                case "3":
                case "4":
                    Notification.erreur("Fonction réservée aux pharmaciens!");
                    break;
                case "5":
                    gererCommandes();
                    break;
                case "6":
                    consulterCommandes();
                    break;
                case "7":
                    consulterHistoriqueStock();
                    break;
                case "8":
                    afficherRapports();
                    break;
                case "0":
                    System.out.println("\n[OK] Deconnexion effectuee.");
                    gestionnaireConnecte = null;
                    menuExecute = false;
                    break;
                default:
                    Notification.erreur("Choix invalide!");
            }
        }
    }

    private static void gererStock() {
        boolean gerer = true;

        while (gerer) {
            Notification.afficherMenuStock();
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    ajouterMedicament();
                    break;
                case "2":
                    afficherTousMedicaments();
                    break;
                case "3":
                    consulterMedicament();
                    break;
                case "4":
                    afficherStockCritique();
                    break;
                case "0":
                    gerer = false;
                    break;
                default:
                    Notification.erreur("Choix invalide!");
            }
        }
    }

    private static void ajouterMedicament() {
        System.out.println("\n--- AJOUTER UN MEDICAMENT ---");
        String nom;
        while (true) {
            System.out.print("> Nom du medicament: ");
            nom = scanner.nextLine().trim();
            if (!nom.isEmpty())
                break;
            Notification.erreur("Le nom ne peut pas etre vide!");
        }
        System.out.print("> Dosage: ");
        String dosage = scanner.nextLine().trim();
        System.out.print("> Stock initial: ");
        int stock = lireEntier();
        System.out.print("> Prix unitaire: ");
        double prix = lireDouble();

        if (Medicament.ajouter(nom, dosage, stock, prix)) {
            Notification.succes("Médicament ajouté avec succès!");
        } else {
            Notification.erreur("Erreur lors de l'ajout du médicament!");
        }
    }

    private static void afficherTousMedicaments() {
        System.out.println("\n--- LISTE DE TOUS LES MEDICAMENTS ---");
        Vector<Medicament> medicaments = Medicament.obtenirTous();

        if (medicaments.isEmpty()) {
            Notification.info("Aucun médicament trouvé.");
            return;
        }

        Notification.afficherSeparateur();
        for (Medicament med : medicaments) {
            System.out.printf("ID: %d | %s (%s) | Stock: %d | Prix: %.2f€%n",
                    med.getId_medicament(),
                    med.getNom(),
                    med.getDosage(),
                    med.getStock(),
                    med.getPrix_unitaire());
        }
        Notification.afficherSeparateur();
    }

    private static void consulterMedicament() {
        System.out.println("\n--- CONSULTER UN MEDICAMENT ---");
        System.out.print("> ID du medicament: ");
        int id = lireEntier();

        Medicament med = Medicament.obtenirParId(id);

        if (med != null) {
            System.out.println("\n" + med.toString());
        } else {
            Notification.erreur("Médicament non trouvé!");
        }
    }

    private static void afficherStockCritique() {
        System.out.println("\n--- STOCKS CRITIQUES (< 5 unités) ---");
        Vector<Medicament> stockCritique = Medicament.obtenirStockCritique();

        if (stockCritique.isEmpty()) {
            Notification.succes("Tous les stocks sont suffisants!");
            return;
        }

        Notification.afficherSeparateur();
        for (Medicament med : stockCritique) {
            System.out.printf("[!]  %s (%s) - Stock: %d unites%n",
                    med.getNom(),
                    med.getDosage(),
                    med.getStock());
        }
        Notification.afficherSeparateur();
    }

    /**
     * Enregistre une nouvelle vente
     */
    private static void enregistrerVente() {
        System.out.println("\n--- ENREGISTRER UNE VENTE ---");

        System.out.print("> ID du client: ");
        int idClient = lireEntier();

        Client client = Client.obtenirParId(idClient);
        if (client == null) {
            Notification.erreur("Client non trouvé!");
            return;
        }

        System.out.print("➜ ID du médicament: ");
        int idMedicament = lireEntier();

        Medicament med = Medicament.obtenirParId(idMedicament);
        if (med == null) {
            Notification.erreur("Médicament non trouvé!");
            return;
        }

        System.out.print("> Quantite: ");
        int quantite = lireEntier();

        // Afficher le client et le medicament
        System.out.println("\nClient: " + client.getPrenom() + " " + client.getNom());
        System.out.println("Medicament: " + med.getNom() + " (" + med.getDosage() + ")");
        System.out.printf("Total: %.2f€%n", quantite * med.getPrix_unitaire());

        if (pharmacienConnecte != null) {
            Vente.enregistrerVente(
                    pharmacienConnecte.getId_pharmacien(),
                    idClient,
                    idMedicament,
                    quantite);
        }
    }

    private static void annulerVente() {
        System.out.println("\n--- ANNULER UNE VENTE ---");
        System.out.print("> ID de la vente a annuler: ");
        int idVente = lireEntier();

        if (Vente.annulerVente(idVente)) {
            Notification.succes("Vente annulée!");
        } else {
            Notification.erreur("Erreur lors de l'annulation!");
        }
    }

    private static void consulterVentes() {
        System.out.println("\n--- CONSULTER LES VENTES ---");
        Vector<Vente> ventes = Vente.obtenirTous();

        if (ventes.isEmpty()) {
            Notification.info("Aucune vente enregistrée.");
            return;
        }

        Notification.afficherSeparateur();
        for (Vente vente : ventes) {
            Medicament med = Medicament.obtenirParId(vente.getId_medicament());
            Client client = Client.obtenirParId(vente.getId_client());

            System.out.printf(
                    "Vente #%d | Client: %s %s | %s | Qty: %d | Date: %s%n",
                    vente.getId_vente(),
                    client != null ? client.getPrenom() : "?",
                    client != null ? client.getNom() : "?",
                    med != null ? med.getNom() : "?",
                    vente.getQuantite(),
                    vente.getDate_vente());
        }
        Notification.afficherSeparateur();
    }

    private static void gererCommandes() {
        System.out.println("\n--- CREER UNE COMMANDE ---");
        System.out.print("> ID du medicament: ");
        int idMedicament = lireEntier();

        Medicament med = Medicament.obtenirParId(idMedicament);
        if (med == null) {
            Notification.erreur("Médicament non trouvé!");
            return;
        }

        System.out.print("> Quantite a commander: ");
        int quantite = lireEntier();

        System.out.println("\nMedicament: " + med.getNom() + " (" + med.getDosage() + ")");
        System.out.println("Quantite: " + quantite);

        if (gestionnaireConnecte != null) {
            Commande.creerCommande(
                    gestionnaireConnecte.getId_gestionnaire(),
                    idMedicament,
                    quantite);
        } else if (pharmacienConnecte != null) {
            System.out.println("\n> Fonctionnalite reservee aux gestionnaires.");
        }
    }

    private static void consulterCommandes() {
        System.out.println("\n--- CONSULTER LES COMMANDES ---");
        Vector<Commande> commandes = Commande.obtenirTous();

        if (commandes.isEmpty()) {
            Notification.info("Aucune commande enregistrée.");
            return;
        }

        Notification.afficherSeparateur();
        for (Commande commande : commandes) {
            Medicament med = Medicament.obtenirParId(commande.getId_medicament());

            System.out.printf(
                    "Cmd #%d | %s | Qty: %d | Date: %s%n",
                    commande.getId_commande(),
                    med != null ? med.getNom() : "?",
                    commande.getQuantite(),
                    commande.getDate_commande());
        }
        Notification.afficherSeparateur();
    }

    private static void consulterHistoriqueStock() {
        System.out.println("\n--- HISTORIQUE DU STOCK ---");
        System.out.print("> ID du medicament (ou 0 pour tout l'historique): ");
        int idMedicament = lireEntier();

        Vector<StockHistorique> historique;

        if (idMedicament == 0) {
            historique = StockHistorique.obtenirTout();
        } else {
            historique = StockHistorique.obtenirHistoriqueMedicament(idMedicament);
        }

        if (historique.isEmpty()) {
            Notification.info("Aucun historique trouvé.");
            return;
        }

        Notification.afficherSeparateur();
        for (StockHistorique sh : historique) {
            Medicament med = Medicament.obtenirParId(sh.getId_medicament());
            System.out.printf(
                    "%s | Quantite: %d | Date: %s%n",
                    med != null ? med.getNom() : "?",
                    sh.getQuantite(),
                    sh.getDate_historique());
        }
        Notification.afficherSeparateur();
    }

    private static void afficherRapports() {
        boolean rapports = true;

        while (rapports) {
            Notification.afficherMenuRapports();
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    rapportVentes();
                    break;
                case "2":
                    rapportStocks();
                    break;
                case "3":
                    rapportStocksCritiques();
                    break;
                case "0":
                    rapports = false;
                    break;
                default:
                    Notification.erreur("Choix invalide!");
            }
        }
    }

    private static void rapportVentes() {
        System.out.println("\n--- RAPPORT DES VENTES ---");
        Vector<Vente> ventes = Vente.obtenirTous();

        if (ventes.isEmpty()) {
            Notification.info("Aucune vente enregistrée.");
            return;
        }

        double totalVentes = 0;
        Notification.afficherSeparateur();
        System.out.println("Total des ventes: " + ventes.size());

        for (Vente vente : ventes) {
            Medicament med = Medicament.obtenirParId(vente.getId_medicament());
            if (med != null) {
                double montant = vente.getQuantite() * med.getPrix_unitaire();
                totalVentes += montant;
            }
        }

        System.out.printf("Montant total: %.2f€%n", totalVentes);
        Notification.afficherSeparateur();
    }

    private static void rapportStocks() {
        System.out.println("\n--- RAPPORT DES STOCKS ACTUELS ---");
        Vector<Medicament> medicaments = Medicament.obtenirTous();

        if (medicaments.isEmpty()) {
            Notification.info("Aucun médicament.");
            return;
        }

        int totalUnites = 0;
        double valeurStock = 0;

        Notification.afficherSeparateur();
        for (Medicament med : medicaments) {
            totalUnites += med.getStock();
            valeurStock += med.getStock() * med.getPrix_unitaire();

            System.out.printf(
                    "%s: %d unites (%.2f EUR)%n",
                    med.getNom(),
                    med.getStock(),
                    med.getStock() * med.getPrix_unitaire());
        }

        System.out.println("\nTotal unites en stock: " + totalUnites);
        System.out.printf("Valeur totale du stock: %.2f EUR%n", valeurStock);
        Notification.afficherSeparateur();
    }

    private static void rapportStocksCritiques() {
        System.out.println("\n--- RAPPORT DES STOCKS CRITIQUES ---");
        Vector<Medicament> stockCritique = Medicament.obtenirStockCritique();

        if (stockCritique.isEmpty()) {
            Notification.succes("Tous les stocks sont suffisants!");
            return;
        }

        Notification.afficherSeparateur();
        System.out.println("Medicaments avec stock < 5:");

        for (Medicament med : stockCritique) {
            System.out.printf(
                    "[!]  %s (%s) | Stock: %d unites%n",
                    med.getNom(),
                    med.getDosage(),
                    med.getStock());
        }

        System.out.println("\nNombre total de produits critiques: " + stockCritique.size());
        Notification.afficherSeparateur();
    }

    private static int lireEntier() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Notification.erreur("Veuillez entrer un nombre entier valide!");
                System.out.print("> Reessayez: ");
            }
        }
    }

    private static double lireDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty())
                    return 0.0;
                // Support both dot and comma
                input = input.replace(',', '.');
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                Notification.erreur("Veuillez entrer un nombre valide (ex: 12.50)!");
                System.out.print("> Reessayez: ");
            }
        }
    }
}
