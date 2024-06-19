import java.util.Scanner;
public class VluchtelingActies extends GebruikerActies {
    private Vluchtelingmanagement vluchtelingManagement;
    private VluchtelingHuisvestingBeheer huisvestingBeheer;
    private Vluchteling ingelogdeVluchteling;

    public VluchtelingActies(Vluchteling ingelogdeVluchteling, Azcmanagement azcManagement, Gemeentemanagement gemeenteManagement, Vluchtelingmanagement vluchtelingManagement, Scanner scanner) {
        super(ingelogdeVluchteling);
        this.ingelogdeVluchteling = ingelogdeVluchteling;
        this.vluchtelingManagement = vluchtelingManagement;
        this.huisvestingBeheer = new VluchtelingHuisvestingBeheer(azcManagement, gemeenteManagement, vluchtelingManagement, scanner);
    }

    @Override
    protected void toonMenu() {
        System.out.println("=== Vluchtelingenmenu ===");
        System.out.println("1. Bekijk mijn dossier");
        System.out.println("2. Bekijk de huisgeveste AZC");
        System.out.println("3. Keer terug naar hoofdmenu");
        System.out.println("Kies een optie:");
    }

    @Override
    protected void voerGekozenActieUit() {
        boolean doorgaan = true;
        while (doorgaan) {
            String keuze = scanner.nextLine();
            switch (keuze) {
                case "1":
                    vluchtelingManagement.bekijkPersoonlijkeDossier(ingelogdeVluchteling);
                    break;
                case "2":
                    huisvestingBeheer.BekijkeigenAZC(ingelogdeVluchteling);
                    break;
                case "3":
                    doorgaan = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }
}
