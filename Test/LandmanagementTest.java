import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

class LandmanagementTest {

    private static final String LANDEN_BESTAND = "Landen.txt";
    private final InputStream originalSystemIn = System.in;

    @BeforeEach
    void setup() {
        setupInitialFile();
    }

    /*@Test
    void voegEenLandToe() {
        Landmanagement lm = new Landmanagement();

        // Simulate user input for adding a land
        String input = "NieuwLand\n1\n";
        provideInput(input);

        lm.voegEenLandToe();

        // Check if land was added
        List<String> landen = readFile();
        assertEquals(5, landen.size(), "Er zouden 5 landen moeten zijn.");
        assertTrue(landen.stream().anyMatch(l -> l.contains("NieuwLand")), "Het nieuwe land moet toegevoegd zijn.");

        restoreSystemIn();
    }*/

    @Test
    void wijzigLanden() {
        Landmanagement lm = new Landmanagement();

        // Simulate user input for modifying a land (modify 'Polen')
        String input = "1\n2\n";
        provideInput(input);

        lm.wijzigLanden();

        // Check if land status was updated
        List<String> landen = readFile();
        assertTrue(landen.stream().anyMatch(l -> l.contains("Polen") && l.contains("false")), "De status van Polen moet gewijzigd zijn.");

        restoreSystemIn();
    }

    @Test
    void toonEnVerwijderLanden() {
        Landmanagement lm = new Landmanagement();

        // Simulate user input for deleting a land (delete 'Ghana')
        String input = "2\n";
        provideInput(input);

        lm.VerwijderLanden();

        // Check if land was removed
        List<String> landen = readFile();
        assertEquals(3, landen.size(), "Er zouden 3 landen moeten zijn.");
        assertFalse(landen.stream().anyMatch(l -> l.contains("Ghana")), "Ghana moet verwijderd zijn.");

        restoreSystemIn();
    }

    private void provideInput(String data) {
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        System.setIn(testInput);
    }

    private void restoreSystemIn() {
        System.setIn(originalSystemIn);
    }

    private void setupInitialFile() {
        try {
            List<String> initialData = new ArrayList<>();
            initialData.add("fbde543f-f96d-4cff-b358-f77c1d9c541f,Polen,true");
            initialData.add("8d3d0f0b-6037-4c47-99d5-028de8621aaf,Ghana,false");
            initialData.add("47ed56b4-2335-42da-9b0a-f909a57e2327,Tunesie,false");
            initialData.add("7639f1b4-4017-4ea2-9a5c-86f7e48d79ad,Rusland,false");
            Files.write(Paths.get(LANDEN_BESTAND), initialData, StandardOpenOption.CREATE);
        } catch (IOException e) {
            fail("Kon het initiële bestand niet instellen.");
        }
    }

    private List<String> readFile() {
        try {
            return Files.readAllLines(Paths.get(LANDEN_BESTAND));
        } catch (IOException e) {
            fail("Kon het bestand niet lezen.");
            return new ArrayList<>();
        }
    }
}
