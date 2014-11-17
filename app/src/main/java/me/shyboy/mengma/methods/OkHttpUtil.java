package me.shyboy.mengma.methods;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import me.shyboy.mengma.Common.OkHttpUtilResponse;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.MainActivity;
import me.shyboy.mengma.database.UserHelper;

/**
 * Created by foul on 14/11/16.
 */
public class OkHttpUtil {
   // private String result;
    private Context context;
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public OkHttpUtil(Context context)
    {
        this.context = context;
       // this.result = "";
    }
    public User login()
    {
        UserHelper userHelper = new UserHelper(context);
        User user = userHelper.getUser();
        if(user != null)
        {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            RequestBody body = RequestBody.create(MEDIA_TYPE,json);
            Request request = new Request.Builder()
                    .url(SignConfig.URLLOGIN)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new ResponseCallBack(context));

        }
        return null;
    }

    private class ResponseCallBack implements Callback
    {

        private Context context;

        public ResponseCallBack(Context context)
        {
            this.context = context;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("response result","failed to post");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful())
            {
                Log.i("response result","response failed");
                throw new IOException();
            }
            else
            {
                String result = response.body().string();
                Gson gson = new Gson();
                OkHttpUtilResponse<User> obj = gson.fromJson(result,new TypeToken<OkHttpUtilResponse<User>>(){}.getType());
                if(Integer.parseInt(obj.getErrcode()) == SignConfig.HTTPERRORCODEOK)
                {
                   // Log.i("errcode panduan","ok");
                    Intent intent = new Intent(context, MainActivity.class);
                    UserHelper helper = new UserHelper(context);
                    helper.update(obj.getData());
                    context.startActivity(intent);
                }
            }
        }
    }
}
