package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SingUpActivity extends ActionBarActivity {
    TextView name, password, email_singup, password2, nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        name = (TextView) findViewById(R.id.Nombre_Usuario);
        password = (TextView) findViewById(R.id.password_singup);
        password2 = (TextView) findViewById(R.id.repeat_password);
        email_singup = (TextView) findViewById(R.id.email_singup);
        nickName = (TextView) findViewById(R.id.nickname);
    }

    public void signUp(View view) {
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("Este campo es requerido");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email_singup.getText())) {
            email_singup.setError("Este campo es requerido");
            email_singup.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_singup.getText()).matches()) {
            email_singup.setError("El correo ingresado no es valido");
            email_singup.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(nickName.getText())) {
            nickName.setError("Este campo es requerido");
            nickName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Este campo es requerido");
            password.requestFocus();
            return;
        }
        if (!password.getText().toString().equalsIgnoreCase(password2.getText().toString())) {
            password2.setError("Las contraseñas no coinciden");
            password2.requestFocus();
            return;
        }
        ParseUser user = new ParseUser();
        user.setUsername(nickName.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email_singup.getText().toString());
        user.put("name", name.getText().toString());
        final ProgressDialog dialog = ProgressDialog.show(SingUpActivity.this, "Creando cuenta...", null, true, false);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                dialog.dismiss();
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Te has registrado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
