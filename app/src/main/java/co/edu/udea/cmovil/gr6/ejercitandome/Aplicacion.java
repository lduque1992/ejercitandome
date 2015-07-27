package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class Aplicacion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ViiRqyyMfk0ZzjqbvougwjFrFPU0A1h9fpRDqYpH", "CfmjU7RVA1GkJLM6kBx3ndzs5OsqtjY7CSOKwBNM");
    }
}
