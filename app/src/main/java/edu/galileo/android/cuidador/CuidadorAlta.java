package edu.galileo.android.cuidador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Cuidadors;
import edu.galileo.android.cuidador.ParseBD.ParseBd;

public class CuidadorAlta extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.dniC)
    EditText dniC;
    @Bind(R.id.TilDniC)
    TextInputLayout TilDniC;
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
    EditText usuari;
    @Bind(R.id.TilUsuari)
    TextInputLayout TilUsuari;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.TilPass)
    TextInputLayout TilPass;
    @Bind(R.id.Rpassword)
    EditText Rpassword;
    @Bind(R.id.TilRePass)
    TextInputLayout TilRePass;
    @Bind(R.id.btAltaP)
    Button btAltaP;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuidador_alta);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btAltaP, R.id.btCancelarP})
    public void onClick(View view) {
        if (view.getId() == R.id.btAltaP) {

            boolean validacio = validarCampsObligatoris();
            if (validacio) {
                Cuidadors cuidadors = new Cuidadors();
                cuidadors.setDni(dniC.getText().toString());
                cuidadors.setNom(nom.getText().toString());
                cuidadors.setCognoms(cognoms.getText().toString());
                if (empresa.getText().toString() != null && !"".equals(empresa.getText().toString())) {
                    cuidadors.setEmpresa(empresa.getText().toString());
                }
                cuidadors.setUsuari(usuari.getText().toString());
                cuidadors.setPassword(password.getText().toString());
                ParseBd taulaCuidador = new ParseBd(getApplicationContext());
                boolean insertat = taulaCuidador.afegirCuidadors(cuidadors);
                if (insertat) {
                    Toast.makeText(getApplicationContext(), "Cuidador añadido.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Este cuidador ya existe en la aplicación.\n Por favor, modifique los datos.", Toast.LENGTH_LONG).show();

                }
            }
        } else if (view.getId() == R.id.btCancelarP) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

    }

    private boolean validarCampsObligatoris() {
        boolean validacio = true;
        if ("".equals(dniC.getText().toString())) {
            dniC.setError("DNI es obligatorio de rellenar");
            validacio = false;
            dniC.requestFocus();
        } else if (dniC.length() != 9) {
            dniC.setError("DNI tiene una longiutd de 9");
            validacio = false;
            dniC.requestFocus();

        } else if (!validarDni()) {
            validacio = false;
            dniC.requestFocus();
        } else {
            dniC.setError(null);
        }
        if (nom.getText().toString() == null || "".equals(nom.getText().toString())) {
            nom.setError("Nombre es obligatorio de rellenar");
            if (validacio) {
                nom.requestFocus();
            }
            validacio = false;
        } else {
            nom.setError(null);
        }
        if (cognoms.getText().toString() == null || "".equals(cognoms.getText().toString())) {
            cognoms.setError("Apellidos es obligatorio de rellenar");
            if (validacio) {
                cognoms.requestFocus();
            }
            validacio = false;
        } else {
            cognoms.setError(null);
        }
        if (usuari.getText().toString() == null || "".equals(usuari.getText().toString())) {
            usuari.setError("Usuario es obligatorio de rellenar");

            if (validacio) {
                usuari.requestFocus();
            }
            validacio = false;
        } else if (usuari.length() < 7) {
            usuari.setError("Usuario Tiene que se, como mínimo, de 7 caracters");

            if (validacio) {
                usuari.requestFocus();
            }
            validacio = false;
        } else {
            usuari.setError(null);
        }
        if (password.getText().toString() == null || "".equals(password.getText().toString())) {
            password.setError("Password és obligatorio de rellenar");
            if (validacio) {
                password.requestFocus();
            }
            validacio = false;
        } else if (password.length() < 8) {
            password.setError("Password tiene que ser, como mínimo, de 8 caracteres");

            if (validacio) {
                password.requestFocus();
            }
            validacio = false;
        } else {
            password.setError(null);
        }

        if (Rpassword.getText().toString() == null || "".equals(Rpassword.getText().toString())) {
            Rpassword.setError("Repetir password es obligatorio de rellenar");

            if (validacio) {
                Rpassword.requestFocus();
            }
            validacio = false;
        } else if (Rpassword.length() < 8) {
            Rpassword.setError("Repetir password tiene que ser como mínimo de 8 caracteres");

            if (validacio) {
                Rpassword.requestFocus();
            }
            validacio = false;
        } else if (!Rpassword.getText().toString().equals(password.getText().toString())) {
            Rpassword.setError("Los passwords introducidos son diferentes");
            if (validacio) {
                Rpassword.requestFocus();
            }
            validacio = false;
        } else {
            Rpassword.setError(null);
        }
        return validacio;
    }

    private boolean validarDni() {
        //si es NIE, eliminar la x,y,z inicial para tratarlo como nif
        String nif = dniC.getText().toString();
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
                dniC.setError("La letra del DNI no es correcta");
                return false;
            }
        } else {
            dniC.setError("El formato del DNI no es correcto. Los 8 primeros digitos son numeros y el último es una letra");
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
