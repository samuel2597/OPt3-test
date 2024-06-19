import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Azc {
    private final String id;
    private final String straat;
    private final String nummer;
    private final String postcode;
    private final String gemeenteId;
    private int aangebodenPlaatsen;
    private int gehuistvest = 0;
    private final List<Vluchteling> gehuisvesteVluchtelingen;

    public Azc(String straat, String nummer, String postcode, String gemeenteId, int aangebodenPlaatsen) {
        this.id = UUID.randomUUID().toString();
        this.straat = straat;
        this.nummer = nummer;
        this.postcode = postcode;
        this.gemeenteId = gemeenteId;
        this.aangebodenPlaatsen = aangebodenPlaatsen;
        this.gehuisvesteVluchtelingen = new ArrayList<>();
    }

    public Azc(String straat, String nummer, String postcode, String gemeenteId, int aangebodenPlaatsen, String id) {
        this.id = id;
        this.straat = straat;
        this.nummer = nummer;
        this.postcode = postcode;
        this.gemeenteId = gemeenteId;
        this.aangebodenPlaatsen = aangebodenPlaatsen;
        this.gehuisvesteVluchtelingen = new ArrayList<>();
    }

    public void huisvestVluchteling(Vluchteling vluchteling) {
        if (gehuistvest < aangebodenPlaatsen) {
            gehuisvesteVluchtelingen.add(vluchteling);
            gehuistvest++;
        } else {
            throw new IllegalStateException("Dit AZC heeft geen beschikbare plaatsen meer.");
        }
    }

    public int getGehuistvest() {
        return gehuistvest;
    }

    public int getAantalPlaatsen() {
        return aangebodenPlaatsen;
    }

    @Override
    public String toString() {
        return id + ") Straat: " + straat + " nummer: " + nummer + " postcode: " + postcode + " Gemeenteid: " + gemeenteId + " aangeboden plaatsen: " + aangebodenPlaatsen;
    }

    public String toString1() {
        return "Straat: " + straat + " Huisnummer: " + nummer + " postcode: " + postcode + " aangeboden plaatsen: " + aangebodenPlaatsen;
    }

    public String getGemeenteId() {
        return gemeenteId;
    }

    public String getId() {
        return id;
    }

    public void setAangebodenPlaatsen(int aangebodenPlaatsen) {
        this.aangebodenPlaatsen = aangebodenPlaatsen;
    }

    public List<Vluchteling> getGehuisvesteVluchtelingen() {
        return new ArrayList<>(gehuisvesteVluchtelingen);  // Return a copy to maintain encapsulation
    }

    public String getStraat() {
        return straat;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getNummer() {
        return nummer;
    }
}