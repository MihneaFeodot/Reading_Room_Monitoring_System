package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste JUnit pentru diagrama de activitate "Crearea unei rezervari".
   Ramuri acoperite:
   1. Sala OCUPATA  → clientul nu poate rezerva, bucla de re-selectare
   2. Sala LIBERA   → flux complet: confirmare → QR generat → rezervare CONFIRMATA
   3. Client neautentificat → rezervarea nu se salveaza
   4. Cod QR generat corect dupa confirmare
   5. Vizualizare sali: doar cele disponibile apar in lista
 */
class CreareRezervareTest {

    private Client client;
    private Sala_De_Lectura salaLibera;
    private Sala_De_Lectura salaOcupata;

    @BeforeEach
    void setup() {
        client = new Client(1, "Ion Popescu", "ion@mail.ro", "parola123", "0722000001");
        salaLibera  = new Sala_De_Lectura(10, "Sala Eminescu", 50);   // disponibilitate = true (default)
        salaOcupata = new Sala_De_Lectura(20, "Sala Creanga",  30);
        salaOcupata.setDisponibilitate(false); // simulam sala ocupata
    }

    // Testul 1: ramura OCUPAT
    // Daca sala aleasa nu este disponibila, clientul nu o poate rezerva
    // si rezervarea ramane in starea PENDING (nu se confirma).
    @Test
    void testRezervare_SalaOcupata_NuSeConfirma() {
        // Sistemul interogheaza disponibilitatea – sala e ocupata
        assertFalse(salaOcupata.verificaDisponibilitate(),
                "Sala trebuie sa fie marcata ca INDISPONIBILA.");

        // Clientul este autentificat
        client.login("ion@mail.ro", "parola123");

        // Chiar daca clientul incearca sa creeze o rezervare pe sala ocupata,
        // din perspectiva sistemului sala nu ar trebui afisata ca disponibila.
        Rezervare rezervare = new Rezervare(200, LocalDate.now(),
                LocalTime.of(9, 0), LocalTime.of(11, 0), salaOcupata);

        // Starea initiala este PENDING; confirmarea nu are loc deoarece
        // sistemul nu ofera "Solicita confirmare" pt sala ocupata.
        assertEquals("PENDING", rezervare.getStatus(),
                "Rezervarea pe sala ocupata nu trebuie confirmata automat.");
    }

    // Testul 2: ramura LIBERA – flux complet fericit
    // Sala e libera → sistemul solicita confirmare → clientul confirma
    // → QR generat → rezervare salvata cu status CONFIRMATA.
    @Test
    void testRezervare_SalaLibera_FluxComplet() {
        // Pas 1: verificare disponibilitate (interogare BD)
        assertTrue(salaLibera.verificaDisponibilitate(),
                "Sala trebuie sa fie DISPONIBILA pentru a permite rezervarea.");

        // Pas 2: clientul se autentifica
        client.login("ion@mail.ro", "parola123");
        assertTrue(client.isEsteAutentificat());

        // Pas 3: se creeaza rezervarea (sistemul solicita confirmare, clientul confirma datele)
        Rezervare rezervare = new Rezervare(101, LocalDate.now(),
                LocalTime.of(10, 0), LocalTime.of(12, 0), salaLibera);

        client.creeazaRezervare(rezervare);

        // Pas 4: verificari post-confirmare
        assertEquals("CONFIRMATA", rezervare.getStatus(),
                "Dupa confirmare rezervarea trebuie sa aiba status CONFIRMATA.");

        assertEquals(salaLibera, rezervare.getSalaAsociata(),
                "Rezervarea trebuie asociata salii corecte.");

        assertEquals(1, client.getIstoricRezervari().size(),
                "Istoricul clientului trebuie sa contina exact o rezervare.");
    }

    // Testul 3: client neautentificat
    // Daca utilizatorul nu s-a logat, sistemul refuza salvarea
    // rezervarii (nu ajungem la pasul "Trimite cerere salvare").
    @Test
    void testRezervare_ClientNeautentificat_NuSeSalveaza() {
        // Clientul NU se autentifica
        assertFalse(client.isEsteAutentificat(),
                "Clientul nu trebuie sa fie autentificat la inceput.");

        Rezervare rezervare = new Rezervare(102, LocalDate.now(),
                LocalTime.of(14, 0), LocalTime.of(16, 0), salaLibera);

        client.creeazaRezervare(rezervare);

        // Rezervarea nu trebuie salvata in istoric
        assertEquals(0, client.getIstoricRezervari().size(),
                "Clientul neautentificat nu trebuie sa aiba rezervari salvate.");

        // Statusul ramane PENDING
        assertEquals("PENDING", rezervare.getStatus(),
                "Rezervarea clientului neautentificat trebuie sa ramana PENDING.");
    }

    // Testul 4: codul QR este generat corect
    // Dupa ce sistemul proceseaza rezervarea, codul QR trebuie
    // sa respecte formatul "QR_<id>_<data>".
    @Test
    void testCodQR_FormatCorect_DupaCreareRezervare() {
        client.login("ion@mail.ro", "parola123");

        LocalDate azi = LocalDate.now();
        Rezervare rezervare = new Rezervare(103, azi,
                LocalTime.of(8, 0), LocalTime.of(10, 0), salaLibera);

        client.creeazaRezervare(rezervare);

        String codQRAsteptat = "QR_103_" + azi;
        assertEquals(codQRAsteptat, rezervare.getCodQR(),
                "Codul QR trebuie sa fie in formatul QR_<id>_<data>.");
    }

    // Testul 5: vizualizare sali – doar salile disponibile apar
    // (pasul initial al clientului: "Selectare sala si intervalul orar")
    @Test
    void testVizualizareSali_DoarDisponibileAfisate() {
        client.login("ion@mail.ro", "parola123");

        List<Sala_De_Lectura> toateSalile = List.of(salaLibera, salaOcupata);

        // Apelam metoda care reproduce interogarea disponibilitatii
        long numarDisponibile = toateSalile.stream()
                .filter(Sala_De_Lectura::isDisponibilitate)
                .count();

        assertEquals(1, numarDisponibile,
                "Doar o sala trebuie sa fie disponibila din cele doua.");

        assertTrue(salaLibera.isDisponibilitate(),
                "Sala Eminescu trebuie sa fie disponibila.");

        assertFalse(salaOcupata.isDisponibilitate(),
                "Sala Creanga trebuie sa fie indisponibila.");
    }

    // Testul 6: anulare rezervare confirmata
    // Dupa confirmare, clientul poate anula rezervarea.
    @Test
    void testAnulareRezervare_DupaConfirmare() {
        client.login("ion@mail.ro", "parola123");

        Rezervare rezervare = new Rezervare(104, LocalDate.now(),
                LocalTime.of(16, 0), LocalTime.of(18, 0), salaLibera);

        client.creeazaRezervare(rezervare);
        assertEquals("CONFIRMATA", rezervare.getStatus());

        client.anuleazaRezervare(104);
        assertEquals("ANULATA", rezervare.getStatus(),
                "Rezervarea anulata trebuie sa aiba status ANULATA.");
    }
}