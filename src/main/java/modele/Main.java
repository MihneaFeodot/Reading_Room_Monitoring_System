package modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Testare Sistem Monitorizare Sali de Lectura ---\n");

        // 1. Creare si testare Sali de Lectura si Dotari
        System.out.println("1. TESTARE SALI SI DOTARI");
        Sala_De_Lectura sala1 = new Sala_De_Lectura(1, "Sala Mihai Eminescu", 50);
        Sala_De_Lectura sala2 = new Sala_De_Lectura(2, "Sala Ion Creanga", 30);
        
        List<Sala_De_Lectura> saliTotale = new ArrayList<>(Arrays.asList(sala1, sala2));

        sala1.updateDotari(Arrays.asList(Dotari_Sala.WI_FI, Dotari_Sala.PRIZA, Dotari_Sala.AER_CONDITIONAT));
        System.out.println("Disponibilitate " + sala1.getDenumire() + ": " + sala1.verificaDisponibilitate());
        System.out.println();

        // 2. Creare si testare Administrator
        System.out.println("2. TESTARE ADMINISTRATOR");
        Administrator admin = new Administrator(101, "Admin Popescu", "admin@biblioteca.ro", "parolaAdmin", "IT", 1);
        admin.setSaliGestionate(saliTotale);
        
        // Testare login
        admin.login("admin@biblioteca.ro", "parolaGresita"); // Autentificare esuata
        admin.login("admin@biblioteca.ro", "parolaAdmin"); // Autentificare reusita
        
        // Admin modifica o sala si ii schimba disponibilitatea
        admin.modificaSpecificatiiSala(sala2, "Sala Ion Creanga (Renovata)", 40);
        admin.gestioneazaDisponibilitate(sala2, false);
        
        // Admin genereaza si afiseaza un raport (compozitie - raportul e adaugat direct la lista adminului)
        Raport_Administrator raport = admin.genereazaRaport(1, "Activitate zilnica", "Sistemul functioneaza in parametri normali.");
        raport.afisareRaport();
        
        System.out.println("Rapoarte generate de " + admin.getNume() + ": " + admin.getRapoarte().size());
        
        admin.logout();
        System.out.println();
        
        // 3. Creare si testare Client si Rezervari
        System.out.println("3. TESTARE CLIENT SI REZERVARI");
        Client client = new Client(201, "Client Ionescu", "client@mail.ro", "parola123", "0712345678");
        client.login("client@mail.ro", "parola123");
        
        // Vizualizare sali (doar sala1 ar trebui sa fie disponibila)
        client.vizualizeazaSali(saliTotale);
        
        // Creare rezervare asociata salii 1
        Rezervare rezervare = new Rezervare(1001, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), sala1);
        client.creeazaRezervare(rezervare);
        
        System.out.println("Status rezervare curenta: " + rezervare.getStatus());
        System.out.println("Cod QR generat: " + rezervare.getCodQR());
        System.out.println("Sala rezervata: " + rezervare.getSalaAsociata().getDenumire());
        System.out.println("Istoric rezervari client: " + client.getIstoricRezervari().size());
        
        // Anulare rezervare
        client.anuleazaRezervare(1001);
        
        client.logout();
    }
}
