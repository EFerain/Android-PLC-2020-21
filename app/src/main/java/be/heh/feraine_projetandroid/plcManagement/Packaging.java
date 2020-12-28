package be.heh.feraine_projetandroid.plcManagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import be.heh.feraine_projetandroid.R;
import be.heh.feraine_projetandroid.database.User;

public class Packaging extends AppCompatActivity
{
    // Layout
    private TextView tv_packaging_title;
    private TextView tv_packaging_isActive;
    private TextView tv_packaging_nbrBottles;
    private TextView tv_packaging_nbrPillsPerBottles;
    private TextView tv_packaging_askForPills;
    private TextView tv_packaging_selector;
    private TextView tv_packaging_bottlesGenerator;

    private Button bt_packaging_connectS7;
    private Button bt_packaging_send;
    private Button bt_packaging_disconnectFromPlc;

    private LinearLayout ll_packaging_writeTask;
    private LinearLayout ll_packaging_formatB;
    private LinearLayout ll_packaging_formatW;
    private LinearLayout ll_packaging_bitInByte;

    private Spinner sp_packaging_dbChoice;
    private Spinner sp_packaging_formatB;
    private Spinner sp_packaging_formatW;
    private Spinner sp_packaging_bitInByte;

    private EditText et_packaging_data;

    private ArrayAdapter adapter;

    // User
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
        setContentView(R.layout.activity_packaging);

        // Get views
        this.tv_packaging_title = findViewById(R.id.tv_packaging_title);
        this.tv_packaging_isActive = findViewById(R.id.tv_packaging_isActive);
        this.tv_packaging_nbrBottles = findViewById(R.id.tv_packaging_nbrBottles);
        this.tv_packaging_nbrPillsPerBottles = findViewById(R.id.tv_packaging_nbrPillsPerBottles);
        this.tv_packaging_askForPills = findViewById(R.id.tv_packaging_askForPills);
        this.tv_packaging_selector = findViewById(R.id.tv_packaging_selector);
        this.tv_packaging_bottlesGenerator = findViewById(R.id.tv_packaging_bottlesGenerator);

        this.bt_packaging_connectS7 = findViewById(R.id.bt_packaging_connectS7);
        this.bt_packaging_send = findViewById(R.id.bt_packaging_send);
        this.bt_packaging_disconnectFromPlc = findViewById(R.id.bt_packaging_disconnectFromPlc);

        this.ll_packaging_writeTask = findViewById(R.id.ll_packaging_writeTask);
        this.ll_packaging_formatB = findViewById(R.id.ll_packaging_formatB);
        this.ll_packaging_formatW = findViewById(R.id.ll_packaging_formatW);
        this.ll_packaging_bitInByte = findViewById(R.id.ll_packaging_bitInByte);

        this.sp_packaging_dbChoice = findViewById(R.id.sp_packaging_dbChoice);
        this.sp_packaging_formatB = findViewById(R.id.sp_packaging_formatB);
        this.sp_packaging_formatW = findViewById(R.id.sp_packaging_formatW);
        this.sp_packaging_bitInByte = findViewById(R.id.sp_packaging_bitInByte);

        this.et_packaging_data = findViewById(R.id.et_packaging_data);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        this.ip = getIntent().getStringExtra("ip");
        this.rack = getIntent().getStringExtra("rack");
        this.slot = getIntent().getStringExtra("slot");

        Log.e("Info PLC", "IP : " + this.ip + " || Rack : " + this.rack + " || Slot : " + this.slot);

        // If user with R/W privilege
        if(this.user.getPrivilege() > 0)
        {
            this.ll_packaging_writeTask.setVisibility(View.VISIBLE);
        }

        // ConnectivityManager -> accès aux réseau
        connexStatus = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // NetworkInfo -> accès aux informations du réseau
        network = connexStatus.getActiveNetworkInfo();

        /** Listener **/
        // Listener for DBB,W
        this.sp_packaging_dbChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        this.sp_packaging_formatB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        this.sp_packaging_formatW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        this.sp_packaging_bitInByte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        String dbChoice = this.sp_packaging_dbChoice.getSelectedItem().toString();
        String dbName = dbChoice.substring(3);
        int dbNum = Integer.parseInt(dbName);

