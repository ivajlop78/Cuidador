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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Pacients;
import edu.galileo.android.cuidador.ParseBD.ParseBd;

public class AltaPacient extends Fragment implements OnClickListener {
    @Bind(R.id.dni)
    EditText dni;
    @Bind(R.id.Tildni)
    TextInputLayout Tildni;
    @Bind(R.id.CognomsP)
    EditText CognomsP;
    @Bind(R.id.TilCognomsP)
    TextInputLayout TilCognomsP;
    @Bind(R.id.Dir)
    EditText Dir;
    @Bind(R.id.TilDir)
    TextInputLayout TilDir;
    @Bind(R.id.TelCte)
    EditText TelCte;
    @Bind(R.id.TilTelCte)
    TextInputLayout TilTelCte;
    @Bind(R.id.TelPac)
    EditText TelPac;
    @Bind(R.id.TilTelPac)
    TextInputLayout TilTelPac;
    @Bind(R.id.NomP)
    EditText NomP;
    @Bind(R.id.TilNomP)
    TextInputLayout TilNomP;
    @Bind(R.id.PerCte)
    EditText PerCte;
    @Bind(R.id.TilPerCte)
    TextInputLayout TilPerCte;
    @Bind(R.id.btAltaP)
    Button btAltaP;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;
    @Bind(R.id.nivell)
    Spinner nivell;
    @Bind(R.id.TilNivell)
    TextInputLayout TilNivell;
    private Context context = null;
    View rootView = null;
    private String usuari = null;
    private String dniOr;
    private String nomOr;
    private String cognomsOr;
    private String adrecaOr;
    private String personaContacteOr;
    private String telefonContacteOr;
    private String telefonPacientOr;
    private String nivellOr;

