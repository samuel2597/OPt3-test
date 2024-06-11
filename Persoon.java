public   abstract class Persoon {
    private String gebruikersnaam;
    private String wachtwoord;

    public Persoon(String gebruikersnaam, String wachtwoord) {
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
    }

    public boolean validerenInloggegevens(String gebruikersnaam, String wachtwoord) {
        return this.gebruikersnaam.equals(gebruikersnaam) && this.wachtwoord.equals(wachtwoord);
    }
    public void setGebruikersnaam(String gebruikersnaam){
        this.gebruikersnaam=gebruikersnaam;
    }
    public void setWachtwoord(String wachtwoord){
        this.wachtwoord=wachtwoord;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }
    public String getWachtwoord(){
        return wachtwoord;
    }
}