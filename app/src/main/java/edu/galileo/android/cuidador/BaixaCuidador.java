package edu.galileo.android.cuidador;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Cuidadors;
import edu.galileo.android.cuidador.ParseBD.ParseBd;


public class BaixaCuidador extends Fragment implements OnClickListener {
    @Bind(R.id.nom)
    TextView nom;
    @Bind(R.id.TilNom)
    TextInputLayout TilNom;
    @Bind(R.id.cognoms)
    TextView cognoms;
    @Bind(R.id.TilCognoms)
    TextInputLayout TilCognoms;
    @Bind(R.id.empresa)
    TextView empresa;
    @Bind(R.id.TilEmpresa)
    TextInputLayout TilEmpresa;
    @Bind(R.id.usuari)
    TextView usuari;
    @Bind(R.id.TilUsuari)
    TextInputLayout TilUsuari;
    @Bind(R.id.password)
    TextView password;
    @Bind(R.id.TilPass)
    TextInputLayout TilPass;
    @Bind(R.id.btBaixaC)
    Button btBaixaC;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;
    private Context context = null;
    private String usuariS;
    private View rootView;

    public BaixaCuidador() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.baixa_cuidador, container, false);
        TextView user = (TextView) rootView.findViewById(R.id.usuari);
        TextView password = (TextView) rootView.findViewById(R.id.password);
        TextView empresa = (TextView) rootView.findViewById(R.id.empresa);
        TextView cognoms = (TextView) rootView.findViewById(R.id.cognoms);
        TextView nom = (TextView) rootView.findViewById(R.id.nom);
        context = container.getContext();
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            usuariS = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_baixaC));
        }
        if (usuari != null && !"".equals(usuari)) {
            user.setText(usuariS);
            Cuidadors cuidador = new Cuidadors();
            cuidador.setUsuari(usuariS);
            ParseBd bd = new ParseBd(context);
            List<Cuidadors> cuidadorses = bd.consultaCuidador(usuariS);
            if (cuidadorses != null && cuidadorses.size() == 1) {
                Cuidadors cuid = cuidadorses.get(0);
                nom.setText(cuid.getNom());
                password.setText(cuid.getPassword());
                cognoms.setText(cuid.getCognoms());
                empresa.setText(cuid.getEmpresa());
            }
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btBaixaC, R.id.btCancelarP})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBaixaC: final TextView password = (TextView) rootView.findViewById(R.id.password);
                final TextView empresa = (TextView) rootView.findViewById(R.id.empresa);
                final TextView cognoms = (TextView) rootView.findViewById(R.id.cognoms);
                final TextView nom = (TextView) rootView.findViewById(R.id.nom);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Borrar cuidador " + nom.getText().toString() + " " + cognoms.getText().toString() + "?");
                AlertDialog.Builder builder1 = builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cuidadors cuidadors = new Cuidadors();
                        cuidadors.setUsuari(usuariS);
                        cuidadors.setPassword(password.getText().toString());
                        cuidadors.setNom(nom.getText().toString());
                        cuidadors.setCognoms(cognoms.getText().toString());
                        cuidadors.setEmpresa(empresa.getText().toString());
                        ParseBd bd = new ParseBd(context);
                        boolean esbCuidador = bd.esborrarCuidador(cuidadors);
                        boolean esbPacient = bd.esborrarPacient(usuariS);
                        boolean esbVisita = bd.esborrarVisita(usuariS);
                        if (esbCuidador && esbPacient && esbVisita) {
                            Toast pruebaM = Toast.makeText(context, "Cuidador dado de baja", Toast.LENGTH_SHORT);
                            pruebaM.show();
                            if (getArguments() != null) {
                                getArguments().clear();
                            }
                            startActivity(new Intent(context, LoginActivity.class));
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
                break;
            case R.id.btCancelarP:
                Fragment fragment = new EntradaCuidador();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle(R.string.title_activity_cuidador_activiy);
                break;
        }
    }
}
