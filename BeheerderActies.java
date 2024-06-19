public class BeheerderActies extends GebruikerActies {
    Landmanagement land;
    Gemeentemanagement gemeente;
    Azcmanagement azc;


    public BeheerderActies(Persoon persoon) {
        super(persoon);
        this.land = new Landmanagement();
        this.gemeente = new Gemeentemanagement();
        this.azc = new Azcmanagement();
    }

    @Override
    protected void toonMenu() {
        System.out.println("=== Beheerdersmenu ===");
        System.out.println("1. Beheer landen");
        System.out.println("2. Beheer gemeentes");
        System.out.println("3. Beheer AZC's");
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
                   land.menuLanden();
                    break;
                case "2":
                    gemeente.menuGemeentes();
                    break;
                case "3":
                    azc.menuAzc();
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
