package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CodQRTest {
    private Client clientTest;
    private Sala_De_Lectura salaTest;

    @BeforeEach
    void setup() {
        // Initializam datele de test inainte de fiecare metoda @Test
        clientTest = new Client(1, "Mihai", "mihai@email.com", "parola123", "0722000000");
        salaTest = new Sala_De_Lectura(101, "Sala A", 20);
    }

    @Test
    void testCheckInSucces() {
        // Scenariul 1: Cod valid si rezervare confirmata
        Rezervare rez = new Rezervare(1, LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(2), salaTest);
        rez.setStatus("CONFIRMATA");

        String rezultat = clientTest.proceseazaCheckIn("QR_1", rez);

        assertEquals("Acces permis", rezultat, "Ar trebui sa permita accesul.");
        assertEquals("OCUPAT", rez.getStatus(), "Starea rezervarii ar trebui sa se schimbe in OCUPAT.");
    }

    @Test
    void testCodQRInvalid() {
        // Scenariul 2: Codul scanat nu este in formatul corect (ramura "Nu" de la prima decizie)
        String rezultat = clientTest.proceseazaCheckIn("COD_GRESIT", null);

        assertEquals("Eroare: Cod invalid", rezultat);
    }

    @Test
    void testRezervareLipsa() {
        // Scenariul 3: Cod valid dar nu exista rezervare in BD (ramura "Doresti creare rezervare?")
        String rezultat = clientTest.proceseazaCheckIn("QR_99", null);

        assertEquals("Optiune: Creare rezervare noua?", rezultat);
    }

    @Test
    void testRezervareInactiva() {
        // Scenariul 4: Exista rezervare dar este deja anulata sau expirata
        Rezervare rez = new Rezervare(2, LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), salaTest);
        rez.setStatus("ANULATA");

        String rezultat = clientTest.proceseazaCheckIn("QR_2", rez);

        assertEquals("Eroare: Rezervare inactiva", rezultat);
    }
}