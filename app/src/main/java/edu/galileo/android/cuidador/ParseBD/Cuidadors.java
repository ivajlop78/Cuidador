package edu.galileo.android.cuidador.ParseBD;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Cuidadors")
public class Cuidadors  extends ParseObject{
    public String getDni() {return getString("dni");}
    public void setDni(String dni) {
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

    public String getEmpresa() {
        return getString("empresa");
    }

    public void setEmpresa(String empresa) {
        put("empresa",empresa);
    }

    public String getUsuari() {
        return getString("usuari");
    }

    public void setUsuari(String usuari) {
        put("usuari",usuari);
    }

    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password",password);
    }
    public static ParseQuery<Cuidadors> getQuery(){
        return ParseQuery.getQuery(Cuidadors.class);
    }


}
