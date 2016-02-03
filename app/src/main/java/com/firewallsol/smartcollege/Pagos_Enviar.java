package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.soundcloud.android.crop.Crop;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Pagos_Enviar extends AppCompatActivity {
    private Activity activity;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;


    public static String Ruta_imagen;
    public static Bitmap imgx = null;
    public static Boolean ESTATUS_CAMBIO_FOTO;
    public static ImageView btnFoto;
    public static MaterialRippleLayout btnFoto2;
    private static int TAKE_PICTURE = 1;
    AlertDialog.Builder alert;
    int codex;
    Button btnEnviar;
    EditText titulo, texto;
    String txtTitulo, txtTexto;
    private LayoutInflater inflater;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_enviar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Map<String, ?> editor = sharedpreferences.getAll();
        color = (String) editor.get("color");

        CustomActionBar();

        btnFoto = (ImageView)findViewById(R.id.foto);


        alert = new AlertDialog.Builder(Pagos_Enviar.this);

        dialog = new ProgressDialog(activity);
        dialog.setMessage("Enviando...");
        dialog.setCancelable(false);

        Uri outputFileUri;
        Date now = new Date();
        long nowLong = now.getTime();
        Ruta_imagen = Environment.getExternalStorageDirectory() + "/" + activity.getString(R.string.app_name) + "/picture_" + nowLong + ".jpg";

        findViewById(R.id.btnIrgaleria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(activity);
            }
        });
        findViewById(R.id.btnTomafoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });


        findViewById(R.id.btnBorrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgx = null;
                btnFoto.setImageResource(R.drawable.transparente);
            }
        });


    }




    private void CustomActionBar() {

        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Map<String, ?> editor = sharedpreferences.getAll();
        color = (String) editor.get("color");

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

        MaterialRippleLayout btn_right = (MaterialRippleLayout) customActionBarView.findViewById(R.id.btn_right);

        RelativeLayout contenedor = (RelativeLayout) customActionBarView.findViewById(R.id.contenedor);
        contenedor.setBackgroundColor(Color.parseColor(color));
        /*if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }*/
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("VISTA PREVIA");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

            }
        });


        iconoDerecho.setVisibility(View.GONE);
        iconoDerecho.setImageResource(R.drawable.comunidad);

        btn_right.removeAllViews();
        Button btn = new Button(activity);
        btn.setText("Enviar");
        btn.setTextColor(getResources().getColor(R.color.color_blanco));
        btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btn_right.addView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (imgx != null){
                                                new escribeGaceta().execute();
                                       } else {
                                           Toast.makeText(getApplicationContext(),"Elija una foto del comprobante",Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               }
        );


        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

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

    public void resultado(String data) {
        Log.i("resultado", data);

        if (data.contains("0")) {
            alert.setTitle("Aviso");
            alert.setMessage("Error al enviar la información");
            alert.setIcon(android.R.drawable.stat_notify_error);
            alert.setPositiveButton("OK", null);
            alert.show();

        } else if (data.contains("1")) {
            alert.setTitle("Aviso");
            alert.setIcon(android.R.drawable.stat_sys_upload_done);
            alert.setMessage("Enviado");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                }
            });
            alert.show();



        } else {

        }


    }

    class escribeGaceta extends AsyncTask<Void, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.subeComprobantePago);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonRead = "";
            try {
                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_pago", Pagos.idPago));
                paramsSend.add(new BasicNameValuePair("id_alumno", MainActivity.alumno));
                paramsSend.add(new BasicNameValuePair("opsys", "android"));

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imgx.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                byte[] byte_arr = stream.toByteArray();
                String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                Log.e("pago", Pagos.idPago);
                Log.i("Foto", image_str);
                paramsSend.add(new BasicNameValuePair("foto", image_str));



                jSONFunciones json = new jSONFunciones();
                jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonRead;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            resultado(aVoid);
        }
    }


}
