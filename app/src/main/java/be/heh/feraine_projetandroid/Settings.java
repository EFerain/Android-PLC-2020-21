package be.heh.feraine_projetandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.DataBaseHelper;
import be.heh.feraine_projetandroid.database.User;

public class Settings extends AppCompatActivity
{
    /** ======== Attributs ======== **/
    private TextView tv_settings_oldPassword;

    private EditText et_settings_loginMail;
    private EditText et_settings_firstName;
    private EditText et_settings_lastName;
    private EditText et_settings_oldPassword;
    private EditText et_settings_newPassword;
    private EditText et_settings_confirmNewPassword;

    private Button bt_settings_saveChanges;
    private Button bt_settings_deleteUser;

    private Switch sw_settings_isSuperUser;

    // Database
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    private User user;

    // LOGIN
    private static String userLoginToModify;

    /** ======== onCreate ======== **/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get views
        this.tv_settings_oldPassword = findViewById(R.id.tv_settings_oldPassword);

        this.et_settings_loginMail = findViewById(R.id.et_settings_loginMail);
        this.et_settings_firstName = findViewById(R.id.et_settings_firstName);
        this.et_settings_lastName = findViewById(R.id.et_settings_lastName);
        this.et_settings_oldPassword = findViewById(R.id.et_settings_oldPassword);
        this.et_settings_newPassword = findViewById(R.id.et_settings_newPassword);
        this.et_settings_confirmNewPassword = findViewById(R.id.et_settings_confirmNewPassword);

        this.sw_settings_isSuperUser = findViewById(R.id.sw_settings_isSuperUser);

        this.bt_settings_saveChanges = findViewById(R.id.bt_settings_saveChanges);
        this.bt_settings_deleteUser = findViewById(R.id.bt_settings_deleteUser);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");

        // User LOGIN to modify
        this.userLoginToModify = getIntent().getStringExtra("userToModify");

        // Get every data from user to modify
        User userData = this.dataBaseHelper.getUser(this.userLoginToModify);

        // Super User => show/hide elements && hint
        if(this.user.getPrivilege() == 2 && !this.user.getLoginMail().equals(this.userLoginToModify))
        {
            this.bt_settings_deleteUser.setVisibility(View.VISIBLE);

            this.tv_settings_oldPassword.setVisibility(View.GONE);
            this.et_settings_oldPassword.setVisibility(View.GONE);

            this.sw_settings_isSuperUser.setVisibility(View.VISIBLE);

            // Hint
            this.et_settings_loginMail.setHint(userData.getLoginMail());
            this.et_settings_firstName.setHint(userData.getFirstName());
            this.et_settings_lastName.setHint(userData.getLastName());
        }
        else
        {
            // Hint
            this.et_settings_loginMail.setHint(this.user.getLoginMail());
            this.et_settings_firstName.setHint(this.user.getFirstName());
            this.et_settings_lastName.setHint(this.user.getLastName());
        }

