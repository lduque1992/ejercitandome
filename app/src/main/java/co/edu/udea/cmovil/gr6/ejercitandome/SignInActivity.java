package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;


public class SignInActivity extends Activity {
    EditText user;
    EditText passwor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validarUsuarioAutenticado();
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.editText);
        passwor = (EditText) findViewById(R.id.password);
    }

    public void login(View view) {
        if (TextUtils.isEmpty(user.getText())) {
            user.setError("Este campo es requerido");
            user.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passwor.getText())) {
            passwor.setError("Este campo es requerido");
            passwor.requestFocus();
            return;
        }
        final ProgressDialog dialog = ProgressDialog.show(SignInActivity.this, "Iniciando Sesión...", null, true, false);
        ParseUser.logInInBackground(user.getText().toString(), passwor.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivityForResult(intent, 123);
    }

    private void validarUsuarioAutenticado() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null && currentUser.isAuthenticated()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        validarUsuarioAutenticado();
    }
}
