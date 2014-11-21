package me.shyboy.mengma.Common;

/**
 * Created by foul on 14/11/20.
 */
public class UserSign {
    private String sno;
    private String sno_admin;
    private String access_token;
    private int none;

    public UserSign(String sno,String sno_admin,String access_token)
    {
        this.sno = sno;
        this.sno_admin = sno_admin;
        this.access_token = access_token;
    }
}
