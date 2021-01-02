package be.heh.feraine_projetandroid.plcManagement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.R;
import be.heh.feraine_projetandroid.database.User;

public class LiquidRegulation extends AppCompatActivity
{
    private TextView tv_liquidRegulation_title;
    private TextView tv_liquidRegulation_liquidLvl;
    private TextView tv_liquidRegulation_manualAutoMode;
    private TextView tv_liquidRegulation_setpoint;
    private TextView tv_liquidRegulation_manualValue;
    private TextView tv_liquidRegulation_outputValue;

    private Button bt_liquidRegulation_connectS7;
    private Button bt_liquidRegulation_send;
    private Button bt_liquidRegulation_disconnect;

    private LinearLayout ll_liquidRegulation_writeTask;
    private LinearLayout ll_liquidRegulation_formatB;
    private LinearLayout ll_liquidRegulation_formatW;
    private LinearLayout ll_liquidRegulation_bitInByte;

    private Spinner sp_liquidRegulation_dbChoice;
    private Spinner sp_liquidRegulation_formatB;
    private Spinner sp_liquidRegulation_formatW;
    private Spinner sp_liquidRegulation_bitInByte;

    private EditText et_liquidRegulation_data;

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
        this.tv_liquidRegulation_liquidLvl = findViewById(R.id.tv_liquidRegulation_liquidLvl);
        this.tv_liquidRegulation_manualAutoMode = findViewById(R.id.tv_liquidRegulation_manualAutoMode);
        this.tv_liquidRegulation_setpoint = findViewById(R.id.tv_liquidRegulation_setpoint);
        this.tv_liquidRegulation_manualValue = findViewById(R.id.tv_liquidRegulation_manualValue);
        this.tv_liquidRegulation_outputValue = findViewById(R.id.tv_liquidRegulation_outputValue);

        this.ll_liquidRegulation_writeTask = findViewById(R.id.ll_liquidRegulation_writeTask);
        this.ll_liquidRegulation_formatB = findViewById(R.id.ll_liquidRegulation_formatB);
        this.ll_liquidRegulation_formatW = findViewById(R.id.ll_liquidRegulation_formatW);
        this.ll_liquidRegulation_bitInByte = findViewById(R.id.ll_liquidRegulation_bitInByte);

        this.bt_liquidRegulation_connectS7 = findViewById(R.id.bt_liquidRegulation_connectS7);
        this.bt_liquidRegulation_send = findViewById(R.id.bt_liquidRegulation_send);
        this.bt_liquidRegulation_disconnect = findViewById(R.id.bt_liquidRegulation_disconnect);

        this.sp_liquidRegulation_dbChoice = findViewById(R.id.sp_liquidRegulation_dbChoice);
        this.sp_liquidRegulation_formatB = findViewById(R.id.sp_liquidRegulation_formatB);
        this.sp_liquidRegulation_formatW = findViewById(R.id.sp_liquidRegulation_formatW);
        this.sp_liquidRegulation_bitInByte = findViewById(R.id.sp_liquidRegulation_bitInByte);

        this.et_liquidRegulation_data = findViewById(R.id.et_liquidRegulation_data);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        this.ip = getIntent().getStringExtra("ip");
        this.rack = getIntent().getStringExtra("rack");
        this.slot = getIntent().getStringExtra("slot");

        // Log.i("Info PLC", "IP : " + this.ip + " || Rack : " + this.rack + " || Slot : " + this.slot);

        // If user with R/W privilege
        if(this.user.getPrivilege() > 0)
        {
            this.ll_liquidRegulation_writeTask.setVisibility(View.VISIBLE);
        }

        // ConnectivityManager -> accès aux réseau
        connexStatus = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // NetworkInfo -> accès aux informations du réseau
        network = connexStatus.getActiveNetworkInfo();

