package com.example.android.smart_home;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Home_Dynamic_Display extends AppCompatActivity {

    private static final String TAG = "bluetooth2";
    Handler h;
    final int RECIEVE_MESSAGE = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:21:13:04:84:66";
    LinearLayout water_heater_lay,water_motor_lay,ironbox_lay,bedroom_light_lay,outside_light_lay,bedroom_fan_lay,mixer_lay,human_detector_lay,nightmode_lay,washingmachine_lay;
    ScrollView appliances;
    DatabaseReference database_reference;
    ArrayList<String> device_status=new ArrayList<String >();
    ArrayList<String> ecomode_status=new ArrayList<String >();
    ArrayList<String> avg_time=new ArrayList<String >();
    CardView water_heater_card,iron_box_card,bedroom_light_card,human_detector_card,bedroom_fan_card,outside_light_card,washingmachine_card,watermotor_card,nightmode_card;
    TextView human_detector_present_absent,timeDiff;
    Dialog myDialog;
    public static ProgressDialog mprogress;
    String cmd;
    public OutputStream outputStream;
    Boolean water_heater_status=false,iron_box_status=false,outside_light_statys=false,bedroom_light_status=false,water_motor_status=false,bedroom_fan_status=false,washing_machine_status=false;
    Boolean water_heater_eco_status=false,iron_box_eco_status=false,outside_light_eco_statys=false,bedroom_light_eco_status=false,water_motor_eco_status=false,bedroom_fan_eco_status=false,washing_machine_eco_status=false;

    String passcode_pass;
    SQLiteDatabase db;
    Cursor c;
    LottieAnimationView animationView;
    AlertDialog.Builder alert;
    TextView enable;
    LottieAnimationView wifi;

    private static final long START_TIME_IN_MILLIS = 10000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private static final long START_TIME_IN_MILLIS1 = 10000;
    private TextView mTextViewCountDown1;
    private Button mButtonStartPause1;
    private Button mButtonReset1;
    private CountDownTimer mCountDownTimer1;
    private boolean mTimerRunning1;
    private long mTimeLeftInMillis1 = START_TIME_IN_MILLIS1;


    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    int t1hours, t1mins, t1day, t1secs, t2hours, t2mins, t2day, t2secs;
    int seconds;

    long avg_time_count;
    long avg_time_total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mprogress=new ProgressDialog(this);
        mprogress.setMessage("Initializing....");
        mprogress.show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__dynamic__display);





        enable=(TextView)findViewById(R.id.textView3);
        wifi=(LottieAnimationView)findViewById(R.id.wifi_image);
        if(am_i_connected())
        {
            enable.setText("Enabled");
            wifi.setVisibility(View.VISIBLE);

        }
        else{

            enable.setText("Disabled");
            wifi.setVisibility(View.INVISIBLE);

        }






        try {
            db = openOrCreateDatabase("REGISTRATION_STATUS", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS reg(sno VARCHAR,passcode VARCHAR);");
            c = db.rawQuery("SELECT * FROM reg", null);
            if (c.getCount() == 0) {

                Intent nxt=new Intent(Home_Dynamic_Display.this,Pairing_Screen.class);
                startActivity(nxt);

                //  showMessage("Oops", "No records");

                return;
            }
            else
            {
                String a="1";


                c = db.rawQuery("SELECT * FROM reg WHERE sno='" + a + "'", null);
                if (c.moveToFirst()) {
                    passcode_pass=c.getString(1);

                }


            }


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),
                    "Database Failure",
                    Toast.LENGTH_SHORT).show();
        }

        //passcode_pass=getIntent().getStringExtra("KEY");

        animationView = (LottieAnimationView) findViewById(R.id.wifi_image);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f)
                .setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationView.setProgress((Float) animation.getAnimatedValue());
            }
        });








        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();


