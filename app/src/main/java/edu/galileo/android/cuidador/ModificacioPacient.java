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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Pacients;
import edu.galileo.android.cuidador.ParseBD.ParseBd;


public class ModificacioPacient extends Fragment implements OnClickListener {
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
    @Bind(R.id.TelCteM)
    EditText TelCteM;
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
    @Bind(R.id.btModif)
    Button btModif;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;
    @Bind(R.id.nivell)
    Spinner nivell;
    @Bind(R.id.TilNivell)
    TextInputLayout TilNivell;
    @Bind(R.id.btDni)
    ImageButton btDni;
    private Context context = null;
    private String cuidador = null;
    private String dniAnt = null;
    private Integer telPacAnt = null;
    private Integer telCteAnt = null;
    View rootView = null;
    private String dniCP;
    private String dniOr;
    private String nomOr;
    private String cognomsOr;
    private String adrecaOr;
    private String personaContacteOr;
    private String telefonContacteOr;
    private String telefonPacientOr;
    private String nivellOr;

    public ModificacioPacient() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent startingIntent = getActivity().getIntent();

        rootView = inflater.inflate(R.layout.modificacio_pacient, container, false);
        context = container.getContext();
        if (startingIntent != null) {
            cuidador = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", "Modificació pacient");
        }
        EditText dniMP = (EditText) rootView.findViewById(R.id.dni);
        if (getArguments() != null) {
            dniCP = getArguments().getString("dniCP");
            dniOr = getArguments().getString("dni");
            nomOr = getArguments().getString("nom");
            cognomsOr = getArguments().getString("cognoms");
            adrecaOr = getArguments().getString("adreca");
            personaContacteOr = getArguments().getString("personaContacte");
            telefonContacteOr = getArguments().getString("telefonContacte");
            telefonPacientOr = getArguments().getString("telefonPacient");
            nivellOr = getArguments().getString("nivell");
        }
        NomP = (EditText) rootView.findViewById(R.id.NomP);
        CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
        Dir = (EditText) rootView.findViewById(R.id.Dir);
        TelCteM = (EditText) rootView.findViewById(R.id.TelCteM);
        TelPac = (EditText) rootView.findViewById(R.id.TelPac);
        PerCte = (EditText) rootView.findViewById(R.id.PerCte);
        nivell = (Spinner) rootView.findViewById(R.id.nivell);
        if (dniCP != null && !"".equals(dniCP)) {
            dniMP.setText(dniCP);
            actualizarDadesPacient(dniCP, cuidador);
        }

        if (dniOr != null) {
            dniMP.setText(dniOr);
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
            TelCteM.setText(telefonContacteOr);
        }

        if (telefonPacientOr != null) {
            TelPac.setText(telefonPacientOr);
        }

