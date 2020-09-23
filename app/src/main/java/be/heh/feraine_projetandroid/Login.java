package be.heh.feraine_projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class Login extends AppCompatActivity
{
    // ======== Attributs ========
    private EditText et_login_loginMail;
    private EditText et_login_password;

    private Button bt_login_logIn;

    // Database
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    private User user = new User();

    // ======== onCreate ========
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get views
        this.et_login_loginMail = findViewById(R.id.et_login_loginMail);
        this.et_login_password = findViewById(R.id.et_login_password);

        this.bt_login_logIn = findViewById(R.id.bt_login_logIn);

        this.user = new User();

        // If db is empty -> create Super User
        if(this.dataBaseHelper.getAllUsers().getCount() == 0)
        {
            Intent createUser = new Intent(this, CreateUser.class);
            createUser.putExtra("superUser", true);
            startActivity(createUser);
        }
    }

    // ======== onClickManager ========
    public void onLoginClickManager(View v)
    {
        switch(v.getId())
        {
            // ==== Log In ====
            case R.id.bt_login_logIn:
                // If Login is empty
                if(this.et_login_loginMail.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Invalid login/mail", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        if(this.user.getLoginMail().equals(this.et_login_loginMail.getText().toString()))
                        {
                            if(this.user.getPassword().equals(this.et_login_password.getText().toString()))
                            {
                                Intent menu = new Intent(this, Menu.class);
                                startActivity(menu);
                                finish();
                            }
                            // Wrong password
                            else
                            {
                                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    // User not found
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Error : user not found", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}
