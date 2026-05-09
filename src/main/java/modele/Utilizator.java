package modele;

public abstract class Utilizator {
    private int ID;
    private String nume;
    private String email;
    private String parola;
    private boolean esteAutentificat;

    public Utilizator(int ID, String nume, String email, String parola) {
        this.ID = ID;
        this.nume = nume;
        this.email = email;
        this.parola = parola;
        this.esteAutentificat = false;
    }

    public void login(String emailIntrodus, String parolaIntrodusa) {
        if (this.email.equals(emailIntrodus) && this.parola.equals(parolaIntrodusa)) {
            this.esteAutentificat = true;
            System.out.println("Autentificare reusita pentru: " + nume);
        } else {
            System.out.println("Email sau parola incorecta.");
        }
    }

    public void logout() {
        this.esteAutentificat = false;
        System.out.println("Utilizatorul " + nume + " s-a deconectat.");
    }

    // Getters si Setters
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }

    public boolean isEsteAutentificat() { return esteAutentificat; }
    public void setEsteAutentificat(boolean esteAutentificat) { this.esteAutentificat = esteAutentificat; }
}
