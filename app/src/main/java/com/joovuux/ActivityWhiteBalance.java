package com.joovuux;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VerticalSeekBar;

import com.joovuux.circularseekbar.DoughterCircularSeekBar;
import com.joovuux.connection.Camera;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ua.net.lsoft.joovuux.R;

import com.joovuux.circularseekbar.CircularSeekBar;
import com.joovuux.settings.MainSettings;
import com.joovuux.settings.ModeSettings;


public class ActivityWhiteBalance extends Activity {

    public static final String WHITE_BALANCE_SPINNER = "wb";

    public static final String EXPOSURE = "Exposure";
    public static final String SHARPNESS = "Sharpness";
    public static final String CONTRAST = "Contrast";

    public static final String[] EXPOSURE_DATA = {"+2.0", "+1.7", "+1.3", "+1.0", "+0.7", "+0.3", "0.0", "-0.3", "-0.7", "-1.0", "-1.3", "-1.7", "-2.0"};
    public static final String[] SHARPNESS_DATA = {"SOFT", "STANDARD", "HARD"};
    public static final String[] CONTRAST_DATA = {"SOFT", "STANDARD", "HARD"};
    public static final String[] WHITE_BALANCE_SPINNER_DATA = {"AUTO",/* "INCANDESCENT", "D400", "D500",*/ "SUNNY", "CLOUDY"/*, "D900", "D1000", "FLASH", "FLUORESCENT_1"*/};


    VerticalSeekBar seekSharpness = null;
    VerticalSeekBar seekExposure = null;
    VerticalSeekBar seekContrast = null;
    private Spinner spinWhiteBalance;
    private boolean whiteBalanceTuched;

    ProgressDialog progDailog;
    private boolean seekPressed;
    private boolean cameraSettingsLoaded;
    private ProgressDialog resetDialog;
    private CircularSeekBar fatherSeekExposure;
    private SeekBar seekBarWBSharpness;
    private SeekBar seekBarContrast;
    private SeekBar seekBarWB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_white_balance);

        final Handler handler = new Handler();

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seekBarWB = (SeekBar) findViewById(R.id.seekBarNWB);

        fatherSeekExposure = (CircularSeekBar) findViewById(R.id.fatherSeekExposure);
        final DoughterCircularSeekBar doughterSeekExposure1 = (DoughterCircularSeekBar) findViewById(R.id.doughterSeekExposure1);
        final DoughterCircularSeekBar doughterSeekExposure2 = (DoughterCircularSeekBar) findViewById(R.id.doughterSeekExposure2);
        final DoughterCircularSeekBar doughterSeekExposure3 = (DoughterCircularSeekBar) findViewById(R.id.doughterSeekExposure3);
        final TextView tvExposure = (TextView) findViewById(R.id.tvExposure);
        fatherSeekExposure.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, int progress, boolean fromUser) {
                doughterSeekExposure1.setProgress(progress);
                doughterSeekExposure2.setProgress(progress);
                doughterSeekExposure3.setProgress(progress);
                tvExposure.setText(EXPOSURE_DATA[progress]);
            }
        });


        fatherSeekExposure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View seekBar, MotionEvent event) {

                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    seekPressed = true;
                }

                if (MotionEvent.ACTION_UP == event.getAction()) {
                    sendSettings(EXPOSURE_DATA[fatherSeekExposure.getProgress()], EXPOSURE);
                }
                return false;
            }
        });



        final TextView[] tvLinesSharpness = {
                (TextView) findViewById(R.id.tvLineSharpness0),
                (TextView) findViewById(R.id.tvLineSharpness1),
                (TextView) findViewById(R.id.tvLineSharpness2),};

        final TextView[] tvNumbersSharpness = {
                (TextView) findViewById(R.id.tvNumberSharpness0),
                (TextView) findViewById(R.id.tvNumberSharpness1),
                (TextView) findViewById(R.id.tvNumberSharpness2),};

        final CustomProgressBar progressSharpness = (CustomProgressBar) findViewById(R.id.progressSharpness);
        seekBarWBSharpness = (SeekBar) findViewById(R.id.seekBarWBSharpness);
        initProgressBar(tvLinesSharpness, tvNumbersSharpness, progressSharpness, seekBarWBSharpness, false);

        progressSharpness.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View seekBar, MotionEvent event) {
                Log.d("ACTION ", event.getAction() + "");
                if (MotionEvent.ACTION_UP == event.getAction()) {

//                    int progress = seekBarWBSharpness.getProgress();
                    Log.d("PROGRESS SS ",seekBarWBSharpness.getProgress()+"" );

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int progress = seekBarWBSharpness.getProgress();
                            if (progress >= 0 && progress < 33) {

                                        sendSettings(SHARPNESS_DATA[0], SHARPNESS);


                            } else if (progress >= 33 && progress < 66) {

                                        sendSettings(SHARPNESS_DATA[1], SHARPNESS);

                            } else if (progress >= 66 && progress <= 100) {

                                        sendSettings(SHARPNESS_DATA[2], SHARPNESS);

                            }
                        }
                    }, 10);


