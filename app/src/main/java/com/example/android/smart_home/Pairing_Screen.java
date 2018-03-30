package com.example.android.smart_home;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Pairing_Screen extends AppCompatActivity {
    EditText et1,et2,et3,et4,et5;
    public SQLiteDatabase db;
    String passcode_code;
    DatabaseReference databaseReference;
    ArrayList<String> login_details=new ArrayList<>();
    LinearLayout lin;
    MediaPlayer mediaplayer;
    VideoView videoview;
    String sno="1";
    String state="false";
    TextView sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing__screen);

        db = openOrCreateDatabase("REGISTRATION_STATUS", Context.MODE_PRIVATE, null);

        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        et5=(EditText)findViewById(R.id.et5);
        lin=(LinearLayout)findViewById(R.id.pair_code_layout);
        sync=(TextView)findViewById(R.id.sync);

        videoview=(VideoView)findViewById(R.id.video_view);



        mediaplayer=MediaPlayer.create(this,R.raw.plucky);

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (state.equals("false"))
                {
                    mediaplayer.start();
                    state="true";
                    videoview.start();
                    sync.setVisibility(View.VISIBLE);
                }
                else if (state.equals("true"))
                {
                    pass_string();
                }








            }
        });

        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.stopper));
        videoview.requestFocus();


        databaseReference= FirebaseDatabase.getInstance().getReference("USER LOGIN DETAILS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren()) {
                    String usrs = child.getValue(String.class);

                    login_details.add(usrs);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et1.getText().toString().length()== 2)
                {
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et2.getText().toString().length()== 2)
                {
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et3.getText().toString().length()== 2)
                {
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et4.getText().toString().length()== 2)
                {
                    et5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et5.getText().toString().length()== 2)
                {

                    try
                    {

                        passcode_code=et1.getText().toString()+et2.getText().toString()+et3.getText().toString()+et4.getText().toString()+et5.getText().toString();


                        if (login_details.contains(passcode_code))
                        {

                            lin.setVisibility(View.INVISIBLE);
                            final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.ok);

                            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f)
                                    .setDuration(5000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    animationView.setProgress((Float) animation.getAnimatedValue());
                                }
                            });
                            animator.start();



                            videoview.start();

                           // showMessage("ok","ok");

//                            Cursor c = db.rawQuery("SELECT * FROM reg", null);
//
//                            db.execSQL("INSERT INTO reg VALUES('" + sno + "','" + passcode_code + "');");
//                            pass_string();
                        }
                        else
                        {
                            showMessage("Error Pairing Code","The pairing code you have entered does'nt Registered yet!");
                        }



                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"ERROR IN DATABASE",Toast.LENGTH_LONG).show();
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }


    void pass_string()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference(passcode_code).child("USER DETAILS");
        databaseReference.child("PAIR STATUS").setValue("true");

        Cursor c = db.rawQuery("SELECT * FROM reg", null);

        db.execSQL("INSERT INTO reg VALUES('" + sno + "','" + passcode_code + "');");


        Intent nxt=new Intent(Pairing_Screen.this,Home_Dynamic_Display.class);
        //nxt.putExtra("KEY",et1.getText().toString()+et2.getText().toString()+et3.getText().toString()+et4.getText().toString()+et5.getText().toString());
        startActivity(nxt);

    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
