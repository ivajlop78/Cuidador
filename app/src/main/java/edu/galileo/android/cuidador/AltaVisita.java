package edu.galileo.android.cuidador;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.cuidador.ParseBD.Pacients;
import edu.galileo.android.cuidador.ParseBD.ParseBd;
import edu.galileo.android.cuidador.ParseBD.Visites;

public class AltaVisita extends Fragment implements OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {
    @Bind(R.id.DniV)
    EditText DniV;
    @Bind(R.id.TilDniV)
    TextInputLayout TilDniV;
    @Bind(R.id.CognomsV)
    TextView CognomsV;
    @Bind(R.id.TilCognomsV)
    TextInputLayout TilCognomsV;
    @Bind(R.id.Data)
    EditText Data;
    @Bind(R.id.TilData)
    TextInputLayout TilData;
    @Bind(R.id.Hora)
    EditText Hora;
    @Bind(R.id.TilHora)
    TextInputLayout TilHora;
    @Bind(R.id.NomV)
    TextView NomV;
    @Bind(R.id.TilNomV)
    TextInputLayout TilNomV;
    @Bind(R.id.btAltaP)
    Button btAltaP;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;
    @Bind(R.id.btDni)
    ImageButton btDni;
    private Context context = null;
    private String cuidador = null;
    private String dniAnt = null;
    private String dniCP = null;
    View rootView = null;
    SimpleDateFormat dateFormatter;
    SimpleDateFormat horaFormatter;
    SimpleDateFormat todoFormatter;
    private String dataOr;
    private String horaOr;
    private String dniOr;
    //private EditText hora;
    TimePickerDialog mTimePicker;
    private DatePickerDialog dpd;
    //private EditText dataMP;
    private String fechaCalendar;
    private String tituloAnt;

