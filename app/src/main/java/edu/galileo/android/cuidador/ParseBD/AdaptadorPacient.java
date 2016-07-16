package edu.galileo.android.cuidador.ParseBD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class AdaptadorPacient extends BaseAdapter{
    private ArrayList<?> entradas;
    private int R_layout;
    private Context contexto;
    public AdaptadorPacient(Context contexto,int R_layout,ArrayList<?> entradas){
        super();
        this.contexto=contexto;
        this.R_layout=R_layout;
        this.entradas=entradas;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            LayoutInflater vi =(LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= vi.inflate(R_layout,null);
        }
        onEntrada(entradas.get(position),view);
        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int position) {
        return entradas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract void onEntrada(Object entrada,View view);
}