        if(dbNum <= 14)
        {
            this.ll_packaging_formatB.setVisibility(View.VISIBLE);
            this.ll_packaging_formatW.setVisibility(View.GONE);

            format = this.sp_packaging_formatB.getSelectedItem().toString();
        }
        else
        {
            this.ll_packaging_formatB.setVisibility(View.GONE);
            this.ll_packaging_formatW.setVisibility(View.VISIBLE);

            format = this.sp_packaging_formatW.getSelectedItem().toString();
        }

        // Format
        if(format.equals("Byte"))
        {
            this.et_packaging_data.setHint("0 - 255");
        }
        else if(format.equals("Int"))
        {
            this.et_packaging_data.setHint("0 - 65 535");
        }
        else
        {
            this.et_packaging_data.setHint("0 - 1");
        }

        // If bit in DBB
        if(dbNum <= 14 && format.equals("Bit"))
        {
            this.ll_packaging_bitInByte.setVisibility(View.VISIBLE);

            bitNumber = this.sp_packaging_bitInByte.getSelectedItem().toString();
        }
        else
        {
            this.ll_packaging_bitInByte.setVisibility(View.GONE);
        }

        Log.e("Write", "DB : " + dbNum + " || Format : " + format + " || Bit : " + bitNumber);
    }

    /** ======== onBackPressed ======== **/
    public void onBackPressed()
    {
        if(this.bt_packaging_connectS7.getText().equals("Disconnect"))
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
    public void onPackagingClickManager(View v)
    {
        switch (v.getId())
        {
            // ======== Connect S7 ========
            case R.id.bt_packaging_connectS7:
                if(this.network != null && this.network.isConnectedOrConnecting())
                {
                    if(this.bt_packaging_connectS7.getText().equals("Connect"))
                    {
                        // ReadTaskS7
                        // Toast.makeText(this, this.network.getTypeName(), Toast.LENGTH_SHORT).show();

                        this.bt_packaging_connectS7.setText("Disconnect");

                        this.readTaskS7 = new ReadTaskS7(tv_packaging_title, tv_packaging_isActive, tv_packaging_nbrBottles, tv_packaging_nbrPillsPerBottles,
                                tv_packaging_askForPills, tv_packaging_selector, tv_packaging_bottlesGenerator);
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
                        this.bt_packaging_connectS7.setText("Connect");

                        this.tv_packaging_isActive.setText("Motor : OFF");
                        this.tv_packaging_selector.setText("Selector : OFF");
                        this.tv_packaging_bottlesGenerator.setText("Arrival of empty bottles : OFF");
                        this.tv_packaging_nbrBottles.setText("Number of bottles : 0");
                        this.tv_packaging_nbrPillsPerBottles.setText("Number of pills per bottles : 0");
                        this.tv_packaging_askForPills.setText("Asking for 0 pills");

                        this.readTaskS7.Stop();

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
            case R.id.bt_packaging_send:
                // If disconnected
                if(this.writeTaskS7 == null)
                {
                    Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
                // If empty field
                else if(this.et_packaging_data.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int dbNumber = Integer.parseInt(this.sp_packaging_dbChoice.getSelectedItem().toString().substring(3));
                    String format;

                    // DBB
                    if(dbNumber <= 14)
                    {
                        format = this.sp_packaging_formatB.getSelectedItem().toString();
                    }
                    else    // DBW
                    {
                        format = this.sp_packaging_formatW.getSelectedItem().toString();
                    }

                    int dataValue;

                    try
                    {
                        dataValue = Integer.parseInt(this.et_packaging_data.getText().toString());

                        // If Bit (0 - 1)
                        if(format.equals("Bit"))
                        {
                            if(dataValue != 0 && dataValue != 1)
                            {
                                Toast.makeText(this, "Please enter a correct value (0 - 1)", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                int bitNumber = Integer.parseInt(this.sp_packaging_bitInByte.getSelectedItem().toString());

                                this.writeTaskS7.setWriteBool(dbNumber, (int)Math.pow(2, bitNumber), dataValue);
                            }
                        }
                        // If Byte (0 - 255)
                        else if(dbNumber <= 14)
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
            case R.id.bt_packaging_disconnectFromPlc:
                if(this.bt_packaging_connectS7.getText().equals("Disconnect"))
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
