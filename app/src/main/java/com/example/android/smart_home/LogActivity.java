package com.example.android.smart_home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<String> Feb=new ArrayList<String >();
    ArrayList<String> Apr=new ArrayList<String >();
    ArrayList<String> Jun=new ArrayList<String >();
    ArrayList<String> Aug=new ArrayList<String >();

    TextView billnumber,Consumer_Number,Units_consumed,Bill_amount,Payment_Date,Payment_Mode;
    LinearLayout main_lay;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log2);
                GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
               // new DataPoint(10, 1000),
               // new DataPoint(18, 900),
                new DataPoint(1, 1300),
                new DataPoint(2, 1100),
                new DataPoint(3, 1150)
        });
        graph.addSeries(series);

       //main_lay.setVisibility(View.INVISIBLE);

        initial_create();


        billnumber=(TextView)findViewById(R.id.bill_Number);
        Consumer_Number=(TextView)findViewById(R.id.Consumer_number);
        Units_consumed=(TextView)findViewById(R.id.Units_consumed);
        Bill_amount=(TextView)findViewById(R.id.bill_amount);
        Payment_Date=(TextView)findViewById(R.id.payment_date);
        Payment_Mode=(TextView)findViewById(R.id.payment_mode);
        cardView=(CardView) findViewById(R.id.Monthly_Consumption_CardView);




     //  //// ******************************************LOG RETRIVEL*********************************************************************

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Feb-2018");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);
                    Toast.makeText(LogActivity.this,usrs,Toast.LENGTH_LONG).show();
                    Feb.add(usrs);
                }


            }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Apr-2018");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);

                    Apr.add(usrs);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Jun-2018");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);
                    Jun.add(usrs);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Aug-2018");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);
                    Aug.add(usrs);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //******************************************LOG RETRIVEL*********************************************************************




    }

    public void create(View view) {
       initial_create();
    }

    public void Feb(View view) {
      //  main_lay.setVisibility(View.VISIBLE);

//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
//       main_lay.startAnimation(animation);

//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
//       cardView.startAnimation(animation);

        billnumber.setText(Feb.get(1));
        Consumer_Number.setText(Feb.get(2));
        Units_consumed.setText(Feb.get(5));
        Bill_amount.setText(Feb.get(0));
        Payment_Date.setText(Feb.get(3));
       Payment_Mode.setText(Feb.get(4));

    }

    public void Apr(View view) {
        billnumber.setText(Apr.get(1));
        Consumer_Number.setText(Apr.get(2));
        Units_consumed.setText(Apr.get(5));
        Bill_amount.setText(Apr.get(0));
        Payment_Date.setText(Apr.get(3));
        Payment_Mode.setText(Apr.get(4));

    }

    public void Jun(View view) {
        billnumber.setText(Jun.get(1));
        Consumer_Number.setText(Jun.get(2));
        Units_consumed.setText(Jun.get(5));
        Bill_amount.setText(Jun.get(0));
        Payment_Date.setText(Jun.get(3));
        Payment_Mode.setText(Jun.get(4));

    }

    public void Aug(View view) {
        billnumber.setText(Aug.get(1));
        Consumer_Number.setText(Aug.get(2));
        Units_consumed.setText(Aug.get(5));
        Bill_amount.setText(Aug.get(0));
        Payment_Date.setText(Aug.get(3));
        Payment_Mode.setText(Aug.get(4));

    }
    public void initial_create()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Feb-2018");
        databaseReference.child("Bill Number").setValue("275385");
        databaseReference.child("Consumer Number").setValue("500-347-453-020");
        databaseReference.child("Units Consumed").setValue("490");
        databaseReference.child("Bill Amount").setValue("1300");
        databaseReference.child("Payment Date").setValue("10-Feb-2018");
        databaseReference.child("Payment Mode").setValue("Net Banking");

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Apr-2018");
        databaseReference.child("Bill Number").setValue("463729");
        databaseReference.child("Consumer Number").setValue("500-347-453-020");
        databaseReference.child("Units Consumed").setValue("480");
        databaseReference.child("Bill Amount").setValue("1200");
        databaseReference.child("Payment Date").setValue("18-Apr-2018");
        databaseReference.child("Payment Mode").setValue("Debit/Credit Card");

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Jun-2018");
        databaseReference.child("Bill Number").setValue("347283");
        databaseReference.child("Consumer Number").setValue("500-347-453-020");
        databaseReference.child("Units Consumed").setValue("450");
        databaseReference.child("Bill Amount").setValue("1100");
        databaseReference.child("Payment Date").setValue("18-Jun-2018");
        databaseReference.child("Payment Mode").setValue("Debit/Credit Card");

        databaseReference= FirebaseDatabase.getInstance().getReference("LOG DETAILS").child("10-Aug-2018");
        databaseReference.child("Bill Number").setValue("637328");
        databaseReference.child("Consumer Number").setValue("500-347-453-020");
        databaseReference.child("Units Consumed").setValue("460");
        databaseReference.child("Bill Amount").setValue("1120");
        databaseReference.child("Payment Date").setValue("18-Aug-2018");
        databaseReference.child("Payment Mode").setValue("UPI");
    }
}
