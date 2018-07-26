package com.example.android.smart_home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Consumption_Activity extends AppCompatActivity {

    String passcode_pass;
    DatabaseReference database_reference;
    ArrayList<String> avg_time_ironbox=new ArrayList<String >();
    ArrayList<String> avg_time_water_heater=new ArrayList<String >();
    ArrayList<String> avg_time_water_motor=new ArrayList<String >();

    long avg_time_iron_box_count;
    long avg_time_water_motor_count;
    long avg_time_water_heater_count;

     double avg_time_iron_box_total;
    double avg_time_water_motor_total;
    double avg_time_water_heater_total;

    TextView head1,head2,head3;
    TextView units1,units2,units3;
    TextView amount1,amount2,amount3;

    double iron_box_cons;
    double water_heater_cons;
    double water_motor_cons;

    double iron_box_watt=1100;
    double water_heater_watt=4500;
    double water_motor_watt=2500;


    double iron_box_ans=000;
   double water_heater_ans=000;
    double water_motor_ans=000;
    static DecimalFormat dec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_);
        passcode_pass=getIntent().getStringExtra("KEY");



        head1 = (TextView) findViewById(R.id.head1);
        head2 = (TextView) findViewById(R.id.head2);
        head3 = (TextView) findViewById(R.id.head3);

        units1 = (TextView) findViewById(R.id.units1);
        units2 = (TextView) findViewById(R.id.units2);
        units3 = (TextView) findViewById(R.id.units3);

        amount1 = (TextView) findViewById(R.id.amount1);
        amount2 = (TextView) findViewById(R.id.amount2);
        amount3 = (TextView) findViewById(R.id.amount3);

       dec = new DecimalFormat("0.#####");





                database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("IRON BOX AVG TIME");
        database_reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    String usrs=child.getValue(String.class);

                    avg_time_ironbox.add(usrs);
                    avg_time_iron_box_count=dataSnapshot.getChildrenCount();





                }
                //Toast.makeText(getApplicationContext(),""+avg_time_count,Toast.LENGTH_LONG).show();
                for (int i=0;i<avg_time_iron_box_count;i++)
                {
                    avg_time_iron_box_total+=Integer.parseInt(avg_time_ironbox.get(i));
                }

              //  Toast.makeText(getApplicationContext(),""+avg_time_iron_box_total,Toast.LENGTH_LONG).show();

                iron_box_cons=avg_time_iron_box_total/3600;

               // Toast.makeText(getApplicationContext(),""+iron_box_cons,Toast.LENGTH_LONG).show();

                iron_box_ans=(iron_box_watt*iron_box_cons)/1000;
                //Toast.makeText(getApplicationContext(),""+iron_box_ans,Toast.LENGTH_LONG).show();



              //  units2.setText("Units Consumed : "+String.valueOf(dec.format(iron_box_ans))+ " kWh");
                update_method();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("WATER HEATER AVG TIME");
        database_reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    String usrs=child.getValue(String.class);

                    avg_time_water_heater.add(usrs);
                    avg_time_water_heater_count=dataSnapshot.getChildrenCount();





                }
                //Toast.makeText(getApplicationContext(),""+avg_time_count,Toast.LENGTH_LONG).show();
                for (int i=0;i<  avg_time_water_heater_count;i++)
                {
                    avg_time_water_heater_total+=Integer.parseInt(avg_time_water_heater.get(i));
                }

                water_heater_cons=avg_time_water_heater_total/3600;
                water_heater_ans=(water_heater_watt*water_heater_cons)/1000;
               // units1.setText("Units Consumed : "+String.valueOf(dec.format(water_heater_ans))+ " kWh");

                update_method();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        database_reference= FirebaseDatabase.getInstance().getReference(passcode_pass).child("WATER MOTOR AVG TIME");
        database_reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    String usrs=child.getValue(String.class);

                    avg_time_water_motor.add(usrs);
                    avg_time_water_motor_count=dataSnapshot.getChildrenCount();





                }
                //Toast.makeText(getApplicationContext(),""+avg_time_count,Toast.LENGTH_LONG).show();
                for (int i=0;i<avg_time_water_motor_count;i++)
                {
                    avg_time_water_motor_total+=Integer.parseInt(avg_time_water_motor.get(i));
                }

                water_motor_cons=avg_time_water_motor_total/3600;
                water_motor_ans=(water_motor_watt*water_motor_cons)/1000;
                //units3.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");

                update_method();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });












    }


    public void update_method()

    {
        head1.setText("");
        head2.setText("");
        head3.setText("");

        units1.setText("");
        units2.setText("");
        units3.setText("");

        double arr[] = {iron_box_ans,water_heater_ans,water_motor_ans};
        double largest = arr[0];
        double secondLargest = arr[0];

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] > largest) {
                secondLargest = largest;
                largest = arr[i];

            } else if (arr[i] > secondLargest) {
                secondLargest = arr[i];

            }
        }

