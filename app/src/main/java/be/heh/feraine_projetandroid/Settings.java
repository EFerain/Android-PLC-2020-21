package be.heh.feraine_projetandroid;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import be.heh.feraine_projetandroid.database.User;

public class Settings extends AppCompatActivity
{
    // ======== Attributs ========

    private User user;

    // ======== onCreate ========
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Saved data
        this.user = (User)getIntent().getSerializableExtra("userData");
    }
}
