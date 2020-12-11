package be.heh.feraine_projetandroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class ManageUsers extends AppCompatActivity
{
    /** ======== Attributs ======== **/
    private Button bt_manageUsers_addNewUsers;
    private Button bt_manageUsers_backMenu;

    private ListView lv_manageUsers_usersList;
    // private LinearLayout lv_manageUsers_usersList;

    // Database
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    // User
    private User user;

    // List
    private ArrayList<String> list;
    private ArrayAdapter adapter;

    /** ======== onCreate ======== **/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Get views
        this.bt_manageUsers_addNewUsers = findViewById(R.id.bt_manageUsers_addNewUser);
        this.bt_manageUsers_backMenu = findViewById(R.id.bt_manageUsers_backMenu);

        this.lv_manageUsers_usersList = findViewById(R.id.lv_manageUsers_usersList);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        /** ======== onItemClick ======== **/
        this.lv_manageUsers_usersList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent settings = new Intent(ManageUsers.this, Settings.class);
                settings.putExtra("userData", user);
                settings.putExtra("userToModify", lv_manageUsers_usersList.getItemAtPosition(position).toString()
                        .split("\n")[0]);
                startActivity(settings);
            }
        });

        showUsersList();
    }

    /** ======== showUsersList ======== **/
    public void showUsersList()
    {
        ArrayList<User> users = dataBaseHelper.getAllUsers();

        list = new ArrayList<>();

        for(User user:users)
        {
            list.add(user.getLoginMail() + "\n" + user.getFirstName() + " " + user.getLastName());

            /* TODO garder ?
            // Bouton next to User in ManageUsers
            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.setGravity(Gravity.CENTER_HORIZONTAL);

            LinearLayout.LayoutParams paramtv = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams parambt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView tv = new TextView(this);
            tv.setText(user.getLoginMail());
            tv.setGravity(Gravity.LEFT);
            tv.setLayoutParams(paramtv);

            Button btn = new Button(this);
            btn.setText("Delete");
            btn.setGravity(Gravity.END);
            btn.setLayoutParams(parambt);

            ll.addView(tv);
            ll.addView(btn);

            this.lv_manageUsers_usersList.addView(ll);
            */
        }

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        this.lv_manageUsers_usersList.setAdapter(adapter);
    }

    /** ======== onClickManager ======== **/
    public void onManageUsersClickManager(View v)
    {
        switch(v.getId())
        {
            // ======== Add New User ========
            case R.id.bt_manageUsers_addNewUser:
                Intent createUser = new Intent(this, CreateUser.class);
                createUser.putExtra("userData", user);
                createUser.putExtra("fromManageUsers", true);
                startActivity(createUser);

                break;

            // ======== Back to the Menu ========
            case R.id.bt_manageUsers_backMenu:
                Intent menu = new Intent(this, Menu.class);
                menu.putExtra("userData", user);
                startActivity(menu);

                break;
        }
    }

    /** ======== Update ======== **/
    @Override
    protected void onResume()
    {
        super.onResume();

        showUsersList();
    }
}
