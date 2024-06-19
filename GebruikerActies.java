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

    public boolean authenticate() {
        System.out.print("Voer uw gebruikersnaam in: ");
        String gebruikersnaam = scanner.nextLine();

        System.out.print("Voer uw wachtwoord in: ");
        String wachtwoord = scanner.nextLine();

        if (persoon.validerenInloggegevens(gebruikersnaam, wachtwoord)) {
            return true;
        } else {
            System.out.println("Authenticatie mislukt.");
            return false;
        }
    }


    protected abstract void toonMenu();
    protected abstract void voerGekozenActieUit();
}