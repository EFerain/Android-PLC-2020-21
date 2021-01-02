package be.heh.feraine_projetandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class Login extends AppCompatActivity
{
    /** ======== Attributs ======== **/
    private EditText et_login_loginMail;
    private EditText et_login_password;

    private Button bt_login_logIn;
    private Button bt_login_createUser;

    // Database
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    /** ======== onCreate ======== **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get views
        this.et_login_loginMail = findViewById(R.id.et_login_loginMail);
        this.et_login_password = findViewById(R.id.et_login_password);

        this.bt_login_logIn = findViewById(R.id.bt_login_logIn);
        this.bt_login_createUser = findViewById(R.id.bt_login_createUser);

        // ==== SUPER USER ====
        // If db is empty -> create Super User
        if(this.dataBaseHelper.getAllUsers() == null)
        {
            // Message Box
            AlertDialog.Builder firstTime = new AlertDialog.Builder(this);

            firstTime.setTitle("Welcome !")
                    .setMessage("Since it's your first time here, you first must create an account.")
                    .setPositiveButton("Let's do this", new DialogInterface.OnClickListener()
                    {
                        // Click on positive button
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Create user
                            Intent createUser = new Intent(Login.this, CreateUser.class);
                            createUser.putExtra("superUser", true);
                            startActivity(createUser);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    /** ======== onClickManager ======== **/
    public void onLoginClickManager(View v)
    {
        switch(v.getId())
        {
            // ======== Log In ========
            case R.id.bt_login_logIn:
                // ==== If Login is empty ====
                if(this.et_login_loginMail.getText().toString().isEmpty())
                {
                    // Toast.makeText(this, "Invalid login/mail", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Invalid login and/or password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User user = this.dataBaseHelper.getUser(this.et_login_loginMail.getText().toString());

                    try
                    {
                        if(user.getLoginMail().equals(this.et_login_loginMail.getText().toString()))
                        {
                            if(user.getPassword().equals(this.et_login_password.getText().toString()))
                            {
                                // Log.i("Log in", "User : " + user.getLoginMail() + " // " + "Privilege : " + user.getPrivilege());

                                // Go to Menu.class
                                Intent menu = new Intent(this, Menu.class);
                                menu.putExtra("userData", user);
                                startActivity(menu);
                                finish();
                            }
                            // -- Wrong password --
                            else
                            {
                                // Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, "Invalid login and/or password", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                    // ---- User not found ----
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Error : user not found", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            // ======== Create User ========
            case R.id.bt_login_createUser:
                Intent createUser = new Intent(this, CreateUser.class);
                startActivity(createUser);

                break;
        }
    }
}
