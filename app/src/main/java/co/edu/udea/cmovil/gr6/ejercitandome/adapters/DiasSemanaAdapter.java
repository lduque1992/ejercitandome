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

import co.edu.udea.cmovil.gr6.ejercitandome.DetalleEjercicioActivity;
import co.edu.udea.cmovil.gr6.ejercitandome.R;

/**
 * Created by langel.duque on 18/06/15.
 */
public class DiasSemanaAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private String[] dias = new String[]{"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private Activity context;

    public DiasSemanaAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return dias.length;
    }

    @Override
    public Object getItem(int position) {
        return dias[position];
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
        nombreEjercicio.setText(dias[position]);
        return convertView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        context.startActivity(new Intent(context, DetalleEjercicioActivity.class).putExtra(DetalleEjercicioActivity.MUSCULO, dias[position]));
    }
}
