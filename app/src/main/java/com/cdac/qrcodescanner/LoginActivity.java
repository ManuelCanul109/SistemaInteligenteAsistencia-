package com.cdac.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegister;

    public static final String URL_LOGIN = "https://taximanfcp.com/sadcum/services/" + "login.php";
    EditText editTextEmail, editTextPassword;
    SharedPreferences sharedPreferences;

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String NOMBRE_ALUMNO = "nombre_alumno";
    public static final String EMAIL_ALUMNO = "email_alumno";
    public static final String MESSAGE = "message";
    public static final String ID = "id_alumno";
    public static final String STATUS = "status";
    private boolean status;
    public String tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        status = sharedPreferences.getBoolean(STATUS,false);

        if (status){

                finish();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

        }


    }

    public void iniciarSesionPapu(View view){
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        if(email.isEmpty()|| password.isEmpty()){
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        }

        else {
            class Login extends AsyncTask<Void, Void, String> {
                ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tInician Sesión...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);

                    //returing the response
                    return requestHandler.sendPostRequest(URL_LOGIN, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdLoading.dismiss();

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {

                            String id = obj.getString(ID);
                            String nombre_alumno = obj.getString(NOMBRE_ALUMNO);
                            String email_alumno = obj.getString(EMAIL_ALUMNO);
                            String message = obj.getString("message");


                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(ID, id);
                                editor.putString(NOMBRE_ALUMNO, nombre_alumno);
                                editor.putString(EMAIL_ALUMNO, email_alumno);
                                editor.putBoolean(STATUS, true);
                                editor.apply();

                                finish();
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);

                        }else{
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
        }
}