//                    if (progress >= 0 && progress < 33) {
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                sendSettings(SHARPNESS_DATA[0], SHARPNESS);
//                            }
//                        }, 200);
//
//                    } else if (progress >= 33 && progress < 66) {
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                sendSettings(SHARPNESS_DATA[1], SHARPNESS);
//                            }
//                        }, 200);
//                    } else if (progress >= 66 && progress <= 100) {
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                sendSettings(SHARPNESS_DATA[2], SHARPNESS);
//                            }
//                        }, 200);
//                    }
                }
                return false;
            }
        });

        final TextView[] tvLinesContrast = {
                (TextView) findViewById(R.id.tvLineContrast0),
                (TextView) findViewById(R.id.tvLineContrast1),
                (TextView) findViewById(R.id.tvLineContrast2),};

        final TextView[] tvNumbersContrast = {
                (TextView) findViewById(R.id.tvNumberContrast0),
                (TextView) findViewById(R.id.tvNumberContrast1),
                (TextView) findViewById(R.id.tvNumberContrast2),};

        final CustomProgressBar progressContrast = (CustomProgressBar) findViewById(R.id.progressContrast);
        seekBarContrast = (SeekBar) findViewById(R.id.seekBarContrast);
        initProgressBar(tvLinesContrast, tvNumbersContrast, progressContrast, seekBarContrast, false);

        progressContrast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View seekBar, MotionEvent event) {
                Log.d("ACTION", event.getAction() + " :: ");
                if (MotionEvent.ACTION_UP == event.getAction()) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int progress = seekBarContrast.getProgress();
                            if (progress >= 0 && progress < 33) {


                                        sendSettings(CONTRAST_DATA[0], CONTRAST);


                            } else if (progress >= 33 && progress < 66) {


                                        sendSettings(CONTRAST_DATA[1], CONTRAST);

                            } else if (progress >= 66 && progress <= 100) {


                                        sendSettings(CONTRAST_DATA[2], CONTRAST);

                            }
                        }
                    }, 10);



                }
                return false;
            }
        });




        final TextView[] tvLinesNWB = {
                (TextView) findViewById(R.id.tvLineNWB0),
                (TextView) findViewById(R.id.tvLineNWB1),
                (TextView) findViewById(R.id.tvLineNWB2),};

        final TextView[] tvNumbersNWB = {
                (TextView) findViewById(R.id.tvNumberNWB0),
                (TextView) findViewById(R.id.tvNumberNWB1),
                (TextView) findViewById(R.id.tvNumberNWB2),};

        final CustomProgressBar progressNWB = (CustomProgressBar) findViewById(R.id.progressNWB);
        final SeekBar seekBarNWB = (SeekBar) findViewById(R.id.seekBarNWB);
        initProgressBar(tvLinesNWB, tvNumbersNWB, progressNWB, seekBarNWB, false);

        progressNWB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View seekBar, MotionEvent event) {
                Log.d("ACTION", event.getAction() + " :: ");
                if (MotionEvent.ACTION_UP == event.getAction()) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int progress = seekBarNWB.getProgress();
                            if (progress >= 0 && progress < 33) {


                                sendSettings(WHITE_BALANCE_SPINNER_DATA[0], WHITE_BALANCE_SPINNER);

                            } else if (progress >= 33 && progress < 66) {


                                sendSettings(WHITE_BALANCE_SPINNER_DATA[1], WHITE_BALANCE_SPINNER);

                            } else if (progress >= 66 && progress <= 100) {


                                sendSettings(WHITE_BALANCE_SPINNER_DATA[2], WHITE_BALANCE_SPINNER);

                            }
                        }
                    }, 10);



                }
                return false;
            }
        });



