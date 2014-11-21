package me.shyboy.mengma.methods;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foul on 14/11/19.
 */
public  class SnoGenerator {

    private Map<Integer,String> dic;
    public SnoGenerator()
    {
        dic = new HashMap<Integer, String>();
        dic.put(0,"01");
        dic.put(1,"02");
        dic.put(2,"03");
        dic.put(3,"10");
        dic.put(4,"12");
        dic.put(5,"13");
        dic.put(6,"20");
        dic.put(7,"21");
        dic.put(8,"23");
        dic.put(9,"30");
    }
    public String encode(String nums)
    {
        String code= "" + dic.get(Integer.parseInt(String.valueOf(nums.charAt(0))));
        for(int i = 1;i < nums.length(); i++)
        {
            code += "-"+dic.get(Integer.parseInt(String.valueOf(nums.charAt(i))));
        }
        Log.i("SnoGender-Encode","Code:"+code+"***********");
        return code;
    }

    public String decode(String code)
    {
        String nums = "";

        String[] codes = code.split("-");

        for(int i = 0;i < codes.length;i++)
        {
            int key = getKeyByValue(codes[i]);
            if(key == -1)
            {
                Log.e("SnoGender-Decode","Invalid Value - "+ codes[i]);
                return null;
            }
            nums += key;
        }
       Log.i("SnoGender-Decode","nums:***********"+nums);
        return nums;
    }

    public int getKeyByValue(String value)
    {
        Log.i("Value","********************************"+value);
        for(int i = 0; i < 10;i++)
        {
            if(dic.get(i).equals(value))
            {
                return i;
            }
        }
        return -1;
    }
}
