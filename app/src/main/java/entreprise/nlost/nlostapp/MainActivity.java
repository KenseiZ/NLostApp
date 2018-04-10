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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
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
    boolean etatDeLaConnexion = false;
    TextView textConnexion;
    TextView textNotification;
    boolean OneNotification = true;
    boolean LaunchOneTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textConnexion = findViewById(R.id.textView7);
        textNotification = findViewById(R.id.textView5);

        /*Declaration de l'activité de configuration*/
        configapp = new ConfigActivity(this, "nlostble.config.xml");
        /*Execution au démarrage (Une seule fois)*/
        if(LaunchOneTime == true){
            StartBLE();
            EtatDeLaConnexion();
            EtatDesNotifications();
            LaunchOneTime = false;
        }
        LaunchOneTime = false;

        /*VERSION COMMERCIALE : Plus besoin de Toast*/
        /*if (configapp.GetActiveNotification()) {
            Toast.makeText(MainActivity.this, "Les notifications sont activées !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Les notifications ne sont pas activées !", Toast.LENGTH_SHORT).show();
        }*/


    }
    /*Clique sur le menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /*Clique sur les options*/
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

    /*Verification de l'etat de la Connexion et changement du texte de statut*/
    void EtatDeLaConnexion() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    if (etatDeLaConnexion == true) {
                        textConnexion.setText("Connectée");
                        textConnexion.setTextColor(Color.GREEN);
                    }
                    else {
                        textConnexion.setText("Déconnectée");
                        textConnexion.setTextColor(Color.RED);
                    }
                }

        });
    }
    /*Boucle des Notifications pour verifier l'etat des notifications*/
    void EtatDesNotifications(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BoucleEtatDesNotifications();
                }
            }
        }).start();

    }
    /*Verification de l'Etat des notifications et changement du texte de statut*/
    void BoucleEtatDesNotifications(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (configapp.GetActiveNotification()) {
                    textNotification.setText("Activées");
                    textNotification.setTextColor(Color.GREEN);
                }
                else {
                    textNotification.setText("Désactivées");
                    textNotification.setTextColor(Color.RED);
                }
            }
        });
    }
    /*Demarrage du processus de connexion du Bluetooth*/
    void StartBLE() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    /*Demarre automatiquement le Bluetooth*/
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
                    /*Processus de connexion*/
                    try {
                        bluetoothSocket.connect();
                        etatDeLaConnexion = true;
                        OneNotification = true;
                    }
                    /*Processus en cas d'echec de connexion (Clé non connectée)*/
                    catch (IOException e) {
                        e.printStackTrace();
                        etatDeLaConnexion = false;
                        if (configapp.GetActiveNotification()) {
                            if(OneNotification == true){
                                NotificationGenerator.OpenActivityNotification(MainActivity.this);
                                OneNotification = false;
                            }
                        }
                    }
                    /*Appel de la fonction pour verifier l'etat de la connexion*/
                    EtatDeLaConnexion();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /*Processus de déconnexion*/
                    try {
                        bluetoothSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