    public AltaVisita() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        todoFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        horaFormatter = new SimpleDateFormat("hh:mm", Locale.getDefault());
        Intent startingIntent = getActivity().getIntent();
        rootView = inflater.inflate(R.layout.alta_visita, container, false);
        context = container.getContext();
        if (startingIntent != null) {
            Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
            cuidador = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_AltaV));
            if (getArguments() != null) {
                startingIntent.putExtra("dniCP", dniCP);
                startingIntent.putExtra("dni", getArguments().getString("dni"));
                startingIntent.putExtra("data", getArguments().getString("data"));
                startingIntent.putExtra("hora", getArguments().getString("hora"));
                fechaCalendar = getArguments().getString("fechaCalendar");
            }
        }

        EditText dniMP = (EditText) rootView.findViewById(R.id.DniV);
        Data = (EditText) rootView.findViewById(R.id.Data);
        Data.setOnTouchListener(this);
        Data.setOnFocusChangeListener(this);
        Hora = (EditText) rootView.findViewById(R.id.Hora);
        Hora.setOnTouchListener(this);

        Hora.setOnFocusChangeListener(this);
        if (getArguments() != null) {
            dniCP = getArguments().getString("dniCP");
            dataOr = getArguments().getString("data");
            dniOr = getArguments().getString("dni");
            horaOr = getArguments().getString("hora");
            tituloAnt = getArguments().getString("tituloAnt");
        }


        if (dniCP != null && !"".equals(dniCP)) {
            dniMP.setText(dniCP);
            actualizarDadesPacient(dniCP, cuidador, rootView);
        }

        if (dniOr != null) {
            dniMP.setText(dniOr);
        }
        if (Data != null && dataOr != null) {
            Data.setText(dataOr);
        }
        if (Hora != null && horaOr != null) {
            Hora.setText(horaOr);
        }
        if (fechaCalendar != null && !"".equals(fechaCalendar)) {
            Data.setText(fechaCalendar);
        }

        Data = (EditText) rootView.findViewById(R.id.Data);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void actualizarDadesPacient(String dni, String cuidador, View v) {
        ParseBd bd = new ParseBd(context);
        List<Pacients> pacientses = bd.consultaPacientModif(dni, cuidador);
        dniAnt = dni;
        if (pacientses != null && pacientses.size() == 1) {
            TextView nom = (TextView) rootView.findViewById(R.id.NomV);
            TextView cognoms = (TextView) rootView.findViewById(R.id.CognomsV);
            Pacients pacients = pacientses.get(0);
            nom.setText(pacients.getNom());
            cognoms.setText(pacients.getCognoms());
        } else {
            Toast.makeText(context, "Tienes que informar un DNI o el que has informado,no existe.", Toast.LENGTH_LONG).show();

        }


    }
    private boolean validarDni() {
        //si es NIE, eliminar la x,y,z inicial para tratarlo como nif
        String nif = DniV.getText().toString();
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
                DniV.setError("La letra del DNI no es correcta");
                return false;
            }
        } else {
            DniV.setError("El formato del DNI no es correcto. Los ocho primeros digitos son numeros y el último es una letra");
            return false;
        }
    }

    private boolean validarData() {
        boolean validarData = true;
        try {
            Date fecha = dateFormatter.parse(Data.getText().toString());
        } catch (ParseException e) {
            validarData = false;
        }
        return validarData;
    }


    private Notification getDefaultNotification(Notification.Builder builder, String data) {
        builder
                .setSmallIcon(R.mipmap.cuidador_50)
                .setTicker("Optional ticker")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Alta visita")
                .setContentText("Alta visita con fecha " + data)
                .setContentInfo("Info");
        //return builder.build(); //A partir de Jelly Bean se usa éste método-
        return builder.getNotification();
    }

    @Override
    public void onStop() {
        super.onStop();
        DniV= (EditText) rootView.findViewById(R.id.DniV);
        Data = (EditText) rootView.findViewById(R.id.Data);
        Hora = (EditText) rootView.findViewById(R.id.Hora);
        Intent intentRe = getActivity().getIntent();
        intentRe.putExtra("dniCP", dniAnt);
        intentRe.putExtra("dni", DniV.getText().toString());
        intentRe.putExtra("data", Data.getText().toString());
    }

    private void onCreateHoraPicker() {
        final Calendar c = Calendar.getInstance();
        if (!"".equals(Hora.getText().toString())) {
            try {
                Date fecha = horaFormatter.parse(Hora.getText().toString());
                c.setTime(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (mTimePicker == null || !mTimePicker.isShowing()) {

            // Create a new instance of TimePickerDialog and return it
            mTimePicker = new TimePickerDialog(getActivity(), R.style.timePickerDialog_Theme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String minutos = String.valueOf(minute);
                    if (minute >= 0 && minute < 10) {
                        minutos = "0" + minutos;
                    }
                    Hora.setText(hourOfDay + ":" + minutos);
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Tria l'hora");
            mTimePicker.show();

        }

    }


    private void onCreateDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        if (!"".equals(Data.getText().toString())) {
            try {
                Date fecha = dateFormatter.parse(Data.getText().toString());
                calendar.setTime(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                Initialize a new DatePickerDialog

                DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                    int year, int monthOfYear, int dayOfMonth)
             */
        if (dpd == null || !dpd.isShowing()) {
            dpd = new DatePickerDialog(getActivity(), R.style.datePickerDialog_Theme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String day = String.valueOf(dayOfMonth);
                    if (dayOfMonth >= 1 && dayOfMonth <= 10) {
                        day = "0" + day;
                    }

                    String month = String.valueOf(monthOfYear + 1);
                    if (monthOfYear >= 0 && monthOfYear <= 8) {
                        month = "0" + month;
                    }
                    Data.setText(day + "/" + month + "/" + year);
                }
            }, year, month, day);
            dpd.setTitle("Escoge el dia");
            dpd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dpd.show();

        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.Hora && hasFocus) {
            onCreateHoraPicker();
        } else if (v.getId() == R.id.Data && hasFocus) {
            onCreateDatePicker();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.Data) {
            if (dpd == null || !dpd.isShowing()) {
                onCreateDatePicker();
            }
        } else if (v.getId() == R.id.Hora) {
            if (mTimePicker == null || !mTimePicker.isShowing()) {
                onCreateHoraPicker();
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btAltaP, R.id.btCancelarP, R.id.btDni})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAltaP:
                DniV = (EditText) rootView.findViewById(R.id.DniV);
                NomV= (TextView) rootView.findViewById(R.id.NomV);
                CognomsV = (TextView) rootView.findViewById(R.id.CognomsV);
                Data = (EditText) rootView.findViewById(R.id.Data);
                Hora = (EditText) rootView.findViewById(R.id.Hora);
                boolean validacio = true;
                if ("".equals(DniV.getText().toString())) {
                    DniV.setError("DNI es obligatorio de rellenar");
                    validacio = false;
                    DniV.requestFocus();
                } else if (DniV.length() != 9) {
                    DniV.setError("DNI debe tener longitud 9");
                    validacio = false;
                    DniV.requestFocus();
                } else if (!validarDni()) {
                    validacio = false;
                    DniV.requestFocus();
                }
                if ("".equals(Data.getText().toString())) {
                    Data.setError("Fecha es obligatorio de rellenar");
                    if (validacio) {
                        validacio = false;
                        Data.requestFocus();
                    }
                } else if (!validarData()) {
                    Data.setError("Formato de fecha incorrecto.");
                    if (validacio) {
                        validacio = false;
                        Data.requestFocus();
                    }
                } else if ("".equals(Hora.getText().toString())) {
                    Hora.setError("Hora és obligatorio de rellenar.");
                    if (validacio) {
                        validacio = false;
                        Hora.requestFocus();
                    }
                } else {
                    ParseBd taulaVisites = new ParseBd(context);
                    List<Pacients> pacients = taulaVisites.consultaPacient(NomV.getText().toString(), DniV.getText().toString(), cuidador);
                    if (pacients != null && pacients.size() > 0) {
                        Visites visites = new Visites();
                        visites.setDni(DniV.getText().toString());
                        visites.setNom(NomV.getText().toString());
                        visites.setCognoms(CognomsV.getText().toString());
                        visites.setCuidador(cuidador);
                        try {
                            visites.setFecha(dateFormatter.parse(Data.getText().toString()));
                            visites.setHora(horaFormatter.parse(Hora.getText().toString()));
                            visites.setData(todoFormatter.parse(Data.getText().toString() + " " + Hora.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        boolean insertat = taulaVisites.afegirVisites(visites);
                        if (insertat) {
                            List<Pacients> pacientsesNot = taulaVisites.consultaPacient(NomV.getText().toString(), DniV.getText().toString(), cuidador);
                            if (pacientsesNot != null && pacientsesNot.size() > 0) {
                                Pacients pacientsNot = pacientsesNot.get(0);
                                Integer telContacte = pacientsNot.getTelCte();
                                Integer telPaciente = pacientsNot.getTelPac();
                                SmsManager sms = SmsManager.getDefault();
                                String telefono = null;
                                if (telContacte != null) {
                                    telefono = String.valueOf(telContacte);
                                } else {
                                    telefono = String.valueOf(telPaciente);
                                }
                                sms.sendTextMessage(telefono, null, "Visita programada para la fecha " + Data.getText().toString() + " " + Hora.getText().toString(), null, null);
                            }


                            Toast.makeText(context, "Visita añadida.", Toast.LENGTH_SHORT).show();
                            DniV.setText("");
                            NomV.setText("");
                            CognomsV.setText("");
                            Data.setText("");
                            DniV.requestFocus();
                            Hora.setText("");
                            //crear notificació


                        } else {
                            Toast.makeText(context, "Esta visita ya existe en la aplicación.\n Por favor, modifique los datos.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "El paciente no existe en la aplicación. Se cancela la inserción de visita", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btCancelarP:
                if (tituloAnt != null && !"".equals(tituloAnt)) {
                    if (getString(R.string.mn_ConsultaP).equals(tituloAnt)) {
                        Fragment fragment = new ConsultaPacient();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        getActivity().setTitle(R.string.mn_ConsultaP);
                    } else if (getString(R.string.mn_Calendari).equals(tituloAnt)) {
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
                break;
            case R.id.btDni:
                DniV = (EditText) rootView.findViewById(R.id.DniV);
                if ("".equals(DniV.getText().toString())) {
                    DniV.setError("DNI es obligatorio de rellenar");

                    DniV.requestFocus();
                } else if (DniV.length() != 9) {
                    DniV.setError("DNI debe tener una longitud de 9");
                    DniV.requestFocus();

                } else if (!validarDni()) {
                    DniV.requestFocus();
                } else {
                    DniV.setError(null);
                    actualizarDadesPacient(DniV.getText().toString(), cuidador, rootView);
                }
                break;
        }
    }
}
