package me.shyboy.mengma.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public String getWeek() {

        String pTime = getDate();
        String Week = "周";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }
}
