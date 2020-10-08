package be.heh.feraine_projetandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class Menu extends AppCompatActivity
{
    // ======== Attributs ========
    private Button bt_menu_plcManagement;
    private Button bt_menu_manageUsers;
    private Button bt_menu_settings;
    private Button bt_menu_signOut;

    private User user;

    // ======== onCreate ========
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get views
        this.bt_menu_plcManagement = findViewById(R.id.bt_menu_plcManagement);
        this.bt_menu_manageUsers = findViewById(R.id.bt_menu_manageUsers);
        this.bt_menu_settings = findViewById(R.id.bt_menu_settings);
        this.bt_menu_signOut = findViewById(R.id.bt_menu_signOut);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        // If SuperUser
        if(this.user.getPrivilege() == 1)
        {
            this.bt_menu_manageUsers.setVisibility(View.VISIBLE);
        }
    }

    // ======== onBackPressed ========
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

    // ======== onClickManager ========
    public void onMenuClickManager(View v)
    {
        switch (v.getId())
        {
            // ==== PLC Management ====
            case R.id.bt_menu_plcManagement:
                // TODO button PLC Management
                // plcManagement.putExtra("userData", user);
                break;

            // ==== Manage users ====
            case R.id.bt_menu_manageUsers:
                Intent manageUsers = new Intent(this, ManageUsers.class);
                manageUsers.putExtra("userData", user);
                startActivity(manageUsers);

                break;

            // ==== Settings ====
            case R.id.bt_menu_settings:
                Intent settings = new Intent(this, Settings.class);
                settings.putExtra("userData", user);
                startActivity(settings);

                break;

            // ==== Sign out ====
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
