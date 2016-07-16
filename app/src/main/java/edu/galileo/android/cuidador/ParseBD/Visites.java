package edu.galileo.android.cuidador.ParseBD;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("Visites")
public class Visites extends ParseObject{


    public String getDni(){return getString("dni");}
    public void setDni(String dni){
        put("dni",dni);
    }
    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom",nom);
    }

    public String getCognoms() {
        return getString("cognoms");
    }

    public void setCognoms(String cognoms) {
        put("cognoms",cognoms);
    }
    public void setCuidador(String cuidador){
        put("cuidador",cuidador);
    }
    public String getCuidador(){
        return getString("cuidador");
    }
    public void setData(Date data){
        put("data",data);
    }
    public Date getData(){
        return getDate("data");
    }
    public Date getHora() {
        return getDate("hora");
    }

    public void setHora(Date hora) {
        put("hora",hora);
    }

    public Date getFecha() {
        return  getDate("fecha");
    }

    public void setFecha(Date fecha) {
        put("fecha",fecha);
    }


    public static ParseQuery<Visites> getQuery(){
        return ParseQuery.getQuery(Visites.class);
    }
}