//        final VideoView videoView = (VideoView)findViewById(R.id.bc_video);



        washingmachine_lay=(LinearLayout)findViewById(R.id.washing_machine_lay);
        water_heater_lay=(LinearLayout)findViewById(R.id.water_heater_lay);
        water_motor_lay=(LinearLayout)findViewById(R.id.water_motor_lay);
        ironbox_lay=(LinearLayout)findViewById(R.id.iron_box_lay);
        bedroom_light_lay=(LinearLayout)findViewById(R.id.beedroom_lay);
        outside_light_lay=(LinearLayout)findViewById(R.id.outside_light_lay);
        bedroom_fan_lay=(LinearLayout)findViewById(R.id.beedroom_fan_lay);
        human_detector_lay=(LinearLayout)findViewById(R.id.human_detector_lay);
        nightmode_lay=(LinearLayout)findViewById(R.id.nightmode_lay);

        appliances=(ScrollView)findViewById(R.id.aplliances);
        mTextViewCountDown = findViewById(R.id.timer);

        washingmachine_card=(CardView)findViewById(R.id.washing_machine);
        outside_light_card=(CardView)findViewById(R.id.outside_light);
        watermotor_card=(CardView)findViewById(R.id.water_motor);
        bedroom_fan_card=(CardView)findViewById(R.id.bedroom_fan);
        water_heater_card=(CardView)findViewById(R.id.water_heater);
        iron_box_card=(CardView)findViewById(R.id.iron_box);
        bedroom_light_card=(CardView)findViewById(R.id.bedroom_light);
        human_detector_card=(CardView)findViewById(R.id.human_detector);
        nightmode_card=(CardView)findViewById(R.id.nightmode);

        human_detector_present_absent=(TextView)findViewById(R.id.human_detector_present_absent);
        timeDiff=(TextView)findViewById(R.id.timeDiff);


        myDialog = new Dialog(this);


//
//        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.blacl);
//        videoView.setVideoURI(uri);
//        videoView.start();
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                videoView.start();
//            }
//        });



