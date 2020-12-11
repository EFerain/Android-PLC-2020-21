package be.heh.feraine_projetandroid.plcManagement;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.R;
import be.heh.feraine_projetandroid.database.User;

public class LiquidRegulation extends AppCompatActivity
{
    private TextView tv_liquidRegulation_title;
    private TextView tv_liquidRegulation_manualAutoMode;
    private Button bt_liquidRegulation_connectS7;
    private Button bt_liquidRegulation_disconnect;

    private LinearLayout ll_liquidRegulation_writeTask;

    private User user;

    // Values
    private String ip;
    private String rack;
    private String slot;

    private ReadTaskS7 readTaskS7;
    private WriteTaskS7 writeTaskS7;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    /** ======== onCreate ======== **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquid_regulation);

        // Get views
        this.tv_liquidRegulation_title = findViewById(R.id.tv_liquidRegulation_title);
        this.tv_liquidRegulation_manualAutoMode = findViewById(R.id.tv_liquidRegulation_manualAutoMode);
        this.ll_liquidRegulation_writeTask = findViewById(R.id.ll_liquidRegulation_writeTask);
        this.bt_liquidRegulation_connectS7 = findViewById(R.id.bt_liquidRegulation_connectS7);
        this.bt_liquidRegulation_disconnect = findViewById(R.id.bt_liquidRegulation_disconnect);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        this.ip = getIntent().getStringExtra("ip");
        this.rack = getIntent().getStringExtra("rack");
        this.slot = getIntent().getStringExtra("slot");

        Log.e("Info PLC", "IP : " + this.ip + " || Rack : " + this.rack + " || Slot : " + this.slot);

        // If user with R/W privilege
        if(this.user.getPrivilege() > 0)
        {
            this.ll_liquidRegulation_writeTask.setVisibility(View.VISIBLE);
        }

        // ConnectivityManager -> accès aux réseau
        connexStatus = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // NetworkInfo -> accès aux informations du réseau
        network = connexStatus.getActiveNetworkInfo();
    }

    /** ======== onClickManager ======== **/
    public void onLiquidRegulationClickManager(View v)
    {
        switch(v.getId())
        {
            // ======== Connect S7 ========
            case R.id.bt_liquidRegulation_connectS7:

                if(this.network != null && this.network.isConnectedOrConnecting())
                {
                    if(this.bt_liquidRegulation_connectS7.getText().equals("Connect"))
                    {
                        // ReadTaskS7
                        // Toast.makeText(this, this.network.getTypeName(), Toast.LENGTH_SHORT).show();

                        this.bt_liquidRegulation_connectS7.setText("Disconnect");

                        this.readTaskS7 = new ReadTaskS7(tv_liquidRegulation_title , tv_liquidRegulation_manualAutoMode);
                        this.readTaskS7.Start(this.ip, this.rack, this.slot);

                        // WriteTaskS7

                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        // writeS7 = new WriteTaskS7();
                        // writeS7.Start("10.1.0.110", "0", "1");
                    }
                    else
                    {
                        // ReadTaskS7
                        readTaskS7.Stop();

                        this.bt_liquidRegulation_connectS7.setText("Connect");
                        this.ll_liquidRegulation_writeTask.setEnabled(false);

                        // WriteTaskS7
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        // writeS7.Stop();
                        // ln_main_ecrireS7.setVisibility(View.INVISIBLE);
                    }
                }
                // Can't connect
                else
                {
                    Toast.makeText(this, "! Connexion réseau IMPOSSIBLE !", Toast.LENGTH_SHORT).show();
                }


                break;

            // ======== Disconnect ========
            case R.id.bt_liquidRegulation_disconnect:
                /*
                this.readTaskS7.Stop();
                if(this.user.getPrivilege() > 0)
                {
                    this.writeTaskS7.Stop();
                }
                */

                Intent plcSettings = new Intent(this, PlcSettings.class);
                plcSettings.putExtra("userData", user);
                startActivity(plcSettings);
                finish();

                break;
        }
    }
}