        if (nivellOr != null) {
            if ("Intermedi".equals(nivellOr)) {
                nivell.setSelection(1);
            } else {
                nivell.setSelection(0);
            }
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void actualizarDadesPacient(String dni, String cuidador) {
        ParseBd bd = new ParseBd(context);
        List<Pacients> pacientses = bd.consultaPacientModif(dni, cuidador);
        dniAnt = dni;
        if (pacientses != null && pacientses.size() == 1) {
            NomP = (EditText) rootView.findViewById(R.id.NomP);
            CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
            Dir = (EditText) rootView.findViewById(R.id.Dir);
            Dir = (EditText) rootView.findViewById(R.id.TelCteM);
            TelPac = (EditText) rootView.findViewById(R.id.TelPac);
            PerCte = (EditText) rootView.findViewById(R.id.PerCte);
            Spinner nivell = (Spinner) rootView.findViewById(R.id.nivell);
            Pacients pacients = pacientses.get(0);
            NomP.setText(pacients.getNom());
            CognomsP.setText(pacients.getCognoms());
            Dir.setText(pacients.getadreca());
            TelCteM.setText(String.valueOf(pacients.getTelCte()));
            TelPac.setText(String.valueOf(pacients.getTelPac()));
            telCteAnt = Integer.parseInt(TelCteM.getText().toString());
            telPacAnt = Integer.parseInt(TelPac.getText().toString());
            PerCte.setText(pacients.getPersonaCte());
            if ("Intermedi".equals(pacients.getNivell())) {
                nivell.setSelection(1);
            }
        } else {
            Toast.makeText(context, "Debes informar un DNI o el que has informado, no existe", Toast.LENGTH_LONG).show();

        }


    }

    private boolean validarCampsObligatoris(EditText dni) {
        boolean validacio = true;
        if ("".equals(dni.getText().toString())) {
            dni.setError("DNI es obligatorio de rellenar");
            validacio = false;
            dni.requestFocus();
        } else if (dni.length() != 9) {
            dni.setError("DNI debe tener longitud de 9");
            validacio = false;
            dni.requestFocus();

        } else if (!validarDni(dni)) {
            validacio = false;
            dni.requestFocus();
        } else if (!dni.getText().toString().equals(dniAnt)) {
            validacio = false;
            dni.setError("No se puede modificar el DNI.");
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
            PerCte.setError("Persona contacto es obligatorio de rellenar");
            if (validacio) {
                PerCte.requestFocus();
            }
            validacio = false;
        } else {
            PerCte.setError(null);
        }

        if ("".equals(TelCteM.getText().toString())) {
            TelCteM.setError("Teléfono de contacto es obligatorio de rellenar");
            if (validacio) {
                TelCteM.requestFocus();
            }
            validacio = false;
        } else if (TelCteM.length() != 9) {
            TelCteM.setError("Teléfono de contacto debe tener longitud de 9");

            if (validacio) {
                TelCteM.requestFocus();
            }
            validacio = false;
        } else {
            TelCteM.setError(null);
        }

        if ("".equals(TelPac.getText().toString())) {
            TelPac.setError("Teléfono del paciente es obligatorio de rellenar");
            if (validacio) {
                TelPac.requestFocus();
            }
            validacio = false;
        } else if (TelPac.length() != 9) {
            TelPac.setError("Teléfono del paciente debe tener una longitud de 9");
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

    private boolean validarDni(EditText dni) {
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
            dni.setError("El formato del DNI no es correcto. Los ocho primeros son digitos y el último es letra");
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EditText dni = (EditText) rootView.findViewById(R.id.dni);
        NomP = (EditText) rootView.findViewById(R.id.NomP);
        CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
        Dir = (EditText) rootView.findViewById(R.id.Dir);
        TelCteM = (EditText) rootView.findViewById(R.id.TelCteM);
        TelPac = (EditText) rootView.findViewById(R.id.TelPac);
        PerCte = (EditText) rootView.findViewById(R.id.PerCte);
        nivell = (Spinner) rootView.findViewById(R.id.nivell);
        Intent intentRe = getActivity().getIntent();
        intentRe.putExtra("dniCP", dniCP);
        intentRe.putExtra("dni", dni.getText().toString());
        intentRe.putExtra("nom", NomP.getText().toString());
        intentRe.putExtra("cognoms", CognomsP.getText().toString());
        intentRe.putExtra("adreca", Dir.getText().toString());
        intentRe.putExtra("personaContacte", PerCte.getText().toString());
        intentRe.putExtra("telefonContacte", TelCteM.getText().toString());
        intentRe.putExtra("telefonPacient", TelPac.getText().toString());
        intentRe.putExtra("nivell", nivell.getSelectedItem().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btModif, R.id.btCancelarP, R.id.btDni})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btModif:
                if (rootView != null) {
                    EditText dniMP = (EditText) rootView.findViewById(R.id.dni);
                    NomP = (EditText) rootView.findViewById(R.id.NomP);
                    CognomsP = (EditText) rootView.findViewById(R.id.CognomsP);
                    Dir = (EditText) rootView.findViewById(R.id.Dir);
                    TelCteM = (EditText) rootView.findViewById(R.id.TelCteM);
                    TelPac = (EditText) rootView.findViewById(R.id.TelPac);
                    PerCte = (EditText) rootView.findViewById(R.id.PerCte);
                    nivell = (Spinner) rootView.findViewById(R.id.nivell);
                    boolean validacio = validarCampsObligatoris(dniMP);
                    if (validacio) {
                        Pacients pacients = new Pacients();
                        pacients.setDni(dniMP.getText().toString());
                        pacients.setNom(NomP.getText().toString());
                        pacients.setCognoms(CognomsP.getText().toString());
                        pacients.setadreca(Dir.getText().toString());
                        pacients.setPersonaCte(PerCte.getText().toString());
                        pacients.setTelCte(Integer.parseInt(TelCteM.getText().toString()));
                        pacients.setTelPac(Integer.parseInt(TelPac.getText().toString()));
                        pacients.setNivell(nivell.getSelectedItem().toString());
                        pacients.setCuidador(cuidador);
                        ParseBd taulaPacient = new ParseBd(context);
                        boolean modificat = taulaPacient.modificarPacients(telCteAnt, telPacAnt, pacients);
                        if (modificat) {
                            Toast.makeText(context, "Paciente Modificado.", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new ConsultaPacient();
                            //Validamos si el fragment no es nulo
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            getActivity().setTitle("Consulta paciente");
                        } else {
                            Toast.makeText(context, "Este paciente no existe en la aplicación.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.btCancelarP:
                break;
            case R.id.btDni:
                EditText dni = (EditText) rootView.findViewById(R.id.dni);
                if ("".equals(dni.getText().toString())) {
                    dni.setError("DNI es obligatorio de rellenar");

                    dni.requestFocus();
                } else if (dni.length() != 9) {
                    dni.setError("DNI debe tener longitud de 9");
                    dni.requestFocus();

                } else if (!validarDni(dni)) {
                    dni.requestFocus();
                } else {
                    dni.setError(null);
                    actualizarDadesPacient(dni.getText().toString(), cuidador);
                }
                break;
        }
    }
}
