
import java.util.UUID;

public class Land {
    String id;
    String naam;
    boolean isVeilig;

    public Land(String naam, boolean isVeilig) {
        this.id = UUID.randomUUID().toString();
        this.naam = naam;
        this.isVeilig = isVeilig;
    }

    public Land(String naam, boolean isVeilig, String id) {
        this.id = id;
        this.naam = naam;
        this.isVeilig = isVeilig;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public boolean isVeilig() {
        return isVeilig;
    }

    public void setVeilig(boolean isVeilig) {
        this.isVeilig = isVeilig;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%b", id, naam, isVeilig);
    }




}
