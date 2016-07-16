package edu.galileo.android.cuidador;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.cuidador.ParseBD.AdaptadorPacient;
import edu.galileo.android.cuidador.ParseBD.Pacients;
import edu.galileo.android.cuidador.ParseBD.ParseBd;
import edu.galileo.android.cuidador.ParseBD.Visites;

public class ConsultaVisita extends Fragment implements OnQueryTextListener {
    @Bind(R.id.searchVisita)
    SearchView searchVisita;
    @Bind(R.id.btEnrere)
    ImageButton btEnrere;
    @Bind(R.id.consultaVisita)
    ListView consultaVisita;
    private Context context = null;
    private String cuidador = null;
    ListView lista = null;
    //private SearchView searchView1;
    final ArrayList<LlistaVisita> lVisites = new ArrayList<LlistaVisita>();
    private String searchViewOr;
    private String fechaCalendar;
    private String tituloAnt;

    public ConsultaVisita() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.consulta_visita, container, false);

        context = container.getContext();
        searchVisita = (SearchView) rootView.findViewById(R.id.searchVisita);
        int id = context.getResources().getIdentifier("android:id/search_src_text", null, null);
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            cuidador = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_consultaV));

        }
        if (getArguments() != null) {
            searchViewOr = getArguments().getString("searchView");
            fechaCalendar = getArguments().getString("fechaCalendar");
            tituloAnt = getArguments().getString("tituloAnt");
        }
        TextView texto = (TextView) rootView.findViewById(id);
        texto.setTextColor(Color.parseColor("#00175c"));
        texto.setTextSize(16);
        lista = (ListView) rootView.findViewById(R.id.consultaVisita);
        lista.setTextFilterEnabled(true);
        lista.setItemsCanFocus(true);
        lista.setClickable(true);
        omplirLista(null);
        configurarSearchView();
        if (searchViewOr != null) {
            searchVisita.setQuery(searchViewOr, true);
        }
        ImageButton btCancelar = (ImageButton) rootView.findViewById(R.id.btEnrere);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tituloAnt != null && !"".equals(tituloAnt)) {
                    if (getString(R.string.mn_Calendari).equals(tituloAnt)) {
                        Fragment fragment = new CalendariPersonalizado();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        getActivity().setTitle(R.string.mn_Calendari);
                    }

                } else {
                    Fragment fragment = new EntradaCuidador();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    getActivity().setTitle(R.string.title_activity_cuidador_activiy);
                }
            }
        });

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void configurarSearchView() {
        searchVisita.setIconified(false);
        searchVisita.setOnQueryTextListener(this);
        searchVisita.setQueryHint("Busca aquí");

    }


    private void omplirLista(final String newText) {

        try {
            List<Visites> listaV;
            lVisites.clear();
            ParseQuery<Visites> query = Visites.getQuery();
            query.fromLocalDatastore();
            if (newText == null || "".equals(newText)) {
                if (cuidador != null && !"".equals(cuidador)) {
                    query.whereEqualTo("cuidador", cuidador);
                }
                if (fechaCalendar != null) {
                    SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yyyy");
                    query.whereEqualTo("fecha", fechaFormat.parse(fechaCalendar));
                }
                query.orderByAscending("data");
                listaV = query.find();
            } else {
                ParseBd bd = new ParseBd(context);
                listaV = bd.consultaVisita(newText, cuidador, fechaCalendar);
            }

            if (listaV != null) {
                for (Visites visitaObject : listaV) {
                    lVisites.add(new LlistaVisita(visitaObject.getDni(), visitaObject.getNom(), visitaObject.getCognoms(), visitaObject.getData(), visitaObject.getFecha(), visitaObject.getHora()));
                }
            }
            lista.setAdapter(new AdaptadorPacient(getActivity(), R.layout.fila_visita, lVisites) {
                @Override
                public void onEntrada(final Object entrada, View view) {
                    if (entrada != null) {
                        final TextView dni = (TextView) view.findViewById(R.id.listDni);
                        dni.setText(((LlistaVisita) entrada).getDNI());
                        final TextView nom = (TextView) view.findViewById(R.id.listNom);
                        nom.setText(((LlistaVisita) entrada).getNom() + " " + ((LlistaVisita) entrada).getCognoms());
                        final TextView nomVisita = (TextView) view.findViewById(R.id.listNomVisita);
                        nomVisita.setText(((LlistaVisita) entrada).getNom());
                        final TextView data = (TextView) view.findViewById(R.id.listData);
                        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        Date fecha = ((LlistaVisita) entrada).getData();
                        data.setText(dateFormatter.format(fecha));
                        ImageButton btElim = (ImageButton) view.findViewById(R.id.btElim);
                        ImageButton btModif = (ImageButton) view.findViewById(R.id.btModif);
                        btElim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                final String nombre = nom.getText().toString();
                                final String docNacional = dni.getText().toString();
                                final String fecha = data.getText().toString();
                                builder.setTitle("Borrar visita " + nombre + " en la fecha " + fecha + "?");
                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Visites visita = new Visites();
                                        visita.setCuidador(cuidador);
                                        visita.setDni(docNacional);
                                        try {
                                            visita.setData(dateFormatter.parse(data.getText().toString()));
                                        } catch (java.text.ParseException e) {
                                            e.printStackTrace();
                                        }
                                        ParseBd bd = new ParseBd(context);
                                        if (bd.esborrarVisita(visita)) {
                                            List<Pacients> pacientsesNot = bd.consultaPacient(nomVisita.getText().toString(), docNacional, cuidador);
                                            if (pacientsesNot != null && pacientsesNot.size() > 0) {
                                                Pacients pacientsNot = pacientsesNot.get(0);
                                                Integer telContacte = pacientsNot.getTelCte();
                                                Integer telPaciente = pacientsNot.getTelPac();
                                                SmsManager sms = SmsManager.getDefault();
                                                String telefono;
                                                if (telContacte != null) {
                                                    telefono = String.valueOf(telContacte);
                                                } else {
                                                    telefono = String.valueOf(telPaciente);
                                                }
                                                sms.sendTextMessage(telefono, null, "Se ha borrado la visita para la fecha " + fecha, null, null);
                                            }
                                            omplirLista(newText);
                                            Toast.makeText(getActivity(), "Visita borrada", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                        .setNegativeButton("No",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                builder.create();
                                AlertDialog d = builder.show();
                                int textTitle = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                                TextView tv = (TextView) d.findViewById(textTitle);
                                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                                Button bSi = d.getButton(DialogInterface.BUTTON_POSITIVE);
                                bSi.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector));
                                bSi.setTextColor(Color.WHITE);
                                Button bNo = d.getButton(DialogInterface.BUTTON_NEGATIVE);
                                bNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector));
                                bNo.setTextColor(Color.WHITE);

                            }
                        });
                        btModif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment fragment = new ModificacioVisita();
                                //Validamos si el fragment no es nulo
                                FragmentManager fragmentManager = getFragmentManager();
                                Bundle parametro = new Bundle();
                                parametro.putString("dniCP", dni.getText().toString());
                                parametro.putString("dataCP", data.getText().toString());
                                parametro.putString("tituloAnt", getString(R.string.mn_consultaV));
                                fragment.setArguments(parametro);
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                getActivity().setTitle("Modificación visita");
                            }
                        });
                    }
                }

            });
        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        omplirLista(newText);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String arg0) {
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent intentRe = getActivity().getIntent();
        intentRe.putExtra("searchView", searchVisita.getQuery().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}