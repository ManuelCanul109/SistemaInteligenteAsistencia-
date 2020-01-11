package com.cdac.qrcodescanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";

    TextView nameProfile,emailProfile, randomEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String nombre_alumno = sharedPreferences.getString("nombre_alumno","");
        String email_alumno = sharedPreferences.getString("email_alumno","");

        final int random = new Random().nextInt(2) + 10;

        nameProfile = (TextView)findViewById(R.id.nameProfile);
        nameProfile.setText(nombre_alumno);

        emailProfile = (TextView)findViewById(R.id.emailProfile);
        emailProfile.setText(email_alumno);

        randomEvent = (TextView)findViewById(R.id.randomEvent);
        randomEvent.setText("" + random);
    }

    public void cerrarSesion(View view) {
        showDialog(ProfileActivity.this,"¿Realmente desea cerrar sesión?","Recuerde que tendra que iniciar sesión la proxima vez :)");
    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(Html.fromHtml("<font color='#FF0000'>" + title + "</font>"));
        builder.setMessage(Html.fromHtml("<font color='#222222'>" + message + "</font>"));
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
