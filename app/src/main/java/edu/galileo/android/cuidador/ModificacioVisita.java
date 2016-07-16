package edu.galileo.android.cuidador;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
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


public class ModificacioVisita extends Fragment implements OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {
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
    @Bind(R.id.btModif)
    Button btModif;
    @Bind(R.id.btCancelarP)
    Button btCancelarP;
    @Bind(R.id.btDni)
    ImageButton btDni;
    private Context context = null;
    private String cuidador = null;
    private String dniAnt = null;
    private String dataAnt = null;
    private String dataOr = null;
    View rootView = null;
    private SimpleDateFormat dateFormatter;
    private String dniOr;
    private SimpleDateFormat horaFormatter;
    private SimpleDateFormat fechaFormatter;
    TimePickerDialog mTimePicker;
    private DatePickerDialog dpd;
    private EditText dataMP;
    private EditText horaMP;
    private String horaOr;
    private String tituloAnt;

    public ModificacioVisita() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String dniCP = null;
        Intent startingIntent = getActivity().getIntent();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        fechaFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        horaFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        rootView = inflater.inflate(R.layout.modificacio_visita, container, false);
        context = container.getContext();
        if (startingIntent != null) {
            cuidador = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", "Modificació visita");
            if (getArguments() != null) {
                startingIntent.putExtra("dniCP", dniCP);
                startingIntent.putExtra("dni", getArguments().getString("dni"));
                startingIntent.putExtra("data", getArguments().getString("data"));
                startingIntent.putExtra("dataCP", dataAnt);
                startingIntent.putExtra("hora", getArguments().getString("hora"));
            }

        }
        EditText dniMP = (EditText) rootView.findViewById(R.id.DniV);
        dataMP = (EditText) rootView.findViewById(R.id.Data);
        dataMP.setOnTouchListener(this);
        dataMP.setOnFocusChangeListener(this);
        horaMP = (EditText) rootView.findViewById(R.id.Hora);
        horaMP.setOnTouchListener(this);
        horaMP.setOnFocusChangeListener(this);

        if (getArguments() != null) {
            dniCP = getArguments().getString("dniCP");
            dataAnt = getArguments().getString("dataCP");
            dataOr = getArguments().getString("data");
            dniOr = getArguments().getString("dni");
            horaOr = getArguments().getString("hora");
            tituloAnt = getArguments().getString("tituloAnt");

        }

