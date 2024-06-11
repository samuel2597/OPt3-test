import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class Gemeentemanagement {
    private static final String Gemeente_Bestand = "Gemeentes.txt";
    List<Gemeente>gemeentes= new ArrayList<>();
    Scanner  scanner= new Scanner (System.in);

    public void voegeenGemeeentetoe() {
        System.out.println("Voer de naam van de gemeente in");
        String naam = scanner.nextLine();
        System.out.println("Voer het aantal inwoners in");
        int aantalInwoners = scanner.nextInt();
        Gemeente gemeente = new Gemeente(naam, aantalInwoners);
        gemeentes.add(gemeente);
        saveNieuwGemeentes(gemeente);
        System.out.printf("Gemeente: %s, Aantal inwoners: %d Aangebodenplaatsen ", gemeente.getNaam(), gemeente.getAantalInwoner(), gemeente.getAangebodenPlaatsen());
        System.out.println();
    }

    public void saveNieuwGemeentes(Gemeente gemeente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Gemeente_Bestand, true))) {
            writer.write(gemeente.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van gemeentes: " + e.getMessage());
        }
    }

    public void saveGemeentes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Gemeente_Bestand))) {
            for (Gemeente g : gemeentes) {
                writer.write(g.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van gemeentes: " + e.getMessage());
        }
    }

    public void laadGemeentes() {
        gemeentes.clear();
        try {
            List<String> lijnen = Files.readAllLines(Paths.get(Gemeente_Bestand));
            for (String lijn : lijnen) {
                // De geserialiseerde vorm is id) Gemeente: naam aantal inwoners: aantal aangeboden plaatsen: aantal
                String[] delen = lijn.split("\\) Gemeente: | aantal inwoners: | aangeboden plaatsen: ");
                if (delen.length == 4) {
                    String id = delen[0].trim();
                    String naam = delen[1].trim();
                    int aantalInwoners = Integer.parseInt(delen[2].trim());
                    int aangebodenPlaatsen = Integer.parseInt(delen[3].trim());

                    // Maak een nieuw Gemeente-object aan en stel de ID expliciet in
                    Gemeente g = new Gemeente(naam, aantalInwoners, id);
                    g.setAangebodenPlaatsen(aangebodenPlaatsen);
                    g.setNaam(naam);
                    gemeentes.add(g);
                }
            }
        } catch (IOException e) {
            System.err.println("Fout bij het laden van gemeentes: " + e.getMessage());
        }
    }

    public void verwijderGemeente(){
        laadGemeentes();
        if(gemeentes.isEmpty()){
            System.out.println("Er zijn geen gemeentes beschikbaar om te verwijderen");
            return;
        }
        System.out.println("Kies een gemeente om te verwijderen");
        for (Gemeente g : gemeentes) {
            System.out.printf("Gemeente: %s, Aantal inwoners:%d, Aangeboden plaatsen: %d", g.getNaam(), g.getAantalInwoner(), g.getAangebodenPlaatsen());
        }

        System.out.println("Voer het number in de gemeente die u wilt verwijderen");
        // Probeer de keuze te verwerken
        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }
        if(keuze>0 && keuze<gemeentes.size()){
            Gemeente gemeenteverijderen=gemeentes.get(keuze);
            gemeentes.remove(gemeenteverijderen);
            saveGemeentes();
            System.out.println("Gemeente " + gemeenteverijderen.getNaam() + " is verwijderd.");
        } else {
            System.out.println("Ongeldige keuze.");
        }

    }

    public void menuGemeentes() {
        boolean doorgaan = true;
        while (doorgaan) {
            System.out.println("=== Gemeentes en AZC's Beheer ===");
            System.out.println("1. Voeg een gemeente toe");
            System.out.println("2. Toon en verwijder gemeente");
            System.out.println("3. Voeg een AZC toe aan een gemeente");
            System.out.println("4. Toon en verwijder AZC's");
            System.out.println("5. Terug naar vorige menu");
            System.out.println("Kies een optie:");
            String keuze = scanner.nextLine();
            switch (keuze) {
                case "1":
                    voegeenGemeeentetoe();
                    break;
                case "2":
                   verwijderGemeente();
                    break;
                case "3":
                    //voegAzcToe();
                    break;
                case "4":
                   // ToonGemeenteenAzcenVerwijder();
                    break;
                case "5":
                    doorgaan = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }



}
