import java.util.Scanner;
abstract class GebruikerActies {
    protected Persoon persoon;
    protected Scanner scanner = new Scanner(System.in);

    public GebruikerActies(Persoon persoon) {
        this.persoon = persoon;
    }

    final void voerActieUit() {
        if (authenticate()) {
            toonMenu();
            voerGekozenActieUit();
        } else {
            System.out.println("Authenticatie mislukt.");
        }
    }

    protected boolean authenticate() {
        return persoon.validerenInloggegevens(persoon.getGebruikersnaam(), persoon.getWachtwoord());
    }

    protected abstract void toonMenu();
    protected abstract void voerGekozenActieUit();
}