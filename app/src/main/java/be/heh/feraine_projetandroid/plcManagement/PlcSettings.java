package be.heh.feraine_projetandroid.plcManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.R;
import be.heh.feraine_projetandroid.database.User;

public class PlcSettings extends AppCompatActivity
{
    /** ======== Attributs ======== **/
    private EditText et_plcSettings_ipAddress;
    private EditText et_plcSettings_rack;
    private EditText et_plcSettings_slot;

    private Button bt_plcSettings_connect;

    private User user;

    private String plc;

    /** ======== onCreate ======== **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plc_settings);

        this.et_plcSettings_ipAddress = findViewById(R.id.et_plcSettings_ipAddress);
        this.et_plcSettings_rack = findViewById(R.id.et_plcSettings_rack);
        this.et_plcSettings_slot = findViewById(R.id.et_plcSettings_slot);

        this.bt_plcSettings_connect = findViewById(R.id.bt_plcSettings_connect);

        this.plc = getIntent().getStringExtra("plc");
    }

    /** ======== onClickManager ======== **/
    public void onPlcSettingsClickManager(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_plcSettings_connect:
                switch (this.plc)
                {
                    case "liquidRegulation":
                        // TODO if connection
                        Intent liquidRegulation = new Intent(this, LiquidRegulation.class);
                        startActivity(liquidRegulation);

                        break;

                    case "packaging":
                        // TODO if connection
                        Intent packaging = new Intent(this, Packaging.class);
                        startActivity(packaging);

                        break;
                }

                break;
        }
    }
}
