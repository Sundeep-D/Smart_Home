package com.example.android.smart_home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class Home_Dynamic_Display extends AppCompatActivity {

    private static final String TAG = "bluetooth2";
    Button btnOn, btnOff;
    TextView txtArduino;
    Button waterheater_on_off;
    Handler h;
    final int RECIEVE_MESSAGE = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:21:13:04:84:66";
    String water_heater_state="off";
    LinearLayout water_heater_lay,water_motor_lay,ironbox_lay,bedroom_light_lay,outside_light_lay,bedroom_fan_lay,mixer_lay,human_detector_lay,nightmode_lay;
    RelativeLayout bigscreen;
    ScrollView appliances;
    DatabaseReference database_reference;
    ArrayList<String> device_status=new ArrayList<String >();
    String status;
    CardView water_heater_card,iron_box_card,bedroom_light_card,human_detector_card;
    TextView human_detector_present_absent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__dynamic__display);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();


        water_heater_lay=(LinearLayout)findViewById(R.id.water_heater_lay);
        water_motor_lay=(LinearLayout)findViewById(R.id.water_motor_lay);
        ironbox_lay=(LinearLayout)findViewById(R.id.iron_box_lay);
        bedroom_light_lay=(LinearLayout)findViewById(R.id.beedroom_lay);
        outside_light_lay=(LinearLayout)findViewById(R.id.outside_light_lay);
        bedroom_fan_lay=(LinearLayout)findViewById(R.id.beedroom_fan_lay);
        //mixer_lay=(LinearLayout)findViewById(R.id.mixer_lay);
        human_detector_lay=(LinearLayout)findViewById(R.id.human_detector_lay);
        nightmode_lay=(LinearLayout)findViewById(R.id.nightmode_lay);

        appliances=(ScrollView)findViewById(R.id.aplliances);
        water_heater_card=(CardView)findViewById(R.id.water_heater);
        iron_box_card=(CardView)findViewById(R.id.iron_box);
        bedroom_light_card=(CardView)findViewById(R.id.bedroom_light);
        human_detector_card=(CardView)findViewById(R.id.human_detector);
        human_detector_present_absent=(TextView)findViewById(R.id.human_detector_present_absent);



        database_reference= FirebaseDatabase.getInstance().getReference("DEVICE STATUS");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren() )
                {
                    String usrs=child.getValue(String.class);
                    device_status.clear();
                    device_status.add(usrs);
//                     Toast.makeText(dummy_firebase_check.this,""+usrs,Toast.LENGTH_SHORT).show();
//                    status=device_status.get(0);

//                    toast_message();
                    String[] check=usrs.split("_");

                    if (check[0].equals("waterheater"))
                    {
                        if (check[1].equals("true"))
                        {
                            //Toast.makeText(Home_Dynamic_Display.this,"Waterheater ON",Toast.LENGTH_SHORT).show();
                            mConnectedThread.write("1");
                            water_heater_lay.setBackgroundColor(Color.parseColor("#FFE0B2"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            water_heater_card.startAnimation(animation);
                        }
                        else if (check[1].equals("false"))
                        {
                            //Toast.makeText(Home_Dynamic_Display.this,"Waterheater OFF",Toast.LENGTH_SHORT).show();
                            mConnectedThread.write("0");

                            water_heater_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            water_heater_card.startAnimation(animation);
                        }
                    }
                    else if (check[0].equals("ironbox"))
                    {
                        if (check[1].equals("true"))
                        {
                            //Toast.makeText(Home_Dynamic_Display.this,"IronBox ON",Toast.LENGTH_SHORT).show();
                            ironbox_lay.setBackgroundColor(Color.parseColor("#FFE0B2"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            iron_box_card.startAnimation(animation);
                        }
                        else if (check[1].equals("false"))
                        {
                            ironbox_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            iron_box_card.startAnimation(animation);
                        }
                    }
                    if (check[0].equals("bedroomlight"))
                    {
                        if (check[1].equals("true"))
                        {
                            //Toast.makeText(Home_Dynamic_Display.this,"BedRoom Light ON",Toast.LENGTH_SHORT).show();
                            bedroom_light_lay.setBackgroundColor(Color.parseColor("#FFE0B2"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                            bedroom_light_card.startAnimation(animation);
                        }
                        else if (check[1].equals("false"))
                        {
                            //Toast.makeText(Home_Dynamic_Display.this,"BedRoom Light OFF",Toast.LENGTH_SHORT).show();
                            bedroom_light_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                            bedroom_light_card.startAnimation(animation);
                        }
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
                            if(sbprint.equals("0011"))
                            {
                                motion_detected();

                            }
                            else if (sbprint.equals("0010"))
                            {

                                no_human_detected();
                            }

                        }

                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();



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
        human_detector_lay.setBackgroundColor(Color.parseColor("#FFE0B2"));
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

    public void zoom(View view)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        water_heater_card.startAnimation(animation);
       iron_box_card.startAnimation(animation);
        bedroom_light_card.startAnimation(animation);
        mConnectedThread.write("1");
                water_heater_state="on";
        waterheater_on_off.setText("O N");
    }

    public void zoomout(View view)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
        water_heater_card.startAnimation(animation);
        iron_box_card.startAnimation(animation);
        bedroom_light_card.startAnimation(animation);
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
    water_heater_lay.setVisibility(View.INVISIBLE);
    water_motor_lay.setVisibility(View.INVISIBLE);
    ironbox_lay.setVisibility(View.INVISIBLE);
    bedroom_light_lay.setVisibility(View.INVISIBLE);
    outside_light_lay.setVisibility(View.INVISIBLE);
    bedroom_fan_lay.setVisibility(View.INVISIBLE);
   // mixer_lay.setVisibility(View.INVISIBLE);
    human_detector_lay.setVisibility(View.INVISIBLE);
    nightmode_lay.setVisibility(View.INVISIBLE);

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




}
