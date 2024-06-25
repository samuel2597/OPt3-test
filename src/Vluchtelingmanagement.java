import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Vluchtelingmanagement implements Subject {
    private static final String Vluchteling_Bestand = "Vluchtelingen.txt";
    private static final String Dossier_Bestand = "Dossier.txt";
    private final List<Dossier> dossiers = new ArrayList<>();
    private List<Land> landen = new ArrayList<>();
    private final List<Vluchteling> vluchtelingen = new ArrayList<>();
    private final Landmanagement land;
    private final Scanner scanner = new Scanner(System.in);

    public Vluchtelingmanagement() {
        this.land = new Landmanagement();
        this.land.laadLanden();
        this.landen = land.getLand();
        laadVluchteling();
    }

    @Override
    public void registerObserver(Observer o) {
        if (o instanceof Vluchteling) {
            vluchtelingen.add((Vluchteling) o);
        }
    }


    public void notifySpecificObserver(Vluchteling vluchteling, String message) {
        vluchteling.update(message);
    }

    public List<Vluchteling> getVluchtelingen() {
        return new ArrayList<>(vluchtelingen);  // Retourneer een kopie om de encapsulatie te behouden
    }

    public void registreerVluchteling() {
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
            saveNieuwVluchtelingen(nieuweVluchteling);

            registerObserver(nieuweVluchteling);  // Register the new refugee as an observer

            System.out.printf("Nieuwe vluchteling geregistreerd: Gebruikersnaam - %s, Wachtwoord - %s%n", nieuweVluchteling.getGebruikersnaam(), nieuweVluchteling.getWachtwoord());
        } else {
            System.out.println("Ongeldige keuze.");
        }
    }



    public void saveNieuwVluchtelingen(Vluchteling v) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Vluchteling_Bestand, true))) {
            writer.write(v.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het opslaan van vluchtelingen: " + e.getMessage());
        }
    }

    public void laadVluchteling() {
        vluchtelingen.clear();
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
                        if (land.getNaam().equalsIgnoreCase(landNaam.trim())) {
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
        laadVluchteling();
        laadVluchtelingDossier();

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
            String veld = null;
            String notificationMessage = null;

            switch (optie) {
                case 1:
                    veld = "Paspoort getoond";
                    notificationMessage = toggleDossierStatus(dossier, veld);
                    break;
                case 2:
                    veld = "Asielaanvraag compleet";
                    notificationMessage = toggleDossierStatus(dossier, veld);
                    break;
                case 3:
                    veld = "Rechter toegewezen";
                    notificationMessage = toggleDossierStatus(dossier, veld);
                    break;
                case 4:
                    veld = "Uitspraak rechter";
                    notificationMessage = toggleDossierStatus(dossier, veld);
                    break;
                case 5:
                    veld = "Toegelaten tot samenleving";
                    notificationMessage = toggleDossierStatus(dossier, veld);
                    break;
                case 6:
                    veld = "Terugkeer naar land van herkomst";
                    notificationMessage = toggleDossierStatus(dossier, veld);
                    break;
                case 7:
                    doorgaan = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
                    break;
            }

            if (notificationMessage != null) {
                notifySpecificObserver(geselecteerdeVluchteling, notificationMessage);
            }

            saveVluchtelingDossier();
        }
    }


    private String toggleDossierStatus(Dossier dossier, String veld) {
        boolean currentStatus = false;
        boolean newStatus = false;
        boolean statusChanged = false;
        String previousStatusString = "Nee";
        String newStatusString = "Nee";
        String notificationMessage = null;

        switch (veld) {
            case "Paspoort getoond":
                currentStatus = dossier.isPaspoortGetoond();
                newStatus = !currentStatus;
                if (newStatus != currentStatus) {
                    dossier.setPaspoortGetoond(newStatus);
                    statusChanged = true;
                }
                break;
            case "Asielaanvraag compleet":
                currentStatus = dossier.isAsielAanvraagCompleet();
                newStatus = !currentStatus;
                if (newStatus != currentStatus) {
                    dossier.setAsielAanvraagCompleet(newStatus);
                    statusChanged = true;
                }
                break;
            case "Rechter toegewezen":
                currentStatus = dossier.isRechterToegewezen();
                newStatus = !currentStatus;
                if (newStatus != currentStatus) {
                    dossier.setRechterToegewezen(newStatus);
                    statusChanged = true;
                }
                break;
            case "Uitspraak rechter":
                currentStatus = dossier.isUitspraakRechter();
                newStatus = !currentStatus;
                if (newStatus != currentStatus) {
                    dossier.setUitspraakRechter(newStatus);
                    statusChanged = true;
                }
                break;
            case "Toegelaten tot samenleving":
                currentStatus = dossier.isToegelatenTotSamenleving();
                newStatus = !currentStatus;
                if (newStatus != currentStatus) {
                    dossier.setToegelatenTotSamenleving(newStatus);
                    statusChanged = true;
                }
                break;
            case "Terugkeer naar land van herkomst":
                currentStatus = dossier.isTerugkeerNaarLandVanHerkomst();
                newStatus = !currentStatus;
                if (newStatus != currentStatus) {
                    dossier.setTerugkeerNaarLandVanHerkomst(newStatus);
                    statusChanged = true;
                }
                break;
            default:
                System.out.println("Ongeldige invoer.");
                return null;
        }

        if (statusChanged) {
            previousStatusString = currentStatus ? "Ja" : "Nee";
            newStatusString = newStatus ? "Ja" : "Nee";
            notificationMessage = String.format("%s: veranderd van %s naar %s", veld, previousStatusString, newStatusString);
            System.out.println("Status van '" + veld + "' is bijgewerkt naar " + newStatusString);
        } else {
            System.out.println("Status van '" + veld + "' is niet gewijzigd.");
        }

        return notificationMessage;
    }


    public void bekijkPersoonlijkeDossier(Vluchteling ingelogdeVluchteling) {
        laadVluchtelingDossier();
        // Ophalen van het dossier
        Dossier vluchtelingDossier = laadDossierVoorVluchteling(ingelogdeVluchteling.getId());
        if (vluchtelingDossier == null) {
            System.out.println("Geen dossier gevonden voor deze vluchteling.");
        } else {
            System.out.println("Dossier van vluchteling: " + ingelogdeVluchteling.getNaam());
            System.out.println(vluchtelingDossier);

            // Display notifications
            List<String> notifications = ingelogdeVluchteling.getNotifications();
            if (!notifications.isEmpty()) {
                System.out.println("\n=== Notificaties ===");
                for (String notification : notifications) {
                    System.out.println(notification);
                }
                ingelogdeVluchteling.clearNotifications(); // Clear notifications after displaying
            }
        }
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

    public void saveVluchtelingDossier() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Dossier_Bestand))) {
            for (Dossier d: dossiers) {
                writer.write(d.toString());
                writer.newLine();
            }
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

    public Vluchteling authenticateVluchteling(String gebruikersnaam, String wachtwoord) {
        for (Vluchteling vluchteling : vluchtelingen) {
            if (vluchteling.getGebruikersnaam().equals(gebruikersnaam) && vluchteling.getWachtwoord().equals(wachtwoord)) {
                return vluchteling;
            }
        }
        return null; // geen vluchteling gevonden met die combinatie
    }
}