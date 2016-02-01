package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class Perfil_Editar extends AppCompatActivity {

    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;
    private Activity activity;
    private LayoutInflater inflater;

    EditText txtNombre, txtCorreo, txtTelefono, txtContrasenaAcual, txtNuevaContrasena, txtRepiteContrasena;
    Button btnEnviar;


    public static String Ruta_imagen;
    public static Bitmap imgx = null;
    public static Boolean ESTATUS_CAMBIO_FOTO;
    public static ImageView btnFoto;
    public static MaterialRippleLayout btnFoto2;
    private static int TAKE_PICTURE = 1;
    AlertDialog.Builder alert;
    int codex;
    EditText titulo, texto;
    String txtTitulo, txtTexto;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_editar);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();
        ESTATUS_CAMBIO_FOTO = false;

        txtNombre = (EditText) findViewById(R.id.nombre);
        txtCorreo = (EditText) findViewById(R.id.correo);
        txtTelefono = (EditText) findViewById(R.id.telefono);
        txtContrasenaAcual = (EditText) findViewById(R.id.contrasenaActual);
        txtNuevaContrasena = (EditText) findViewById(R.id.nuevacontrasena);
        txtRepiteContrasena = (EditText) findViewById(R.id.repetircontrasena);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnFoto = (ImageView) findViewById(R.id.fotosubir);
        registerForContextMenu(btnFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContextMenu(v);
            }
        });


        GradientDrawable drawablex = (GradientDrawable) btnEnviar.getBackground();
        drawablex.setColor(Color.parseColor(color));

        SQLiteDatabase db = MainActivity.db_sqlite.getWritableDatabase();
        Cursor tutor = db.rawQuery("select * from tutor", null);
        if (tutor.moveToFirst()) {

            txtNombre.setText(tutor.getString(1));
            txtCorreo.setText(tutor.getString(2));
            txtTelefono.setText(tutor.getString(5));
            if (tutor.getString(6) != null && tutor.getString(6).length() > 5)
                Picasso.with(activity).load(tutor.getString(6)).placeholder(R.drawable.logosc).into(btnFoto);
        }

        tutor.close();
        db.close();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });


    }




    private void CustomActionBar() {
        // TODO Auto-generated method stub
        final LayoutInflater inflater = (LayoutInflater) mActionBar.getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.activity_main_actionbar, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        imagenPrincipal = (ImageView) customActionBarView.findViewById(R.id.imagenPrincipal);
        textoPrincipal = (TextView) customActionBarView.findViewById(R.id.textoPrincipal);
        textoSecundario = (TextView) customActionBarView.findViewById(R.id.textoSecundario);
        iconoDerecho = (ImageView) customActionBarView.findViewById(R.id.iconoDerecho);
        iconoIzquierdo = (ImageView) customActionBarView.findViewById(R.id.iconoIzquierdo);

        RelativeLayout contenedor = (RelativeLayout) customActionBarView.findViewById(R.id.contenedor);
        contenedor.setBackgroundColor(Color.parseColor(color));
        /*if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }*/
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("EDITAR PERFIL");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

            }
        });

        iconoDerecho.setVisibility(View.GONE);
        iconoDerecho.setImageResource(R.drawable.lapizcomenta);
        iconoDerecho.setColorFilter(Color.parseColor("#FFFFFF"));
        /*iconoDerecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Ir", "a nuevo comentario");
                Intent itn = new Intent(activity, Galerias_Comentar.class);
                itn.putExtra("id", id);
                itn.putExtra("titulo", titulo);
                itn.putExtra("url", url);
                startActivity(itn);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });*/

        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }


 /** Cambiar la foto **/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_null, R.anim.slide_right);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("¿Qué desea hacer?");
        menu.add(0, v.getId(), 0, "Tomar foto");
        menu.add(0, v.getId(), 0, "Elegir foto existente");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Tomar foto") {
            Uri outputFileUri;
            Date now = new Date();
            long nowLong = now.getTime();
            Ruta_imagen = Environment.getExternalStorageDirectory() + "/" + activity.getString(R.string.app_name) + "/picture_" + nowLong + ".jpg";
            File photo = new File(Ruta_imagen);
            photo.getParentFile().mkdirs();
            outputFileUri = Uri.fromFile(photo);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, TAKE_PICTURE);
        } else if (item.getTitle() == "Elegir foto existente") {
            Crop.pickImage(this);
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        codex = requestCode;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            Uri selectedImage = Uri.fromFile(new File(Ruta_imagen));
            scanPhoto(Ruta_imagen);
            beginCrop(selectedImage);
        } else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        //Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        //new Crop(source).output(outputUri).asSquare().start(this);

        if (codex == TAKE_PICTURE) {
            try {

                File f = new File(source.getPath());
                ExifInterface exif = new ExifInterface(f.getPath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }

                Matrix mat = new Matrix();
                mat.postRotate(angle);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);

                imgx = bitmap;
                //Bitmap x = decodeSampledBitmapFromUri(source, btnFoto.getWidth(), btnFoto.getHeight());
                btnFoto.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Bitmap x = decodeSampledBitmapFromUri(source, btnFoto.getWidth(), btnFoto.getHeight());
            imgx = x;
            btnFoto.setImageBitmap(x);
        }

    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            ESTATUS_CAMBIO_FOTO = true;
            Uri uri = Crop.getOutput(result);
            Bitmap x = decodeSampledBitmapFromUri(uri, btnFoto.getWidth(), btnFoto.getHeight());
            btnFoto.setImageBitmap(x);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void scanPhoto(String imageFileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imageFileName);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {

        Bitmap bm = null;

        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    /** Validacion de campos **/
    private void attemptLogin() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        txtCorreo.setError(null);
        txtTelefono.setError(null);
        txtNombre.setError(null);

        // Store values at the time of the login attempt.
        String nombre = txtNombre.getText().toString();
        String email = txtCorreo.getText().toString();
        String telefono = txtTelefono.getText().toString();

        Log.i("datos","telefono:"+telefono+"|correo"+email+"|nombre:"+nombre+"|");
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(telefono)) {
            txtTelefono.setError(getString(R.string.error_field_required));
            focusView = txtTelefono;
            cancel = true;
        }


        if (TextUtils.isEmpty(nombre)) {
            txtNombre.setError(getString(R.string.error_field_required));
            focusView = txtNombre;
            cancel = true;
        }


        if (TextUtils.isEmpty(email)) {
            txtCorreo.setError(getString(R.string.error_field_required));
            focusView = txtCorreo;
            cancel = true;
        }
        else if (!isEmailValid(email)) {
            txtCorreo.setError(getString(R.string.error_invalid_email));
            focusView = txtCorreo;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);

            Log.e("Todo ok", "ok");
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }


}
