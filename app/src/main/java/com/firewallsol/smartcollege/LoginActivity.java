package com.firewallsol.smartcollege;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firewallsol.smartcollege.Database.Database;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    public static Database db_sqlite;
    public static InputMethodManager inputManager;
    public static Activity activity;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        db_sqlite = new Database(activity);
        SQLiteDatabase db = db_sqlite.getWritableDatabase();


        findViewById(R.id.lost_password).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), RecuperarPass.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            }
        });


        Cursor config = db.rawQuery("select * from tutor", null);
        if (config.moveToFirst()) {
            Intent it = new Intent(getApplicationContext(), SeleccionAlumno.class);
            startActivity(it);
            finish();
            activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

        }


        if (db != null) {
            db.close();
        }


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        String id_escuela = "";
        String id_tutor = "";

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("usuario", mEmail.trim()));
            paramsSend.add(new BasicNameValuePair("pass", mPassword.trim()));
            jSONFunciones jSONFunciones = new jSONFunciones();
            String url = getString(R.string.autenticaUsuario);
            jSONFunciones json = new jSONFunciones();
            String jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
            return jsonRead;
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String jsonRead) {
            mAuthTask = null;
            Boolean success = false;

            Log.e("json", jsonRead);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonRead);
                if (jsonObject.has("error")) {
                    //Tiene Error
                    success = false;
                }
                if (jsonObject.has("tutor")) {
                    success = true;
                    JSONArray array = jsonObject.getJSONArray("tutor");
                    llenatutor(array);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                new getColegiosTask(id_escuela, id_tutor).execute();
            } else {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        protected void llenatutor(JSONArray array) throws JSONException {
            SQLiteDatabase db = db_sqlite.getWritableDatabase();
            db.execSQL("delete from tutor");
            for (int i = 0; i < array.length(); i++) {
                JSONObject c = array.getJSONObject(i);
                db.execSQL("insert into tutor values ('" + c.getString("id") + "','" + c.getString("nombre") + "', '" + c.getString("email") + "', " +
                        "'" + c.getString("usuario") + "', '" + c.getString("id_escuela") + "', " +
                        "'" + c.getString("telefono") + "' ,'" + c.getString("foto") + "' ,'"+mPassword+"')");

                id_escuela = c.getString("id_escuela");
                id_tutor = c.getString("id");
            }
            db.close();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class getColegiosTask extends AsyncTask<Void, Void, String> {

        private final String escuela;
        String tutor;

        getColegiosTask(String id_escuela, String id_tutor) {
            escuela = id_escuela;
            tutor = id_tutor;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("id_escuela", escuela.trim()));
            jSONFunciones jSONFunciones = new jSONFunciones();
            String url = getString(R.string.getColegio);
            jSONFunciones json = new jSONFunciones();
            String jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
            return jsonRead;
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String jsonRead) {
            mAuthTask = null;

            Boolean success = false;

            Log.e("json", jsonRead);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonRead);
                if (jsonObject.has("error")) {
                    //Tiene Error
                    success = false;
                }
                if (jsonObject.has("colegio")) {
                    success = true;
                    JSONArray array = jsonObject.getJSONArray("colegio");
                    llenacole(jsonObject);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                new getHijosTask(escuela, tutor).execute();
            } else {

                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        protected void llenacole(JSONObject jsonobj) throws JSONException {
            JSONArray array = jsonobj.getJSONArray("colegio");
            SQLiteDatabase db = db_sqlite.getWritableDatabase();
            db.execSQL("delete from colegio");
            for (int i = 0; i < array.length(); i++) {
                JSONObject c = array.getJSONObject(i);
                String sqlQuery = "insert into colegio values ('" + c.getString("id") + "','" + c.getString("nombre") + "', '" + c.getString("contacto") + "', " +
                        "'" + c.getString("telefono") + "', '" + c.getString("descripcion") + "', " +
                        "'" + c.getString("color") + "', '" + c.getString("correo") + "', " +
                        "'" + c.getString("direccion") + "', '" + c.getString("sitioweb") + "', " +
                        "'" + c.getString("foto") + "')";
                Log.i("Constula", sqlQuery);
                db.execSQL(sqlQuery);
            }

            db.execSQL("delete from directorio");
            if (jsonobj.has("directorio")) {
                JSONArray directorio = jsonobj.getJSONArray("directorio");
                for (int i = 0; i < directorio.length(); i++) {
                    JSONObject c = directorio.getJSONObject(i);
                    db.execSQL("insert into directorio (nombre, puesto, contacto) values ('" + c.getString("nombre") + "', '" + c.getString("puesto") + "', '" + c.getString("contacto") + "')");
                }
            }

            db.close();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class getHijosTask extends AsyncTask<Void, Void, String> {

        private final String escuela;
        private final String tutor;

        getHijosTask(String id_escuela, String id_tutor) {
            escuela = id_escuela;
            tutor = id_tutor;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("id_escuela", escuela.trim()));
            paramsSend.add(new BasicNameValuePair("id_tutor", tutor.trim()));
            jSONFunciones jSONFunciones = new jSONFunciones();
            String url = getString(R.string.getAlumnosDeTutor);
            jSONFunciones json = new jSONFunciones();
            String jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
            return jsonRead;
            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(String jsonRead) {
            mAuthTask = null;

            Boolean success = false;

            Log.e("json", jsonRead);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonRead);
                if (jsonObject.has("error")) {
                    //Tiene Error
                    success = false;
                }
                if (jsonObject.has("tutor")) {
                    success = true;
                    llenaHijos(jsonObject);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                Intent it = new Intent(getApplicationContext(), SeleccionAlumno.class);
                startActivity(it);
                finish();
            } else {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();

            }
        }

        protected void llenaHijos(JSONObject jsonObject) throws JSONException {
            JSONArray array = jsonObject.getJSONArray("tutor");
            SQLiteDatabase db = db_sqlite.getWritableDatabase();
            db.execSQL("delete from hijos");
            for (int i = 0; i < array.length(); i++) {
                JSONObject c = array.getJSONObject(i);
                db.execSQL("insert into hijos values ('" + c.getString("id") + "','" + c.getString("nombre") + "', '" + c.getString("clave") + "', " +
                        "'" + c.getString("id_grado") + "', '" + c.getString("grado") + "', " +
                        "'" + c.getString("id_grupo") + "', '" + c.getString("grupo") + "', " +
                        "'" + c.getString("id_escuela") + "' ,'" + c.getString("referencia_bancaria") + "')");
            }
            db.close();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

