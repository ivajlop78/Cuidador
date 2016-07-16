package edu.galileo.android.cuidador;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.cuidador.ParseBD.ParseBd;

public class CalendariPersonalizado extends Fragment implements View.OnClickListener {
    @Bind(R.id.prevMonth)
    ImageView prevMonth;
    @Bind(R.id.currentMonth)
    TextView currentMonth;
    @Bind(R.id.nextMonth)
    ImageView nextMonth;
    @Bind(R.id.addEvent)
    Button addEvent;
    @Bind(R.id.buttonlayout)
    LinearLayout buttonlayout;
    @Bind(R.id.calendarheader)
    TextView calendarheader;
    @Bind(R.id.calendar)
    GridView calendar;
    private Context context = null;
    View rootView = null;
    private String usuari = null;
    //private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    //private ImageView prevMonth;
    //private ImageView nextMonth;
   //private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    public CalendariPersonalizado() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calendario_personalizado, container, false);
        context = container.getContext();
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
            usuari = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", getString(R.string.mn_Calendari));
        }

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        prevMonth = (ImageView) rootView.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) rootView.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) rootView.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);
        Button enrere = (Button) rootView.findViewById(R.id.addEvent);
        enrere.setOnClickListener(this);

        calendar = (GridView) rootView.findViewById(R.id.calendar);


// Initialised
        adapter = new GridCellAdapter(context, R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendar.setAdapter(adapter);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(context, R.id.calendar_day_gridcell, month, year);

        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendar.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String tag = null;
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year = year - 1;
            } else {
                month = month - 1;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v.getId() == R.id.addEvent) {
            Fragment fragment = new EntradaCuidador();
            //Validamos si el fragment no es nulo
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            getActivity().setTitle("Entrada cuidador");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"L", "M", "X", "J", "V", "S", "D"};
        private final String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**–> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***—> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 2;
            if (currentWeekDay < 0) {
                currentWeekDay = 6;
            }
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            int iMesP = (prevMonth + 1);
            String mesPrevi = iMesP < 10 ? "0" + iMesP : String.valueOf(iMesP);
            int iMesA = (nextMonth + 1);
            String mesAnt = iMesA < 10 ? "0" + iMesA : String.valueOf(iMesA);
            int iMesAct = (currentMonth + 1);
            String mesActual = iMesAct < 10 ? "0" + iMesAct : String.valueOf(iMesAct);
            String fechaP = "01/" + mesPrevi + "/" + prevYear;
            String fechaD = "31/" + mesAnt + "/" + nextYear;
            ParseBd parseBd = new ParseBd(context);
            SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDesde = null;
            Date fechaHasta = null;
            try {
                fechaDesde = fechaFormat.parse(fechaP);
                fechaHasta = fechaFormat.parse(fechaD);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Date> dates = parseBd.consultaVisita(fechaDesde, fechaHasta, usuari);
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
                try {
                    int diaI = (daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i;
                    String dia = diaI < 10 ? "0" + diaI : String.valueOf(diaI);

                    if (dates != null && dates.contains(fechaFormat.parse(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "/" + mesPrevi + "/" + prevYear))) {
                        list.add(dia + "-ORANGE" + "-" + mesPrevi + "-" + prevYear);
                    } else {
                        list.add(dia + "-GREY" + "-" + mesPrevi + "-" + prevYear);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
                String dia = i < 10 ? "0" + i : String.valueOf(i);
                if (i == getCurrentDayOfMonth()) {
                    try {
                        if (dates != null && dates.contains(fechaFormat.parse(dia + "/" + mesActual + "/" + yy))) {
                            list.add(dia + "-ORANGE" + "-" + mesActual + "-" + yy);
                        } else {
                            list.add(dia + "-BLUE" + "-" + mesActual + "-" + yy);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {

                        if (dates != null && dates.contains(fechaFormat.parse(dia + "/" + mesActual + "/" + yy))) {
                            list.add(dia + "-ORANGE" + "-" + mesActual + "-" + yy);
                        } else {
                            list.add(dia + "-WHITE" + "-" + mesActual + "-" + yy);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                int diaI = i + 1;
                String dia = diaI < 10 ? "0" + diaI : String.valueOf(diaI);
                try {

                    if (dates != null && dates.contains(fechaFormat.parse(diaI + "/" + mesAnt + "/" + nextYear))) {
                        list.add(dia + "-ORANGE" + "-" + mesAnt + "-" + nextYear);
                    } else {
                        list.add(dia + "-GREY" + "-" + mesAnt + "-" + nextYear);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            return new HashMap<String, Integer>();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);

            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.colorGrey));
                HashMap<String, String> valores = new HashMap<String, String>();
                valores.put("N", theday + "/" + themonth + "/" + theyear);
                gridcell.setTag(valores);
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(Color.WHITE);
                HashMap<String, String> valores = new HashMap<String, String>();
                valores.put("N", theday + "/" + themonth + "/" + theyear);
                gridcell.setTag(valores);
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.colorBlue));
                HashMap<String, String> valores = new HashMap<String, String>();
                valores.put("N", theday + "/" + themonth + "/" + theyear);
                gridcell.setTag(valores);
            }
            if (day_color[1].equals("ORANGE")) {
                gridcell.setTextColor(getResources().getColor(R.color.colorPrimary));
                gridcell.setBackground(getResources().getDrawable(R.drawable.selector_grey));
                HashMap<String, String> valores = new HashMap<String, String>();
                valores.put("Y", theday + "/" + themonth + "/" + theyear);
                gridcell.setTag(valores);
            }
            return row;
        }

        @Override
        public void onClick(View view) {
            HashMap<String, String> tag = (HashMap<String, String>) view.getTag();
            if (tag != null && tag.containsKey("Y")) {
                String fecha = tag.get("Y");
                Fragment fragment = new ConsultaVisita();
                Bundle parametro = new Bundle();
                parametro.putString("fechaCalendar", fecha);
                parametro.putString("tituloAnt", getString(R.string.mn_Calendari));
                fragment.setArguments(parametro);
                //Validamos si el fragment no es nulo
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle(R.string.mn_consultaV);
            } else {
                String fecha = tag.get("N");
                Fragment fragment = new AltaVisita();
                Bundle parametro = new Bundle();
                parametro.putString("fechaCalendar", fecha);
                parametro.putString("tituloAnt", getString(R.string.mn_Calendari));
                fragment.setArguments(parametro);
                //Validamos si el fragment no es nulo
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle(R.string.mn_AltaV);
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }
}
