import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static Vluchteling ingelogdeVluchteling = null; // Houd de ingelogde vluchteling bij

    public static void main(String[] args) {
        Azcmanagement azcs = new Azcmanagement(); // Vul deze lijst met je AZC-gegevens
        Gemeentemanagement gemeentes = new Gemeentemanagement(); // Vul deze lijst met je gemeentegegevens
        Vluchtelingmanagement vluchtelingen = new Vluchtelingmanagement();
        // Gebruikers
        Beheerder beheerder = new Beheerder("admin", "admin123");
        COAMedewerker coaMedewerker = new COAMedewerker("coauser", "coa123");

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
                    new BeheerderActies(beheerder).voerActieUit();
                    break;
                case "2":
                    new COAMedewerkerActies(coaMedewerker, azcs, gemeentes, vluchtelingen, scanner).voerActieUit();
                    break;
                case "3":
                    if (ingelogdeVluchteling == null) {
                        System.out.print("Voer uw gebruikersnaam in: ");
                        String gebruikersnaam = scanner.nextLine();
                        System.out.print("Voer uw wachtwoord in: ");
                        String wachtwoord = scanner.nextLine();
                        ingelogdeVluchteling = vluchtelingen.authenticateVluchteling(gebruikersnaam, wachtwoord);
                        if (ingelogdeVluchteling == null) {
                            System.out.println("Authenticatie mislukt. Toegang geweigerd.");
                            break;
                        }
                    }
                    new VluchtelingActies(ingelogdeVluchteling, azcs, gemeentes, vluchtelingen, scanner).voerActieUit();
                    break;
                case "4":
                    System.out.println("Programma afgesloten.");
                    doorgaan = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
                    break;
            }
        }

        scanner.close();
    }
}
