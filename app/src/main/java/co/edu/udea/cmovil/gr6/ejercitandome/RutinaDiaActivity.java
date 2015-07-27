package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.cmovil.gr6.ejercitandome.adapters.MusculosAdapter;
import co.edu.udea.cmovil.gr6.ejercitandome.adapters.RutinasAdapter;

public class RutinaDiaActivity extends ActionBarActivity implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener{
    private int dia;
    ListView rutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rutina);
        rutinas = (ListView) findViewById(R.id.rutinas);
        rutinas.setOnItemClickListener(this);
        rutinas.setOnItemLongClickListener(this);
        dia = getIntent().getIntExtra("dia", 0);
        if (getSupportActionBar() != null) {
            switch (dia) {
                case 0: {
                    getSupportActionBar().setTitle("Lunes");
                    break;
                }
                case 1: {
                    getSupportActionBar().setTitle("Martes");
                    break;
                }
                case 2: {
                    getSupportActionBar().setTitle("Miercoles");
                    break;
                }
                case 3: {
                    getSupportActionBar().setTitle("Jueves");
                    break;
                }
                case 4: {
                    getSupportActionBar().setTitle("Viernes");
                    break;
                }
                case 5: {
                    getSupportActionBar().setTitle("Sabado");
                    break;
                }
                case 6: {
                    getSupportActionBar().setTitle("Domingo");
                    break;
                }
            }
        }
        cargarListaEjercicios();
    }

    private void cargarListaEjercicios() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rutina");
        query.whereEqualTo("usuario", ParseUser.getCurrentUser());
        query.whereEqualTo("dia", dia);
        query.include("ejercicio");
        query.include("ejercicio.musculo");
        final ProgressDialog dialog = ProgressDialog.show(RutinaDiaActivity.this, "Cargando Rutina...", null, true, false);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                dialog.dismiss();
                if (scoreList.isEmpty()) {
                    Toast.makeText(RutinaDiaActivity.this, "No has agregado ejrcicios para este dia", Toast.LENGTH_SHORT).show();
                }
                rutinas.setAdapter(new RutinasAdapter(scoreList, RutinaDiaActivity.this));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ParseObject rutina = (ParseObject) rutinas.getAdapter().getItem(i);
        Intent intent = new Intent(this, DetalleEjercicioActivity.class);
        intent.putExtra(DetalleEjercicioActivity.MUSCULO, "Rutina");
        intent.putExtra(DetalleEjercicioActivity.MUSCULO_ID, rutina.getParseObject("ejercicio").getObjectId());
        intent.putExtra(DetalleEjercicioActivity.EJERCICIO_UNICO, true);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RutinaDiaActivity.this);
        builder.setMessage("¿desea eliminar este ejercicio?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ParseObject rutina = (ParseObject) rutinas.getAdapter().getItem(pos);
                final ProgressDialog dialog = ProgressDialog.show(RutinaDiaActivity.this, "Eliminando ejercicio...", null, true, false);
                rutina.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        cargarListaEjercicios();
                    }
                });
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mis_ejercicios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_crear: {
                dialogoCrear();
                return true;
            }
        }
        return false;
    }

    protected void dialogoCrear() {

        final Dialog dialog = new Dialog(RutinaDiaActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_crear_rutina);
        dialog.setCancelable(true);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.ejercicios);

        Button cancelar = (Button) dialog.findViewById(R.id.cancelar);

        Button guardar = (Button) dialog.findViewById(R.id.guardar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject ejercicio = (ParseObject) (spinner.getAdapter()).getItem(spinner.getSelectedItemPosition());
                final ParseObject rutina = ParseObject.create("Rutina");
                rutina.put("ejercicio", ejercicio);
                rutina.put("usuario", ParseUser.getCurrentUser());
                rutina.put("dia", dia);
                final ProgressDialog progressDialog = ProgressDialog.show(RutinaDiaActivity.this, "Guardando Rutina...", null, true, false);
                rutina.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        cargarListaEjercicios();
                        if (e != null) {
                            Toast.makeText(RutinaDiaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RutinaDiaActivity.this, "Ejercicio guardado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
        cargarEjercicios(spinner);
    }

    private void cargarEjercicios(final Spinner spinner) {
        ParseQuery<ParseObject> queryDefaultUser = ParseQuery.getQuery("Ejercicios");
        ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Ejercicios");
        queryDefaultUser.whereEqualTo("usuario", ParseObject.createWithoutData("_User", "XV57KfUObS"));
        queryCurrentUser.whereEqualTo("usuario", ParseUser.getCurrentUser());
        List<ParseQuery<ParseObject>> orQueries = new ArrayList<>();
        orQueries.add(queryCurrentUser);
        orQueries.add(queryDefaultUser);
        ParseQuery<ParseObject> query = ParseQuery.or(orQueries);
        query.include("musculo");
        final ProgressDialog dialog = ProgressDialog.show(RutinaDiaActivity.this, "Cargando Ejercicios...", null, true, false);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> ejercicios, ParseException e) {
                dialog.dismiss();
                MusculosAdapter adapter = new MusculosAdapter(ejercicios, RutinaDiaActivity.this);
                adapter.setEjercicio(true);
                spinner.setAdapter(adapter);
            }
        });
    }

}