//   amount1.setText(String.valueOf(largest)+" "+String.valueOf(secondLargest));

//
//        if(largest==water_motor_ans)
//        {
//            head1.setText("WATER MOTOR");
//     units1.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");
//        }
//        else if(String.valueOf(secondLargest).equals(water_heater_ans))
//        {
//            head2.setText("WATER MOTOR");
//            units2.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");
//        }
//        else
//        {
//
//        }










       if(largest==water_motor_ans)
       {

           //Toast.makeText(getApplicationContext(),"Large",Toast.LENGTH_LONG).show();
           head1.setText("W A T E R  M O T O R");
           units1.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");
           if (water_motor_ans>100&&water_motor_ans<200)
           {
               double bill=Math.round(water_motor_ans*2);
               amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
           }
           else if (water_motor_ans>200&&water_motor_ans<300)
           {
               double bill=Math.round(water_motor_ans*3);
               amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

           }
           else if(water_motor_ans>300)
           {
               double bill=Math.round(water_motor_ans*4);
               amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
           }
           else
           {
               double bill=Math.round(water_motor_ans*1);
               amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

           }
       }
         else if(secondLargest==water_motor_ans)
        {

           // Toast.makeText(getApplicationContext(),"second Large",Toast.LENGTH_LONG).show();

            head2.setText("W A T E R  M O T O R");
            units2.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");
            if (water_motor_ans>100&&water_motor_ans<200)
            {
                double bill=Math.round(water_motor_ans*2);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (water_motor_ans>200&&water_motor_ans<300)
            {
                double bill=Math.round(water_motor_ans*3);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(water_motor_ans>300)
            {
                double bill=Math.round(water_motor_ans*4);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else
            {
                double bill=Math.round(water_motor_ans*1);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
        }
        else
       {

           //Toast.makeText(getApplicationContext(),"third Large",Toast.LENGTH_LONG).show();


           head3.setText("W A T E R  M O T O R");
           units3.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");
           if (water_motor_ans>100&&water_motor_ans<200)
           {
               double bill=Math.round(water_motor_ans*2);
               amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
           }
           else if (water_motor_ans>200&&water_motor_ans<300)
           {
               double bill=Math.round(water_motor_ans*3);
               amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

           }
           else if(water_motor_ans>300)
           {
               double bill=Math.round(water_motor_ans*4);
               amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
           }
           else
           {
               double bill=Math.round(water_motor_ans*1);
               amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

           }
       }


        if(largest==water_heater_ans)
        {
            head1.setText("W A T E R  H E A T E R");
            units1.setText("Units Consumed : "+String.valueOf(dec.format(water_heater_ans))+ " kWh");

            if (water_heater_ans>100&&water_heater_ans<200)
            {
                double bill=Math.round(water_heater_ans*2);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (water_heater_ans>200&&water_heater_ans<300)
            {
                double bill=Math.round(water_heater_ans*3);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(water_heater_ans>300)
            {
                double bill=Math.round(water_heater_ans*4);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else {
                double bill=Math.round(water_heater_ans*1);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }


        }
        else if(secondLargest==water_heater_ans)
        {
            head2.setText("W A T E R  H E A T E R");
            units2.setText("Units Consumed : "+String.valueOf(dec.format(water_heater_ans))+ " kWh");
            if (water_heater_ans>100&&water_heater_ans<200)
            {
                double bill=Math.round(water_heater_ans*2);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (water_heater_ans>200&&water_heater_ans<300)
            {
                double bill=Math.round(water_heater_ans*3);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(water_heater_ans>300)
            {
                double bill=Math.round(water_heater_ans*4);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else {
                double bill=Math.round(water_heater_ans*1);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
        }
        else
        {
            head3.setText("W A T E R  H E A T E R");
            units3.setText("Units Consumed : "+String.valueOf(dec.format(water_heater_ans))+ " kWh");
            if (water_heater_ans>100&&water_heater_ans<200)
            {
                double bill=Math.round(water_heater_ans*2);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (water_heater_ans>200&&water_heater_ans<300)
            {
                double bill=Math.round(water_heater_ans*3);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(water_heater_ans>300)
            {
                double bill=Math.round(water_heater_ans*4);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else {
                double bill=Math.round(water_heater_ans*1);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
        }





        if(largest==iron_box_ans)
        {
            head1.setText("I R O N  B O X");
            units1.setText("Units Consumed : "+String.valueOf(dec.format(iron_box_ans))+ " kWh");
            if (iron_box_ans>100&&iron_box_ans<200)
            {
                double bill=Math.round(iron_box_ans*2);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (iron_box_ans>200&&iron_box_ans<300)
            {
                double bill=Math.round(iron_box_ans*3);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(iron_box_ans>300)
            {
                double bill=Math.round(iron_box_ans*4);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else
            {
                double bill=Math.round(iron_box_ans*1);
                amount1.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
        }
        else if(secondLargest==iron_box_ans)
        {
            head2.setText("I R O N  B O X");
            units2.setText("Units Consumed : "+String.valueOf(dec.format(iron_box_ans))+ " kWh");
            if (iron_box_ans>100&&iron_box_ans<200)
            {
                double bill=Math.round(iron_box_ans*2);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (iron_box_ans>200&&iron_box_ans<300)
            {
                double bill=Math.round(iron_box_ans*3);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(iron_box_ans>300)
            {
                double bill=Math.round(iron_box_ans*4);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else
            {
                double bill=Math.round(iron_box_ans*1);
                amount2.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
        }
        else
        {
            head3.setText("I R O N  B O X");
            units3.setText("Units Consumed : "+String.valueOf(dec.format(iron_box_ans))+ " kWh");
            if (iron_box_ans>100&&iron_box_ans<200)
            {
                double bill=Math.round(iron_box_ans*2);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else if (iron_box_ans>200&&iron_box_ans<300)
            {
                double bill=Math.round(iron_box_ans*3);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
            else if(iron_box_ans>300)
            {
                double bill=Math.round(iron_box_ans*4);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");
            }
            else
            {
                double bill=Math.round(iron_box_ans*1);
                amount3.setText(String.valueOf(bill)+" Rupees charged for this Appliance");

            }
        }






//        if(iron_box_ans>water_heater_ans && iron_box_ans>water_motor_ans)
//        {
//            head3.setText("I R O N  B O X");
//            units3.setText("Units Consumed : "+String.valueOf(dec.format(iron_box_ans))+ " kWh");
//        }
//        else if(water_heater_ans>water_motor_ans)
//        {
//            head2.setText("W A T E R  H E A T E R");
//            units2.setText("Units Consumed : "+String.valueOf(dec.format(water_heater_ans))+ " kWh");
//        }
//        else
//        {
//            head1.setText("W A T E R  M O T O R");
//            units1.setText("Units Consumed : "+String.valueOf(dec.format(water_motor_ans))+ " kWh");
//        }
    }


}
