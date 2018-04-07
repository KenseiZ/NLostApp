package entreprise.nlost.nlostapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.nio.channels.Channel;

public class MainActivity extends AppCompatActivity {

    public static ConfigActivity configapp;

    private Set<BluetoothDevice> devices;
    //Addresse du module bluetooth
    private String deviceAddress = "00:14:03:06:53:C3";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothSocket bluetoothSocket = null;
    Button buttonConnect;
    Button buttonDisconnect;
    Button buttonLancerLaProcedure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configapp = new ConfigActivity(this,"nlostble.config.xml");


        StartBLE();

        if(configapp.GetActiveNotification())
        {
            Toast.makeText(MainActivity.this, "Les notifications sont activées !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Les notifications ne sont pas activées !", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent param = new Intent(MainActivity.this, ParamActivity.class);

            startActivity(param);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void StartBLE()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Check si l'utilisateur a activé le bluetooth sinon l'activer
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        buttonConnect = (Button)findViewById(R.id.button);
        buttonDisconnect= (Button)findViewById(R.id.button2);
        buttonLancerLaProcedure = (Button)findViewById(R.id.button3);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }

                devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bluetoothDevice : devices) {


                    //Check si l'adresse est valide
                    if (deviceAddress.equals(bluetoothDevice.getAddress())) {

                        bluetoothSocket = null;
                        try {
                            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                        } catch (IOException e1) {

                        }

                    }
                }
                try {
                    bluetoothSocket.connect();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        });
        buttonLancerLaProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {

                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                            if (!bluetoothAdapter.isEnabled()) {
                                bluetoothAdapter.enable();
                            }

                            devices = bluetoothAdapter.getBondedDevices();
                            for (BluetoothDevice bluetoothDevice : devices) {


                                //Check si l'adresse est valide
                                if (deviceAddress.equals(bluetoothDevice.getAddress())) {

                                    bluetoothSocket = null;
                                    try {
                                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                                    } catch (IOException e1) {

                                    }

                                }
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                bluetoothSocket.connect();

                            } catch (IOException e) {
                                e.printStackTrace();

                                if(configapp.GetActiveNotification())
                                {
                                    NotificationGenerator.OpenActivityNotification(MainActivity.this);
                                }



                            }
//

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                bluetoothSocket.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }).start();
            }
        });
    }

}


    @Override
    protected void onDestroy(){
        //myHandler.removeCallbacks(myRunnable);
        super.onDestroy();
    }



    /*private Handler myHandler;
    private Runnable myRunnable;*/

    /*{
        myRunnable = new Runnable() {
            @Override
            public void run() {

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                //Check si l'utilisateur a activé le bluetooth sinon l'activer
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }

                devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bluetoothDevice : devices) {


                    //Check si l'adresse est valide
                    if (deviceAddress.equals(bluetoothDevice.getAddress())) {

                        bluetoothSocket = null;
                        try {
                            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                        } catch (IOException e1) {

                        }

                    }
                     tmpIn = null;
                     tmpOut = null;
                    Connection();
                    Toast.makeText(MainActivity.this, "Je me connecte", Toast.LENGTH_SHORT).show();

                }

                myHandler.postDelayed(this, 5000);
            }
        };
    }
    public void Connection(){

        if (bluetoothSocket.isConnected() == false){
            //bluetoothSocket = null;
            try {
                bluetoothSocket.connect();
                Toast.makeText(MainActivity.this, "Je suis Connecté", Toast.LENGTH_SHORT).show();
                //isConnectedBool = true;
                //if (!bluetoothSocket.isConnected()){
                //    NotificationGenerator.OpenActivityNotification(this);
                //}
            } catch (IOException e) {
                //isConnectedBool = false;
                Toast.makeText(MainActivity.this, "Je lis le Catch", Toast.LENGTH_SHORT).show();
                try {
                    bluetoothSocket.close();
                    Toast.makeText(MainActivity.this, "Je Ferme le bluetooth", Toast.LENGTH_SHORT).show();
                } catch (IOException f) {}

            }
            try {
                Toast.makeText(MainActivity.this, "Je lis le temp", Toast.LENGTH_SHORT).show();
                tmpIn = bluetoothSocket.getInputStream();
                tmpOut = bluetoothSocket.getOutputStream();
            }
            catch (IOException tmpError){
                NotificationGenerator.OpenActivityNotification(this);
            }
            return;

        }

    }*/
}
