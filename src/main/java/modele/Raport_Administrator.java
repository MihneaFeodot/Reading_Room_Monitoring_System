package modele;

import java.time.LocalDate;

public class Raport_Administrator {
    private int IDReport;
    private LocalDate dataGenerare;
    private String tipRaport;
    private String continut;

    public Raport_Administrator(int IDReport, LocalDate dataGenerare, String tipRaport, String continut) {
        this.IDReport = IDReport;
        this.dataGenerare = dataGenerare;
        this.tipRaport = tipRaport;
        this.continut = continut;
    }

    public void afisareRaport() {
        System.out.println("--- RAPORT " + tipRaport + " ---");
        System.out.println("Data: " + dataGenerare);
        System.out.println("Continut: " + continut);
    }

    // Getters si Setters
    public int getIDReport() { return IDReport; }
    public void setIDReport(int IDReport) { this.IDReport = IDReport; }

    public LocalDate getDataGenerare() { return dataGenerare; }
    public void setDataGenerare(LocalDate dataGenerare) { this.dataGenerare = dataGenerare; }

    public String getTipRaport() { return tipRaport; }
    public void setTipRaport(String tipRaport) { this.tipRaport = tipRaport; }

    public String getContinut() { return continut; }
    public void setContinut(String continut) { this.continut = continut; }
}
