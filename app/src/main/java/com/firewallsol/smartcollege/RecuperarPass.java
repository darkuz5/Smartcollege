package com.firewallsol.smartcollege;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firewallsol.smartcollege.Funciones.jSONFunciones;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecuperarPass extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    public static InputMethodManager inputManager;
    public static Activity activity;
    private View mProgressView;
    private View mLoginFormView;
    private UserRecuperaPass mAuthTask = null;
    android.app.AlertDialog.Builder alert;
    Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);



    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);

        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRecuperaPass(email);
            mAuthTask.execute((Void) null);
        }
    }


    public class UserRecuperaPass extends AsyncTask<Void, Void, String> {

        private final String mEmail;

        UserRecuperaPass(String email) {
            mEmail = email;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("nombre_usuario", mEmail.trim()));
            jSONFunciones jSONFunciones = new jSONFunciones();
            String url = getString(R.string.recuperarPassword);
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
                if (jsonObject.has("resultado")) {
                    success = true;
                    JSONArray array = jsonObject.getJSONArray("resultado");
                    llenatutor(array);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {

            } else {
                showProgress(false);
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        protected void llenatutor(JSONArray array) throws JSONException {
            String srr = array.toString();
            Log.i("arr", srr);
            if (srr.contains("0")){
                mEmailView.setText("");
                mEmailView.setError("Error, Intente nuevamente");
                mEmailView.requestFocus();
                showProgress(false);
            } else if (srr.contains("1")){
                mEmailView.clearFocus();
                mEmailSignInButton.requestFocus();
                Toast.makeText(getApplicationContext(),"Un correo fue enviado con los datos para acceder a su cuenta", Toast.LENGTH_LONG).show();
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
            } else if (srr.contains("2")){
                mEmailView.setText("");
                mEmailView.setError("Usuario no encontrado");
                mEmailView.requestFocus();
                showProgress(false);
            } else {
                mEmailView.setText("");
                mEmailView.setError("Error desconocido");
                mEmailView.requestFocus();
                showProgress(false);
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
