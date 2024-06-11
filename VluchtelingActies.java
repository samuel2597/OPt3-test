public class VluchtelingActies extends GebruikerActies {

    public VluchtelingActies(Persoon persoon) {
        super(persoon);
    }

    @Override
    protected void toonMenu() {
        System.out.println("=== Vluchtelingenmenu ===");
        System.out.println("1. Bekijk mijn dossier");
        System.out.println("2. Keer terug naar hoofdmenu");
        System.out.println("Kies een optie:");
    }

    @Override
    protected void voerGekozenActieUit() {

    }
}