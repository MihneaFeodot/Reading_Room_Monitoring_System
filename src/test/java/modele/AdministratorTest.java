package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

    class ModificareSpecificatiiSalaTest {

        private Administrator admin;
        private Sala_De_Lectura sala;

        // Valorile initiale ale salii (simulate ca "BD initial")
        private static final String NUME_INITIAL    = "Sala Eminescu";
        private static final int CAPACITATE_INITIALA = 50;

        @BeforeEach
        void setup() {
            admin = new Administrator(1, "Admin Popescu", "admin@biblioteca.ro", "parolaAdmin", "IT", 1);
            sala = new Sala_De_Lectura(10, NUME_INITIAL, CAPACITATE_INITIALA);
            admin.getSaliGestionate().add(sala);
        }

        // Testul 1: Selectare sala → specificatiile initiale sunt accesibile
        // Simuleaza "Accesarea specificatiilor initiale ale salii" din BD
        @Test
        void testSelectareSala_SpecificatiiInitialeAccesibile() {
            // Dupa selectare, adminul poate citi datele curente ale salii
            assertNotNull(sala, "Sala selectata nu trebuie sa fie null.");
            assertEquals(NUME_INITIAL, sala.getDenumire(), "Denumirea initiala trebuie sa fie accesibila.");
            assertEquals(CAPACITATE_INITIALA, sala.getCapacitate(), "Capacitatea initiala trebuie sa fie accesibila.");
            assertTrue(sala.isDisponibilitate(), "Disponibilitatea initiala trebuie sa fie true.");
        }

        // Testul 2: Decizia "modifici specificatiile?" → ramura NU
        // Adminul decide sa nu modifice nimic; specificatiile raman neschimbate
        @Test
        void testDecizie_NuModifica_SpecificatiiNeschimbate() {
            // Adminul NU apeleaza modificaSpecificatiiSala → nicio actiune
            // Verificam ca datele salii sunt identice cu cele initiale
            assertEquals(NUME_INITIAL, sala.getDenumire(), "Denumirea nu trebuie modificata daca adminul alege NU.");
            assertEquals(CAPACITATE_INITIALA, sala.getCapacitate(), "Capacitatea nu trebuie modificata daca adminul alege NU.");
        }

        // Testul 3: Decizia "modifici specificatiile?" → ramura DA
        // Adminul aplica modificarile; acestea se reflecta pe obiect
        @Test
        void testDecizie_DaModifica_SpecificatiiActualizate() {
            String numeNou = "Sala Eminescu (Renovata)";
            int capacitateNoua = 80;

            // Adminul aplica modificarile (pasul "Modificarea specificatiilor salii")
            admin.modificaSpecificatiiSala(sala, numeNou, capacitateNoua);

            assertEquals(numeNou, sala.getDenumire(), "Denumirea salii trebuie actualizata.");
            assertEquals(capacitateNoua, sala.getCapacitate(), "Capacitatea salii trebuie actualizata.");
        }

        // Testul 4: Cerere de confirmare → ramura NU
        // Adminul modifica in memorie dar NU confirma salvarea → BD ramane neschimbata (simulat prin restaurarea valorilor initiale)

        @Test
        void testConfirmare_Nu_ModificarileNuSePersista() {
            // Simulam modificarea in memorie
            String numeModificat = "Sala Temporara";
            int capacitateModificata = 20;
            sala.setDenumire(numeModificat);
            sala.setCapacitate(capacitateModificata);

            // Adminul NU confirma → simulam rollback la valorile din "BD"
            sala.setDenumire(NUME_INITIAL);
            sala.setCapacitate(CAPACITATE_INITIALA);

            // Verificam ca "BD-ul" (valorile restaurate) e intact
            assertEquals(NUME_INITIAL, sala.getDenumire(), "Fara confirmare, denumirea din BD trebuie sa ramana neschimbata.");
            assertEquals(CAPACITATE_INITIALA, sala.getCapacitate(), "Fara confirmare, capacitatea din BD trebuie sa ramana neschimbata.");
        }

        // Testul 5: Cerere de confirmare → ramura DA
        // Adminul modifica SI confirma → modificarile sunt persistate in BD

        @Test
        void testConfirmare() {
            String numeNou = "Sala Creanga (Extinsa)";
            int capacitateNoua = 100;

            // Pasul "Modificarea specificatiilor salii"
            admin.modificaSpecificatiiSala(sala, numeNou, capacitateNoua);

            // Pasul "Se salveaza modificarile in baza de date" (confirmare DA)
            // In implementarea curenta, modificaSpecificatiiSala scrie direct pe obiect
            assertEquals(numeNou, sala.getDenumire(), "Dupa confirmare (DA), denumirea trebuie salvata.");
            assertEquals(capacitateNoua, sala.getCapacitate(), "Dupa confirmare (DA), capacitatea trebuie salvata.");
        }

        // Testul 6: Flux complet – modificare + confirmare + verificare finala
        // Acopera intregul drum fericit din diagrama de activitate

        @Test
        void testFluxComplet_ModificareConfirmata() {
            // Pas 1: Admin autentificat selecteaza sala
            admin.login("admin@biblioteca.ro", "parolaAdmin");
            assertTrue(admin.isEsteAutentificat(), "Adminul trebuie sa fie autentificat.");

            // Pas 2: Specificatii initiale accesate
            assertEquals(NUME_INITIAL, sala.getDenumire());
            assertEquals(CAPACITATE_INITIALA, sala.getCapacitate());

            // Pas 3: Decizie DA – adminul modifica specificatiile
            String numeNou = "Sala Eminescu (Reamenajata)";
            int capacitateNoua = 60;
            admin.modificaSpecificatiiSala(sala, numeNou, capacitateNoua);

            // Pas 4: Cerere confirmare DA – modificarile se salveaza in BD
            assertEquals(numeNou, sala.getDenumire(), "Denumirea finala trebuie sa fie cea modificata si confirmata.");
            assertEquals(capacitateNoua, sala.getCapacitate(), "Capacitatea finala trebuie sa fie cea modificata si confirmata.");

            // Pas 5: Sala ramane in lista adminului
            assertTrue(admin.getSaliGestionate().contains(sala), "Sala modificata trebuie sa ramana gestionata de admin.");

            admin.logout();
            assertFalse(admin.isEsteAutentificat(), "Adminul trebuie sa fie deconectat.");
        }

        // Testul 7: Modificare disponibilitate in cadrul specificatiilor
        // Diagrama permite si schimbarea disponibilitatii ca specificatie

        @Test
        void testModificare_Disponibilitate_SalaIndisponibila() {
            // Adminul seteaza sala ca indisponibila (ex. renovare)
            admin.gestioneazaDisponibilitate(sala, false);

            assertFalse(sala.isDisponibilitate(), "Sala trebuie marcata INDISPONIBILA dupa modificare.");

            // Adminul restaureaza disponibilitatea
            admin.gestioneazaDisponibilitate(sala, true);

            assertTrue(sala.isDisponibilitate(), "Sala trebuie marcata DISPONIBILA dupa restaurare.");
        }
    }

