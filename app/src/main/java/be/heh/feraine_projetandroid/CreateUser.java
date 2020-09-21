package be.heh.feraine_projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.DataBaseHelper;

public class CreateUser extends AppCompatActivity
{
    // ======== Attributs ========
    EditText et_createUser_loginMail;
    EditText et_createUser_firstName;
    EditText et_createUser_lastName;
    EditText et_createUser_password;
    EditText et_createUser_confirmPassword;

    CheckBox cb_createUser_isSuperUser;

    Button bt_createUser_createAccount;

    // DataBase
    DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    // ======== onCreate ========
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Get views
        this.et_createUser_loginMail = findViewById(R.id.et_createUser_loginMail);
        this.et_createUser_firstName = findViewById(R.id.et_createUser_firstName);
        this.et_createUser_lastName = findViewById(R.id.et_createUser_lastName);
        this.et_createUser_password = findViewById(R.id.et_createUser_password);
        this.et_createUser_confirmPassword = findViewById(R.id.et_createUser_confirmPassword);

        this.cb_createUser_isSuperUser = findViewById(R.id.cb_createUser_isSuperUser);

        this.bt_createUser_createAccount = findViewById(R.id.bt_createUser_createAccount);

        // If db empty -> create Super Admin

    }

    // ======== onClickManager ========
    public void onCreateUserClickManager(View v)
    {
        switch(v.getId())
        {
            // ==== Create Account ====
            case R.id.bt_createUser_createAccount:

        }
    }
}
