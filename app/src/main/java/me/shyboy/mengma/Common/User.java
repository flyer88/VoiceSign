package me.shyboy.mengma.Common;

/**
 * Created by foul on 14/11/16.
 */
public class User {
    private String sno;
    private String name;
    private String password;
    private String access_token;
    private int pid;
    private int none;
    public User(String sno,String name,String password,String access_token,int pid)
    {
        this.sno = sno;
        this.name = name;
        this.password = password;
        this.access_token = access_token;
        this.pid = pid;
    }

    public User(String sno,String name,int pid)
    {
        this.sno = sno;
        this.name = name;
        this.pid = pid;
    }

    public void  setSno(String sno)
    {
        this.sno = sno;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public  void setPid(int pid)
    {
        this.pid = pid;
    }

    private void setPassword(String password)
    {
        this.password = password;
    }

    public String getSno()
    {
        return sno;
    }

    public String getAccess_token()
    {
        return access_token;
    }

    public String getPassword()
    {
        return password;
    }

    public int getPid()
    {
        return pid;
    }

    public String getName()
    {
        return name;
    }
}