        Date data = null;
        if (dataAnt != null) {
            try {
                data = dateFormatter.parse(dataAnt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (dniCP != null && !"".equals(dniCP)) {
            dniMP.setText(dniCP);
            actualitzarDadesVisita(data, dniCP, cuidador);
        }
        if (dniOr != null && dniOr != null) {
            dniMP.setText(dniOr);
        }
        if (dataMP != null && dataOr != null) {
            dataMP.setText(dataOr);
        }
        if (horaMP != null && horaOr != null) {
            horaMP.setText(horaOr);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void actualitzarDadesVisita(Date data, String dni, String cuidador) {
        ParseBd bd = new ParseBd(context);
        List<Visites> visiteses = bd.consultaVisitaModif(data, dni, cuidador);
        dniAnt = dni;
        if (visiteses != null && visiteses.size() == 1) {
            NomV = (TextView) rootView.findViewById(R.id.NomV);
            CognomsV = (TextView) rootView.findViewById(R.id.CognomsV);
            Data = (EditText) rootView.findViewById(R.id.Data);
            Hora = (EditText) rootView.findViewById(R.id.Hora);
            Visites visites = visiteses.get(0);
            NomV.setText(visites.getNom());
            CognomsV.setText(visites.getCognoms());
            Data.setText(fechaFormatter.format(visites.getData()));
            Hora.setText(horaFormatter.format(visites.getHora()));
        } else {
            Toast.makeText(context, "Tienes que informar un DNI o el que has informado, no existe", Toast.LENGTH_LONG).show();

        }


    }


    @Override
    public void onStop() {
        super.onStop();
        DniV = (EditText) rootView.findViewById(R.id.DniV);
        Data = (EditText) rootView.findViewById(R.id.Data);
        Intent intentRe = getActivity().getIntent();
        intentRe.putExtra("dniCP", dniAnt);
        intentRe.putExtra("dni", DniV.getText().toString());
        intentRe.putExtra("data", Data.getText().toString());
        intentRe.putExtra("dataCP", dataAnt);
        intentRe.putExtra("hora", horaMP.getText().toString());
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
            DniV.setError("El format del DNI no és correcto. Los ocho primeros digitos son numeros y el último es una letra");
            return false;
        }
    }

    private boolean validarData(EditText data) {
        boolean validarData = true;
        try {
            fechaFormatter.parse(data.getText().toString());
        } catch (ParseException e) {
            validarData = false;
        }
        return validarData;
    }


    private void onCreateHoraPicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (horaMP != null) {
            try {
                Date parseDate = horaFormatter.parse(horaMP.getText().toString());
                c.setTime(parseDate);
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (mTimePicker == null || !mTimePicker.isShowing()) {

            // Create a new instance of TimePickerDialog and return it
            mTimePicker = new TimePickerDialog(getActivity(), R.style.timePickerDialog_Theme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String minutos = String.valueOf(minute);
                    if (minute >= 0 && minute < 10) {
                        minutos = "0" + minutos;
                    }
                    horaMP.setText(hourOfDay + ":" + minutos);
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Tria l'hora");
            mTimePicker.show();

        }

    }


    private void onCreateDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (dataMP != null) {
            try {
                Date parseDate = fechaFormatter.parse(dataMP.getText().toString());
                calendar.setTime(parseDate);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

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
                    dataMP.setText(day + "/" + month + "/" + year);
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

    @OnClick({R.id.btModif, R.id.btCancelarP, R.id.btDni})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btModif:
                if (rootView != null) {
                    NomV = (TextView) rootView.findViewById(R.id.NomV);
                    CognomsV = (TextView) rootView.findViewById(R.id.CognomsV);

                    DniV = (EditText) rootView.findViewById(R.id.DniV);
                    Data = (EditText) rootView.findViewById(R.id.Data);
                    Hora = (EditText) rootView.findViewById(R.id.Hora);
                    boolean validacio = true;
                    if ("".equals(DniV.getText().toString())) {
                        DniV.setError("DNI es obligatorio de rellenar");
                        validacio = false;
                        DniV.requestFocus();
                    } else if (DniV.length() != 9) {
                        DniV.setError("DNI debe tener una longitud de 9");
                        validacio = false;
                        DniV.requestFocus();
                    } else if (!validarDni()) {
                        validacio = false;
                        DniV.requestFocus();
                    }
                    if ("".equals(Data.getText().toString())) {
                        Data.setError("Data es obligatorio de rellenar");
                        if (validacio) {
                            Data.requestFocus();
                        }
                        validacio = false;
                    } else if (!validarData(Data)) {
                        Data.setError("Formato de la fecha es incorrecto.");
                        if (validacio) {
                            Data.requestFocus();
                        }
                        validacio = false;
                    } else if ("".equals(Hora.getText().toString())) {
                        Hora.setError("Hora es obligatorio de rellenar");
                        if (validacio) {
                            Hora.requestFocus();
                        }
                        validacio = false;

                    } else {
                        Visites visites = new Visites();
                        visites.setDni(DniV.getText().toString());
                        visites.setNom(NomV.getText().toString());
                        visites.setCognoms(CognomsV.getText().toString());
                        try {
                            visites.setFecha(fechaFormatter.parse(Data.getText().toString()));
                            visites.setData(dateFormatter.parse(Data.getText().toString() + " " + Hora.getText().toString()));
                            visites.setHora(horaFormatter.parse(Hora.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        visites.setCuidador(cuidador);
                        ParseBd taulaVisites = new ParseBd(context);
                        Date fechaAnt = null;
                        try {
                            fechaAnt = dateFormatter.parse(dataAnt);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        boolean modificat = taulaVisites.modificarVisites(fechaAnt, visites);
                        if (modificat) {
                            List<Pacients> pacientsesNot = taulaVisites.consultaPacient(NomV.getText().toString(), DniV.getText().toString(), cuidador);
                            if (pacientsesNot != null && pacientsesNot.size() > 0) {
                                Pacients pacientsNot = pacientsesNot.get(0);
                                Integer telContacte = pacientsNot.getTelCte();
                                Integer telPaciente = pacientsNot.getTelPac();
                                SmsManager sms = SmsManager.getDefault();
                                String telefono;
                                if (telContacte != null) {
                                    telefono = String.valueOf(telContacte);
                                } else {
                                    telefono = String.valueOf(telPaciente);
                                }
                                String missatge = "Su visita ha estado modificada del " + dateFormatter.format(fechaAnt) + " en la fecha" + Data.getText().toString() + " " + Hora.getText().toString();
                                if (telefono != null) {
                                    sms.sendTextMessage(telefono, null, missatge, null, null);
                                }
                            }
                            Toast.makeText(context, "Visita Modificada.", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new ConsultaVisita();
                            //Validamos si el fragment no es nulo
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            getActivity().setTitle("Consulta Visitas");
                        } else {
                            Toast.makeText(context, "Este paciente no existe en la aplicación.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.btCancelarP:
                if (tituloAnt != null && !"".equals(tituloAnt)) {
                    if (getString(R.string.mn_consultaV).equals(tituloAnt)) {
                        Fragment fragment = new ConsultaVisita();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        getActivity().setTitle(R.string.mn_consultaV);
                    } else if (getString(R.string.mn_Calendari).equals(tituloAnt)) {
                        Fragment fragment = new CalendariPersonalizado();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        getActivity().setTitle(R.string.mn_Calendari);
                    }
                } else {
                    Fragment fragment = new ConsultaVisita();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    getActivity().setTitle(R.string.mn_consultaV);
                }
                break;
            case R.id.btDni:
                DniV = (EditText) rootView.findViewById(R.id.DniV);
                Data = (EditText) rootView.findViewById(R.id.Data);
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
                    Date fecha = null;
                    try {
                        fecha = dateFormatter.parse(Data.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    actualitzarDadesVisita(fecha, DniV.getText().toString(), cuidador);
                }
                break;
        }
    }
}
