package com.cdac.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

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

import com.cdac.qrcodescanner.Modelos.Evento;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConfirmEventActivity extends AppCompatActivity {
    public static final String URL_ASISTENCIA = "https://taximanfcp.com/sadcum/services/" + "evento.php";
    TextView txtRecibeMensaje;

    Button btnMenuPrincipal;

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";

    String id_evento, id_alumno;

    ImageView imagenCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_event);
        new IntentIntegrator(this).initiateScan();
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        txtRecibeMensaje = (TextView)findViewById(R.id.txtRecibeMensaje);
        imagenCheck = (ImageView)findViewById(R.id.imgCheck);
/*
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            Evento evento = bundle.getParcelable("EVENTO");

            id_evento = ""+evento.getId_evento();

        }
*/
        id_alumno = sharedPreferences.getString("id_alumno","");

        btnMenuPrincipal = (Button)findViewById(R.id.btnConfirmar);
        btnMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmEventActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelado",Toast.LENGTH_LONG).show();
            } else {
               // txtRecibeMensaje.setText(result.getContents());
                String string = result.getContents();
                String[] parts = string.split("_");
                String tias = parts[0]; // 123
                String fechaas = parts[1];

                enviarEvento(tias,id_alumno);
               // Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(HomeActivity.this, ConfirmClassActivity.class);
                intent.putExtra("codeqr", result.getContents());
                startActivity(intent);*/

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void enviarEvento(final String tias,final String id_alumno) {
        class Login extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(ConfirmEventActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //this method will be running on UI thread
                pdLoading.setMessage("\tProcesando Evento...");
                pdLoading.setCancelable(false);
                pdLoading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id_evento", tias);
                params.put("id_alumno", id_alumno);

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
                    Toast.makeText(ConfirmEventActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                }
            }



        }
        Login login = new Login();
        login.execute();
    }
}
