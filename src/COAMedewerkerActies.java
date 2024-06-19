import java.util.Scanner;
import java.util.List;
public class COAMedewerkerActies extends GebruikerActies {
    private final Azcmanagement azcManagement;
    private final Gemeentemanagement gemeenteManagement;
    private final Vluchtelingmanagement vluchtelingManagement;
    private final VluchtelingHuisvestingBeheer huisvestingBeheer;
    private final Scanner scanner;

    public COAMedewerkerActies(Persoon persoon, Azcmanagement azcManagement, Gemeentemanagement gemeenteManagement, Vluchtelingmanagement vluchtelingManagement, Scanner scanner) {
        super(persoon);
        this.azcManagement = azcManagement;
        this.gemeenteManagement = gemeenteManagement;
        this.vluchtelingManagement = vluchtelingManagement;
        this.huisvestingBeheer = new VluchtelingHuisvestingBeheer(azcManagement, gemeenteManagement, vluchtelingManagement, scanner);
        this.scanner = scanner;
    }

    @Override
    protected void toonMenu() {
        System.out.println("=== COA Medewerkersmenu ===");
        System.out.println("1. Registreer een nieuwe vluchteling");
        System.out.println("2. Bewerk vluchtelingendossier");
        System.out.println("3. Huisvest een vluchteling");
        System.out.println("4. Keer terug naar hoofdmenu");
        System.out.println("Kies een optie:");
    }

    @Override
    protected void voerGekozenActieUit() {
        boolean doorgaan = true;
        while (doorgaan) {
            String keuze = scanner.nextLine();
            switch (keuze) {
                case "1":
                    vluchtelingManagement.registreerVluchteling();
                    break;
                case "2":
                    vluchtelingManagement.BewerkVluchtelingDossier();
                    break;
                case "3":
                    huisvestingBeheer.huisvestVluchteling();
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
