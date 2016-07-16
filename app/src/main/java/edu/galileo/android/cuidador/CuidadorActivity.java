package edu.galileo.android.cuidador;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuidadorActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content_frame)
    FrameLayout contentFrame;
    @Bind(R.id.lista)
    NavigationView lista;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private String[] titulos;
    private DrawerLayout mDrawerLayout;
    Intent startingIntent = null;
    private String dniCP;
    private String dataCP;
    private String dataOr;
    private String dni;
    private String nom;
    private String cognoms;
    private String password;
    private String repassword;
    private String empresa;
    private String adreca;
    private String personaContacte;
    private String telefonContacte;
    private String telefonPacient;
    private String nivell;
    private String searchView;
    private String horaOr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cuidador_activity);
        ButterKnife.bind(this);
        startingIntent = getIntent();
        String usuari;
        String titulo = "Entrada cuidador";
        setToolbar();
        if (lista != null) {
            setupDrawerContent(lista);
            View headerLayout = lista.getHeaderView(0);
            TextView username = (TextView) headerLayout.findViewById(R.id.username);
            if (startingIntent != null) {
                usuari = startingIntent.getStringExtra("usuari");
                username.setText("Hola " + usuari);


            }
        }
        if (startingIntent != null) {
            String title = startingIntent.getStringExtra("titulo");
            if (title != null && title.equals("Modificació visita")) {
                dni = startingIntent.getStringExtra("dni");
                dniCP = startingIntent.getStringExtra("dniCP");
                dataCP = startingIntent.getStringExtra("dataCP");
                dataOr = startingIntent.getStringExtra("data");
                horaOr = startingIntent.getStringExtra("hora");
            } else if (title != null && title.equals(getString(R.string.mn_AltaV))) {
                dni = startingIntent.getStringExtra("dni");
                dniCP = startingIntent.getStringExtra("dniCP");
                dataOr = startingIntent.getStringExtra("data");
                horaOr = startingIntent.getStringExtra("hora");
            } else if (title != null && title.equals(getString(R.string.mn_modificacioC))) {
                nom = startingIntent.getStringExtra("nom");
                cognoms = startingIntent.getStringExtra("cognoms");
                password = startingIntent.getStringExtra("password");
                repassword = startingIntent.getStringExtra("repassword");
                empresa = startingIntent.getStringExtra("empresa");
            } else if (title != null && title.equals(getString(R.string.mn_AltaP))) {
                dni = startingIntent.getStringExtra("dni");
                nom = startingIntent.getStringExtra("nom");
                cognoms = startingIntent.getStringExtra("cognoms");
                adreca = startingIntent.getStringExtra("adreca");
                personaContacte = startingIntent.getStringExtra("personaContacte");
                telefonContacte = startingIntent.getStringExtra("telefonContacte");
                telefonPacient = startingIntent.getStringExtra("telefonPacient");
                nivell = startingIntent.getStringExtra("nivell");
            } else if (title != null && title.equals("Modificació pacient")) {
                dniCP = startingIntent.getStringExtra("dniCP");
                dni = startingIntent.getStringExtra("dni");
                nom = startingIntent.getStringExtra("nom");
                cognoms = startingIntent.getStringExtra("cognoms");
                adreca = startingIntent.getStringExtra("adreca");
                personaContacte = startingIntent.getStringExtra("personaContacte");
                telefonContacte = startingIntent.getStringExtra("telefonContacte");
                telefonPacient = startingIntent.getStringExtra("telefonPacient");
                nivell = startingIntent.getStringExtra("nivell");
            } else if (title != null && title.equals(getString(R.string.mn_consultaV))) {
                searchView = startingIntent.getStringExtra("searchView");
            } else if (title != null && title.equals(getString(R.string.mn_ConsultaP))) {
                searchView = startingIntent.getStringExtra("searchView");
            }
            if (title != null) {
                MostrarFragment(title);
            } else {
                MostrarFragment("Entrada cuidador");
            }

        }
    }


    /*Pasando la posicion de la opcion en el menu nos mostrara el Fragment correspondiente*/
    private void MostrarFragment(String titulo) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        if (titulo.equals(getString(R.string.mn_baixaC))) {
            fragment = new BaixaCuidador();
        }else if(titulo.equals(getString(R.string.mn_modificacioC))){
            fragment = new ModificaCuidador();
            Bundle parametro =new Bundle();
            parametro.putString("nom",nom);
            parametro.putString("cognoms",cognoms);
            parametro.putString("empresa",empresa);
            parametro.putString("password",password);
            parametro.putString("repassword",repassword);
            fragment.setArguments(parametro);
        }else if(titulo.equals(getString(R.string.mn_AltaP))) {
            fragment = new AltaPacient();
            Bundle parametro =new Bundle();
            parametro.putString("dni",dni);
            parametro.putString("nom",nom);
            parametro.putString("cognoms",cognoms);
            parametro.putString("adreca",adreca);
            parametro.putString("personaContacte",personaContacte);
            parametro.putString("telefonContacte",telefonContacte);
            parametro.putString("telefonPacient",telefonPacient);
            parametro.putString("nivell",nivell);
            fragment.setArguments(parametro);
        }else if(titulo.equals(getString(R.string.mn_ConsultaP))){
            fragment = new ConsultaPacient();
            Bundle parametro =new Bundle();
            parametro.putString("searchView",searchView);
            fragment.setArguments(parametro);
        }else if(titulo.equals("Modificació pacient")) {
            fragment = new ModificacioPacient();
            Bundle parametro =new Bundle();
            parametro.putString("dniCP",dniCP);
            parametro.putString("dni",dni);
            parametro.putString("nom",nom);
            parametro.putString("cognoms",cognoms);
            parametro.putString("adreca",adreca);
            parametro.putString("personaContacte",personaContacte);
            parametro.putString("telefonContacte",telefonContacte);
            parametro.putString("telefonPacient",telefonPacient);
            parametro.putString("nivell",nivell);
            fragment.setArguments(parametro);
        }else if(titulo.equals(getString(R.string.mn_AltaV))) {
            fragment = new AltaVisita();
            Bundle parametro =new Bundle();
            parametro.putString("dni",dni);
            parametro.putString("dniCP",dniCP);
            parametro.putString("dataCP",dataCP);
            parametro.putString("data",dataOr);
            parametro.putString("hora",horaOr);
            fragment.setArguments(parametro);
        }else if(titulo.equals(getString(R.string.mn_consultaV))){
            fragment = new ConsultaVisita();
            Bundle parametro =new Bundle();
            parametro.putString("searchView",searchView);
            fragment.setArguments(parametro);
        }else if(titulo.equals("Modificación visita")) {
            fragment = new ModificacioVisita();
            Bundle parametro =new Bundle();
            parametro.putString("dni",dni);
            parametro.putString("dniCP",dniCP);
            parametro.putString("dataCP",dataCP);
            parametro.putString("data",dataOr);
            parametro.putString("hora",horaOr);
            fragment.setArguments(parametro);
        }else if(titulo.equals("Entrada cuidador")){
            fragment = new EntradaCuidador();
        }else if(titulo.equals(getString(R.string.log_out_item))){
            posarDialogSessio();
        }else if(titulo.equals(getString(R.string.mn_Calendari))){
            fragment = new CalendariPersonalizado();
        }
        //Validamos si el fragment no es nulo
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Actualizamos el contenido segun la opcion elegida
            setTitle(titulo);
            drawerLayout.closeDrawer(GravityCompat.START);


        } else {
            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment" + titulo);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.mipmap.iconetitol);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.fondoAzul));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.menu_cuidadors, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.pacients_altaP:
                MostrarFragment(getString(R.string.mn_AltaP));
                return true;
            case R.id.pacients_consultaP:
                MostrarFragment(getString(R.string.mn_ConsultaP));
                return true;
            case R.id.visites_altaV:
                MostrarFragment(getString(R.string.mn_AltaV));
                return true;
            case R.id.visites_calendari:
                MostrarFragment(getString(R.string.mn_Calendari));
                return true;
            case R.id.visites_consultaV:
                MostrarFragment(getString(R.string.mn_consultaV));
                return true;
            case R.id.cuidadors_baixaC:
                MostrarFragment(getString(R.string.mn_baixaC));
                return true;
            case R.id.cuidadors_modificacioC:
                MostrarFragment(getString(R.string.mn_modificacioC));
                return true;
            case R.id.nav_log_out:
                posarDialogSessio();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        MostrarFragment(title);
                        return true;
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        posarDialogSessio();

    }

    public void posarDialogSessio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quieres salir de la sesión del cuidador?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (startingIntent != null) {
                    Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
                    if (b != null) {
                        b.clear();
                    }
                    startingIntent.getExtras().clear();
                }
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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

    @OnClick({R.id.toolbar, R.id.content_frame, R.id.lista, R.id.drawer_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.content_frame:
                break;
            case R.id.lista:
                break;
            case R.id.drawer_layout:
                break;
        }
    }
}
