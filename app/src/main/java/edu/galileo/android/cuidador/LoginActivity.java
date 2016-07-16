package edu.galileo.android.cuidador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Cuidadors;
import edu.galileo.android.cuidador.ParseBD.ParseBd;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.usuari)
    EditText usuari;
    @Bind(R.id.TilUsuari)
    TextInputLayout TilUsuari;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.TilPass)
    TextInputLayout TilPass;
    @Bind(R.id.btAltaP)
    Button btAltaP;
    @Bind(R.id.btAltaC)
    Button btAltaC;
    @Bind(R.id.TilLblAlta)
    TextInputLayout TilLblAlta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btAltaP, R.id.btAltaC})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAltaP:
                boolean validacio = validarCampsObligatoris();
                if (validacio) {
                    Cuidadors cuidadors = new Cuidadors();
                    cuidadors.setUsuari(usuari.getText().toString());
                    cuidadors.setPassword(password.getText().toString());
                    ParseBd bd = new ParseBd(getApplicationContext());
                    //saber si és a la base de dades local.
                    if (bd.esCuidador(cuidadors)) {
                        Intent intent = new Intent(this, CuidadorActivity.class);
                        intent.putExtra("usuari", cuidadors.getUsuari());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario y password no encontrados en la aplicación", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btAltaC:
                startActivity(new Intent(this,CuidadorAlta.class));
                break;
        }

    }


    private boolean validarCampsObligatoris() {
        boolean validacio = true;

        if ("".equals(usuari.getText().toString())) {
            usuari.setError("Usuario es obligatorio de rellenar");
            usuari.requestFocus();
            validacio = false;
        } else if (usuari.length() < 7) {
            usuari.setError("Usuari tiene que se como mínimo de 7 caracteres");
            usuari.requestFocus();
        } else {
            usuari.setError(null);
        }
        if ("".equals(password.getText().toString())) {
            password.setError("Password es obligatorio de rellenar");
            if (validacio) {
                password.requestFocus();
            }
            validacio = false;
        } else if (password.length() < 8) {
            password.setError("Password tiene que ser como mínimo de 8 caracteres");

            if (validacio) {
                password.requestFocus();
            }
            validacio = false;
        } else {
            password.setError(null);
        }
        return validacio;
    }
}