//        final TextView[] tvLines = {
//                (TextView) findViewById(R.id.tvLineWB0),
//                (TextView) findViewById(R.id.tvLineWB1),
//                (TextView) findViewById(R.id.tvLineWB2),
//                (TextView) findViewById(R.id.tvLineWB3),
//                (TextView) findViewById(R.id.tvLineWB4),
//                (TextView) findViewById(R.id.tvLineWB5),
//                (TextView) findViewById(R.id.tvLineWB6),
//                (TextView) findViewById(R.id.tvLineWB7),
//                (TextView) findViewById(R.id.tvLineWB8),
//                (TextView) findViewById(R.id.tvLineWB9),};
//
//        final TextView[] tvNumbers = {
//                (TextView) findViewById(R.id.tvNumberWB0),
//                (TextView) findViewById(R.id.tvNumberWB1),
//                (TextView) findViewById(R.id.tvNumberWB2),
//                (TextView) findViewById(R.id.tvNumberWB3),
//                (TextView) findViewById(R.id.tvNumberWB4),
//                (TextView) findViewById(R.id.tvNumberWB5),
//                (TextView) findViewById(R.id.tvNumberWB6),
//                (TextView) findViewById(R.id.tvNumberWB7),
//                (TextView) findViewById(R.id.tvNumberWB8),
//                (TextView) findViewById(R.id.tvNumberWB9),};
//
//        final CustomProgressBar progressWB = (CustomProgressBar) findViewById(R.id.progressWB);
//        seekBarWB = (SeekBar) findViewById(R.id.seekBarWB);
//        initProgressBar(tvLines, tvNumbers, progressWB, seekBarWB, true);
//
//        progressWB.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(final View seekBar, MotionEvent event) {
//                Log.d("ACTION", event.getAction() + " :: ");
//                if (MotionEvent.ACTION_UP == event.getAction()) {
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            int progress = seekBarWB.getProgress();
//                            if (progress >= 0 && progress < 10) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[0], WHITE_BALANCE_SPINNER);
//
//
//                            } else if (progress >= 10 && progress < 20) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[1], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 20 && progress < 31) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[2], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 31 && progress < 41) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[3], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 41 && progress < 51) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[4], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 51 && progress < 61) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[5], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 61 && progress < 70) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[6], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 70 && progress < 81) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[7], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 81 && progress < 91) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[8], WHITE_BALANCE_SPINNER);
//
//                            } else if (progress >= 91 && progress <= 100) {
//
//                                        sendSettings(WHITE_BALANCE_SPINNER_DATA[9], WHITE_BALANCE_SPINNER);
//
//                            }
//                        }
//                    }, 10);
//
//
//
//
//                }
//                return false;
//            }
//        });




        findViewById(R.id.btnResetAllSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDialog = ProgressDialog.show(ActivityWhiteBalance.this, null, "reset all settings", true);

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Camera.sendSettings(CONTRAST_DATA[1], CONTRAST);
                        Camera.sendSettings(SHARPNESS_DATA[1], SHARPNESS);
                        Camera.sendSettings(EXPOSURE_DATA[6], EXPOSURE);
                        Camera.sendSettings(WHITE_BALANCE_SPINNER_DATA[0], WHITE_BALANCE_SPINNER);


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        resetDialog.dismiss();
                        setCurrentSettings(fatherSeekExposure, seekBarWBSharpness, seekBarContrast, seekBarWB);


                    }
                }.executeOnExecutor(Camera.getExecutorCameraCommands());
            }
        });


    }

    private void setCurrentSettings(CircularSeekBar fatherSeekExposure, SeekBar seekBarWBSharpness, SeekBar seekBarContrast, SeekBar seekBarWB) {
        progDailog = ProgressDialog.show(this, null, "loading...", true);
        setCurrentSeekSettings(EXPOSURE, fatherSeekExposure, EXPOSURE_DATA);
        setCurrentSeekSettings(SHARPNESS, seekBarWBSharpness, SHARPNESS_DATA);
        setCurrentSeekSettings(CONTRAST, seekBarContrast, CONTRAST_DATA);
        setCurrentSeekSettings(WHITE_BALANCE_SPINNER, seekBarWB, WHITE_BALANCE_SPINNER_DATA);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                Camera.send260();
                return getOptionPosition(WHITE_BALANCE_SPINNER_DATA, Camera.getCurrentOption(WHITE_BALANCE_SPINNER));
            }
            @Override
            protected void onPostExecute(Integer integer) {
                progDailog.dismiss();
                cameraSettingsLoaded = true;

                if(getSharedPreferences("MainSettings", Context.MODE_PRIVATE).getString(MainSettings.CONNECTION_LOG, "off").equalsIgnoreCase("on")){
                    ((MyApp)ActivityWhiteBalance.this.getApplication()).showLogDialog(ActivityWhiteBalance.this);
                }
            }
        }.executeOnExecutor(Camera.getExecutorCameraCommands());
    }

    private void initProgressBar(final TextView[] tvLines, final TextView[] tvNumbers, final CustomProgressBar progressWB, SeekBar seekBarWB, final boolean lagre) {
        progressWB.setSeekBar(seekBarWB);
        seekBarWB.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("PROGRESS", progress + "");

                if (lagre) {
                    if (progress >= 0 && progress < 10) {
                        selectTvs(tvLines, tvNumbers, 0);
                    } else if (progress >= 10 && progress < 20) {
                        selectTvs(tvLines, tvNumbers, 1);
                    } else if (progress >= 20 && progress < 31) {
                        selectTvs(tvLines, tvNumbers, 2);
                    } else if (progress >= 31 && progress < 41) {
                        selectTvs(tvLines, tvNumbers, 3);
                    } else if (progress >= 41 && progress < 51) {
                        selectTvs(tvLines, tvNumbers, 4);
                    } else if (progress >= 51 && progress < 61) {
                        selectTvs(tvLines, tvNumbers, 5);
                    } else if (progress >= 61 && progress < 70) {
                        selectTvs(tvLines, tvNumbers, 6);
                    } else if (progress >= 70 && progress < 81) {
                        selectTvs(tvLines, tvNumbers, 7);
                    } else if (progress >= 81 && progress < 91) {
                        selectTvs(tvLines, tvNumbers, 8);
                    } else if (progress >= 91 && progress <= 100) {
                        selectTvs(tvLines, tvNumbers, 9);
                    }
                } else {
                    if (progress >= 0 && progress < 33) {
                        selectTvs(tvLines, tvNumbers, 0);
                    } else if (progress >= 33 && progress < 66) {
                        selectTvs(tvLines, tvNumbers, 1);
                    } else if (progress >= 66 && progress <= 100) {
                        selectTvs(tvLines, tvNumbers, 2);
                    }
                }



                progressWB.setProgress(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void selectTvs(TextView[] tvLines, TextView[] tvNumbers, int position) {
        int greyColor = Color.parseColor("#999999");
        int blueColor = Color.parseColor("#48d8b7");

        for(int i = 0; i < tvLines.length; i++){
            if(i <= position){
                tvLines[i].setTextColor(blueColor);
            } else {
                tvLines[i].setTextColor(greyColor);
            }
        }

        for(int i = 0; i < tvNumbers.length; i++){
            if(i <= position){
                tvNumbers[i].setTextColor(blueColor);
            } else {
                tvNumbers[i].setTextColor(greyColor);
            }
        }
    }


    private void setCurrentSeekSettings(final String type, final View seekBar, final String[] data){

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return getOptionPosition(data, Camera.getCurrentOption(type));
            }
            @Override
            protected void onPostExecute(Integer integer) {

                Log.e("INTEGER", integer + "  " + type);

                if(seekBar instanceof CircularSeekBar){
                    ((CircularSeekBar)seekBar).setProgress(integer);
                } else {

//                    if(type == SHARPNESS || type == CONTRAST){
                        if (integer == 0) {
                            ((ProgressBar)seekBar).setProgress(16);
                        } else if (integer == 1) {
                            ((ProgressBar)seekBar).setProgress(50);
                        } else if (integer == 2) {
                            ((ProgressBar)seekBar).setProgress(83);
                        }
//                    } else {
//                        if (integer == 0) {
//                            ((ProgressBar)seekBar).setProgress(5);
//                        } else if (integer == 1) {
//                            ((ProgressBar)seekBar).setProgress(15);
//                        } else if (integer == 2) {
//                            ((ProgressBar)seekBar).setProgress(25);
//                        } else if (integer == 3) {
//                            ((ProgressBar)seekBar).setProgress(35);
//                        } else if (integer == 4) {
//                            ((ProgressBar)seekBar).setProgress(45);
//                        } else if (integer == 5) {
//                            ((ProgressBar)seekBar).setProgress(55);
//                        } else if (integer == 6) {
//                            ((ProgressBar)seekBar).setProgress(65);
//                        } else if (integer == 7) {
//                            ((ProgressBar)seekBar).setProgress(75);
//                        } else if (integer == 8) {
//                            ((ProgressBar)seekBar).setProgress(85);
//                        } else if (integer == 9) {
//                            ((ProgressBar)seekBar).setProgress(95);
//                        }
//                    }

                }

            }
        }.executeOnExecutor(Camera.getExecutorCameraCommands());



    }

    private int getOptionPosition(String[] data, String option){
        int result = 0;
        for(int i = 0; i < data.length; i++) {
            if(data[i].equalsIgnoreCase(option)){
                result = i;
                break;
            }
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        final MyApp myApp = ((MyApp)getApplication());
        myApp.connect(this);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (myApp.isConnecting()){
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setCurrentSettings(fatherSeekExposure, seekBarWBSharpness, seekBarContrast, seekBarWB);
            }
        }.execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        ActiveActivitiesTracker.activityStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActiveActivitiesTracker.activityStopped(this);
    }


    private void sendSettings(final String value, final String key) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return Camera.sendSettings(value, key);
            }

            @Override
            protected void onPostExecute(String result) {
                if(getSharedPreferences("MainSettings", Context.MODE_PRIVATE).getString(MainSettings.CONNECTION_LOG, "off").equalsIgnoreCase("on")){
                    Toast.makeText(ActivityWhiteBalance.this, result, Toast.LENGTH_SHORT).show();
                }
                final Toast toast = Toast.makeText(ActivityWhiteBalance.this, " Setting updated", Toast.LENGTH_SHORT);
                toast.show();

            }
        }.executeOnExecutor(Camera.getExecutorCameraCommands());
    }

}