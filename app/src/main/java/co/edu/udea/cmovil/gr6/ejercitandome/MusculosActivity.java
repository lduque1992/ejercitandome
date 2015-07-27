package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import co.edu.udea.cmovil.gr6.ejercitandome.adapters.MusculosAdapter;


public class MusculosActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public static final String SON_BASE = "t";
    private ListView lvMusculos;
    private boolean sonEjerciciosBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musculos);
        lvMusculos = (ListView) findViewById(R.id.lista_musculos);
        sonEjerciciosBase = getIntent().getBooleanExtra(SON_BASE, true);
        if (getSupportActionBar() != null) {
            if (sonEjerciciosBase) {
                getSupportActionBar().setTitle("Ejercicios Base");
            } else {
                getSupportActionBar().setTitle("Mis Ejercicios");
            }
            getSupportActionBar().setSubtitle("Musculos");
        }
        cargarListaMusculos();
    }

    private void cargarListaMusculos() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Musculo");
        final ProgressDialog dialog = ProgressDialog.show(MusculosActivity.this, "Cargando Musculos...", null, true, false);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> musculos, ParseException e) {
                dialog.dismiss();
                MusculosAdapter adapter = new MusculosAdapter(musculos, MusculosActivity.this);
                lvMusculos.setAdapter(adapter);
                if (sonEjerciciosBase) {
                    lvMusculos.setOnItemClickListener(adapter);
                } else {
                    lvMusculos.setOnItemClickListener(MusculosActivity.this);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, DetalleEjercicioActivity.class);
        ParseObject musculo = (ParseObject) lvMusculos.getAdapter().getItem(i);
        intent.putExtra(DetalleEjercicioActivity.MUSCULO, musculo.getString("nombre"));
        intent.putExtra(DetalleEjercicioActivity.MUSCULO_ID, musculo.getObjectId());
        intent.putExtra(DetalleEjercicioActivity.EJERCICIO_UNICO, true);
        startActivity(intent);
    }
}
