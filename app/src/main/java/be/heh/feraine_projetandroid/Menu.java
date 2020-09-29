package be.heh.feraine_projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.User;

public class Menu extends AppCompatActivity
{
    // ======== Attributs ========
    private Button bt_menu_plcManagement;
    private Button bt_menu_manageUsers;
    private Button bt_menu_settings;
    private Button bt_menu_signOut;

    private User user = new User();

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

        /* // TODO
        // If SuperUser
        if(this.user.getPrivilege() == 1)
        {
            findViewById(R.id.bt_menu_manageUsers).setVisibility(View.VISIBLE);
            this.bt_menu_manageUsers.setVisibility(View.VISIBLE);
        }
        */
    }

    // ======== onClickManager ========
    public void onMenuClickManager(View v)
    {
        switch (v.getId())
        {
            // ==== PLC Management ====
            case R.id.bt_menu_plcManagement:
                // TODO

            // ==== Manage users ====
            case R.id.bt_menu_manageUsers:
                // TODO

            // ==== Settings ====
            case R.id.bt_menu_settings:
                // TODO

            // ==== Sign out ====
            case R.id.bt_menu_signOut:
                Intent logOut = new Intent(this, Login.class);
                startActivity(logOut);
                finish();
                break;
        }
    }
}
