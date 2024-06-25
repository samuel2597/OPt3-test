import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VluchtelingHuisvestingBeheer {
    private final Azcmanagement azcManagement;
    private final Gemeentemanagement gemeenteManagement;
    private final Vluchtelingmanagement vluchtelingManagement;
    private final Scanner scanner;

    public VluchtelingHuisvestingBeheer(Azcmanagement azcManagement, Gemeentemanagement gemeenteManagement, Vluchtelingmanagement vluchtelingManagement, Scanner scanner) {
        this.azcManagement = azcManagement;
        this.gemeenteManagement = gemeenteManagement;
        this.vluchtelingManagement = vluchtelingManagement;
        this.scanner = scanner;
    }

    public void huisvestVluchteling() {
        gemeenteManagement.laadGemeentes();
        azcManagement.laadAzcs();
        vluchtelingManagement.laadVluchteling();

        List<Gemeente> gemeentes = gemeenteManagement.getGemeentes();
        List<Azc> azcs = azcManagement.getAzcs();
        List<Vluchteling> vluchtelingen = vluchtelingManagement.getVluchtelingen();

        if (gemeentes.isEmpty()) {
            System.out.println("Er zijn geen gemeenten beschikbaar om te tonen.");
            return;
        }

        System.out.println("Kies een gemeente om een AZC te selecteren:");
        for (int i = 0; i < gemeentes.size(); i++) {
            Gemeente g = gemeentes.get(i);
            System.out.printf("%d. %s\n", i + 1, g.getNaam());
        }
        int gemeenteIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (gemeenteIndex < 0 || gemeenteIndex >= gemeentes.size()) {
            System.out.println("Ongeldige keuze.");
            return;
        }

        Gemeente gekozenGemeente = gemeentes.get(gemeenteIndex);
        List<Azc> azcsInGemeente = azcs.stream()
                .filter(a -> a.getGemeenteId().equals(gekozenGemeente.getID()))
                .collect(Collectors.toList());

        System.out.println("Kies een AZC uit de gekozen gemeente:");
        for (int j = 0; j < azcsInGemeente.size(); j++) {
            Azc azc = azcsInGemeente.get(j);
            System.out.printf("%d. %s, Beschikbare plaatsen: %d\n", j + 1, azc.toString(), azc.getAantalPlaatsen() - azc.getGehuistvest());
        }
        int azcIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (azcIndex < 0 || azcIndex >= azcsInGemeente.size()) {
            System.out.println("Ongeldige keuze.");
            return;
        }

        Azc gekozenAzc = azcsInGemeente.get(azcIndex);

        System.out.println("Kies een vluchteling om te huisvesten:");
        for (int k = 0; k < vluchtelingen.size(); k++) {
            Vluchteling v = vluchtelingen.get(k);
            System.out.printf("%d. %s\n", k + 1, v.getNaam());
        }
        int vluchtelingIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (vluchtelingIndex < 0 || vluchtelingIndex >= vluchtelingen.size()) {
            System.out.println("Ongeldige keuze.");
            return;
        }

        Vluchteling gekozenVluchteling = vluchtelingen.get(vluchtelingIndex);

        try {
            gekozenAzc.huisvestVluchteling(gekozenVluchteling);
            System.out.println("Vluchteling " + gekozenVluchteling.getNaam() + " is gehuisvest in AZC " + gekozenAzc.getId());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        azcManagement.saveAzcs();

    }

    public void BekijkeigenAZC(Vluchteling vluchteling) {
        azcManagement.laadAzcs();
        List<Azc> azcs = azcManagement.getAzcs();

        for (Azc azc : azcs) {
            if (azc.getGehuisvesteVluchtelingen().contains(vluchteling)) {
                System.out.println("U bent gehuisvest in AZC " + azc.getId() + ", Locatie: " + azc.getStraat() + " " + azc.getNummer() + ", " + azc.getPostcode());
                return;
            }
        }

        System.out.println("U bent momenteel niet gehuisvest in een AZC.");
    }
}
