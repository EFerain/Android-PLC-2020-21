package be.heh.feraine_projetandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.User;
import be.heh.feraine_projetandroid.plcManagement.PlcSettings;

public class Menu extends AppCompatActivity
{
    /** ======== Attributs ======== **/
    private Button bt_menu_liquidRegulation;
    private Button bt_menu_packaging;
    private Button bt_menu_manageUsers;
    private Button bt_menu_settings;
    private Button bt_menu_signOut;

    private User user;

    /** ======== onCreate ======== **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get views
        this.bt_menu_liquidRegulation = findViewById(R.id.bt_menu_liquidRegulation);
        this.bt_menu_packaging = findViewById(R.id.bt_menu_packaging);
        this.bt_menu_manageUsers = findViewById(R.id.bt_menu_manageUsers);
        this.bt_menu_settings = findViewById(R.id.bt_menu_settings);
        this.bt_menu_signOut = findViewById(R.id.bt_menu_signOut);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        // If Super User
        if(this.user.getPrivilege() == 2)
        {
            this.bt_menu_manageUsers.setVisibility(View.VISIBLE);
        }
    }

    /** ======== onBackPressed ======== **/
    public void onBackPressed()
    {
        final Intent signOut = new Intent(this, Login.class);

        AlertDialog.Builder signOutDialog = new AlertDialog.Builder(this);

        signOutDialog.setTitle("Sign out")
                .setMessage("Are you sure you want to sign out ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        startActivity(signOut);
                        finishAffinity();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /** ======== onClickManager ======== **/
    public void onMenuClickManager(View v)
    {
        switch (v.getId())
        {
            // ======== PLC Management ========
            // ==== Liquid Regulation ====
            case R.id.bt_menu_liquidRegulation:
                Intent liquidRegulation = new Intent(this, PlcSettings.class);
                liquidRegulation.putExtra("userData", user);
                liquidRegulation.putExtra("plc", "liquidRegulation");
                startActivity(liquidRegulation);

                break;

            // ==== Packaging ====
            case R.id.bt_menu_packaging:
                Intent packaging = new Intent(this, PlcSettings.class);
                packaging.putExtra("userData", user);
                packaging.putExtra("plc", "packaging");
                startActivity(packaging);

                break;

            // ======== Manage users ========
            case R.id.bt_menu_manageUsers:
                Intent manageUsers = new Intent(this, ManageUsers.class);
                manageUsers.putExtra("userData", user);
                startActivity(manageUsers);

                break;

            // ======== Settings ========
            case R.id.bt_menu_settings:
                Intent settings = new Intent(this, Settings.class);
                settings.putExtra("userData", user);
                settings.putExtra("userToModify", user.getLoginMail());
                startActivity(settings);

                break;

            // ======== Sign out ========
            case R.id.bt_menu_signOut:
                final Intent signOut = new Intent(this, Login.class);

                AlertDialog.Builder signOutDialog = new AlertDialog.Builder(this);

                signOutDialog.setTitle("Sign out")
                        .setMessage("Are you sure you want to sign out ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                startActivity(signOut);
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

                break;
        }
    }
}