    public AltaPacient() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.alta_pacient, container, false);
        context = container.getContext();
        Button btAlta = (Button) rootView.findViewById(R.id.btAltaP);
        btAlta.setOnClickListener(this);
        Button btCancelar = (Button) rootView.findViewById(R.id.btCancelarP);
        btCancelar.setOnClickListener(this);
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            usuari = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_AltaP));
        }

        if (getArguments() != null) {
            dniOr = getArguments().getString("dni");
            nomOr = getArguments().getString("nom");
            cognomsOr = getArguments().getString("cognoms");
            adrecaOr = getArguments().getString("adreca");
            personaContacteOr = getArguments().getString("personaContacte");
            telefonContacteOr = getArguments().getString("telefonContacte");
            telefonPacientOr = getArguments().getString("telefonPacient");
            nivellOr = getArguments().getString("nivell");

        }
        dni = (EditText) rootView.findViewById(R.id.dni);
        NomP = (EditText) rootView.findViewById(R.id.NomP);
        CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
        Dir = (EditText) rootView.findViewById(R.id.Dir);
        PerCte = (EditText) rootView.findViewById(R.id.PerCte);
        TelCte = (EditText) rootView.findViewById(R.id.TelCte);
        TelPac = (EditText) rootView.findViewById(R.id.TelPac);
        nivell = (Spinner) rootView.findViewById(R.id.nivell);

        if (dniOr != null) {
            dni.setText(dniOr);
        }
        if (nomOr != null) {
            NomP.setText(nomOr);
        }

        if (cognomsOr != null) {
            CognomsP.setText(cognomsOr);
        }

        if (adrecaOr != null) {
            Dir.setText(adrecaOr);
        }

        if (personaContacteOr != null) {
            PerCte.setText(personaContacteOr);
        }

        if (telefonContacteOr != null) {
            TelCte.setText(telefonContacteOr);
        }

        if (telefonPacientOr != null) {
            TelPac.setText(telefonPacientOr);
        }

        if (nivellOr != null && "Intermedi".equals(nivellOr)) {
            nivell.setSelection(1);
        } else {
            nivell.setSelection(0);
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean validarCampsObligatoris() {
        boolean validacio = true;
        if ("".equals(dni.getText().toString())) {
            dni.setError("DNI es obligatorio de rellenar");
            validacio = false;
            dni.requestFocus();
        } else if (dni.length() != 9) {
            dni.setError("DNI ha de tener una longitud de 9");
            validacio = false;
            dni.requestFocus();

        } else if (!validarDni()) {
            validacio = false;
            dni.requestFocus();
        } else {
            dni.setError(null);
        }
        if ("".equals(NomP.getText().toString())) {
            NomP.setError("Nombre es obligatorio de rellenar");
            if (validacio) {
                NomP.requestFocus();
            }
            validacio = false;
        } else {
            NomP.setError(null);
        }
        if ("".equals(CognomsP.getText().toString())) {
            CognomsP.setError("Apellidos es obligatorio de rellenar");
            if (validacio) {
                CognomsP.requestFocus();
            }
            validacio = false;
        } else {
            CognomsP.setError(null);
        }
        if ("".equals(Dir.getText().toString())) {
            Dir.setError("Dirección es obligatorio de rellenar");

            if (validacio) {
                Dir.requestFocus();
            }
            validacio = false;
        } else {
            Dir.setError(null);
        }
        if ("".equals(PerCte.getText().toString())) {
            PerCte.setError("Persona de contacto es obligatorio de rellenar");
            if (validacio) {
                PerCte.requestFocus();
            }
            validacio = false;
        } else {
            PerCte.setError(null);
        }

        if ("".equals(TelCte.getText().toString())) {
            TelCte.setError("Teléfono de contacto es obligatorio de rellenar");
            if (validacio) {
                TelCte.requestFocus();
            }
            validacio = false;
        } else if (TelCte.length() != 9) {
            TelCte.setError("Teléfono de contacto debe tener longitud de 9 digitos");
            if (validacio) {
                TelCte.requestFocus();
            }
            validacio = false;
        } else {
            TelCte.setError(null);
        }

        if ("".equals(TelPac.getText().toString())) {
            TelPac.setError("Teléfono del paciente es obligatorio de rellenar");
            if (validacio) {
                TelPac.requestFocus();
            }
            validacio = false;
        } else if (TelPac.length() != 9) {
            TelPac.setError("Teléfono del paciente debe tener longitud de 9 digitos");
            if (validacio) {
                TelPac.requestFocus();
            }
            validacio = false;
        } else {
            TelPac.setError(null);
        }

        if (nivell.getSelectedItem().toString() == null || "".equals(nivell.getSelectedItem().toString())) {
            Toast.makeText(context, "Nivel es obligatorio de rellenar", Toast.LENGTH_LONG).show();
            if (validacio) {
                nivell.requestFocus();
            }
            validacio = false;
        }
        return validacio;
    }

    private boolean validarDni() {
        //si es NIE, eliminar la x,y,z inicial para tratarlo como nif
        String nif = dni.getText().toString();
        if (nif.toUpperCase().startsWith("X") || nif.toUpperCase().startsWith("Y") || nif.toUpperCase().startsWith("Z"))
            nif = nif.substring(1);

        Pattern nifPattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher m = nifPattern.matcher(nif);
        if (m.matches()) {
            String letra = m.group(2);
            //Extraer letra del NIF
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            int dni2 = Integer.parseInt(m.group(1));
            dni2 = dni2 % 23;
            String reference = letras.substring(dni2, dni2 + 1);

            if (reference.equalsIgnoreCase(letra)) {
                return true;
            } else {
                dni.setError("La letra del DNI no es correcta");
                return false;
            }
        } else {
            dni.setError("El formato del DNI no és correcto. Los ocho primeros digitos son numeros y el último es una letra");
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dni = (EditText) rootView.findViewById(R.id.dni);
        NomP = (EditText) rootView.findViewById(R.id.NomP);
        CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
        Dir = (EditText) rootView.findViewById(R.id.Dir);
        PerCte = (EditText) rootView.findViewById(R.id.PerCte);
        TelCte = (EditText) rootView.findViewById(R.id.TelCte);
        TelPac = (EditText) rootView.findViewById(R.id.TelPac);
        nivell = (Spinner) rootView.findViewById(R.id.nivell);
        Intent intentRe = getActivity().getIntent();
        intentRe.putExtra("dni", dni.getText().toString());
        intentRe.putExtra("nom", NomP.getText().toString());
        intentRe.putExtra("cognoms", CognomsP.getText().toString());
        intentRe.putExtra("adreca", Dir.getText().toString());
        intentRe.putExtra("personaContacte", PerCte.getText().toString());
        intentRe.putExtra("telefonContacte", TelCte.getText().toString());
        intentRe.putExtra("telefonPacient", TelPac.getText().toString());
        intentRe.putExtra("nivell", nivell.getSelectedItem().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btAltaP, R.id.btCancelarP})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAltaP:
                if (rootView != null) {
                    dni = (EditText) rootView.findViewById(R.id.dni);
                    NomP = (EditText) rootView.findViewById(R.id.NomP);
                    CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
                    Dir = (EditText) rootView.findViewById(R.id.Dir);
                    PerCte = (EditText) rootView.findViewById(R.id.PerCte);
                    TelCte = (EditText) rootView.findViewById(R.id.TelCte);
                    TelPac = (EditText) rootView.findViewById(R.id.TelPac);
                    nivell = (Spinner) rootView.findViewById(R.id.nivell);
                    boolean validacio = validarCampsObligatoris();
                    if (validacio) {
                        Pacients pacients = new Pacients();
                        pacients.setDni(dni.getText().toString());
                        pacients.setNom(NomP.getText().toString());
                        pacients.setCognoms(CognomsP.getText().toString());
                        pacients.setadreca(Dir.getText().toString());
                        pacients.setPersonaCte(PerCte.getText().toString());
                        pacients.setTelCte(Integer.parseInt(TelCte.getText().toString()));
                        pacients.setTelPac(Integer.parseInt(TelPac.getText().toString()));
                        pacients.setNivell(nivell.getSelectedItem().toString());
                        pacients.setCuidador(usuari);
                        ParseBd taulaPacient = new ParseBd(context);
                        boolean insertat = taulaPacient.afegirPacients(pacients);
                        if (insertat) {
                            Toast.makeText(context, "Paciente añadido.", Toast.LENGTH_SHORT).show();
                            dni.setText(" ");
                            NomP.setText(" ");
                            CognomsP.setText(" ");
                            Dir.setText(" ");
                            PerCte.setText(" ");
                            TelCte.setText(" ");
                            TelPac.setText(" ");
                            nivell.setSelection(0);
                            dni.requestFocus();
                        } else {
                            Toast.makeText(context, "Este paciente ya existe en la aplicación.\n Por favor, modifique los datos", Toast.LENGTH_LONG).show();
                        }
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