        // Switch
        if(userData.getPrivilege() == 1)
        {
            this.sw_settings_isSuperUser.setChecked(true);
        }
    }

    /** ======== onClickManager ======== **/
    public void onSettingsClickManager(View v)
    {
        switch(v.getId())
        {
            // ======== Save changes ========
            /** If EditText is empty -> no change **/
            case R.id.bt_settings_saveChanges:
                final User modifiedUser = new User();

                boolean doTheRedirection = true;

                // ==== First name ====
                // New first name
                if(!this.et_settings_firstName.getText().toString().isEmpty())
                {
                    modifiedUser.setFirstName(this.et_settings_firstName.getText().toString());

                    // Log.i("Update", "First name : " + user.getFirstName() + "\n");
                }

                // ==== Last name ====
                // New last name
                if(!this.et_settings_lastName.getText().toString().isEmpty())
                {
                    modifiedUser.setLastName(this.et_settings_lastName.getText().toString());

                    // Log.i("Update", "Last name : " + user.getLastName() + "\n");
                }

                // ==== From Manage Users ====
                if(this.user.getPrivilege() == 2 && !this.user.getLoginMail().equals(this.userLoginToModify))
                {
                    // -- Password --
                    if(!this.et_settings_newPassword.getText().toString().isEmpty() && !this.et_settings_confirmNewPassword.getText().toString().isEmpty())
                    {
                        // If newPassword != confirmNewPassword
                        if(!this.et_settings_newPassword.getText().toString().equals(this.et_settings_confirmNewPassword.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();

                            doTheRedirection = false;
                        }
                        // If password < 4 characters
                        else if(this.et_settings_newPassword.getText().toString().length() < 4)
                        {
                            Toast.makeText(getApplicationContext(), "Password is too short. Please enter a password with at least 4 characters",
                                    Toast.LENGTH_SHORT).show();

                            doTheRedirection = false;
                        }
                        // OK
                        else
                        {
                            modifiedUser.setPassword(this.et_settings_newPassword.getText().toString());
                        }
                    }
                    else if(this.et_settings_newPassword.getText().toString().isEmpty() != this.et_settings_confirmNewPassword.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Error : If you want to change the password, you must fill the two password fields", Toast.LENGTH_SHORT).show();

                        doTheRedirection = false;
                    }

                    if(this.sw_settings_isSuperUser.isChecked())
                    {
                        modifiedUser.setPrivilege(1);
                    }
                    else
                    {
                        modifiedUser.setPrivilege(0);
                    }

                    // Log.i("Update", "Privilege : " + user.getPrivilege() + "\n");

                    // ==== Login ====
                    if(!this.et_settings_loginMail.getText().toString().isEmpty())
                    {
                        modifiedUser.setId(this.dataBaseHelper.getUser(this.userLoginToModify).getId());
                        modifiedUser.setLoginMail(this.et_settings_loginMail.getText().toString());
                    }
                    else
                    {
                        modifiedUser.setLoginMail(this.userLoginToModify);
                    }

                    // SAVE
                    this.dataBaseHelper.updatePrivilege(modifiedUser);
                }
                // ==== From Settings ====
                else
                {
                    // -- Password --
                    if(!this.et_settings_oldPassword.getText().toString().isEmpty() && !this.et_settings_newPassword.getText().toString().isEmpty() &&
                            !this.et_settings_confirmNewPassword.getText().toString().isEmpty())
                    {
                        // If new password = actual password
                        if(this.et_settings_newPassword.getText().toString().equals(user.getPassword()))
                        {
                            Toast.makeText(getApplicationContext(), "New password cannot be the same as the actual one", Toast.LENGTH_SHORT).show();

                            doTheRedirection = false;
                        }
                        // If old password != actual password
                        else if(!this.et_settings_oldPassword.getText().toString().equals(user.getPassword()))
                        {
                            Toast.makeText(getApplicationContext(), "Wrong old password", Toast.LENGTH_SHORT).show();

                            doTheRedirection = false;
                        }
                        // If newPassword != confirmPassword
                        else if(!this.et_settings_newPassword.getText().toString().equals(this.et_settings_confirmNewPassword.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(), "New and confirmed passwords are different", Toast.LENGTH_SHORT).show();

                            doTheRedirection = false;
                        }
                        // If password < 4 characters
                        else if(this.et_settings_newPassword.getText().toString().length() < 4)
                        {
                            Toast.makeText(getApplicationContext(), "Password is too short. Please enter a password with at least 4 characters",
                                    Toast.LENGTH_SHORT).show();

                            doTheRedirection = false;
                        }
                        // Ok
                        else
                        {
                            modifiedUser.setPassword(this.et_settings_newPassword.getText().toString());
                        }
                    }
                    else if((this.et_settings_oldPassword.getText().toString().isEmpty() != this.et_settings_newPassword.getText().toString().isEmpty()) ||
                            (this.et_settings_confirmNewPassword.getText().toString().isEmpty() != this.et_settings_oldPassword.getText().toString().isEmpty()))
                    {
                        Toast.makeText(getApplicationContext(), "Error : If you want to change your password, you must fill the three password fields", Toast.LENGTH_SHORT).show();

                        doTheRedirection = false;
                    }

                    // ==== Login ====
                    modifiedUser.setId(this.user.getId());

                    if(!this.et_settings_loginMail.getText().toString().isEmpty())
                    {
                        modifiedUser.setLoginMail(this.et_settings_loginMail.getText().toString());
                    }
                    else
                    {
                        modifiedUser.setLoginMail(this.user.getLoginMail());
                    }

                }

                // Log.i("Full update",  "Login : " + modifiedUser.getLoginMail() + " || First name : " + modifiedUser.getFirstName() +
                //        " || Last name : " + modifiedUser.getLastName() + " || Password : " + modifiedUser.getPassword());

                // SAVE
                this.dataBaseHelper.updateLoginMail(modifiedUser);

                if(modifiedUser.getFirstName() != null)
                {
                    this.dataBaseHelper.updateFirstName(modifiedUser);
                }

                if(modifiedUser.getLastName() != null)
                {
                    this.dataBaseHelper.updateLastName(modifiedUser);
                }

                if(modifiedUser.getPassword() != null)
                {
                    this.dataBaseHelper.updatePassword(modifiedUser);
                }

                // ==== Redirection ====
                if(((user.getPrivilege() == 2 && !user.getLoginMail().equals(userLoginToModify)) ||
                        (user.getLoginMail().equals(userLoginToModify) && getIntent().getBooleanExtra("fromManageUsers", false))) &&
                        doTheRedirection)
                {
                    finish();
                }
                else if(doTheRedirection)
                {
                    Intent menu = new Intent(this, Menu.class);
                    menu.putExtra("userData", this.user);
                    startActivity(menu);
                    finishAffinity();
                }

                break;
                
            // ======== Delete User ========
            case R.id.bt_settings_deleteUser:
                // -- Delete User --
                // AlertDialog
                final Intent manageUsers = new Intent(this, ManageUsers.class);

                AlertDialog.Builder deleteUserDialog = new AlertDialog.Builder(this);

                deleteUserDialog.setTitle("Warning")
                        .setMessage("Are you sure you want to delete this user ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                User userToDelete = dataBaseHelper.getUser(userLoginToModify);
                                dataBaseHelper.deleteUser(userToDelete);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

                break;
        }
    }
}