//        database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("IRON BOX AVG TIME");
//        database_reference.addValueEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren())
//                {
//                    String usrs=child.getValue(String.class);
//                    avg_time.clear();
//                    avg_time.add(usrs);
//                    avg_time_count=dataSnapshot.getChildrenCount();
//
//
//
//
//
//                }
////                for (int i=0;i<avg_time_count;i++)
////                {
////                    avg_time_total+=Integer.parseInt(avg_time.get(i));
////                }
////                mTimeLeftInMillis1=avg_time_total;
////                Toast.makeText(getApplicationContext(),""+avg_time_total,Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });






        database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);
                    device_status.clear();
                    device_status.add(usrs);

                    String[] check=usrs.split("_");

                    if (check[0].equals("Water Heater"))
                    {
                        if (check[1].equals("true"))
                        {


                            water_heater_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            water_heater_card.startAnimation(animation);

                            water_heater_status=true;
                            mConnectedThread.write("a");


                        }
                        else if (check[1].equals("false"))
                        {



                            water_heater_lay.setBackgroundColor(Color.parseColor("#ffffff"));
//                            water_heater_lay.setBackgroundResource(R.drawable.bc);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            water_heater_card.startAnimation(animation);

                            water_heater_status=false;
                            mConnectedThread.write("b");


                        }

                    }
                    else if (check[0].equals("Iron Box"))
                    {
                        if (check[1].equals("true"))
                        {

                            ironbox_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            iron_box_card.startAnimation(animation);

                            iron_box_status=true;
                            mConnectedThread.write("c");

                            startTimer_IronBox();
                            timeDiff.setText("");


                        }
                        else if (check[1].equals("false"))
                        {

                            ironbox_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            iron_box_card.startAnimation(animation);

                            iron_box_status=false;
                            mConnectedThread.write("d");

                            endTimer_IronBox();
                            diffTimer_IronBox();
                            upload_Time_Diff_In_Firebase();

                        }
                    }
                    else if (check[0].equals("Bedroom Light"))
                    {
                        if (check[1].equals("true"))
                        {

                            bedroom_light_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            bedroom_light_card.startAnimation(animation);

                            bedroom_light_status=true;
                            mConnectedThread.write("e");
                        }
                        else if (check[1].equals("false"))
                        {

                            bedroom_light_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            bedroom_light_card.startAnimation(animation);

                            bedroom_light_status=false;
                            mConnectedThread.write("f");
                        }
                    }
                   else if (check[0].equals("Bedroom Fan"))
                    {
                        if (check[1].equals("true"))
                        {

                            bedroom_fan_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            bedroom_fan_card.startAnimation(animation);

                            bedroom_fan_status=true;
                            mConnectedThread.write("g");
                        }
                        else if (check[1].equals("false"))
                        {

                            bedroom_fan_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            bedroom_fan_card.startAnimation(animation);

                            bedroom_fan_status=false;
                            mConnectedThread.write("h");
                        }
                    }
                    else if (check[0].equals("Washing Machine"))
                    {
                        if (check[1].equals("true"))
                        {

                            washingmachine_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            washingmachine_card.startAnimation(animation);

                            washing_machine_status=true;

                            mConnectedThread.write("i");

                        }
                        else if (check[1].equals("false"))
                        {

                            washingmachine_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            washingmachine_card.startAnimation(animation);

                            washing_machine_status=false;
                            mConnectedThread.write("j");
                        }
                    }
                    else if (check[0].equals("Water Motor"))
                    {
                        if (check[1].equals("true"))
                        {

                            water_motor_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            watermotor_card.startAnimation(animation);

                            water_motor_status=true;
                            mConnectedThread.write("k");
                        }
                        else if (check[1].equals("false"))
                        {

                            water_motor_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            watermotor_card.startAnimation(animation);

                            water_motor_status=false;
                            mConnectedThread.write("l");
                        }
                    }
                    else if (check[0].equals("Outside Light"))
                    {
                        if (check[1].equals("true"))
                        {

                            outside_light_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            outside_light_card.startAnimation(animation);

                            outside_light_statys=true;
                            mConnectedThread.write("m");
                        }
                        else if (check[1].equals("false"))
                        {

                            outside_light_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            outside_light_card.startAnimation(animation);

                            outside_light_statys=false;
                            mConnectedThread.write("n");
                        }
                    }
                    if(am_i_connected())
                    {
                        enable.setText("Enabled");
                        wifi.setVisibility(View.VISIBLE);

                    }
                    else{

                        enable.setText("Disabled");
                        wifi.setVisibility(View.INVISIBLE);

                    }





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("ECOMODE STATUS");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);
                    ecomode_status.clear();
                    ecomode_status.add(usrs);

                    String[] check=usrs.split("_");

                    if (check[0].equals("Water Heater"))
                    {
                        if (check[1].equals("true"))
                        {

                            if(check[2].equals("10000"))
                        {
                            mTimeLeftInMillis=10000;

                            if (mTimerRunning) {
                                pauseTimer();
                            } else {
                                startTimer();
                            }

                        } else if(check[2].equals("20000"))
                            {
                                mTimeLeftInMillis=20000;

                                if (mTimerRunning) {
                                   pauseTimer();
                                } else {
                                    startTimer();
                                }

                            }


                        }
                        else if (check[1].equals("false"))
                        {

                            mTextViewCountDown.setText("");

                        }
                    }
                    else if (check[0].equals("Iron Box"))
                    {
                        if (check[1].equals("true"))
                        {

                            iron_box_eco_status=true;
//                            if (mTimerRunning)
//                            {
//                                pauseTimer1();
//                            }
//                            else
//                            {
//                                startTimer1();
//                            }

                        }
                        else if (check[1].equals("false"))
                        {

                            iron_box_eco_status=false;
                        }
                    }
                    else if (check[0].equals("Bedroom Light"))
                    {
                        if (check[1].equals("true"))
                        {

                        }
                        else if (check[1].equals("false"))
                        {

                        }
                    }
                    else if (check[0].equals("Bedroom Fan"))
                    {
                        if (check[1].equals("true"))
                        {

                        }
                        else if (check[1].equals("false"))
                        {

                        }
                    }
                    else if (check[0].equals("Washing Machine"))
                    {
                        if (check[1].equals("true"))
                        {


                        }
                        else if (check[1].equals("false"))
                        {

                        }
                    }
                    else if (check[0].equals("Water Motor"))
                    {
                        if (check[1].equals("true"))
                        {

                        }
                        else if (check[1].equals("false"))
                        {

                        }
                    }
                    else if (check[0].equals("Outside Light"))
                    {
                        if (check[1].equals("true"))
                        {

                        }
                        else if (check[1].equals("false"))
                        {

                        }
                    }
                    if(am_i_connected())
                    {
                        enable.setText("Enabled");
                        wifi.setVisibility(View.VISIBLE);

                    }
                    else{

                        enable.setText("Disabled");
                        wifi.setVisibility(View.INVISIBLE);

                    }





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // and clear

                            //Toast.makeText(Home_Dynamic_Display.this,""+sbprint,Toast.LENGTH_SHORT).show();
                            if(sbprint.equals("0001"))
                            {
                                motion_detected();

                            }
                            else if (sbprint.equals("0000"))
                            {

                                no_human_detected();
                            }
                            else if(sbprint.equals("1111"))
                            {
                                darkness_detected();

                            }
                            else if (sbprint.equals("1110"))
                            {

                                no_darkness();
                            }
                            else if (sbprint.equals("1010"))
                            {

                                Toast.makeText(getApplicationContext(),
                                        "water tank empty",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else if (sbprint.equals("1011"))
                            {

                                Toast.makeText(getApplicationContext(),
                                        "water tank full",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


        mprogress.dismiss();

        water_heater_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"touched",Toast.LENGTH_SHORT).show();
                if(water_heater_status==false)
                {
                    mConnectedThread.write("a");
                    water_heater_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    water_heater_card.startAnimation(animation);

                    water_heater_status=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("WATER HEATER").setValue("Water Heater_true");


                }
                else if (water_heater_status==true)
                {
                    mConnectedThread.write("b");

                    water_heater_lay.setBackgroundColor(Color.parseColor("#ffffff"));
//                            water_heater_lay.setBackgroundResource(R.drawable.bc);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    water_heater_card.startAnimation(animation);

                    water_heater_status=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("WATER HEATER").setValue("Water Heater_false");

                }
            }
        });

        ironbox_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(iron_box_status==false)
                {
                    mConnectedThread.write("c");
                    ironbox_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    iron_box_card.startAnimation(animation);

                    iron_box_status=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("IRON BOX").setValue("Iron Box_true");
                    startTimer_IronBox();
                    timeDiff.setText("");


                }

                else if(iron_box_status==true)
                {
                    mConnectedThread.write("d");
                    ironbox_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    iron_box_card.startAnimation(animation);

                    iron_box_status=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("IRON BOX").setValue("Iron Box_false");
                    endTimer_IronBox();
                    diffTimer_IronBox();
                    upload_Time_Diff_In_Firebase();


                }
            }
        });

        outside_light_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(outside_light_statys==false)
                {
                    mConnectedThread.write("m");
                    outside_light_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    outside_light_card.startAnimation(animation);

                    outside_light_statys=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("OUTSIDE LIGHT").setValue("Outside Light_true");

                }

                else if(outside_light_statys==true)
                {
                    mConnectedThread.write("n");
                    outside_light_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    outside_light_card.startAnimation(animation);

                    outside_light_statys=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("OUTSIDE LIGHT").setValue("Outside Light_false");

                }
            }
        });

        bedroom_light_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bedroom_light_status==false)
                {
                    mConnectedThread.write("e");
                    bedroom_light_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    bedroom_light_card.startAnimation(animation);

                    bedroom_light_status=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("BEDROOM LIGHT").setValue("Bedroom Light_true");

                }

                else if(bedroom_light_status==true)
                {
                    mConnectedThread.write("f");
                    bedroom_light_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    bedroom_light_card.startAnimation(animation);

                    bedroom_light_status=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("BEDROOM LIGHT").setValue("Bedroom Light_false");

                }

            }
        });

        water_motor_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(water_motor_status==false)
                {

                    mConnectedThread.write("k");
                    water_motor_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    watermotor_card.startAnimation(animation);

                    water_motor_status=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("WATER MOTOR").setValue("Water Motor_true");
                }

                else if(water_motor_status==true)
                {

                    mConnectedThread.write("l");
                    water_motor_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    watermotor_card.startAnimation(animation);

                    water_motor_status=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("WATER MOTOR").setValue("Water Motor_false");
                }
            }
        });

        bedroom_fan_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bedroom_fan_status==false)
                {
                    mConnectedThread.write("g");
                    bedroom_fan_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    bedroom_fan_card.startAnimation(animation);

                    bedroom_fan_status=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("BEDROOM FAN").setValue("Bedroom Fan_true");

                }

                else if(bedroom_fan_status==true)
                {
                    mConnectedThread.write("h");
                    bedroom_fan_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    bedroom_fan_card.startAnimation(animation);

                    bedroom_fan_status=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("BEDROOM FAN").setValue("Bedroom Fan_false");

                }
            }
        });
        washingmachine_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(washing_machine_status==false)
                {

                    mConnectedThread.write("i");
                    washingmachine_lay.setBackgroundColor(Color.parseColor("#ef4343"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                    washingmachine_card.startAnimation(animation);

                    washing_machine_status=true;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("WASHING MACHINE").setValue("Washing Machine_true");
                }

                else if(washing_machine_status==true)
                {
                    mConnectedThread.write("j");
                    washingmachine_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                    washingmachine_card.startAnimation(animation);

                    washing_machine_status=false;
                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("DEVICE STATUS");
                    database_reference.child("WASHING MACHINE").setValue("Washing Machine_false");

                }
            }
        });

        //TIMER



    }// **********************// ON CREATE *************************************************************************
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
               // mButtonStartPause.setText("Start");
