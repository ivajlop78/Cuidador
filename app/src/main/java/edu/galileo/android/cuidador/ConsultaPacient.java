package edu.galileo.android.cuidador;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.AdaptadorPacient;
import edu.galileo.android.cuidador.ParseBD.Pacients;
import edu.galileo.android.cuidador.ParseBD.ParseBd;
import edu.galileo.android.cuidador.ParseBD.Visites;


public class ConsultaPacient extends Fragment implements OnQueryTextListener {
    @Bind(R.id.searchPacient)
    SearchView searchPacient;
    @Bind(R.id.btEnrere)
    ImageButton btEnrere;
    @Bind(R.id.consultaPacient)
    ListView consultaPacient;
    private Context context = null;
    private String cuidador = null;
    public String posicion = "";
    ListView lista = null;
    //private SearchView searchView1;
    ArrayAdapter<String> adaptador;
    final ArrayList<LlistaPacient> lPacients = new ArrayList<LlistaPacient>();
    private String searchViewOr;

    public ConsultaPacient() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.consulta_pacient, container, false);

        context = container.getContext();
        searchPacient = (SearchView) rootView.findViewById(R.id.searchPacient);
        int id = context.getResources().getIdentifier("android:id/search_src_text", null, null);
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            cuidador = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_ConsultaP));
        }
        if (getArguments() != null) {
            searchViewOr = getArguments().getString("searchView");
        }
        TextView texto = (TextView) rootView.findViewById(id);
        texto.setTextColor(Color.parseColor("#00175c"));
        texto.setTextSize(16);
        lista = (ListView) rootView.findViewById(R.id.consultaPacient);
        lista.setTextFilterEnabled(true);
        lista.setItemsCanFocus(true);
        lista.setClickable(true);
        omplirLista(null);
        configurarSearchView();
        if (searchViewOr != null) {
            searchPacient.setQuery(searchViewOr, true);
        }
        ImageButton btCancelar = (ImageButton) rootView.findViewById(R.id.btEnrere);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EntradaCuidador();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle(R.string.title_activity_cuidador_activiy);
            }
        });
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void configurarSearchView() {
        searchPacient.setIconified(false);
        searchPacient.setOnQueryTextListener(this);
        searchPacient.setQueryHint("Busca aquí");

    }


    private void omplirLista(final String newText) {

        try {
            List<Pacients> listaP = null;
            lPacients.clear();
            Pacients pacient = new Pacients();
            ParseQuery<Pacients> query = pacient.getQuery();
            query.fromLocalDatastore();
            if (newText == null || "".equals(newText)) {
                if (cuidador != null && !"".equals(cuidador)) {
                    query.whereEqualTo("cuidador", cuidador);
                }
                query.orderByAscending("nom");
                int contador = query.count();
                listaP = query.find();
            } else {

                ParseBd bd = new ParseBd(context);
                listaP = bd.consultaPacient(newText.toString(), cuidador);
                if (listaP != null) {
                    Iterator<Pacients> it = listaP.iterator();
                }
            }

            if (listaP != null) {
                Iterator<Pacients> it = listaP.iterator();
                int x = 0;
                while (it.hasNext()) {
                    Pacients pacientObject = it.next();
                    lPacients.add(new LlistaPacient(pacientObject.getDni(), pacientObject.getNom(), pacientObject.getCognoms()));
                    x++;
                }
            }
            lista.setAdapter(new AdaptadorPacient(getActivity(), R.layout.fila, lPacients) {
                @Override
                public void onEntrada(final Object entrada, View view) {
                    if (entrada != null) {
                        final TextView dni = (TextView) view.findViewById(R.id.listDni);
                        dni.setText(((LlistaPacient) entrada).getDNI());
                        final TextView nom = (TextView) view.findViewById(R.id.listNom);
                        nom.setText(((LlistaPacient) entrada).getNom());
                        final TextView cognoms = (TextView) view.findViewById(R.id.listCognoms);
                        cognoms.setText(((LlistaPacient) entrada).getCognoms());
                        ImageButton btElim = (ImageButton) view.findViewById(R.id.btElim);
                        ImageButton btModif = (ImageButton) view.findViewById(R.id.btModif);
                        ImageButton btAltaV = (ImageButton) view.findViewById(R.id.btAltaV);
                        btElim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                final String nombre = nom.getText().toString();
                                final String apell = cognoms.getText().toString();
                                final String docNacional = dni.getText().toString();
                                builder.setTitle("Borrar paciente " + nombre + " " + apell + " con DNI " + docNacional + "?");
                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Pacients pacient = new Pacients();
                                        pacient.setCuidador(cuidador);
                                        pacient.setDni(docNacional);
                                        pacient.setNom(nombre);
                                        pacient.setCognoms(apell);
                                        ParseBd bd = new ParseBd(context);
                                        boolean esbPacient = bd.esborrarPacient(pacient);
                                        Visites visita = new Visites();
                                        visita.setCuidador(cuidador);
                                        visita.setDni(pacient.getDni());
                                        boolean esbVisita = bd.esborrarVisita(visita);
                                        if (esbPacient && esbVisita) {
                                            omplirLista(newText);
                                            Toast.makeText(getActivity(), "Paciente borrado", Toast.LENGTH_SHORT).show();
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
                                bSi.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_dialog));
                                bSi.setTextColor(Color.WHITE);

                                Button bNo = d.getButton(DialogInterface.BUTTON_NEGATIVE);
                                bNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_dialog));
                                bNo.setTextColor(Color.WHITE);


                            }
                        });
                        btModif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment fragment = new ModificacioPacient();
                                //Validamos si el fragment no es nulo
                                if (fragment != null) {

                                    FragmentManager fragmentManager = getFragmentManager();
                                    Bundle parametro = new Bundle();
                                    parametro.putString("dniCP", dni.getText().toString());
                                    parametro.putString("tituloAnt", getString(R.string.mn_ConsultaP));
                                    fragment.setArguments(parametro);
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                    getActivity().setTitle("Modificación paciente");
                                } else {
                                    //Si el fragment es nulo mostramos un mensaje de error.
                                    Log.e("Error  ", "Mostrando fragmento modificación paciente");
                                }
                            }
                        });

                        btAltaV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment fragment = new AltaVisita();
                                //Validamos si el fragment no es nulo
                                if (fragment != null) {

                                    FragmentManager fragmentManager = getFragmentManager();
                                    Bundle parametro = new Bundle();
                                    parametro.putString("dniCP", dni.getText().toString());
                                    parametro.putString("tituloAnt", getString(R.string.mn_ConsultaP));
                                    fragment.setArguments(parametro);
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                    getActivity().setTitle("Alta Visita");
                                } else {
                                    //Si el fragment es nulo mostramos un mensaje de error.
                                    Log.e("Error  ", "Mostrar Fragmento modificación paciente");
                                }
                            }
                        });

                    }
                }

            });
        } catch (ParseException e1) {
            e1.printStackTrace();
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
        intentRe.putExtra("searchView", searchPacient.getQuery().toString());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btEnrere)
    public void onClick() {
    }
}