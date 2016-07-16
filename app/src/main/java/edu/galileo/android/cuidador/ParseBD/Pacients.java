package edu.galileo.android.cuidador.ParseBD;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Pacients")
public class Pacients extends ParseObject{
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

    public String getadreca() {
        return getString("adreca");
    }

    public void setadreca(String adreca) {
        put("adreca",adreca);
    }

    public String getPersonaCte() {
        return getString("personaCte");
    }

    public void setPersonaCte(String personaCte) {
        put("personaCte",personaCte);
    }

    public Integer getTelCte() {
        return getInt("telCte");
    }

    public void setTelCte(Integer telCte) {
        put("telCte",telCte);
    }
    public Integer getTelPac() {
        return getInt("telPac");
    }
    public void setCuidador(String cuidador){
        put("cuidador",cuidador);
    }
    public String getCuidador(){
        return getString("cuidador");
    }

    public void setTelPac(Integer telPac) {
        put("telPac",telPac);
    }
    public String getNivell(){return getString("nivell");}
    public void setNivell(String nivell){
        put("nivell",nivell);
    }
    public static ParseQuery<Pacients> getQuery(){
        return ParseQuery.getQuery(Pacients.class);
    }
}
