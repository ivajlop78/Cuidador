package edu.galileo.android.cuidador;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntradaCuidador extends Fragment implements OnClickListener {
    @Bind(R.id.btModifC)
    Button btModifC;
    @Bind(R.id.btConsultaP)
    Button btConsultaP;
    @Bind(R.id.btConsultaV)
    Button btConsultaV;
    private Context context = null;
    View rootView = null;
    private String usuari = null;

    public EntradaCuidador() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.entrada_cuidador, container, false);
        context = container.getContext();
        Intent startingIntent = getActivity().getIntent();
        if (startingIntent != null) {
            usuari = startingIntent.getStringExtra("usuari");
            startingIntent.putExtra("titulo", "Entrada cuidador");
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btModifC, R.id.btConsultaP, R.id.btConsultaV})
    public void onClick(View view) {
        Fragment fragment=null;
        FragmentManager fragmentManager = getFragmentManager();
        switch (view.getId()) {
            case R.id.btModifC:
                fragment=new ModificaCuidador();
                //Validamos si el fragment no es nulo

                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle("Modificación cuidador");
                break;
            case R.id.btConsultaP:
                fragment=new ConsultaPacient();
                //Validamos si el fragment no es nulo
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle("Consulta paciente");
                break;
            case R.id.btConsultaV:
                fragment=new CalendariPersonalizado();
                //Validamos si el fragment no es nulo
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                getActivity().setTitle(R.string.mn_Calendari);
                break;
        }
    }
}
