package me.shyboy.mengma.Common;

/**
 * Created by foul on 14/11/21.
 */
public class SignDetail {
    private String sno;
    private int sign_id;
    private String description;
    private String name;
    private String created_at;
    private int state;
    public SignDetail(String sno,int sign_id,String description,String user_name,String created_at,int state)
    {
        this.sno = sno;
        this.sign_id = sign_id;
        this.description = description;
        this.name = user_name;
        this.created_at = created_at;
        this.state = state;
    }
    public String getSno(){return sno;}
    public int getSign_id(){return sign_id;}
    public String getDescription(){return description;}
    public String getName(){return name;}
    public String getCreated_at(){return created_at;}
    public String getTime()
    {
        if(created_at.length() != 0)
            return this.created_at.substring(11,16);
        return "尚未签到";
    }
    public int getState(){return state;}
}
