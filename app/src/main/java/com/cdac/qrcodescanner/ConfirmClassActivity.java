package com.cdac.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConfirmClassActivity extends AppCompatActivity {
    public static final String URL_ASISTENCIA = "https://taximanfcp.com/sadcum/services/" + "asistencia.php";
    TextView txtRecibeMensaje;

    Button btnMenuPrincipal;

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";

    ImageView imagenCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_class);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        // Estamos en OtraActivity
        Bundle datos = this.getIntent().getExtras();
        String recuperamos_variable_string = datos.getString("codeqr");

        String string = recuperamos_variable_string;
        String[] parts = string.split("_");
        String tias = parts[0]; // 123
        String fechaas = parts[1];
        String id_team = parts[2];// 654321
        String id_alumno = sharedPreferences.getString("id_alumno","");

        enviarAsistencia(tias,fechaas,id_team,id_alumno);

        txtRecibeMensaje = (TextView)findViewById(R.id.txtRecibeMensaje);

        imagenCheck = (ImageView)findViewById(R.id.imgCheck);


        btnMenuPrincipal = (Button)findViewById(R.id.btnConfirmar);
        btnMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmClassActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void enviarAsistencia(final String tipo_asistencia,final String fecha_asistencia,final String id_grupo,final String id_alumno){

            class Login extends AsyncTask<Void, Void, String> {
                ProgressDialog pdLoading = new ProgressDialog(ConfirmClassActivity.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //this method will be running on UI thread
                    pdLoading.setMessage("\tProcesando Asistencia...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("tipo_asistencia", tipo_asistencia);
                    params.put("fecha_asistencia", fecha_asistencia);
                    params.put("id_alumno", id_grupo);
                    params.put("id_grupo", id_alumno);

                    //returing the response
                    return requestHandler.sendPostRequest(URL_ASISTENCIA, params);
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

                            String message = obj.getString("message");
                            txtRecibeMensaje.setText(message);
                            imagenCheck.setVisibility(View.VISIBLE);

                        }else{
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ConfirmClassActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                    }
                }



        }
        Login login = new Login();
        login.execute();
    }
}
