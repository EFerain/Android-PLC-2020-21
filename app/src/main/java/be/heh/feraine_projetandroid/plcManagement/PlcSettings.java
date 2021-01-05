package be.heh.feraine_projetandroid.plcManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.Menu;
import be.heh.feraine_projetandroid.R;
import be.heh.feraine_projetandroid.database.User;

public class PlcSettings extends AppCompatActivity
{
    /** ======== Attributs ======== **/
    private EditText et_plcSettings_ipAddress;
    private EditText et_plcSettings_rack;
    private EditText et_plcSettings_slot;

    private Button bt_plcSettings_connect;
    private Button bt_plcSettings_backMenu;

    private User user;

    private String plc;

    /** ======== onCreate ======== **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plc_settings);

        // Get views
        this.et_plcSettings_ipAddress = findViewById(R.id.et_plcSettings_ipAddress);
        this.et_plcSettings_rack = findViewById(R.id.et_plcSettings_rack);
        this.et_plcSettings_slot = findViewById(R.id.et_plcSettings_slot);

        this.bt_plcSettings_connect = findViewById(R.id.bt_plcSettings_connect);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        this.plc = getIntent().getStringExtra("plc");
    }

    /** ======== onClickManager ======== **/
    public void onPlcSettingsClickManager(View v)
    {
        switch (v.getId())
        {
            // ======== Connect =======
            case R.id.bt_plcSettings_connect:
                switch (this.plc)
                {
                    // ======== Liquid Regulation ========
                    case "liquidRegulation":
                        // ==== If missing field(s) ====
                        if(this.et_plcSettings_ipAddress.getText().toString().isEmpty() ||
                                this.et_plcSettings_rack.getText().toString().isEmpty() ||
                                this.et_plcSettings_slot.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Missing fields !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // ==== Wrong IP ====
                            if(!Patterns.IP_ADDRESS.matcher(this.et_plcSettings_ipAddress.getText().toString()).matches())
                            {
                                Toast.makeText(getApplicationContext(), "Error : Can't connect to this network", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                Intent liquidRegulation = new Intent(this, LiquidRegulation.class);
                                liquidRegulation.putExtra("userData", user);
                                liquidRegulation.putExtra("ip", this.et_plcSettings_ipAddress.getText().toString());
                                liquidRegulation.putExtra("rack", this.et_plcSettings_rack.getText().toString());
                                liquidRegulation.putExtra("slot", this.et_plcSettings_slot.getText().toString());
                                startActivity(liquidRegulation);
                            }
                        }

                        break;

                    // ======== Packaging ========
                    case "packaging":
                        // ==== If missing field(s) ====
                        if(this.et_plcSettings_ipAddress.getText().toString().isEmpty() ||
                                this.et_plcSettings_rack.getText().toString().isEmpty() ||
                                this.et_plcSettings_slot.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Missing fields !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // ==== Wrong IP ====
                            if(!Patterns.IP_ADDRESS.matcher(this.et_plcSettings_ipAddress.getText().toString()).matches())
                            {
                                Toast.makeText(getApplicationContext(), "Error : Can't connect to this network", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Intent packaging = new Intent(this, Packaging.class);
                                packaging.putExtra("userData", user);
                                packaging.putExtra("ip", this.et_plcSettings_ipAddress.getText().toString());
                                packaging.putExtra("rack", this.et_plcSettings_rack.getText().toString());
                                packaging.putExtra("slot", this.et_plcSettings_slot.getText().toString());
                                startActivity(packaging);
                            }
                        }

                        break;
                }

                break;
        }
    }
}
