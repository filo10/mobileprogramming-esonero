package it.dualcore.sensonero.activities.use_cases.compass;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import it.dualcore.sensonero.R;


/***************************************************************************************************
*   credits for this compass app to: https://github.com/iutinvg/
*   code partially modified by Filippo Maria Briscese
***************************************************************************************************/

public class CompassActivity extends AppCompatActivity {

    private Compass compass;
    private ImageView arrowView;
    private TextView tv_azimuth;

    private float currentAzimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_12compass);

        /* give the user some suggestions */
        Button btn_info_compass = findViewById(R.id.btn_info_compass);
        btn_info_compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CompassActivity.this, R.style.MyAlertDialogStyle);
                dialog.setTitle(R.string.str_compass);
                dialog.setMessage(R.string.str_compass_calibration);
                dialog.setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener(){
                    @Override public void onClick(DialogInterface dialog, int which){} });
                dialog.create().show();
            }
        });

        tv_azimuth = findViewById(R.id.tv_azimuth);
        arrowView = findViewById(R.id.img_hands);

        setupCompass();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* listen again to sensors */
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* stop listening to sensors */
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* listen again to sensors */
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /* stop listening to sensors */
        compass.stop();
    }

    private void setupCompass() {
        compass = new Compass(this);
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
    }

    private void adjustArrow(float azimuth) {
        /* the animation of the rotating hand of the compass */
        tv_azimuth.setText(String.format(Locale.getDefault(), "%d °", (int)azimuth));
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    private Compass.CompassListener getCompassListener() {
        /* run the animation on a different thread */
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
                /* UI updates only in UI thread
                https://stackoverflow.com/q/11140285/444966 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustArrow(azimuth);
                    }
                });
            }
        };
    }
}