//                mButtonStartPause.setVisibility(View.INVISIBLE);
//                mButtonReset.setVisibility(View.VISIBLE);
                database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("NOTIFICATION");
                database_reference.child("WATER HEATER").setValue("Water Heater_true");
                /////
            }
        }.start();

        mTimerRunning = true;
//        mButtonStartPause.setText("pause");
//        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
//        mButtonStartPause.setText("Start");
//        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
//        mButtonReset.setVisibility(View.INVISIBLE);
//        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText("Timer : "+timeLeftFormatted);
    }

    //*************************************************for ironbox****************************88888

    //timer code ends

    //timer code ends


    private void no_darkness() {
        nightmode_lay.setBackgroundColor(Color.parseColor("#ffffff"));
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
       nightmode_card.startAnimation(animation);



    }

    private void darkness_detected() {
        nightmode_lay.setBackgroundColor(Color.parseColor("#ef4343"));
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        nightmode_card.startAnimation(animation);



    }

    public void ShowPopup(View v) {

        invisible();
        Button btnok,btnlogout;
        myDialog.setContentView(R.layout.custom_popup);
        btnok = (Button) myDialog.findViewById(R.id.btnok);
        btnlogout = (Button) myDialog.findViewById(R.id.btnlogout);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                visible();
            }
        });
        //myDialog.dismiss();
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home_Dynamic_Display.this,"Logged Out",Toast.LENGTH_SHORT).show();
                db = openOrCreateDatabase("REGISTRATION_STATUS", Context.MODE_PRIVATE, null);

                String sno="1";
                c = db.rawQuery("SELECT * FROM reg WHERE sno='" + sno + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM reg WHERE sno='" + sno + "'");


                    database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("USER DETAILS");
                    database_reference.child("PAIR STATUS").setValue("false");

                    Intent nxt=new Intent(Home_Dynamic_Display.this,Pairing_Screen.class);
                    startActivity(nxt);
                }

            }
        });
    }

    private void no_human_detected()
    {
        human_detector_lay.setBackgroundColor(Color.parseColor("#ffffff"));
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
        human_detector_card.startAnimation(animation);
        human_detector_present_absent.setText("A B S E N T");
    }

    private void motion_detected()
    {
        human_detector_lay.setBackgroundColor(Color.parseColor("#ef4343"));
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        human_detector_card.startAnimation(animation);
        human_detector_present_absent.setText("P R E S E N T");

    }


    //Bluetooth code ends here*******************************************************************************

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");


        BluetoothDevice device = btAdapter.getRemoteDevice(address);


        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
            Toast.makeText(Home_Dynamic_Display.this,"CONNECTED",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

//    public void zoom(View view)
//    {
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
//        water_heater_card.startAnimation(animation);
//       iron_box_card.startAnimation(animation);
//        bedroom_light_card.startAnimation(animation);
//        mConnectedThread.write("1");
//                water_heater_state="on";
//        waterheater_on_off.setText("O N");
//    }

    public void zoomout(View view)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
        water_heater_card.startAnimation(animation);
        iron_box_card.startAnimation(animation);
        bedroom_light_card.startAnimation(animation);
    }

    public void waterheater_on_off(View view) {

        if(water_heater_status==false)
        {
            mConnectedThread.write("a");
            water_heater_lay.setBackgroundColor(Color.parseColor("#ef4343"));
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
            water_heater_card.startAnimation(animation);

            water_heater_status=true;


        }
        else
        {
            mConnectedThread.write("b");

            water_heater_lay.setBackgroundColor(Color.parseColor("#ffffff"));
//                            water_heater_lay.setBackgroundResource(R.drawable.bc);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
            water_heater_card.startAnimation(animation);

            water_heater_status=false;

        }
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {

            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }


//Bluetooth code ends here*******************************************************************************





void invisible()
{
    water_heater_card.setVisibility(View.INVISIBLE);
    watermotor_card.setVisibility(View.INVISIBLE);
    iron_box_card.setVisibility(View.INVISIBLE);
    bedroom_light_card.setVisibility(View.INVISIBLE);
    outside_light_card.setVisibility(View.INVISIBLE);
    bedroom_fan_card.setVisibility(View.INVISIBLE);
   // mixer_lay.setVisibility(View.INVISIBLE);
    human_detector_card.setVisibility(View.INVISIBLE);
    nightmode_card.setVisibility(View.INVISIBLE);
    washingmachine_card.setVisibility(View.INVISIBLE);

}
    void visible()
    {
        water_heater_card.setVisibility(View.VISIBLE);
        watermotor_card.setVisibility(View.VISIBLE);
        iron_box_card.setVisibility(View.VISIBLE);
        bedroom_light_card.setVisibility(View.VISIBLE);
        outside_light_card.setVisibility(View.VISIBLE);
        bedroom_fan_card.setVisibility(View.VISIBLE);
        // mixer_lay.setVisibility(View.INVISIBLE);
        human_detector_card.setVisibility(View.VISIBLE);
        nightmode_card.setVisibility(View.VISIBLE);
        washingmachine_card.setVisibility(View.VISIBLE);

    }

//public void switching_function()
//{
//    if(status.equals("true"))
//    {
//        mConnectedThread.write("1");
//        water_heater_state="on";
//        waterheater_on_off.setText("O N");
//        water_heater_lay.setBackgroundColor(Color.parseColor("#FFE0B2"));
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
//        water_heater_card.startAnimation(animation);
//    }
//    else if(status.equals("false"))
//    {
//        mConnectedThread.write("0");
//        water_heater_state="off";
//        waterheater_on_off.setText("O F F");
//        water_heater_lay.setBackgroundColor(Color.parseColor("#ffffff"));
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
//        water_heater_card.startAnimation(animation);
//    }
//
//}

    public boolean am_i_connected()
    {
        ConnectivityManager con=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=con.getActiveNetworkInfo();

        return info!=null&&info.isConnected();
    }


    public void startTimer_IronBox()
    {

        Calendar start = Calendar.getInstance();
        t1hours  = start.get(Calendar.HOUR_OF_DAY);
        t1mins = start.get(Calendar.MINUTE);
        t1day = start.get(Calendar.DAY_OF_WEEK);
        t1secs = start.get(Calendar.SECOND);
    }
    public void endTimer_IronBox()
    {
        Calendar stop = Calendar.getInstance();
        t2hours = stop.get(Calendar.HOUR_OF_DAY);
        t2mins = stop.get(Calendar.MINUTE);
        t2day = stop.get(Calendar.DAY_OF_WEEK);
        t2secs = stop.get(Calendar.SECOND);

    }
    public void diffTimer_IronBox()
    {
        int hours, minutes;

        // same day
        if(t2day-t1day==0) {
            hours = t2hours - t1hours;
            minutes = t2mins - t1mins;
            seconds = t2secs - t1secs;
            if (minutes < 0) {
                minutes += 60;
                hours -= 1;

            }
            if (seconds<0) {
                seconds += 60;
                minutes -= 1;
            }
           timeDiff.setText("Used Duration "+seconds+ " seconds" );
        }
        // different days
        else {
            // t2day-t1day not equals 0
            int n = ((t2day-t1day) + 7)%7;
            hours = ((t2hours - t1hours) + 24)%24;
            minutes = t2mins - t1mins;
            seconds = t2secs - t1secs;
            if(minutes<0) {
                minutes += 60;
                hours -=1;
            }
            if(seconds<0) {
                seconds += 60;
                minutes -= 1;
            }
            hours += (n-1)*24;
            timeDiff.setText("Used Duration "+seconds+ " seconds" );
        }
    }

    public void upload_Time_Diff_In_Firebase()
    {
        database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("IRON BOX AVG TIME");
        database_reference.child(String.valueOf(seconds)).setValue(String.valueOf(seconds));

    }

    private void startTimer1() {
        mCountDownTimer1 = new CountDownTimer(mTimeLeftInMillis1, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis1 = millisUntilFinished;
                updateCountDownText1();
            }

            @Override
            public void onFinish() {
                mTimerRunning1 = false;
               Toast.makeText(getApplicationContext(),""+avg_time_total,Toast.LENGTH_LONG).show();
            }
        }.start();

        mTimerRunning1 = true;
//        mButtonStartPause.setText("pause");
//        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer1() {
        mCountDownTimer1.cancel();
        mTimerRunning1 = false;
//        mButtonStartPause.setText("Start");
//        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer1() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText1();
//        mButtonReset.setVisibility(View.INVISIBLE);
//        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText1() {
        int minutes = (int) (mTimeLeftInMillis1 / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis1 / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        // mTextViewCountDown.setText("Timer : "+timeLeftFormatted);
    }




}
