package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_nivel);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bienvenido " + ParseUser.getCurrentUser().getUsername());
        }
    }

    public void ejerciciosBase(View view){
        Intent intent = new Intent(getApplicationContext(), MusculosActivity.class);
        startActivity(intent);
    }

    public void misRutinas(View view){
        Intent intent = new Intent(getApplicationContext(), MisRutinasActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signout) {
            final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "Cerrando sesión...", null, true, false);
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
