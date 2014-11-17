package me.shyboy.mengma.Common;

import java.util.List;
import java.util.Objects;

/**
 * Created by foul on 14/11/16.
 */
public class OkHttpUtilResponse<T> {
    private String errcode;
    private String description;
    private T data;

    public OkHttpUtilResponse()
    {     }
    public OkHttpUtilResponse(String errcode,String description, T data)
    {
        this.errcode = errcode;
        this.description = description;
        this.data = data;
    }

    public String getErrcode()
    {
        return errcode;
    }

    public String getDescription()
    {
        return description;
    }

    public T getData()
    {
        return data;
    }
}
