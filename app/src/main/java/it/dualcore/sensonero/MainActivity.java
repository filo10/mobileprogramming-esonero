package it.dualcore.sensonero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.design.widget.Snackbar;


import it.dualcore.sensonero.activities.ShowSensorsActivity;
import it.dualcore.sensonero.activities.TrySensorsActivity;
import it.dualcore.sensonero.activities.UseCasesActivity;

 /**************************************************************************************************
 *
 *  UNIVERSITà DEGLI STUDI DI ROMA TOR VERGATA - A.A. 2018/2019
 *
 *  PROGETTO PER LA PROVA DI ESONERO
 *  MOBILE PROGRAMMING
 *
 *  ARGOMENTO: SENSORI
 *
 *  GRUPPO: DUAL CORE
 *  COMPONENTI: BRISCESE FILIPPO MARIA
 *              ILARDI DAVIDE
 *
 **************************************************************************************************/

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button btn_showSensors, btn_trySensors, btn_useCases, btn_info;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_showSensors = findViewById(R.id.btn_showSensors);
        btn_showSensors.setOnClickListener(this);
        btn_trySensors = findViewById(R.id.btn_trySensors);
        btn_trySensors.setOnClickListener(this);
        btn_useCases = findViewById(R.id.btn_useCases);
        btn_useCases.setOnClickListener(this);
        btn_info = findViewById(R.id.btn_main_info);
        btn_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.btn_showSensors){
            Intent i = new Intent(this, ShowSensorsActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_trySensors) {
            Intent i = new Intent(this, TrySensorsActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_useCases) {
            Intent i = new Intent(this, UseCasesActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_main_info){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.about_app);
            dialog.setMessage(R.string.about_app_text);
            dialog.setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener(){
                @Override public void onClick(DialogInterface dialog, int which){} });
            dialog.create().show();
        }
    }
}
