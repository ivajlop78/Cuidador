package edu.galileo.android.cuidador;

/**
 * Created by Javier on 09/12/2015.
 */
public class LlistaPacient {
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getCognoms() {
        return cognoms;
    }
    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getDNI() {
        return DNI;
    }

    private String DNI;
    private String nom;
    private String cognoms;
    public LlistaPacient(String dni,String nom,String cognoms){
        this.DNI=dni;
        this.nom=nom;
        this.cognoms=cognoms;
    }

}
