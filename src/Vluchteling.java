import java.util.Random;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Vluchteling  extends Persoon{

        private final String id;
        private final String naam;
        private final Land landVanHerkomst;
        List<Vluchteling>vluchtelingen= new ArrayList<> ();
        List<Land> landen= new ArrayList<Land>();
        Landmanagement land;
        Scanner scanner= new Scanner (System.in);


        public Vluchteling(String naam, Land landVanHerkomst) {
            super(naam, generatePassword(naam)); // Gebruik de naam als gebruikersnaam en genereer een wachtwoord
            this.id = UUID.randomUUID().toString();
            this.naam = naam;
            this.landVanHerkomst = landVanHerkomst;
        }
        public Vluchteling(String naam, Land landVanHerkomst, String id) {
            super(naam, generatePassword(naam)); // Gebruik de naam als gebruikersnaam en genereer een wachtwoord
            this.id = id;
            this.naam = naam;
            this.landVanHerkomst = landVanHerkomst;  // Referentie naar het Land-object
        }
        private static String generatePassword(String naam) {
            Random random = new Random();
            int digits = 100 + random.nextInt(900); // Genereert een getal tussen 100 en 999
            return naam.substring(0, Math.min(naam.length(), 2)).toLowerCase() + digits;
        }

        public String getId() {
            return id;
        }

        public String getNaam() {
            return naam;
        }

        public Land getLandVanHerkomst() {
            return landVanHerkomst;
        }


        @Override
        public String toString() {
            return String.format("ID: %s, Naam: %s, Gebruikersnaam: %s, Wachtwoord: %s, Land van Herkomst: %s", id, naam, getGebruikersnaam(), getWachtwoord(), landVanHerkomst.getNaam());
        }



    }
