package edu.galileo.android.cuidador.ParseBD;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParseBd {
     private Context contexto;
     public ParseBd(Context context){
         contexto =context;
     }

    /*** Solament s'ha d'obrir un cop
     *
     */
     public void obrirBDLocal(){
         Parse.enableLocalDatastore(contexto);
         ParseObject.registerSubclass(Cuidadors.class);
         ParseObject.registerSubclass(Pacients.class);
         ParseObject.registerSubclass(Visites.class);

     }

    public void sincronitzarTaulasC(Cuidadors cuidadors){
        ParseQuery<Cuidadors> queryC = Cuidadors.getQuery();
        queryC.findInBackground(new FindCallback<Cuidadors>() {
            @Override
            public void done(List<Cuidadors> objects, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground((List<Cuidadors>) objects, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.i("Cuidadors", "error al ficar dades en local");
                            }
                        }
                    });
                }
            }
        });


        /*try {
            queryC.orderByDescending("username");
            List<Cuidadors> cuidadores = queryC.find();
            int contadorCExtern = queryC.count();
            int contadorCLocal = queryC.fromLocalDatastore().count();
            List<Cuidadors> cuidadoresBdLocal =queryC.fromLocalDatastore().find();
            if (contadorCExtern != contadorCLocal) {
             /*   for (int x = 0; x < cuidadores.size(); x++) {
                    Cuidadors cuidador = cuidadores.get(x);
                    if(cuidador)
                    queryC.fromLocalDatastore().whereEqualTo("usuari", cuidador.getUsuari());
                    if (queryC.fromLocalDatastore().count() == 0) {
                        cuidador.pinInBackground();
                    }
                }
                for (int x = 0; x < cuidadores.size(); x++) {

                    if(!cuidadoresBdLocal.contains(cuidadores.get(x).getUsuari())){
                        cuidadores.get(x).pinInBackground();
                    }
                }

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public void sincronitzarTaulasP(Pacients pacients){
        ParseQuery<Pacients> queryC = Pacients.getQuery();
        queryC.findInBackground(new FindCallback<Pacients>() {
            @Override
            public void done(List<Pacients> objects, ParseException e) {
                if(e==null){
                    ParseObject.pinAllInBackground((List <Pacients>)objects, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!=null){
                                Log.i("Pacients", "error al ficar dades en local");
                            }
                        }
                    });
                }
            }
        });

        /*ParseQuery<Pacients> queryC = Pacients.getQuery();
        try {
            queryC.orderByDescending("username");
            List<Pacients> pacientes = queryC.find();
            int contadorCExtern = queryC.count();
            int contadorCLocal = queryC.fromLocalDatastore().count();
            List<Pacients> pacientesBdLocal =queryC.fromLocalDatastore().find();
            if (contadorCExtern != contadorCLocal) {
                /*for (int x = 0; x < pacientes.size(); x++) {
                    Pacients pacient = pacientes.get(x);
                    queryC.fromLocalDatastore().whereEqualTo("dni", pacient.getDni());
                    queryC.fromLocalDatastore().whereEqualTo("nom",pacient.getNom());
                    queryC.fromLocalDatastore().whereEqualTo("cognoms",pacient.getCognoms());
                    queryC.fromLocalDatastore().whereEqualTo("cuidador",pacient.getCuidador());
                    if (queryC.fromLocalDatastore().count() == 0) {
                        pacient.pinInBackground();
                    }
                }
                for (int x = 0; x < pacientes.size(); x++) {
                    if(!pacientesBdLocal.contains(pacientes.get(x).getDni())
                            ||!pacientesBdLocal.contains(pacientes.get(x).getNom())
                            || !pacientesBdLocal.contains(pacientes.get(x).getCognoms())
                            || !pacientesBdLocal.contains(pacientes.get(x).getCuidador())){
                        pacientes.get(x).pinInBackground();
                    }
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public void sincronitzarTaulasV(Visites visites){
        ParseQuery<Visites> queryC = Visites.getQuery();
        queryC.findInBackground(new FindCallback<Visites>() {
            @Override
            public void done(List<Visites> objects, ParseException e) {
                if(e==null){
                    ParseObject.pinAllInBackground((List <Visites>)objects, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!=null){
                                Log.i("Visites", "error al ficar dades en local");
                            }
                        }
                    });
                }
            }
        });
    }

    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param cuidador
     * @return
     */
    public boolean afegirCuidadors(Cuidadors cuidador){
        boolean inserta=false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cuidadors");
        query.whereEqualTo("usuari", cuidador.getUsuari().toLowerCase());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador==0){
                inserta =true;
                cuidador.pinInBackground();
                cuidador.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;

    }

    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param cuidador
     * @return
     */
    public boolean esCuidador(Cuidadors cuidador){
        boolean inserta=false;
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Cuidadors");
            query.whereEqualTo("usuari", cuidador.getUsuari());
            query.whereEqualTo("password", cuidador.getPassword());
            query.fromLocalDatastore();
            int contador =query.count();
            if (contador>0){
                inserta=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return inserta;
    }

    /***
     *  S'utilitza per la part de consulta de visites.
     * @param cuidador
     * @return
     */
    public List<Cuidadors> consultaCuidador(String cuidador){
        List<Cuidadors> cuidadores =null;
        ParseQuery<Cuidadors> query = Cuidadors.getQuery();
        query.whereEqualTo("usuari", cuidador);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                cuidadores = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cuidadores;
    }

    public boolean esborrarCuidador(Cuidadors cuidador){
        boolean isCuidador=false;
        ParseQuery<Cuidadors> query = Cuidadors.getQuery();
        query.whereEqualTo("usuari", cuidador.getUsuari());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                List<Cuidadors> cuidadorses = query.find();
                for(int x=0;x<cuidadorses.size();x++){
                    Cuidadors cuidadors = cuidadorses.get(x);
                    cuidadors.deleteEventually();
                }
                isCuidador=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isCuidador;
    }



    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param cuidadors
     * @return
     */
    public boolean modificarCuidadors(Cuidadors cuidadors){
        boolean inserta=false;
        ParseQuery<Cuidadors> query = Cuidadors.getQuery();
        query.whereEqualTo("usuari", cuidadors.getUsuari());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador>0){
                inserta =true;
                List<Cuidadors> cuidadorses = query.find();
                if(cuidadorses.size()==1){
                    Cuidadors esborraCuidador=cuidadorses.get(0);
                    esborraCuidador.unpin();
                    esborraCuidador.deleteEventually();
                }
                cuidadors.pinInBackground();
                cuidadors.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;
    }

    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param cuidadors
     * @return
     */
    public boolean modifPassword(Cuidadors cuidadors){
        boolean inserta=false;
        ParseQuery<Cuidadors> query = Cuidadors.getQuery();
        query.whereEqualTo("dni",cuidadors.getDni());
        query.whereEqualTo("usuari", cuidadors.getUsuari());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador>0){
                inserta =true;
                List<Cuidadors> cuidadorses = query.find();
                if(cuidadorses.size()==1){
                    Cuidadors esborraCuidador=cuidadorses.get(0);
                    esborraCuidador.unpin();
                    esborraCuidador.deleteEventually();
                }
                cuidadors.pinInBackground();
                cuidadors.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;
    }

    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param pacient
     * @return
     */
    public boolean afegirPacients(Pacients pacient){
        boolean inserta=false;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("dni", pacient.getDni().toLowerCase());
        query.whereEqualTo("telCte", pacient.getTelCte());
        query.whereEqualTo("telPac", pacient.getTelPac());
        query.whereEqualTo("cuidador",pacient.getCuidador());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador==0){
                inserta =true;
                pacient.pinInBackground();
                pacient.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;
    }

    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param pacient
     * @return
     */
    public boolean modificarPacients(Integer telCteAnt,Integer telPacAnt,Pacients pacient){
        boolean inserta=false;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("dni", pacient.getDni().toLowerCase());
        query.whereEqualTo("telCte",telCteAnt);
        query.whereEqualTo("telPac", telPacAnt);
        query.whereEqualTo("cuidador",pacient.getCuidador());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador>0){
                inserta =true;
                List<Pacients> pacientses = query.find();
                if(pacientses.size()==1){
                    Pacients esborraPacient=pacientses.get(0);
                    esborraPacient.unpin();
                    esborraPacient.deleteEventually();
                }
                pacient.pinInBackground();
                pacient.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;
    }

    public boolean esborrarPacient(String cuidador){
        boolean isPacient=false;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("cuidador", cuidador);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                List<Pacients> pacientses = query.find();
                for(int x=0;x<pacientses.size();x++){
                    Pacients pacients = pacientses.get(x);
                    pacients.deleteEventually();
                }
                isPacient=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isPacient;
    }

    public boolean esborrarPacient(Pacients pacient){
        boolean isPacient=false;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("dni",pacient.getDni());
        query.whereEqualTo("cuidador", pacient.getCuidador());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                List<Pacients> pacientses = query.find();
                for(int x=0;x<pacientses.size();x++){
                    Pacients pacients = pacientses.get(x);
                    pacients.deleteEventually();
                }
                isPacient=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isPacient;
    }

    /***
     *  S'utilitza per la part de consulta de visites.
     * @param telPac
     * @return
     */
    public List<Pacients> consultaPacient(Integer telPac){
        List<Pacients> pacientses =null;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("telPac", telPac);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                pacientses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pacientses;
    }


    public List<Pacients> consultaPacient(String text,String cuidador){
        List<Pacients> pacientses =null;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("cuidador",cuidador);
        query.whereContains("nom", text);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                 pacientses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pacientses;
    }

    public List<Pacients> consultaPacient(String nom,String dni,String cuidador){
        List<Pacients> pacientses =null;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("cuidador",cuidador);
        query.whereEqualTo("dni", dni);
        query.whereEqualTo("nom", nom);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                pacientses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pacientses;
    }


    public List<Pacients> consultaPacientModif(String dni,String cuidador){
        List<Pacients> pacientses =null;
        ParseQuery<Pacients> query = Pacients.getQuery();
        query.whereEqualTo("cuidador",cuidador);
        query.whereEqualTo("dni", dni);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                pacientses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pacientses;
    }


    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param visites
     * @return
     */
    public boolean modificarVisites(Date fechaAnt,Visites visites){
        boolean inserta=false;
        ParseQuery<Visites> query = visites.getQuery();
        query.whereEqualTo("dni", visites.getDni().toLowerCase());
        query.whereEqualTo("cuidador", visites.getCuidador());
        query.whereEqualTo("data", fechaAnt);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador>0){
                inserta =true;
                List<Visites> visiteses = query.find();
                if(visiteses.size()==1){
                    Visites esborraVisita=visiteses.get(0);
                    esborraVisita.unpin();
                    esborraVisita.deleteEventually();
                }
                visites.pinInBackground();
                visites.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;
    }

    /***
     *  S'utilitza per la part de consulta de visites.
     * @param text
     * @param cuidador
     * @return
     */
    public List<Visites> consultaVisita(String text,String cuidador,String fechaCalendar){
        List<Visites> visiteses =null;
        ParseQuery<Visites> query = Visites.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("cuidador", cuidador);
        if(fechaCalendar!=null && !"".equals(fechaCalendar)){
            SimpleDateFormat fechaFormat=new SimpleDateFormat("dd/MM/yyyy");
            try {
                query.whereEqualTo("fecha",fechaFormat.parse(fechaCalendar));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        query.whereContains("nom", text);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                visiteses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return visiteses;
    }

    /***
     *  S'utilitza per la part de consulta de visites.
     * @param data
     * @param cuidador
     * @return
     */
    public List<Visites> consultaVisita(Date data ,String cuidador){
        List visiteses =null;
        String fecha;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        fecha=dateFormatter.format(data);
        ParseQuery query = Visites.getQuery();
        query.whereEqualTo("cuidador", cuidador);
        query.whereContains("data", fecha);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                visiteses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return visiteses;
    }

    public List<Date> consultaVisita(Date fechaD ,Date fechaH,String cuidador){
        List<Date> fechas=null;
        List visiteses =null;
        String fechaDesde;
        String fechaHasta;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaDesde =dateFormatter.format(fechaD);
        fechaHasta =dateFormatter.format(fechaH);
        ParseQuery query = Visites.getQuery();
        query.whereEqualTo("cuidador",cuidador);
        query.whereGreaterThanOrEqualTo("fecha", fechaD);
        query.whereLessThanOrEqualTo("fecha", fechaH);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                visiteses = query.find();
                for(int x=0;x<visiteses.size();x++){
                    Visites visita=(Visites)visiteses.get(x);
                    if(visita.getFecha()!=null) {
                        if(fechas==null){
                            fechas=new ArrayList<Date>();
                        }
                        fechas.add(visita.getFecha());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechas;
    }

    public List<Date> consultaVisita(Date fechaD ,Date fechaH,String dni,boolean esPacient){
        List<Date> fechas=null;
        List visiteses =null;
        String fechaDesde;
        String fechaHasta;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaDesde =dateFormatter.format(fechaD);
        fechaHasta =dateFormatter.format(fechaH);
        ParseQuery query = Visites.getQuery();
        query.whereEqualTo("dni",dni);
        query.whereGreaterThanOrEqualTo("fecha", fechaD);
        query.whereLessThanOrEqualTo("fecha", fechaH);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                visiteses = query.find();
                for(int x=0;x<visiteses.size();x++){
                    Visites visita=(Visites)visiteses.get(x);
                    if(visita.getFecha()!=null) {
                        if(fechas==null){
                            fechas=new ArrayList<Date>();
                        }
                        fechas.add(visita.getFecha());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechas;
    }

    public boolean esborrarVisita(String cuidador){
        boolean isVisita=false;
        ParseQuery<Visites> query = Visites.getQuery();
        query.whereEqualTo("cuidador", cuidador);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                List<Visites> visiteses = query.find();
                for(int x=0;x<visiteses.size();x++){
                    Visites visites = visiteses.get(x);
                    visites.deleteEventually();
                }
                isVisita=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isVisita;
    }

    public boolean esborrarVisita(Visites visita){
        boolean isVisita=false;
        ParseQuery<Visites> query = Visites.getQuery();
        query.whereEqualTo("dni", visita.getDni());
        query.whereEqualTo("cuidador", visita.getCuidador());
        if(visita.getData()!=null) {
            query.whereEqualTo("data", visita.getData());
        }
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                List<Visites> visiteses = query.find();
                for(int x=0;x<visiteses.size();x++){
                    Visites visites = visiteses.get(x);
                    visites.deleteEventually();
                }
                isVisita=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isVisita;
    }

    /**** Si ha anat bé retorna un true. En altre cas un false;
     *
     * @param visites
     * @return
     */
    public boolean afegirVisites(Visites visites){
        boolean inserta=false;
        ParseQuery<Visites> query = Visites.getQuery();
        query.whereEqualTo("cuidador", visites.getCuidador());
        query.whereEqualTo("fecha",visites.getFecha());
        query.whereEqualTo("hora",visites.getHora());
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if (contador==0){
                inserta =true;
                visites.pinInBackground();
                visites.saveEventually();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inserta;
    }

    public List<Visites> consultaVisitaModif(Date data,String dni,String cuidador){
        List<Visites> visiteses =null;
        ParseQuery<Visites> query = Visites.getQuery();
        query.whereEqualTo("cuidador",cuidador);
        query.whereEqualTo("dni", dni);
        query.whereEqualTo("data",data);
        query.fromLocalDatastore();
        try {
            int contador=query.count();
            if(contador>0){
                visiteses = query.find();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return visiteses;
    }




}