        /** Listener **/
        // Listener for DBB,W
        this.sp_liquidRegulation_dbChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                dbChoice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                // VOID
            }
        });

        // Listener for Format B
        this.sp_liquidRegulation_formatB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                dbChoice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // VOID
            }
        });

        // Lister for Format W
        this.sp_liquidRegulation_formatW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                dbChoice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // VOID
            }
        });

        // Lister for Bit
        this.sp_liquidRegulation_bitInByte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                dbChoice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // VOID
            }
        });
    }

    /** ======== DBB,W ======== **/
    public void dbChoice()
    {
        // Attributes
        String format;
        String bitNumber = "";

        // If B or W
        String dbChoice = this.sp_liquidRegulation_dbChoice.getSelectedItem().toString();
        String dbName = dbChoice.substring(3);
        int dbNum = Integer.parseInt(dbName);

        if(dbNum <= 15)
        {
            this.ll_liquidRegulation_formatB.setVisibility(View.VISIBLE);
            this.ll_liquidRegulation_formatW.setVisibility(View.GONE);

            format = this.sp_liquidRegulation_formatB.getSelectedItem().toString();
        }
        else
        {
            this.ll_liquidRegulation_formatB.setVisibility(View.GONE);
            this.ll_liquidRegulation_formatW.setVisibility(View.VISIBLE);

            format = this.sp_liquidRegulation_formatW.getSelectedItem().toString();
        }

        // Format
        if(format.equals("Byte"))
        {
            this.et_liquidRegulation_data.setHint("0 - 255");
        }
        else if(format.equals("Int"))
        {
            this.et_liquidRegulation_data.setHint("0 - 65 535");
        }
        else
        {
            this.et_liquidRegulation_data.setHint("0 - 1");
        }

        // If bit in DBB
        if(dbNum <= 15 && format.equals("Bit"))
        {
            this.ll_liquidRegulation_bitInByte.setVisibility(View.VISIBLE);

            bitNumber = this.sp_liquidRegulation_bitInByte.getSelectedItem().toString();
        }
        else
        {
            this.ll_liquidRegulation_bitInByte.setVisibility(View.GONE);
        }

        // Log.i("Write", "DB : " + dbNum + " || Format : " + format + " || Bit : " + bitNumber);
    }

    /** ======== onBackPressed ======== **/
    public void onBackPressed()
    {
        if(this.bt_liquidRegulation_connectS7.getText().equals("Disconnect"))
        {
            this.readTaskS7.Stop();

            if(this.user.getPrivilege() > 0)
            {
                this.writeTaskS7.Stop();
            }
        }

        finish();
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

                        this.readTaskS7 = new ReadTaskS7(tv_liquidRegulation_title, tv_liquidRegulation_liquidLvl, tv_liquidRegulation_manualAutoMode,
                                tv_liquidRegulation_setpoint, tv_liquidRegulation_manualValue, tv_liquidRegulation_outputValue);
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

                        if(this.user.getPrivilege() > 0)
                        {
                            this.writeTaskS7 = new WriteTaskS7();
                            this.writeTaskS7.Start(this.ip, this.rack, this.slot);
                        }
                    }
                    else
                    {
                        // ReadTaskS7
                        this.bt_liquidRegulation_connectS7.setText("Connect");

                        this.tv_liquidRegulation_liquidLvl.setText("Liquid level : ");
                        this.tv_liquidRegulation_manualAutoMode.setText("Mode : ");
                        this.tv_liquidRegulation_setpoint.setText("Setpoint : ");
                        this.tv_liquidRegulation_manualValue.setText("Manual value : ");
                        this.tv_liquidRegulation_outputValue.setText("Output value : ");

                        readTaskS7.Stop();

                        // WriteTaskS7
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        if(this.user.getPrivilege() > 0)
                        {
                            this.writeTaskS7.Stop();
                        }
                    }
                }
                // Can't connect
                else
                {
                    Toast.makeText(this, "Cannot connect to the network", Toast.LENGTH_SHORT).show();
                }

                break;

            // ======== Send ========
            case R.id.bt_liquidRegulation_send:
                // If disconnected
                if(this.writeTaskS7 == null)
                {
                    Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
                // If empty field
                else if(this.et_liquidRegulation_data.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int dbNumber = Integer.parseInt(this.sp_liquidRegulation_dbChoice.getSelectedItem().toString().substring(3));
                    String format;

                    if(dbNumber <= 15)
                    {
                        format = this.sp_liquidRegulation_formatB.getSelectedItem().toString();
                    }
                    else
                    {
                        format = this.sp_liquidRegulation_formatW.getSelectedItem().toString();
                    }

                    int dataValue;

                    try
                    {
                        dataValue = Integer.parseInt(this.et_liquidRegulation_data.getText().toString());

                        // If Bit (0 - 1)
                        if(format.equals("Bit"))
                        {
                            if(dataValue != 0 && dataValue != 1)
                            {
                                Toast.makeText(this, "Please enter a correct value (0 - 1)", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                int bitNumber = Integer.parseInt(this.sp_liquidRegulation_bitInByte.getSelectedItem().toString());

                                this.writeTaskS7.setWriteBool(dbNumber, (int)Math.pow(2, bitNumber), dataValue);
                            }
                        }
                        // If Byte (0 - 255)
                        else if(dbNumber <= 15)
                        {
                            if(dataValue < 0 || dataValue > 255)
                            {
                                Toast.makeText(this, "Please enter a correct value (0 - 255)", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                this.writeTaskS7.setWriteByte(dbNumber, dataValue);
                            }
                        }
                        // If Int (0 - 65535)
                        else
                        {
                            if(dataValue < 0 || dataValue > 65535)
                            {
                                Toast.makeText(this, "Please enter a correct value (0 - 65 535)", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                this.writeTaskS7.setWriteInt(dbNumber, dataValue);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            // ======== Disconnect ========
            case R.id.bt_liquidRegulation_disconnect:
                if(this.bt_liquidRegulation_connectS7.getText().equals("Disconnect"))
                {
                    this.readTaskS7.Stop();

                    if(this.user.getPrivilege() > 0)
                    {
                        this.writeTaskS7.Stop();
                    }
                }

                finish();

                break;
        }
    }
}
