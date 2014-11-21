package me.shyboy.mengma.Common;

/**
 * Created by foul on 14/11/20.
 *
 */
public class Sign {
    private int id;
    private String user_sno;
    private String started_at;
    private String ended_at;
    private String description;
    private String access_token;
    private int none;
    public Sign(String user_sno,String started_at,String ended_at,String description,String access_token)
    {
        this.user_sno = user_sno;
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.description = description;
        this.access_token = access_token;

    }
    public Sign(int id,String user_sno,String started_at,String ended_at,String description,String access_token)
    {
        this.id = id;
        this.user_sno = user_sno;
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.description = description;
        this.access_token = access_token;
    }
    public Sign(int id,String started_at,String ended_at,String description)
    {
        this.id = id;
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }
    public String getUser_sno()
    {
        return user_sno;
    }
    public String getStarted_at()
    {
        return started_at;
    }
    public String getEnded_at()
    {
        return ended_at;
    }
    public String getDescription()
    {
        return description;
    }
    public String getAccess_token()
    {
        return access_token;
    }
    public String getDate()
    {
        return this.started_at.substring(0,10);
    }
    public String getTime()
    {
        return this.started_at.substring(11,16) + " —— " + this.ended_at.substring(11,16);
    }
}
