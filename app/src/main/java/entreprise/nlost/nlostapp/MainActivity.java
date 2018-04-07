package entreprise.nlost.nlostapp;

import android.app.Notification;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.os.Handler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import java.io.*;

import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private Set<BluetoothDevice> devices;
    //Addresse du module bluetooth
    private String deviceAddress = "00:14:03:06:53:C3";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothSocket bluetoothSocket = null;
    boolean isConnectedBool = true;
    int newState;
    InputStream tmpIn = null;
    OutputStream tmpOut = null;
    Button buttonConnect;
    Button buttonDisconnect;
    Button buttonLancerLaProcedure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*myHandler = new Handler();
        myHandler.postDelayed(myRunnable, 5000);*/
        buttonConnect = findViewById(R.id.button);
        buttonDisconnect= findViewById(R.id.button2);
        buttonLancerLaProcedure = findViewById(R.id.button3);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                }
                try {
                    bluetoothSocket.connect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bluetoothSocket.isConnected() == true){
                    Toast.makeText(MainActivity.this, "Je suis Connecté", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Je suis Déconnété", Toast.LENGTH_SHORT).show();
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
                if (bluetoothSocket.isConnected() == true){
                    Toast.makeText(MainActivity.this, "Je suis Connecté", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Je suis Déconnété", Toast.LENGTH_SHORT).show();
            }
        });
        buttonLancerLaProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (true) {
                    Toast.makeText(MainActivity.this, "While est en route", Toast.LENGTH_SHORT).show();
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
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        bluetoothSocket.connect();
                        Toast.makeText(MainActivity.this, "Je suis connecté", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        NotificationGenerator.OpenActivityNotification(MainActivity.this);

                    }
//                    if (bluetoothSocket.isConnected() == true){
//                        Toast.makeText(MainActivity.this, "Je suis Connecté", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                        Toast.makeText(MainActivity.this, "Je suis Déconnété", Toast.LENGTH_SHORT).show();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        bluetoothSocket.close();
                        Toast.makeText(MainActivity.this, "Je me déconnecte", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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