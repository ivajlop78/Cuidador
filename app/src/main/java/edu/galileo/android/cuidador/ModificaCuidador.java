package edu.galileo.android.cuidador;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Cuidadors;
import edu.galileo.android.cuidador.ParseBD.ParseBd;

;

public class ModificaCuidador extends Fragment implements OnClickListener {
    @Bind(R.id.nom)
    EditText nom;
    @Bind(R.id.TilNom)
    TextInputLayout TilNom;
    @Bind(R.id.cognoms)
    EditText cognoms;
    @Bind(R.id.TilCognoms)
    TextInputLayout TilCognoms;
    @Bind(R.id.empresa)
    EditText empresa;
    @Bind(R.id.TilEmpresa)
    TextInputLayout TilEmpresa;
    @Bind(R.id.usuari)
    TextView usuari;
    @Bind(R.id.TilUsuari)
    TextInputLayout TilUsuari;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.TilPass)
    TextInputLayout TilPass;
    @Bind(R.id.rePassword)
    EditText rePassword;
    @Bind(R.id.TilRePass)
    TextInputLayout TilRePass;
    @Bind(R.id.btModifP)
    Button btModifP;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;
    private Context context = null;
    private String usuariS;
    private View rootView;
    private String dniOr;
    private String passwordOr;
    private String repasswordOr;
    private String nomOr;
    private String cognomsOr;
    private String empresaOr;

    public ModificaCuidador() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.modifica_cuidador, container, false);
        TextView user = (TextView) rootView.findViewById(R.id.usuari);
        EditText password = (EditText) rootView.findViewById(R.id.password);
        EditText rePass = (EditText) rootView.findViewById(R.id.rePassword);
        EditText empresa = (EditText) rootView.findViewById(R.id.empresa);
        EditText cognoms = (EditText) rootView.findViewById(R.id.cognoms);
        EditText nom = (EditText) rootView.findViewById(R.id.nom);

        context = container.getContext();
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            usuariS = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_modificacioC));
        }
        if (usuariS != null && !"".equals(usuariS)) {
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
                rePass.setText(cuid.getPassword());
            }
        }

        if (getArguments() != null) {
            passwordOr = getArguments().getString("password");
            repasswordOr = getArguments().getString("repassword");
            nomOr = getArguments().getString("nom");
            cognomsOr = getArguments().getString("cognoms");
            empresaOr = getArguments().getString("empresa");
        }

        if (passwordOr != null) {
            password.setText(passwordOr);
        }

        if (repasswordOr != null) {
            rePass.setText(repasswordOr);
        }

        if (nomOr != null) {
            nom.setText(nomOr);
        }

        if (cognomsOr != null) {
            cognoms.setText(cognomsOr);
        }

        if (empresaOr != null) {
            empresa.setText(empresaOr);
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean validarCampsObligatoris() {
        boolean validacio = true;
        if ("".equals(password.getText().toString())) {
            password.setError("Password es obligatorio de rellenar");
            password.requestFocus();
        } else if (password.length() < 8) {
            password.setError("Password tiene que ser, como mínimo de 8 caracteres");
            password.requestFocus();
        } else {
            password.setError(null);
        }

        if ("".equals(rePassword.getText().toString())) {
            rePassword.setError("Repetir password es obligatorio de rellenar");
            rePassword.requestFocus();
            validacio = false;
        } else if (rePassword.length() < 8) {
            rePassword.setError("Repetir password tiene que ser, como mínimo de 8 caracteres");
            rePassword.requestFocus();
            validacio = false;
        } else if (!rePassword.getText().toString().equals(password.getText().toString())) {
            rePassword.setError("Los passwords introducidos son diferentes");
            rePassword.requestFocus();
            validacio = false;
        } else {
            rePassword.setError(null);
        }
        if ("".equals(nom.getText().toString())) {
            nom.setError("Nombre es obligatorio de rellenar");
            if (validacio) {
                nom.requestFocus();
            }
            validacio = false;
        } else {
            nom.setError(null);
        }
        if ("".equals(cognoms.getText().toString())) {
            cognoms.setError("Apellidos es obligatorios de rellenar");
            if (validacio) {
                cognoms.requestFocus();
            }
            validacio = false;
        } else {
            cognoms.setError(null);
        }
        return validacio;
    }

    @Override
    public void onStop() {
        super.onStop();
        final EditText password = (EditText) rootView.findViewById(R.id.password);
        final EditText empresa = (EditText) rootView.findViewById(R.id.empresa);
        final EditText cognoms = (EditText) rootView.findViewById(R.id.cognoms);
        final EditText nom = (EditText) rootView.findViewById(R.id.nom);
        final EditText rePass = (EditText) rootView.findViewById(R.id.rePassword);
        Intent intentRe = getActivity().getIntent();
        intentRe.putExtra("nom", nom.getText().toString());
        intentRe.putExtra("empresa", empresa.getText().toString());
        intentRe.putExtra("cognoms", cognoms.getText().toString());
        intentRe.putExtra("password", password.getText().toString());
        intentRe.putExtra("repassword", rePass.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btModifP, R.id.btCancelarP})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btModifP:
                final EditText password = (EditText) rootView.findViewById(R.id.password);
                final EditText empresa = (EditText) rootView.findViewById(R.id.empresa);
                final EditText cognoms = (EditText) rootView.findViewById(R.id.cognoms);
                final EditText nom = (EditText) rootView.findViewById(R.id.nom);
                final EditText rePass = (EditText) rootView.findViewById(R.id.rePassword);
                if (validarCampsObligatoris()) {
                    Cuidadors cuidadors = new Cuidadors();
                    cuidadors.setPassword(password.getText().toString());
                    cuidadors.setUsuari(usuariS);
                    cuidadors.setNom(nom.getText().toString());
                    cuidadors.setCognoms(cognoms.getText().toString());
                    cuidadors.setEmpresa(empresa.getText().toString());
                    ParseBd bd = new ParseBd(context);
                    boolean modificat = bd.modificarCuidadors(cuidadors);
                    if (modificat) {
                        Toast.makeText(context, "Cuidador modificado.", Toast.LENGTH_SHORT).show();
                    }
                }
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
