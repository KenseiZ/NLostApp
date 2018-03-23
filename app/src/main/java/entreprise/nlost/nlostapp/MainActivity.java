package entreprise.nlost.nlostapp;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private Set<BluetoothDevice> devices;
    //Addresse du module bluetooth
    private String deviceAddress = "00:14:03:06:53:C3";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long Delay = System.currentTimeMillis() + 5000;
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, 5000);
    }





    private Handler myHandler;
    private Runnable myRunnable;

    {
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

                        BluetoothSocket bluetoothSocket = null;
                        try {
                            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                        } catch (IOException e1) {

                        }
                        try {
                            bluetoothSocket.connect();
                            Toast.makeText(MainActivity.this, "Founded", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            NotificationGenerator.OpenActivityNotification(getApplicationContext());
                        }

                    }


                    Toast.makeText(MainActivity.this, "Tick", Toast.LENGTH_SHORT).show();
                    //}
                    myHandler.postDelayed(this, 5000);

                }
            }
        };
    }
}