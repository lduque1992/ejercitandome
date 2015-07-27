package co.edu.udea.cmovil.gr6.ejercitandome;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class DetalleEjercicioActivity extends ActionBarActivity {
    public static final String EJERCICIO_UNICO = "me";
    public static final String MUSCULO = "m";
    public static final String MUSCULO_ID = "mi";
    private static List<ParseObject> ejercicios;
    ImageView imageView;
    byte[] img;
    String musculoId;
    boolean esUnico;

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ejercicio);
        String musculo = getIntent().getStringExtra(MUSCULO);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(musculo);
            getSupportActionBar().setSubtitle("Ejercicios");
        }
        musculoId = getIntent().getStringExtra(MUSCULO_ID);
        esUnico = getIntent().getBooleanExtra(EJERCICIO_UNICO, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        cargarEjercicios();
    }


    private void cargarEjercicios() {
        ParseQuery<ParseObject> query;
        if (esUnico) {
            query = ParseQuery.getQuery("Ejercicios");
            final ProgressDialog dialog = ProgressDialog.show(DetalleEjercicioActivity.this, "Cargando Detalle del Ejercicio...", null, true, false);
            query.getInBackground(musculoId, new GetCallback<ParseObject>() {
                public void done(ParseObject ejercicio, ParseException e) {
                    dialog.dismiss();
                    List<ParseObject> ejercicios = new ArrayList<>();
                    ejercicios.add(ejercicio);
                    DetalleEjercicioActivity.ejercicios = ejercicios;
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                }
            });
        } else {
            ParseQuery<ParseObject> queryDefaultUser = ParseQuery.getQuery("Ejercicios");
            ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Ejercicios");
            queryDefaultUser.whereEqualTo("usuario", ParseObject.createWithoutData("_User", "XV57KfUObS"));
            queryCurrentUser.whereEqualTo("usuario", ParseUser.getCurrentUser());
            List<ParseQuery<ParseObject>> orQueries = new ArrayList<>();
            orQueries.add(queryCurrentUser);
            orQueries.add(queryDefaultUser);
            query = ParseQuery.or(orQueries);
            ParseObject musculo = ParseObject.createWithoutData("Musculo", musculoId);
            query.whereEqualTo("musculo", musculo);
            final ProgressDialog dialog = ProgressDialog.show(DetalleEjercicioActivity.this, "Cargando Ejercicios...", null, true, false);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> ejercicios, ParseException e) {
                    dialog.dismiss();
                    if (ejercicios.isEmpty()) {
                        Toast.makeText(DetalleEjercicioActivity.this, "No hay ejercicios para este musculo", Toast.LENGTH_SHORT).show();
                    }
                    DetalleEjercicioActivity.ejercicios = ejercicios;
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                }
            });
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return ejercicios.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ejercicios.get(position).getString("nombre");
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int pos;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.pos = sectionNumber;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detalle_ejercicio, container, false);
            ParseObject ejercicio = ejercicios.get(pos);
            TextView tvNombre = (TextView) rootView.findViewById(R.id.nombre);
            TextView tvDescripcion = (TextView) rootView.findViewById(R.id.descripcion_ejercicio);
            final ImageView ivIcono = (ImageView) rootView.findViewById(R.id.icono_ejercicio);
            tvNombre.setText(ejercicio.getString("nombre"));
            tvDescripcion.setText(ejercicio.getString("Descripcion"));
            ParseFile imageFile = (ParseFile) ejercicio.get("Imagen");
            imageFile.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivIcono.setImageBitmap(image);
                    } else {

                    }
                }
            });
            return rootView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mis_ejercicios, menu);
        return !esUnico;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_crear: {
                dialogoCrear().show();
                return true;
            }
        }
        return false;
    }

    protected Dialog dialogoCrear() {

        final Dialog dialog = new Dialog(DetalleEjercicioActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_crear);
        dialog.setCancelable(true);

        final EditText nombre = (EditText) dialog.findViewById(R.id.nombre);

        final EditText descripcion = (EditText) dialog.findViewById(R.id.descripcion);

        imageView = (ImageView) dialog.findViewById(R.id.imageView);

        Button imagen = (Button) dialog.findViewById(R.id.image);

        Button cancelar = (Button) dialog.findViewById(R.id.cancelar);

        Button guardar = (Button) dialog.findViewById(R.id.guardar);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 123);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nombre.getText())) {
                    nombre.setError("Este campo es requerido");
                    nombre.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(descripcion.getText())) {
                    descripcion.setError("Este campo es requerido");
                    descripcion.requestFocus();
                    return;
                }
                if (img == null) {
                    Toast.makeText(DetalleEjercicioActivity.this, "debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ParseObject ejercicio = ParseObject.create("Ejercicios");
                ejercicio.put("nombre", nombre.getText().toString());
                ejercicio.put("Descripcion", descripcion.getText().toString());
                ParseObject musculo = ParseObject.createWithoutData("Musculo", musculoId);
                ejercicio.put("musculo", musculo);
                ejercicio.put("usuario", ParseUser.getCurrentUser());
                final ParseFile file = new ParseFile(img);
                final ProgressDialog progressDialog = ProgressDialog.show(DetalleEjercicioActivity.this, "Guardando Ejercicio...", null, true, false);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ejercicio.put("Imagen", file);
                        ejercicio.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                progressDialog.dismiss();
                                dialog.dismiss();
                                cargarEjercicios();
                                if (e != null) {
                                    Toast.makeText(DetalleEjercicioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetalleEjercicioActivity.this, "Ejercicio guardado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        img = stream.toByteArray();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
