package be.heh.feraine_projetandroid.database;

import java.io.Serializable;

public class User implements Serializable
{
    // ======== Attributs ========
    private int id;
    private String loginMail;
    private String password;
    private String firstName;
    private String lastName;
    private int privilege;  // 0 -> lecture seul / 1 -> lecture et écriture

    // ======== Constructeur ========
    public User()
    {
        // VOID
    }

    public User(int _id, String _loginMail, String _password, String _firstName, String _lastName, int _privilege)
    {
        this.id = _id;
        this.loginMail = _loginMail;
        this.password = _password;
        this.firstName = _firstName;
        this.lastName = _lastName;
        this.privilege = _privilege;
    }

    // ======== Accesseurs & modificateurs ========
    // id
    public int getId()
    {
        return this.id;
    }
    public void setId(int _id)
    {
        this.id = _id;
    }

    // loginMail
    public String getLoginMail()
    {
        return this.loginMail;
    }
    public void setLoginMail(String _loginMail)
    {
        this.loginMail = _loginMail;
    }

    // password
    public String getPassword()
    {
        return this.password;
    }
    public void setPassword(String _password)
    {
        this.password = _password;
    }

    // firstName
    public String getFirstName()
    {
        return this.firstName;
    }
    public void setFirstName(String _firstName)
    {
        this.firstName = _firstName;
    }

    // lastName
    public String getLastName()
    {
        return this.lastName;
    }
    public void setLastName(String _lastName)
    {
        this.lastName = _lastName;
    }

    // privilege
    public int getPrivilege()
    {
        return this.privilege;
    }
    public void setPrivilege(int _privilege)
    {
        this.privilege = _privilege;
    }
}
