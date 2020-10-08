package be.heh.feraine_projetandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class ManageUsers extends AppCompatActivity
{
    // ======== Attributs ========
    private Button bt_manageUsers_addNewUsers;

    private ListView lv_manageUsers_usersList;

    // Database
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    // User
    private User user;

    // List
    private ArrayList<String> list;
    private ArrayAdapter adapter;

    // ======== onCreate ========
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Get views
        this.bt_manageUsers_addNewUsers = findViewById(R.id.bt_manageUsers_addNewUser);

        this.lv_manageUsers_usersList = findViewById(R.id.lv_manageUsers_usersList);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        showUsersList();
    }

    // ======== showUsersList ========
    public void showUsersList()
    {
        ArrayList<User> users = dataBaseHelper.getAllUsers();

        list = new ArrayList<>();

        for(User user:users)
        {
            list.add(user.getLoginMail() + "\n" + user.getFirstName() + " " + user.getLastName());
        }

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        this.lv_manageUsers_usersList.setAdapter(adapter);
    }

    // ======== onClickManager ========
    public void onManageUsersClickManager(View v)
    {
        switch(v.getId())
        {
            case R.id.bt_manageUsers_addNewUser:
                Intent createUser = new Intent(this, CreateUser.class);
                startActivity(createUser);

                break;
        }
    }
}
