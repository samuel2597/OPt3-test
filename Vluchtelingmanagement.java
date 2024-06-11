import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Vluchtelingmanagement {
    private static final String Vluchteling_Bestand = "Vluchtelingen.txt";
    private static final String Dossier_Bestand = "Dossier.txt";
    List<Dossier> dossiers= new ArrayList<>();
    List<Land>landen= new ArrayList<>();
    Landmanagement land;
    List<Vluchteling>vluchtelingen= new ArrayList<>();
    Scanner scanner= new Scanner(System.in);
    public void registreerVluchteling() {
        land.laadLanden(); // Ensure countries are loaded

        System.out.println("Voer de naam van de vluchteling in:");
        String naam = scanner.nextLine();

        if (landen.isEmpty()) {
            System.out.println("Er zijn geen landen beschikbaar.");
            return;
        }

        System.out.println("Kies een land van herkomst:");
        for (int i = 0; i < landen.size(); i++) {
            Land l = landen.get(i);
            System.out.printf("%d. %s (Veilig: %b)%n", i + 1, l.getNaam(), l.isVeilig());
        }

        System.out.println("Voer het nummer in van het land van herkomst:");
        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            return;
        }

        if (keuze >= 0 && keuze < landen.size()) {
            Land gekozenLand = landen.get(keuze);
            Vluchteling nieuweVluchteling = new Vluchteling(naam, gekozenLand);
            vluchtelingen.add(nieuweVluchteling);

            Dossier nieuwDossier = new Dossier(nieuweVluchteling.getId(), false, false, false, false, false, false);
            dossiers.add(nieuwDossier);
            saveNieuweVluchtelingDossier(nieuwDossier);
            saveNieuwVuchtelingen(nieuweVluchteling);

            System.out.printf("Nieuwe vluchteling geregistreerd: Gebruikersnaam - %s, Wachtwoord - %s%n", nieuweVluchteling.getGebruikersnaam(), nieuweVluchteling.getWachtwoord());
        } else {
            System.out.println("Ongeldige keuze.");
        }
    }


    public void saveVluchtelingen() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Vluchteling_Bestand, true))) {
            for (Vluchteling v : vluchtelingen) {
                writer.write(v.toString());
                writer.newLine();

            }
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van landen: " + e.getMessage());
        }
    }

    public void saveNieuwVuchtelingen(Vluchteling v) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Vluchteling_Bestand, true))) {
            writer.write(v.toString());
            writer.newLine();


        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van landen: " + e.getMessage());
        }
    }

    public void laadVluchteling() {
        vluchtelingen.clear();
        land.laadLanden(); // Ensure all countries are loaded

        try {
            List<String> lines = Files.readAllLines(Paths.get(Vluchteling_Bestand));
            for (String line : lines) {
                String[] parts = line.split(", ");
                if (parts.length >= 5) {
                    String id = parts[0].substring(parts[0].indexOf(": ") + 2).trim();
                    String naam = parts[1].substring(parts[1].indexOf(": ") + 2).trim();
                    String gebruikersnaam = parts[2].substring(parts[2].indexOf(": ") + 2).trim();
                    String wachtwoord = parts[3].substring(parts[3].indexOf(": ") + 2).trim();
                    String landNaam = parts[4].substring(parts[4].indexOf(": ") + 2).trim();

                    Land correspondingLand = null;
                    for (Land land : landen) {
                        if (land.getNaam().equals(landNaam)) {
                            correspondingLand = land;
                            break;
                        }
                    }

                    if (correspondingLand != null) {
                        Vluchteling vluchteling = new Vluchteling(naam, correspondingLand, id);
                        vluchteling.setGebruikersnaam(gebruikersnaam);
                        vluchteling.setWachtwoord(wachtwoord);
                        vluchtelingen.add(vluchteling);
                    } else {
                        System.out.println("No corresponding land found for: " + landNaam);
                    }
                } else {
                    System.out.println("Incorrect format in line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading refugees: " + e.getMessage());
        }
    }

    public void BewerkVluchtelingDossier() {
        laadVluchteling(); // Laad de meest recente vluchtelinggegevens
        laadVluchtelingDossier();//laadVluchtelingDossier(); // Laad de meest recente dossiers

        if (vluchtelingen.isEmpty()) {
            System.out.println("Er zijn geen vluchtelingen geregistreerd.");
            return;
        }

        System.out.println("Kies een vluchteling om het dossier te bewerken:");
        for (int i = 0; i < vluchtelingen.size(); i++) {
            Vluchteling v = vluchtelingen.get(i);
            System.out.printf("%d. %s (Land van herkomst: %s)\n", i + 1, v.getNaam(), v.getLandVanHerkomst().getNaam());
        }

        System.out.println("Voer het nummer in van de vluchteling die u wilt bewerken:");
        int keuzeIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (keuzeIndex < 0 || keuzeIndex >= vluchtelingen.size()) {
            System.out.println("Ongeldige keuze.");
            return;
        }

        Vluchteling geselecteerdeVluchteling = vluchtelingen.get(keuzeIndex);
        Dossier dossier = laadDossierVoorVluchteling(geselecteerdeVluchteling.getId());
        if (dossier == null) {
            System.out.println("Geen dossier gevonden voor deze vluchteling.");
            return;
        }

        boolean doorgaan = true;
        while (doorgaan) {
            System.out.println("\nDossier van vluchteling: " + geselecteerdeVluchteling.getNaam());
            System.out.println(dossier);
            System.out.println("\n1. Paspoort getoond");
            System.out.println("2. Asielaanvraag compleet");
            System.out.println("3. Rechter toegewezen");
            System.out.println("4. Uitspraak rechter");
            System.out.println("5. Toegelaten tot samenleving");
            System.out.println("6. Terugkeer naar land van herkomst");
            System.out.println("7. Terug naar vorige menu");
            System.out.println("Kies een optie:");

            int optie = Integer.parseInt(scanner.nextLine());
            switch (optie) {
                case 1:
                    toggleDossierStatus(dossier, "Paspoort getoond");
                    break;
                case 2:
                    toggleDossierStatus(dossier, "Asielaanvraag compleet");
                    break;
                case 3:
                    toggleDossierStatus(dossier, "Rechter toegewezen");
                    break;
                case 4:
                    toggleDossierStatus(dossier, "Uitspraak rechter");
                    break;
                case 5:
                    toggleDossierStatus(dossier, "Toegelaten tot samenleving");
                    break;
                case 6:
                    toggleDossierStatus(dossier, "Terugkeer naar land van herkomst");
                    break;
                case 7:
                    doorgaan = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
                    break;
            }
        }
        saveNieuweVluchtelingDossier(dossier); // Sla de wijzigingen op
    }

    private void toggleDossierStatus(Dossier dossier, String veld) {
        boolean validInput = false;
        boolean status = false;

        // Toon de huidige status
        switch (veld) {
            case "Paspoort getoond":
                System.out.println("Huidige status van 'Paspoort getoond': " + (dossier.isPaspoortGetoond() ? "Ja" : "Nee"));
                break;
            case "Asielaanvraag compleet":
                System.out.println("Huidige status van 'Asielaanvraag compleet': " + (dossier.isAsielAanvraagCompleet() ? "Ja" : "Nee"));
                break;
            case "Rechter toegewezen":
                System.out.println("Huidige status van 'Rechter toegewezen': " + (dossier.isRechterToegewezen() ? "Ja" : "Nee"));
                break;
            case "Uitspraak rechter":
                System.out.println("Huidige status van 'Uitspraak rechter': " + (dossier.isUitspraakRechter() ? "Ja" : "Nee"));
                break;
            case "Toegelaten tot samenleving":
                System.out.println("Huidige status van 'Toegelaten tot samenleving': " + (dossier.isToegelatenTotSamenleving() ? "Ja" : "Nee"));
                break;
            case "Terugkeer naar land van herkomst":
                System.out.println("Huidige status van 'Terugkeer naar land van herkomst': " + (dossier.isTerugkeerNaarLandVanHerkomst() ? "Ja" : "Nee"));
                break;
        }

        while (!validInput) {
            System.out.println("Wilt u deze status wijzigen? (1) Ja / (2) Nee:");
            String input = scanner.nextLine();

            if ("1".equals(input)) {
                status = true;
                validInput = true;
            } else if ("2".equals(input)) {
                status = false;
                validInput = true;
            } else {
                System.out.println("Ongeldige invoer. Voer '1' voor Ja of '2' voor Nee.");
            }
        }

        switch (veld) {
            case "Paspoort getoond":
                dossier.setPaspoortGetoond(status);
                break;
            case "Asielaanvraag compleet":
                dossier.setAsielAanvraagCompleet(status);
                break;
            case "Rechter toegewezen":
                dossier.setRechterToegewezen(status);
                break;
            case "Uitspraak rechter":
                dossier.setUitspraakRechter(status);
                break;
            case "Toegelaten tot samenleving":
                dossier.setToegelatenTotSamenleving(status);
                break;
            case "Terugkeer naar land van herkomst":
                dossier.setTerugkeerNaarLandVanHerkomst(status);
                break;
        }

        System.out.println("Status van '" + veld + "' is bijgewerkt naar " + (status ? "Ja" : "Nee"));
    }

    public Dossier laadDossierVoorVluchteling(String vluchtelingId) {
        for (Dossier d : dossiers) {
            if (d.getVluchtelingId().equals(vluchtelingId)) {
                return d;
            }
        }
        return null;  // Return null if no matching dossier is found
    }
    public void saveNieuweVluchtelingDossier(Dossier d) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Dossier_Bestand, true))) {
            writer.write(d.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Fout bij het opslaan van het dossier: " + e.getMessage());
        }
    }

    public void laadVluchtelingDossier() {
        dossiers.clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get(Dossier_Bestand));
            for (String line : lines) {
                String[] parts = line.split(", ");
                if (parts.length == 7) {
                    String vluchtelingId = parts[6].split(": ")[1].trim();
                    boolean paspoortGetoond = Boolean.parseBoolean(parts[0].split(": ")[1].trim());
                    boolean asielAanvraagCompleet = Boolean.parseBoolean(parts[1].split(": ")[1].trim());
                    boolean rechterToegewezen = Boolean.parseBoolean(parts[2].split(": ")[1].trim());
                    boolean uitspraakRechter = Boolean.parseBoolean(parts[3].split(": ")[1].trim());
                    boolean toegelatenTotSamenleving = Boolean.parseBoolean(parts[4].split(": ")[1].trim());
                    boolean terugkeerNaarLandVanHerkomst = Boolean.parseBoolean(parts[5].split(": ")[1].trim());

                    Dossier dossier = new Dossier(vluchtelingId, paspoortGetoond, asielAanvraagCompleet, rechterToegewezen, uitspraakRechter, toegelatenTotSamenleving, terugkeerNaarLandVanHerkomst);
                    dossiers.add(dossier);
                }
            }
        } catch (IOException e) {
            System.err.println("Fout bij het laden van dossiers: " + e.getMessage());
        }
    }


}
