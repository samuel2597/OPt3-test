import java.util.ArrayList;
import java.util.UUID;

public class Gemeente {
        private String id;
        private String naam;
        private int aantalInwoner;
        private int aangebodenPlaatsen = 0;  // Nieuwe variabele
        ArrayList<Azc> azcs = new ArrayList<>();


        // Constructor voor nieuwe gemeente met automatisch gegenereerde UUID
        public Gemeente(String naam, int aantalInwoner) {
            this.id = UUID.randomUUID().toString();
            this.naam = naam;
            this.aantalInwoner = aantalInwoner;
        }

        // Constructor voor bestaande gemeente met een gegeven UUID
        public Gemeente(String naam, int aantalInwoner, String id) {
            this.id = id;  // Gebruik de meegegeven UUID, geen nieuwe genereren
            this.naam = naam;
            this.aantalInwoner = aantalInwoner;
        }
        // Nieuwe methode om het totaal aangeboden plaatsen bij te werken
        public void voegAangebodenPlaatsenToe(int aantal) {
            this.aangebodenPlaatsen += aantal;
        }

        // Getter voor aangeboden plaatsen
        public int getAangebodenPlaatsen() {
            return aangebodenPlaatsen;
        }

        public String toString() {
            return id + ") Gemeente: " + naam + " aantal inwoners: " + aantalInwoner + " aangeboden plaatsen: " + aangebodenPlaatsen;
        }

        public Gemeente(String naam) {
            this.naam = naam;
        }

        public void setNaam(String naam) {
            this.naam = naam;
        }
        public void setAantalInwoner(int aantalInwoner) {
            this.aantalInwoner = aantalInwoner;
        }
        public void setAangebodenPlaatsen(int aangebodenPlaatsen) {
            this.aangebodenPlaatsen = aangebodenPlaatsen;
        }
        public String getNaam(){
            return naam;
        }
        public int getAantalInwoner(){
            return aantalInwoner;
        }

        public String getID(){
            return id;
        }



    }
