package be.heh.feraine_projetandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper
{
    //
    public static final String USER_TABLE = "user_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOGINMAIL = "mail";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_PRIVILEGE = "privilege";


    public DataBaseHelper(@Nullable Context context)
    {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + USER_TABLE + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGINMAIL + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                COLUMN_LASTNAME + " TEXT NOT NULL, " +
                COLUMN_PRIVILEGE + " INTEGER NOT NULL );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    // ======== METHODES ========
    // Add User
    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Write into DB

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LOGINMAIL, user.getLoginMail());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_FIRSTNAME, user.getFirstName());
        cv.put(COLUMN_LASTNAME, user.getLastName());
        cv.put(COLUMN_PRIVILEGE, user.getPrivilege());

        db.insert(USER_TABLE, null, cv);
    }
}
