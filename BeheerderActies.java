public class BeheerderActies extends GebruikerActies {
    Landmanagement land;
    Gemeentemanagement gemeente;
    Azcmangement azc;
    public BeheerderActies(Persoon persoon) {
        super(persoon);
    }

    @Override
    protected void toonMenu() {
        System.out.println("=== Beheerdersmenu ===");
        System.out.println("1. Beheer landen");
        System.out.println("2. Beheer gemeentes en AZC's");
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
                    land.menuLanden();
                    break;
                case "2":
                   // menuGemeentes();
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

