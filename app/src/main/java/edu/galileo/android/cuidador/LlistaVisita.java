package edu.galileo.android.cuidador;

import java.util.Date;

/**
 * Created by Javier on 09/12/2015.
 */
public class LlistaVisita {
    private String DNI;
    private String nom;
    private String cognoms;
    private Date data;
    private Date hora;
    private Date fecha;

    public Date getHora() {
        return hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

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
    public void setData(Date data){ this.data=data;}
    public Date getData(){return data;}



    public LlistaVisita(String dni, String nom, String cognoms,Date data,Date hora,Date fecha){
        this.DNI=dni;
        this.nom=nom;
        this.cognoms=cognoms;
        this.data=data;
        this.hora=hora;
        this.fecha=fecha;
    }

}
