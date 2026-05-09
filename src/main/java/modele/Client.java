package modele;

import java.util.ArrayList;
import java.util.List;

public class Client extends Utilizator {
    private String numarTelefon;
    
    // Asociere 1 la *: Rezervare
    private List<Rezervare> istoricRezervari;

    public Client(int ID, String nume, String email, String parola, String numarTelefon) {
        super(ID, nume, email, parola);
        this.numarTelefon = numarTelefon;
        this.istoricRezervari = new ArrayList<>();
    }

    public void creeazaRezervare(Rezervare rezervareNoua) {
        if (isEsteAutentificat()) {
            this.istoricRezervari.add(rezervareNoua);
            rezervareNoua.setStatus("CONFIRMATA");
            System.out.println("Rezervarea cu ID " + rezervareNoua.getIDrezervare() + " a fost salvata.");
        } else {
            System.out.println("Trebuie sa fiti autentificat pentru a face o rezervare.");
        }
    }

    public void anuleazaRezervare(int idRezervare) {
        for (Rezervare r : istoricRezervari) {
            if (r.getIDrezervare() == idRezervare) {
                r.setStatus("ANULATA");
                System.out.println("Rezervarea " + idRezervare + " a fost anulata.");
                return;
            }
        }
        System.out.println("Rezervarea nu a fost gasita.");
    }

    public void vizualizeazaSali(List<Sala_De_Lectura> saliTotale) {
        System.out.println("Sali disponibile in acest moment:");
        for (Sala_De_Lectura s : saliTotale) {
            if (s.isDisponibilitate()) {
                System.out.println("- " + s.getDenumire() + " (Capacitate: " + s.getCapacitate() + ")");
            }
        }
    }

    public String proceseazaCheckIn(String codQR, Rezervare rezervareExistenta) {
        if (codQR == null || !codQR.startsWith("QR_")) {
            return "Eroare: Cod invalid";
        }
        if (rezervareExistenta == null) {
            return "Optiune: Creare rezervare noua?";
        }
        if (rezervareExistenta.getStatus().equals("CONFIRMATA")) {
            rezervareExistenta.setStatus("OCUPAT");
            return "Acces permis";
        }
        return "Eroare: Rezervare inactiva";
    }

    // Getters si Setters
    public String getNumarTelefon() { return numarTelefon; }
    public void setNumarTelefon(String numarTelefon) { this.numarTelefon = numarTelefon; }

    public List<Rezervare> getIstoricRezervari() { return istoricRezervari; }
    public void setIstoricRezervari(List<Rezervare> istoricRezervari) { this.istoricRezervari = istoricRezervari; }
}
