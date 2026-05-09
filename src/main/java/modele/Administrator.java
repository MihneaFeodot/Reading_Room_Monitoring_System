package modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrator extends Utilizator {
    private String departament;
    private int nivelAcces;
    
    // Asociere 1 la * (One-To-Many): Sala_De_Lectura
    private List<Sala_De_Lectura> saliGestionate;
    
    // Compozitie cu RaportAdministrator
    private List<Raport_Administrator> rapoarte;

    public Administrator(int ID, String nume, String email, String parola, String departament, int nivelAcces) {
        super(ID, nume, email, parola);
        this.departament = departament;
        this.nivelAcces = nivelAcces;
        this.saliGestionate = new ArrayList<>();
        this.rapoarte = new ArrayList<>();
    }

    public void modificaSpecificatiiSala(Sala_De_Lectura sala, String numeNou, int capacitateNoua) {
        sala.setDenumire(numeNou);
        sala.setCapacitate(capacitateNoua);
        System.out.println("Sala " + sala.getIDSala() + " a fost modificata de administrator.");
    }

    public Raport_Administrator genereazaRaport(int idRaport, String tip, String continut) {
        System.out.println("Se genereaza raportul tip: " + tip);
        Raport_Administrator raport = new Raport_Administrator(idRaport, LocalDate.now(), tip, continut);
        // Implementarea compozitiei, adaugand in lista rapoarte interna a instantei care il creeaza
        this.rapoarte.add(raport);
        return raport;
    }

    public void gestioneazaDisponibilitate(Sala_De_Lectura sala, boolean statusNou) {
        sala.setDisponibilitate(statusNou);
        String st = statusNou ? "DISPONIBILA" : "INDISPONIBILA";
        System.out.println("Sala " + sala.getDenumire() + " este acum " + st);
    }

    // Getters si Setters
    public String getDepartament() { return departament; }
    public void setDepartament(String departament) { this.departament = departament; }

    public int getNivelAcces() { return nivelAcces; }
    public void setNivelAcces(int nivelAcces) { this.nivelAcces = nivelAcces; }

    public List<Sala_De_Lectura> getSaliGestionate() { return saliGestionate; }
    public void setSaliGestionate(List<Sala_De_Lectura> saliGestionate) { this.saliGestionate = saliGestionate; }

    public List<Raport_Administrator> getRapoarte() { return rapoarte; }
    public void setRapoarte(List<Raport_Administrator> rapoarte) { this.rapoarte = rapoarte; }
}
