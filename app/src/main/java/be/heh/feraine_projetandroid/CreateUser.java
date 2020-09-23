package be.heh.feraine_projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class CreateUser extends AppCompatActivity
{
    // ======== Attributs ========
    private EditText et_createUser_loginMail;
    private EditText et_createUser_firstName;
    private EditText et_createUser_lastName;
    private EditText et_createUser_password;
    private EditText et_createUser_confirmPassword;

    private CheckBox cb_createUser_isSuperUser;

    private Button bt_createUser_createAccount;

    // DataBase
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    private User user = new User();

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
    }

    // ======== onClickManager ========
    public void onCreateUserClickManager(View v)
    {
        switch(v.getId())
        {
            // ==== Create Account ====
            case R.id.bt_createUser_createAccount:
                DataBaseHelper db = new DataBaseHelper(this);

                this.user.setLoginMail(this.et_createUser_loginMail.getText().toString());
                this.user.setPassword(this.et_createUser_password.getText().toString());
                this.user.setFirstName(this.et_createUser_firstName.getText().toString());
                this.user.setLastName(this.et_createUser_lastName.getText().toString());

                // -- If first start --
                if(getIntent().getBooleanExtra("SuperUser", false))
                {
                    user.setPrivilege(1);
                }
                else
                {
                    user.setPrivilege(0);
                }

                // -- If missing field(s) --
                if(this.et_createUser_loginMail.getText().toString().isEmpty() ||
                this.et_createUser_firstName.getText().toString().isEmpty() ||
                this.et_createUser_lastName.getText().toString().isEmpty() ||
                this.et_createUser_password.getText().toString().isEmpty() ||
                this.et_createUser_confirmPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Missing fields !", Toast.LENGTH_SHORT).show();
                }

                // -- If password != confirmPassword --
                if(!this.et_createUser_password.getText().toString().equals(this.et_createUser_confirmPassword.getText().toString()))
                {
                    Toast.makeText(this, "Passwords are different", Toast.LENGTH_SHORT).show();
                }

                // -- If password < 4 ? --
                if(this.et_createUser_password.getText().toString().length() < 4)
                {
                    Toast.makeText(getApplicationContext(), "Password is too short. Please enter a password with at least 4 ?",
                            Toast.LENGTH_SHORT).show();
                }

                // -- If user is already existing --
                if(dataBaseHelper.getUser(this.et_createUser_loginMail.getText().toString()) != null)
                {
                    Toast.makeText(getApplicationContext(), "Error : User already exists", Toast.LENGTH_SHORT).show();
                }

                Intent menu = new Intent(this, Menu.class);
                startActivity(menu);
                finish();
        }
    }
}
