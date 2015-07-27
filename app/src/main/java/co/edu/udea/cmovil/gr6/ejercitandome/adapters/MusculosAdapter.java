package co.edu.udea.cmovil.gr6.ejercitandome.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import co.edu.udea.cmovil.gr6.ejercitandome.DetalleEjercicioActivity;
import co.edu.udea.cmovil.gr6.ejercitandome.R;

/**
 * Created by langel.duque on 18/06/15.
 */
public class MusculosAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private List<ParseObject> musculos;
    private Activity context;
    private boolean ejercicio;

    public MusculosAdapter(List<ParseObject> musculos, Activity context) {
        this.musculos = musculos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return musculos.size();
    }

    @Override
    public Object getItem(int position) {
        return musculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (ejercicio) {
            TextView nombreEjercicio = new TextView(context);
            nombreEjercicio.setText(musculos.get(position).getParseObject("musculo").getString("nombre") + " " + musculos.get(position).getString("nombre"));
            return nombreEjercicio;
        }
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_ejercicio, null);
        }
        TextView nombreEjercicio = (TextView) convertView.findViewById(R.id.nombre);
        nombreEjercicio.setText(musculos.get(position).getString("nombre"));
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, DetalleEjercicioActivity.class);
        intent.putExtra(DetalleEjercicioActivity.MUSCULO, musculos.get(position).getString("nombre"));
        intent.putExtra(DetalleEjercicioActivity.MUSCULO_ID, musculos.get(position).getObjectId());
        intent.putExtra(DetalleEjercicioActivity.EJERCICIO_UNICO, false);
        context.startActivity(intent);
    }

    public boolean isEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(boolean ejercicio) {
        this.ejercicio = ejercicio;
    }
}
