package edu.galileo.android.cuidador;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.parse.Parse;

import edu.galileo.android.cuidador.ParseBD.ParseBd;

/**
 * Created by Javier on 04/12/2015.
 */
public class ParseCuidadorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseBd parseBd =new ParseBd(getApplicationContext());
        parseBd.obrirBDLocal();
        Parse.initialize(this, BuildConfig.API_KEY, BuildConfig.CLIENT_KEY);
        //Llegir el número de movil que utilitza la meva aplicación
        TelephonyManager telApp=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //si no està posat el telefon a la SIM, llavors surt la pantalla de activity
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }
}
