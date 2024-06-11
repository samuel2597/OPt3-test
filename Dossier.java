import java.util.ArrayList;
import java.util.List;

public class Dossier {
    private static final String Dossier_Bestand = "Dossier.txt";
    List<Dossier> dossiers = new ArrayList<>();
        private String vluchtelingId;
        private boolean paspoortGetoond;
        private boolean asielAanvraagCompleet;
        private boolean rechterToegewezen;
        private boolean uitspraakRechter;
        private boolean toegelatenTotSamenleving;
        private boolean terugkeerNaarLandVanHerkomst;

        public Dossier(String vluchtelingId,boolean paspoortGetoond,boolean asielAanvraagCompleet ,boolean rechterToegewezen, boolean uitspraakRechter, boolean toegelatenTotSamenleving, boolean terugkeerNaarLandVanHerkomst) {
            // Standaard waarden instellen
            this.vluchtelingId= vluchtelingId;
            this.paspoortGetoond = false;
            this.asielAanvraagCompleet = false;
            this.rechterToegewezen = false;
            this.uitspraakRechter = false;
            this.toegelatenTotSamenleving = false;
            this.terugkeerNaarLandVanHerkomst = false;
        }


        // Getters en setters voor de variabelen
        public String getVluchtelingId(){
            return vluchtelingId;
        }
        public boolean isPaspoortGetoond() {
            return paspoortGetoond;
        }

        public void setPaspoortGetoond(boolean paspoortGetoond) {
            this.paspoortGetoond = paspoortGetoond;
        }

        public boolean isAsielAanvraagCompleet() {
            return asielAanvraagCompleet;
        }

        public void setAsielAanvraagCompleet(boolean asielAanvraagCompleet) {
            this.asielAanvraagCompleet = asielAanvraagCompleet;
        }

        public boolean isRechterToegewezen() {
            return rechterToegewezen;
        }

        public void setRechterToegewezen(boolean rechterToegewezen) {
            this.rechterToegewezen = rechterToegewezen;
        }

        public boolean isUitspraakRechter() {
            return uitspraakRechter;
        }

        public void setUitspraakRechter(boolean uitspraakRechter) {
            this.uitspraakRechter = uitspraakRechter;
        }

        public boolean isToegelatenTotSamenleving() {
            return toegelatenTotSamenleving;
        }

        public void setToegelatenTotSamenleving(boolean toegelatenTotSamenleving) {
            this.toegelatenTotSamenleving = toegelatenTotSamenleving;
        }

        public boolean isTerugkeerNaarLandVanHerkomst() {
            return terugkeerNaarLandVanHerkomst;
        }

        public void setTerugkeerNaarLandVanHerkomst(boolean terugkeerNaarLandVanHerkomst) {
            this.terugkeerNaarLandVanHerkomst = terugkeerNaarLandVanHerkomst;
        }

        @Override
        public String toString() {
            return String.format("Paspoort Getoond: %b, Asielaanvraag Compleet: %b, Rechter Toegewezen: %b, Uitspraak Rechter: %b, Toegelaten Tot Samenleving: %b, Terugkeer Naar Land Van Herkomst: %b, VluvhtelingID: %s",
                    paspoortGetoond, asielAanvraagCompleet, rechterToegewezen, uitspraakRechter, toegelatenTotSamenleving, terugkeerNaarLandVanHerkomst, vluchtelingId);
        }



    }
