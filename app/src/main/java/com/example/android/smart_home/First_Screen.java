package com.example.android.smart_home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class First_Screen extends AppCompatActivity {
    public ProgressDialog mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__screen);
        mprogress=new ProgressDialog(this);
    }

    public void nextscreen(View view) {
        Intent nxtscreen=new Intent(First_Screen.this,Home_Dynamic_Display.class);

        mprogress.setMessage("Initializing");
        mprogress.show();
        startActivity(nxtscreen);
    }
}
