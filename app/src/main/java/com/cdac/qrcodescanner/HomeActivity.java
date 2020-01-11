package com.cdac.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeActivity extends AppCompatActivity {

    CardView cardViewCodigo,cardViewClases,cardViewEventos;

    ImageView imgProfile;

    TextView texto_de_panel_de_control;

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);


        cardViewCodigo = (CardView) findViewById(R.id.cardViewCodigo);
        cardViewCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LANZAR EL LECTO DEL CODIGO
                //startActivity(new Intent(v.getContext(),ScannerBarcodeActivity.class));
                startQRScanner();
            }
        });

        cardViewClases = (CardView) findViewById(R.id.cardViewClases);
        cardViewClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), ClasesActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        cardViewEventos = (CardView) findViewById(R.id.cardViewEvento);
        cardViewEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), EventosActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LANZAR EL PERFIL
                startActivity(new Intent(v.getContext(),ProfileActivity.class));
            }
        });



        texto_de_panel_de_control = (TextView)findViewById(R.id.texto_de_panel_de_control);
        String nombre_alumno = sharedPreferences.getString("nombre_alumno","");
        String string = nombre_alumno;
        String[] parts = string.split(" ");
        String tias = parts[0]; // 123
        texto_de_panel_de_control.setText("Panel de " + tias);
    }

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();

        int requestCode = 2;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelado",Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, ConfirmClassActivity.class);
                intent.putExtra("codeqr", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
