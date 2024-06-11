import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Landmanagement {
    private static final String Landen_Bestand = "Landen.txt"; // Het pad naar het bestand
    private List<Land> landen = new ArrayList<>(); // Een lijst om landen op te slaan
    Scanner scanner= new Scanner(System.in);

    public void voegEenLandToe() {
        System.out.println("Voer de naam van het land in:");
        String naam = scanner.nextLine();

        boolean isVeilig = false;
        boolean isGeldigeInvoer = false;

        do {
            System.out.println("Is het land veilig? \n1) Ja \n2) Nee:");
            String keuze = scanner.nextLine();
            switch (keuze) {
                case "1":
                    isVeilig = true;
                    isGeldigeInvoer = true;
                    break;
                case "2":
                    isVeilig = false;
                    isGeldigeInvoer = true;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer het opnieuw.");
                    break;
            }
        } while (!isGeldigeInvoer);

        Land land = new Land(naam, isVeilig);
        landen.add(land);
        System.out.printf("Land: %s, Veilig: %b%n", land.getNaam(), land.isVeilig());
        saveNieuwLand(land);
    }

    public void saveLanden() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Landen_Bestand))) {
            for (Land l : landen) {
                writer.write(l.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van landen: " + e.getMessage());
        }
    }

    public void saveNieuwLand(Land l) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Landen_Bestand, true))) {
            writer.write(l.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van landen: " + e.getMessage());
        }
    }

    public void laadLanden() {
        landen.clear();
        try {
            List<String> lijnen = Files.readAllLines(Paths.get(Landen_Bestand));
            for (String lijn : lijnen) {
                String[] delen = lijn.split(",");
                if (delen.length == 3) {
                    String id = delen[0];
                    String naam = delen[1];
                    boolean isVeilig = Boolean.parseBoolean(delen[2]);
                    Land land = new Land(naam, isVeilig);
                    land.getId(); // Behoud het oorspronkelijke ID
                    landen.add(land);
                }
            }
        } catch (IOException e) {
            System.err.println("Fout bij het laden van landen: " + e.getMessage());
        }
    }

    public void wijzigLanden() {
        laadLanden();
        if (landen.isEmpty()) {
            System.out.println("Er zijn geen landen beschikbaar om te wijzigen.");
            return;
        }

        // Toon landen met numerieke opties
        System.out.println("Kies een land om te wijzigen:");
        for (int i = 0; i < landen.size(); i++) {
            Land l = landen.get(i);
            System.out.printf("%d. %s (Veilig: %b)%n", i + 1, l.getNaam(), l.isVeilig());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Voer het nummer in van het land dat u wilt wijzigen:");

        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        if (keuze >= 0 && keuze < landen.size()) {
            Land land = landen.get(keuze);
            boolean isVeilig = false;
            boolean isGeldigeInvoer = false;

            do {
                System.out.println("Is het land veilig? \n1) Ja \n2) Nee:");
                String keuzen = scanner.nextLine();
                switch (keuzen) {
                    case "1":
                        isVeilig = true;
                        isGeldigeInvoer = true;
                        break;
                    case "2":
                        isVeilig = false;
                        isGeldigeInvoer = true;
                        break;
                    default:
                        System.out.println("Ongeldige keuze. Probeer het opnieuw.");
                        break;
                }
            } while (!isGeldigeInvoer);
            land.setVeilig(isVeilig);
            System.out.printf("Land: %s, Veilig: %b%n", land.getNaam(), land.isVeilig());
            saveLanden();
        }

    }

    public void toonEnVerwijderLanden() {
        laadLanden();

        if (landen.isEmpty()) {
            System.out.println("Er zijn geen landen beschikbaar om te verwijderen.");
            return;
        }

        // Toon landen met numerieke opties
        System.out.println("Kies een land om te verwijderen:");
        for (int i = 0; i < landen.size(); i++) {
            Land l = landen.get(i);
            System.out.printf("%d. %s (Veilig: %b)%n", i + 1, l.getNaam(), l.isVeilig());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Voer het nummer in van het land dat u wilt verwijderen:");

        // Probeer de keuze te verwerken
        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        if (keuze >= 0 && keuze < landen.size()) {
            Land teVerwijderen = landen.get(keuze);
            landen.remove(teVerwijderen);
            saveLanden();
            System.out.println("Land " + teVerwijderen.getNaam() + " is verwijderd.");
        } else {
            System.out.println("Ongeldige keuze.");
        }
    }

    public void menuLanden() {
        boolean doorgaan = true;
        while (doorgaan) {
            System.out.println("=== Landen Beheer ===");
            System.out.println("1. Voeg een land toe");
            System.out.println("2. Wijzig de status van een land");
            System.out.println("3. Verwijder een land");
            System.out.println("4. Terug naar vorige menu");
            System.out.println("Kies een optie:");
            String keuze = scanner.nextLine();
            switch (keuze) {
                case "1":
                    voegEenLandToe();
                    break;
                case "2":
                    wijzigLanden();
                    break;
                case "3":
                    toonEnVerwijderLanden();
                    break;
                case "4":
                    doorgaan = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }


}
