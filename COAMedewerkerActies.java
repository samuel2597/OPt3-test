public class COAMedewerkerActies extends GebruikerActies {


    public COAMedewerkerActies(Persoon persoon) {
        super(persoon);
    }

    @Override
    protected void toonMenu() {
        System.out.println("=== COA Medewerkersmenu ===");
        System.out.println("1. Registreer een nieuwe vluchteling");
        System.out.println("2. Bewerk vluchtelingendossier");
        System.out.println("3. Huisvest een vluchteling");
        System.out.println("4. Keer terug naar hoofdmenu");
    }

    @Override
    protected void voerGekozenActieUit() {
        System.out.print("Kies een optie: ");
        int keuze = Integer.parseInt(scanner.nextLine());
        switch (keuze) {
            case 1:
                // Registreer vluchteling
                break;
            case 2:
                // Bewerk dossier
                break;
            case 3:
                // Huisvest vluchteling
                break;
            case 4:
                System.out.println("Terugkeren naar het hoofdmenu...");
                break;
            default:
                System.out.println("Ongeldige keuze. Probeer opnieuw.");
                toonMenu(); // Toont opnieuw het menu als de keuze ongeldig was
                voerGekozenActieUit(); // Vraagt opnieuw om een actie
                break;
        }
    }
}
