package co.edu.udea.cmovil.gr6.ejercitandome.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import co.edu.udea.cmovil.gr6.ejercitandome.R;

/**
 * Created by langel.duque on 18/06/15.
 */
public class RutinasAdapter extends BaseAdapter {
    private List<ParseObject> rutinas;
    private Activity context;

    public RutinasAdapter(List<ParseObject> rutinas, Activity context) {
        this.rutinas = rutinas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rutinas.size();
    }

    @Override
    public Object getItem(int position) {
        return rutinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_ejercicio, null);
        }
        TextView nombreEjercicio = (TextView) convertView.findViewById(R.id.nombre);
        ParseObject ejercicio = rutinas.get(position).getParseObject("ejercicio");
        nombreEjercicio.setText(ejercicio.getParseObject("musculo").getString("nombre") + " " + ejercicio.getString("nombre"));
        return convertView;
    }
}
