import java.util.List;
import java.util.UUID;

public class Azc {
    private String id;
    private String straat;
    private String nummer;
    private String postcode;
    private String gemeenteId;
    private int aangebodenPlaatsen;
    private int gehuistvest = 0; // Nieuw veld voor gehuisveste vluchtelingen

    public Azc(String straat, String nummer, String postcode, String gemeenteId, int aangebodenPlaatsen) {
        this.id = UUID.randomUUID().toString();
        this.straat = straat;
        this.nummer = nummer;
        this.postcode = postcode;
        this.gemeenteId = gemeenteId;
        this.aangebodenPlaatsen = aangebodenPlaatsen; // Zorg dat dit het juiste aantal is
    }
    public Azc(String straat, String nummer, String postcode, String gemeenteId, int aangebodenPlaatsen, String id) {
        this.id = id;
        this.straat = straat;
        this.nummer = nummer;
        this.postcode = postcode;
        this.gemeenteId = gemeenteId;
        this.aangebodenPlaatsen = aangebodenPlaatsen; // Zorg dat dit het juiste aantal is
    }
    public void huisvestVluchteling() {
        if (gehuistvest < aangebodenPlaatsen) {
            gehuistvest++;
        } else {
            System.out.println("Dit AZC heeft geen beschikbare plaatsen meer.");
        }
    }

    public int getGehuistvest() {
        return gehuistvest;
    }


    // Getter voor aangeboden plaatsen
    public int getAantalPlaatsen() {
        return aangebodenPlaatsen;
    }

    @Override
    public String toString() {
        return id + ") Straat: " + straat + " nummer: " + nummer + " postcode: " + postcode + " Gemeenteid: " + gemeenteId + " aangeboden plaatsen: " + aangebodenPlaatsen;
    }
    public String toString1(){
        return  "Straat;  " + straat + " Huisnummer; " + nummer + " postcode; " + postcode + " aangebodenplaatsen:  " + aangebodenPlaatsen;
    }

    public String getGemeenteId() {
        return gemeenteId;
    }

    public String getId() {
        return this.id;
    }


    public int setAangebodenPlaatsen(int aangebodenPlaatsen) {
        return aangebodenPlaatsen;
    }


    public List<Vluchteling> getGehuisvesteVluchtelingen() {
        return getGehuisvesteVluchtelingen();
    }

    public String getStraat() {
        return straat;
    }
    public String getPostcode(){
        return postcode;
    }
    public String getNummer(){
        return nummer;
    }
}
