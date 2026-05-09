package modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rezervare {
    private int IDrezervare;
    private LocalDate dataRezervare;
    private LocalTime oraInceput;
    private LocalTime oraSfarsit;
    private String status;
    private String codQR;
    
    // Asociere catre Sala_De_Lectura
    private Sala_De_Lectura salaAsociata;

    public Rezervare(int IDrezervare, LocalDate data, LocalTime start, LocalTime sfarsit, Sala_De_Lectura salaAsociata) {
        this.IDrezervare = IDrezervare;
        this.dataRezervare = data;
        this.oraInceput = start;
        this.oraSfarsit = sfarsit;
        this.salaAsociata = salaAsociata;
        this.status = "PENDING";
        this.codQR = "QR_" + IDrezervare + "_" + data.toString();
    }

    // Getters si Setters
    public int getIDrezervare() { return IDrezervare; }
    public void setIDrezervare(int IDrezervare) { this.IDrezervare = IDrezervare; }

    public LocalDate getDataRezervare() { return dataRezervare; }
    public void setDataRezervare(LocalDate dataRezervare) { this.dataRezervare = dataRezervare; }

    public LocalTime getOraInceput() { return oraInceput; }
    public void setOraInceput(LocalTime oraInceput) { this.oraInceput = oraInceput; }

    public LocalTime getOraSfarsit() { return oraSfarsit; }
    public void setOraSfarsit(LocalTime oraSfarsit) { this.oraSfarsit = oraSfarsit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCodQR() { return codQR; }
    public void setCodQR(String codQR) { this.codQR = codQR; }

    public Sala_De_Lectura getSalaAsociata() { return salaAsociata; }
    public void setSalaAsociata(Sala_De_Lectura salaAsociata) { this.salaAsociata = salaAsociata; }
}
