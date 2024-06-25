
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Azcmanagement {
    private static final String Azc_Bestand = "AZC.txt";
    List<Azc> azcs = new ArrayList<>();
    List<Gemeente> gemeentes = new ArrayList<>();
    Gemeentemanagement gemeente;
    Scanner scanner = new Scanner(System.in);

    public Azcmanagement() {
        this.gemeente = new Gemeentemanagement();
    }

    public List<Azc> getAzcs() {
        return new ArrayList<>(azcs);  // Retourneer een kopie om de encapsulatie te behouden
    }

    public void voegAzcToe() {
        gemeente.laadGemeentes();
        this.gemeentes = gemeente.getGemeentes();
        if (gemeentes.isEmpty()) {
            System.out.println("Er zijn geen gemeenten beschikbaar om te wijzigen.");
            return;
        }

        // Toon beschikbare gemeenten
        System.out.println("Kies een gemeente om een AZC toe te voegen:");
        for (int i = 0; i < gemeentes.size(); i++) {
            Gemeente g = gemeentes.get(i);
            System.out.printf("%d. %s (Aantal inwoners: %d, Aangeboden plaatsen: %d)%n", i + 1, g.getNaam(), g.getAantalInwoner(), g.getAangebodenPlaatsen());
        }

        System.out.println("Voer het nummer in van de gemeente waaraan u een AZC wilt toevoegen:");
        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        if (keuze >= 0 && keuze < gemeentes.size()) {
            // Vraag om de gegevens van de nieuwe AZC
            System.out.println("Voer de straat van het AZC in:");
            String straat = scanner.nextLine();
            System.out.println("Voer het nummer van het AZC in:");
            String nummer = scanner.nextLine();
            System.out.println("Voer de postcode van het AZC in:");
            String postcode = scanner.nextLine();
            System.out.println("Voer het aantal aangeboden plaatsen in:");
            int aangebodenPlaatsen = scanner.nextInt();

            // Haal de geselecteerde gemeente op
            Gemeente geselecteerdeGemeente = gemeentes.get(keuze);

            // Maak een nieuw AZC aan
            Azc nieuwAzc = new Azc(straat, nummer, postcode, geselecteerdeGemeente.getID(), aangebodenPlaatsen);

            // Voeg het nieuwe AZC toe aan de lijst van de gemeente
            geselecteerdeGemeente.azcs.add(nieuwAzc);

            // Werk het aantal aangeboden plaatsen van de gemeente bij
            geselecteerdeGemeente.voegAangebodenPlaatsenToe(aangebodenPlaatsen);

            // Voeg het nieuwe AZC toe aan de hoofd AZC-lijst
            azcs.add(nieuwAzc);

            // Geef de bevestiging weer
            System.out.printf("Nieuw AZC toegevoegd aan gemeente %s met adres %s %s, %s%n",
                    geselecteerdeGemeente.getNaam(), straat, nummer, postcode);

            // Sla de gegevens op
            saveNieuweAzcs(nieuwAzc);
            gemeente.saveGemeentes();
        } else {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
        }
    }

    public void saveAzcs() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Azc_Bestand))) {
            for (Azc azc : azcs) {
                writer.write(azc.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van AZC's: " + e.getMessage());
        }
    }

    public void saveNieuweAzcs(Azc azcs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Azc_Bestand, true))) {
            writer.write(azcs.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van AZC's: " + e.getMessage());
        }
    }

    public void laadAzcs() {
        azcs.clear();
        try {
            List<String> lijnen = Files.readAllLines(Paths.get(Azc_Bestand));
            for (String lijn : lijnen) {
                // Update de regex om alle velden correct te splitsen
                String[] delen = lijn.split("\\) Straat: | nummer: | postcode: | Gemeenteid: | aangeboden plaatsen: ");
                if (delen.length == 6) {
                    String id = delen[0].trim(); // UUID
                    String straat = delen[1].trim(); // Straatnaam
                    String nummer = delen[2].trim(); // Huisnummer
                    String postcode = delen[3].trim(); // Postcode
                    String gemeenteId = delen[4].trim(); // Gemeente ID
                    int aangebodenPlaatsen = Integer.parseInt(delen[5].split(",")[0].trim()); // Aantal aangeboden plaatsen

                    // Maak een nieuw Azc-object met de juiste waarden
                    Azc azc = new Azc(straat, nummer, postcode, gemeenteId, aangebodenPlaatsen);
                    azcs.add(azc);
                } else {
                    System.out.println("Fout in het formaat van de regel: " + lijn);
                }
            }
        } catch (IOException e) {
            System.err.println("Fout bij het laden van AZC's: " + e.getMessage());
        }
    }

    public void VerwijderAzc() {
        gemeente.laadGemeentes();
        this.gemeentes = gemeente.getGemeentes();
        laadAzcs();

        if (gemeentes.isEmpty()) {
            System.out.println("Er zijn geen gemeenten beschikbaar om te tonen.");
            return;
        }

        // Toon beschikbare gemeenten
        System.out.println("Kies een gemeente om de AZC's te bekijken:");
        for (int i = 0; i < gemeentes.size(); i++) {
            Gemeente g = gemeentes.get(i);
            System.out.printf("%d. %s (Aantal inwoners: %d, Aangeboden plaatsen: %d)%n", i + 1, g.getNaam(), g.getAantalInwoner(), g.getAangebodenPlaatsen());
        }

        // Vraag de gebruiker om een gemeente te kiezen
        System.out.println("Voer het nummer in van de gemeente waarvan u de AZC's wilt zien:");
        int gemeenteKeuze;
        try {
            gemeenteKeuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        // Controleer of de selectie geldig is
        if (gemeenteKeuze < 0 || gemeenteKeuze >= gemeentes.size()) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        // Haal de geselecteerde gemeente op
        Gemeente geselecteerdeGemeente = gemeentes.get(gemeenteKeuze);
        String geselecteerdeGemeenteId = geselecteerdeGemeente.getID();

        // Filter de AZC's die bij de geselecteerde gemeente horen
        System.out.printf("AZC's in gemeente %s:%n", geselecteerdeGemeente.getNaam());
        List<Azc> gefilterdeAzcs = new ArrayList<>();
        for (Azc azc : azcs) {
            if (azc.getGemeenteId().equals(geselecteerdeGemeenteId)) {
                gefilterdeAzcs.add(azc);
            }
        }

        // Controleer of er AZC's zijn gevonden
        if (gefilterdeAzcs.isEmpty()) {
            System.out.printf("Geen AZC's gevonden in gemeente %s.%n", geselecteerdeGemeente.getNaam());
            return;
        }

        // Toon de lijst van gevonden AZC's met indexen
        for (int i = 0; i < gefilterdeAzcs.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, gefilterdeAzcs.get(i).toString1());
        }

        // Vraag de gebruiker om een AZC te kiezen om te verwijderen
        System.out.println("Voer het nummer in van het AZC dat u wilt verwijderen:");
        int azcKeuze;
        try {
            azcKeuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        // Controleer of de selectie geldig is
        if (azcKeuze < 0 || azcKeuze >= gefilterdeAzcs.size()) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        // Haal het te verwijderen AZC op
        Azc teVerwijderenAzc = gefilterdeAzcs.get(azcKeuze);

        // Verminder het aantal aangeboden plaatsen in de geselecteerde gemeente
        geselecteerdeGemeente.setAangebodenPlaatsen(geselecteerdeGemeente.getAangebodenPlaatsen() - teVerwijderenAzc.getAantalPlaatsen());

        // Verwijder het AZC uit de hoofd AZC-lijst en de lijst van de geselecteerde gemeente
        azcs.remove(teVerwijderenAzc);
        geselecteerdeGemeente.azcs.remove(teVerwijderenAzc);

        // Bevestiging van verwijdering
        System.out.println("Het AZC is verwijderd.");

        // Sla de wijzigingen op
        saveAzcs();
        gemeente.saveGemeentes();
    }

    public void menuAzc(){
        boolean doorgaan=true;
        while(doorgaan) {
            System.out.println("== Azc Beheer ==");
            System.out.println("1. Voeg een azc toe.");
            System.out.println("2. Verwijder een azc");
            System.out.println("3. Terug naar de vorige menu");
            System.out.println( "Kies een van de optie hierboven");
            String keuze= scanner.nextLine();
            switch (keuze){
                case("1"):
                    voegAzcToe();
                    break;
                case("2"):
                    VerwijderAzc();
                    break;
                case("3"):
                    doorgaan=false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw");

            }
        }
    }
}