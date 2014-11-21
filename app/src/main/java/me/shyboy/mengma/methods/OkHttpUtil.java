package me.shyboy.mengma.methods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import me.shyboy.mengma.Common.OkHttpUtilResponse;
import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.SignDetail;
import me.shyboy.mengma.Common.SignList;
import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.Common.UserSign;
import me.shyboy.mengma.MainActivity;
import me.shyboy.mengma.SignDetailActivity;
import me.shyboy.mengma.SignListActivity;
import me.shyboy.mengma.database.SignDetailHelper;
import me.shyboy.mengma.database.SignHelper;
import me.shyboy.mengma.database.UserHelper;

/**
 * Created by foul on 14/11/16.
 */
public class OkHttpUtil {
   // private String result;
    private Context context;
    private OkHttpClient client;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public OkHttpUtil(Context context)
    {
        this.context = context;
        client = new OkHttpClient();
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
                    ((Activity)context).finish();
                }
                else
                {
                    new UserHelper(context).logout();
                    if(Integer.parseInt(obj.getErrcode()) == 4000)
                    {
                        Looper.prepare();
                        Toast.makeText(context,"学号或密码错误",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }
        }
    }

    public void newSign(Sign sign)
    {
        Gson gson = new Gson();
        String json = gson.toJson(sign);
        RequestBody body = RequestBody.create(MEDIA_TYPE,json);
        Request request = new Request.Builder()
                .url(SignConfig.URLNEWSIGN)
                .post(body)
                .build();
        client.newCall(request).enqueue(new NewSignCallBack());
    }

    private class NewSignCallBack implements Callback
    {
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
                OkHttpUtilResponse<Sign> obj = gson.fromJson(result,new TypeToken<OkHttpUtilResponse<Sign>>(){}.getType());
                if(Integer.parseInt(obj.getErrcode()) == SignConfig.HTTPERRORCODEOK)
                {
                    Looper.prepare();
                    Toast.makeText(context,"创建成功",Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                    Looper.loop();
                }
                else
                {
                    Looper.prepare();
                    Toast.makeText(context,"创建失败",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }
    }

    public void Sign(UserSign userSign)
    {
        Gson gson = new Gson();
        String json = gson.toJson(userSign);
        RequestBody body = RequestBody.create(MEDIA_TYPE,json);
        Request request = new Request.Builder()
                .url(SignConfig.URLSIGNCHECK)
                .post(body)
                .build();
        client.newCall(request).enqueue(new SignCheckCallBack());
    }

    class SignCheckCallBack implements Callback
    {
        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("response result","failed to sign--------");
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
                OkHttpUtilResponse<UserSign> obj = gson.fromJson(result,new TypeToken<OkHttpUtilResponse<UserSign>>(){}.getType());
                int code = Integer.parseInt(obj.getErrcode());
                if(code == SignConfig.HTTPERRORCODEOK)
                {
                    Looper.prepare();
                    Toast.makeText(context,"签到成功",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else if(code == SignConfig.HTTPERRORCODE_NOSIGNEXISTS)
                {
                    Looper.prepare();
                    Toast.makeText(context,"你还没有任何签到表",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else if(code == SignConfig.HTTP_CODE_SIGNED)
                {
                    Looper.prepare();
                    Toast.makeText(context,"已经签过到了",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else if(code == SignConfig.HTTPERRORCODE_OUTOFDATE)
                {
                    Looper.prepare();
                    Toast.makeText(context,"已经下课了.",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }
    }

    //获取列表
    public void list()
    {
        Gson gson = new Gson();
        User user = new UserHelper(context).getUser();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(MEDIA_TYPE,json);
        Request request = new Request.Builder()
                .url(SignConfig.URLLISTSIGN)
                .post(body)
                .build();
        client.newCall(request).enqueue(new ListCallBack());
    }

    class ListCallBack implements Callback
    {
        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("response result","failed to sign--------");
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
                OkHttpUtilResponse<SignList<Sign>> obj =
                        gson.fromJson(result,new TypeToken<OkHttpUtilResponse<SignList<Sign>>>(){}.getType());
                int code = Integer.parseInt(obj.getErrcode());
                if(code == SignConfig.HTTPERRORCODEOK)
                {
                    List<Sign> list = obj.getData().getList();
                    new SignHelper(context).update(list);
                    Intent intent = new Intent(context, SignListActivity.class);
                    context.startActivity(intent);

                }
                else if(code == SignConfig.HTTPERRORCODE_NOSIGNEXISTS)
                {
                    Looper.prepare();
                    Toast.makeText(context,"你还没有任何签到表",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else if(code == SignConfig.HTTPERRORCODE_TOKENWRONG)
                {
                    Looper.prepare();
                    Toast.makeText(context,"身份验证失败",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }
    }

    //获取学生-签到表
    public void details(Sign sign)
    {
        Gson gson = new Gson();
        String json = gson.toJson(sign);
        RequestBody body = RequestBody.create(MEDIA_TYPE,json);
        Request request = new Request.Builder()
                .url(SignConfig.URLSIGNDETAIL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new DetailCallBack());
    }

    class DetailCallBack implements Callback
    {
        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("response result","failed to sign--------");
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
                OkHttpUtilResponse<SignList<SignDetail>> obj =
                        gson.fromJson(result,new TypeToken<OkHttpUtilResponse<SignList<SignDetail>>>(){}.getType());
                int code = Integer.parseInt(obj.getErrcode());
                if(code == SignConfig.HTTPERRORCODEOK)
                {
                    List<SignDetail> list = obj.getData().getList();
                    new SignDetailHelper(context).update(list);
                    context.startActivity(new Intent(context, SignDetailActivity.class));

                }
                else
                {
                    Looper.prepare();
                    Toast.makeText(context,"没有响应",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }
    }
}
