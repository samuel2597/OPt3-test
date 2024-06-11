    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.util.*;
    import java.util.stream.Collectors;


    public class Main {
        private static final String Gemeente_Bestand = "Gemeentes.txt";
        private static final String Azc_Bestand = "AZC.txt";
        private static final String Landen_Bestand = "Landen.txt";
        private static final String Vluchteling_Bestand = "Vluchtelingen.txt";
        private static final String Dossier_Bestand = "Dossier.txt";
        private static Beheerder beheerder = new Beheerder("admin", "admin123");
        private static COAMedewerker coaMedewerker = new COAMedewerker("coauser", "coa123");
        List<Azc> azcs = new ArrayList<>();
        List<Gemeente> gemeentes = new ArrayList<>();
        List<Vluchteling> vluchtelingen = new ArrayList<>();
        List<Land> landen = new ArrayList<>();
        List<Dossier> dossiers = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);


        public void voegEenLandToe() {
            Scanner scanner = new Scanner(System.in);
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
            laadAzcs(); // Zorg dat je de meest recente lijst van AZC's hebt geladen
            if(gemeentes.isEmpty()){
                System.out.println("Er zijn geen gemeentes beschikbaar om te verwijderen");
                return;
            }
            System.out.println("Kies een gemeente om te verwijderen");
            for(int i=0; i<gemeentes.size(); i++){
                Gemeente g = gemeentes.get(i);
                System.out.printf("%d. Gemeente: %s, Aantal inwoners:%d, Aangeboden plaatsen: %d%n", i + 1, g.getNaam(), g.getAantalInwoner(), g.getAangebodenPlaatsen());
            }

            System.out.println("Voer het nummer in van de gemeente die u wilt verwijderen");
            int keuze;
            try {
                keuze = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Ongeldige keuze. Probeer opnieuw.");
                return;
            }
            if(keuze >= 0 && keuze < gemeentes.size()){
                Gemeente gemeenteVerwijderen = gemeentes.get(keuze);

                // Verwijder alle AZC's gekoppeld aan deze gemeente
                Iterator<Azc> iterator = azcs.iterator();
                while (iterator.hasNext()) {
                    Azc azc = iterator.next();
                    if (azc.getGemeenteId().equals(gemeenteVerwijderen.getID())) {
                        iterator.remove(); // Verwijder AZC uit de lijst
                    }
                }

                // Verwijder de gemeente uit de lijst
                gemeentes.remove(gemeenteVerwijderen);
                saveGemeentes(); // Sla de gewijzigde lijst van gemeentes op
                saveAzcs(); // Sla de gewijzigde lijst van AZC's op
                System.out.println("Gemeente " + gemeenteVerwijderen.getNaam() + " en alle bijbehorende AZC's zijn verwijderd.");
            } else {
                System.out.println("Ongeldige keuze.");
            }
        }



        public void voegAzcToe() {
            laadGemeentes();
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
                saveGemeentes();
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
                        int aangebodenPlaatsen = Integer.parseInt(delen[5].trim()); // Aantal aangeboden plaatsen

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

        public void ToonGemeenteenAzcenVerwijder() {
            laadGemeentes();
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
            saveGemeentes();
        }

        public void registreerVluchteling() {
            laadLanden(); // Ensure countries are loaded

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
            laadLanden(); // Ensure all countries are loaded

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


        public void BekijkVluchtelingDossier() {
            laadVluchteling(); // Zorg ervoor dat de laatste vluchtelinggegevens zijn geladen

            if (vluchtelingen.isEmpty()) {
                System.out.println("Er zijn geen vluchtelingen geregistreerd.");
                return;
            }

            System.out.println("Kies een vluchteling om het dossier te bekijken:");
            for (int i = 0; i < vluchtelingen.size(); i++) {
                Vluchteling v = vluchtelingen.get(i);
                System.out.printf("%d. %s (Land van herkomst: %s)\n", i + 1, v.getNaam(), v.getLandVanHerkomst().getNaam());
            }

            System.out.println("Voer het nummer in van de vluchteling wiens dossier u wilt bekijken:");
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

            System.out.println("Dossier van vluchteling: " + geselecteerdeVluchteling.getNaam());
            System.out.println(dossier);
        }

        public void bekijkPersoonlijkeDossier() {
            // Eerst zorgen dat alle benodigde data geladen is
            laadVluchteling();
            laadVluchtelingDossier();

            // Gebruikersnaam en wachtwoord input
            System.out.println("Voer uw gebruikersnaam in:");
            String gebruikersnaam = scanner.nextLine();
            System.out.println("Voer uw wachtwoord in:");
            String wachtwoord = scanner.nextLine();

            // Authenticatie
            Vluchteling ingelogdeVluchteling = authenticateVluchteling(gebruikersnaam, wachtwoord);
            if (ingelogdeVluchteling == null) {
                System.out.println("Ongeldige gebruikersnaam of wachtwoord.");
                return;
            }

            // Ophalen van het dossier
            Dossier vluchtelingDossier = laadDossierVoorVluchteling(ingelogdeVluchteling.getId());
            if (vluchtelingDossier == null) {
                System.out.println("Geen dossier gevonden voor deze vluchteling.");
            } else {
                System.out.println("Dossier van vluchteling: " + ingelogdeVluchteling.getNaam());
                System.out.println(vluchtelingDossier);
            }
        }


        private Vluchteling authenticateVluchteling(String gebruikersnaam, String wachtwoord) {
            laadVluchteling();
            for (Vluchteling vluchteling : vluchtelingen) {
                if (vluchteling.getGebruikersnaam().equals(gebruikersnaam) && vluchteling.getWachtwoord().equals(wachtwoord)) {
                    return vluchteling;
                }
            }
            return null; // geen vluchteling gevonden met die combinatie
        }


        public Dossier laadDossierVoorVluchteling(String vluchtelingId) {
            for (Dossier d : dossiers) {
                if (d.getVluchtelingId().equals(vluchtelingId)) {
                    return d;
                }
            }
            return null;  // Return null if no matching dossier is found
        }


        public void saveVluchtelingDossier() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Dossier_Bestand))) {
                for (Dossier d : dossiers) {
                    writer.write(d.toString());  // Gebruik de aangepaste toString1 methode
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error met het opslaan van vluchteling data: " + e.getMessage());
            }
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

        public void vluchtelingHuisvest() {
            laadGemeentes(); // Zorg dat de gemeentes geladen zijn
            laadAzcs();     // Zorg dat de AZC's geladen zijn
            laadVluchteling(); // Zorg dat de vluchtelingen geladen zijn

            if (gemeentes.isEmpty()) {
                System.out.println("Er zijn geen gemeenten beschikbaar om te tonen.");
                return;
            }

            System.out.println("Kies een gemeente om een AZC te selecteren:");
            for (int i = 0; i < gemeentes.size(); i++) {
                Gemeente g = gemeentes.get(i);
                System.out.printf("%d. %s\n", i + 1, g.getNaam());
            }
            int gemeenteIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (gemeenteIndex < 0 || gemeenteIndex >= gemeentes.size()) {
                System.out.println("Ongeldige keuze.");
                return;
            }

            Gemeente gekozenGemeente = gemeentes.get(gemeenteIndex);
            List<Azc> azcsInGemeente = azcs.stream()
                    .filter(a -> a.getGemeenteId().equals(gekozenGemeente.getID()))
                    .collect(Collectors.toList());

            System.out.println("Kies een AZC uit de gekozen gemeente:");
            for (int j = 0; j < azcsInGemeente.size(); j++) {
                Azc azc = azcsInGemeente.get(j);
                System.out.printf("%d. %s, Beschikbare plaatsen: %d\n", j + 1, azc.toString(), azc.getAantalPlaatsen() - azc.getGehuistvest());
            }
            int azcIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (azcIndex < 0 || azcIndex >= azcsInGemeente.size()) {
                System.out.println("Ongeldige keuze.");
                return;
            }

            Azc gekozenAzc = azcsInGemeente.get(azcIndex);

            System.out.println("Kies een vluchteling om te huisvesten:");
            for (int k = 0; k < vluchtelingen.size(); k++) {
                Vluchteling v = vluchtelingen.get(k);
                System.out.printf("%d. %s\n", k + 1, v.getNaam());
            }
            int vluchtelingIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (vluchtelingIndex < 0 || vluchtelingIndex >= vluchtelingen.size()) {
                System.out.println("Ongeldige keuze.");
                return;
            }

            Vluchteling gekozenVluchteling = vluchtelingen.get(vluchtelingIndex);

            if (gekozenAzc.getAantalPlaatsen() - gekozenAzc.getGehuistvest() > 0) {
                gekozenAzc.huisvestVluchteling();
                System.out.println("Vluchteling " + gekozenVluchteling.getNaam() + " is gehuisvest in AZC " + gekozenAzc.getId());
            } else {
                System.out.println("Er zijn geen beschikbare plaatsen in dit AZC.");
            }

            saveAzcs();
            saveGemeentes();
        }

        public void ToonVluchtelingenInAzc() {
            laadAzcs();  // Zorg dat de meest recente AZC-gegevens geladen zijn
            if (azcs.isEmpty()) {
                System.out.println("Er zijn geen AZC's beschikbaar.");
                return;
            }

            System.out.println("Selecteer een AZC:");
            for (int i = 0; i < azcs.size(); i++) {
                Azc azc = azcs.get(i);
                System.out.printf("%d. %s, Locatie: %s %s, %s\n", i + 1, azc.getId(), azc.getStraat(), azc.getNummer(), azc.getPostcode());
            }

            System.out.println("Voer het nummer van het AZC in:");
            int index = scanner.nextInt();
            scanner.nextLine();  // consume newline left-over

            if (index < 1 || index > azcs.size()) {
                System.out.println("Ongeldige keuze.");
                return;
            }

            Azc gekozenAzc = azcs.get(index - 1);
            List<Vluchteling> gehuisvesteVluchtelingen = gekozenAzc.getGehuisvesteVluchtelingen();

            if (gehuisvesteVluchtelingen.isEmpty()) {
                System.out.println("Er zijn momenteel geen vluchtelingen gehuisvest in dit AZC.");
            } else {
                System.out.println("Vluchtelingen gehuisvest in AZC " + gekozenAzc.getId() + ":");
                for (Vluchteling vluchteling : gehuisvesteVluchtelingen) {
                    System.out.println(vluchteling.getNaam() + " uit " + vluchteling.getLandVanHerkomst().getNaam());
                }
            }
        }


        // Update de menuLanden, menuGemeentes, en menuVluchtelingen methoden
        public void menuBeheerder() {
            boolean doorgaan = true;
            while (doorgaan) {
                System.out.println("=== Beheerdersmenu ===");
                System.out.println("1. Beheer landen");
                System.out.println("2. Beheer gemeentes en AZC's");
                System.out.println("3. Keer terug naar hoofdmenu");
                System.out.println("Kies een optie:");
                String keuze = scanner.nextLine();
                switch (keuze) {
                    case "1":
                        menuLanden();
                        break;
                    case "2":
                        menuGemeentes();
                        break;
                    case "3":
                        doorgaan = false;
                        break;
                    default:
                        System.out.println("Ongeldige keuze. Probeer opnieuw.");
                }
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
                        voegAzcToe();
                        break;
                    case "4":
                        ToonGemeenteenAzcenVerwijder();
                        break;
                    case "5":
                        doorgaan = false;
                        break;
                    default:
                        System.out.println("Ongeldige keuze. Probeer opnieuw.");
                }
            }
        }


        public void menuCoamederwerkers() {
            boolean doorgaan = true;
            while (doorgaan) {
                System.out.println("=== COA Medewerkersmenu ===");
                System.out.println("1. Registreer een nieuwe vluchteling");
                System.out.println("2. Bewerk vluchtelingendossier");
                System.out.println("3. Huisvest een vluchteling");
                System.out.println("4. Keer terug naar hoofdmenu");
                System.out.println("Kies een optie:");
                String keuze = scanner.nextLine();
                switch (keuze) {
                    case "1":
                        registreerVluchteling();
                        break;
                    case "2":
                        BewerkVluchtelingDossier();
                        break;
                    case "3":
                        vluchtelingHuisvest();
                        break;
                    case "4":
                        doorgaan = false;
                        break;
                    default:
                        System.out.println("Ongeldige keuze. Probeer opnieuw.");
                }
            }
        }

        public void menuVluchtelingen() {
            boolean doorgaan = true;
            while (doorgaan) {
                System.out.println("=== Vluchtelingenmenu ===");
                System.out.println("1. Bekijk mijn dossier");
                System.out.println("2. Keer terug naar hoofdmenu");
                System.out.println("Kies een optie:");
                String keuze = scanner.nextLine();
                switch (keuze) {
                    case "1":
                        bekijkPersoonlijkeDossier(); // Dit is een fictieve methode die de details van het eigen dossier toont
                        break;
                    case "2":
                        doorgaan = false;
                        break;
                    default:
                        System.out.println("Ongeldige keuze. Probeer opnieuw.");
                }
            }
        }

        // Fictieve methode om het dossier van de ingelogde vluchteling te bekijken


        // De overige methoden voor het beheren van vluchtelingen en COA-medewerkers blijven hetzelfde als eerder gedefinieerd

        public static void main(String[] args) {
            Scanner scanner= new Scanner(System.in);
            Main beheer = new Main();
            boolean doorgaan = true;

            while (doorgaan) {
                System.out.println("=== Hoofdmenu ===");
                System.out.println("1. Beheerdersmenu");
                System.out.println("2. COA Medewerkersmenu");
                System.out.println("3. Vluchtelingenmenu");
                System.out.println("4. Afsluiten");
                System.out.println("Kies een optie:");
                String keuze = scanner.nextLine();

                switch (keuze) {
                    case "1":
                        if (authenticate(beheerder)) {
                            beheer.menuBeheerder();
                        } else {
                            System.out.println("Ongeldige gebruikersnaam of wachtwoord.");
                        }
                        break;
                    case "2":
                        if (authenticate(coaMedewerker)) {
                            beheer.menuCoamederwerkers();
                        } else {
                            System.out.println("Ongeldige gebruikersnaam of wachtwoord.");
                        }
                        break;
                    case "3":
                        beheer.menuVluchtelingen();
                        break;
                    case "4":
                        doorgaan = false;
                        break;
                    default:
                        System.out.println("Ongeldige keuze. Probeer opnieuw.");
                        break;
                }
            }
        }

        private static boolean authenticate(Persoon persoon) {
            Scanner scanner= new Scanner(System.in);
            System.out.print("Voer uw gebruikersnaam in: ");
            String gebruikersnaam = scanner.nextLine();
            System.out.print("Voer uw wachtwoord in: ");
            String wachtwoord = scanner.nextLine();

            return persoon.validerenInloggegevens(gebruikersnaam, wachtwoord);
        }
    }