package modele;

import java.util.ArrayList;
import java.util.List;

public class Sala_De_Lectura {
    private int IDSala;
    private String denumire;
    private int capacitate;
    private boolean disponibilitate;
    
    // Relatie cu Dotari_Sala
    private List<Dotari_Sala> dotari;

    public Sala_De_Lectura(int IDSala, String denumire, int capacitate) {
        this.IDSala = IDSala;
        this.denumire = denumire;
        this.capacitate = capacitate;
        this.disponibilitate = true;
        this.dotari = new ArrayList<>();
    }

    public void updateDotari(List<Dotari_Sala> noiDotari) {
        this.dotari = noiDotari;
        System.out.println("Dotarile salii " + denumire + " au fost actualizate.");
    }

    public boolean verificaDisponibilitate() {
        return this.disponibilitate;
    }

    // Getters si Setters
    public int getIDSala() { return IDSala; }
    public void setIDSala(int IDSala) { this.IDSala = IDSala; }
    
    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }
    
    public int getCapacitate() { return capacitate; }
    public void setCapacitate(int capacitate) { this.capacitate = capacitate; }
    
    public boolean isDisponibilitate() { return disponibilitate; }
    public void setDisponibilitate(boolean disponibilitate) { this.disponibilitate = disponibilitate; }

    public List<Dotari_Sala> getDotari() { return dotari; }
    public void setDotari(List<Dotari_Sala> dotari) { this.dotari = dotari; }
}
