package co.edu.udea.cmovil.gr6.ejercitandome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import co.edu.udea.cmovil.gr6.ejercitandome.adapters.DiasSemanaAdapter;


public class MisRutinasActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Rutinas");
        }
        setContentView(R.layout.activity_nivel_experimentado);
        ListView dias = (ListView) findViewById(R.id.dias);
        dias.setAdapter(new DiasSemanaAdapter(this));
        dias.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), RutinaDiaActivity.class);
        intent.putExtra("dia", i);
        startActivity(intent);
    }
}
