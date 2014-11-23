package me.shyboy.mengma;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.Common.UserSign;
import me.shyboy.mengma.database.MemberHelper;
import me.shyboy.mengma.database.UserHelper;


public class UserManagerActivity extends Activity {

    private ImageButton bt_out;
    private ListView listView;
    private List<User> list;
    private OkHttpClient client;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i("handle","hello world");
                    initListView();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_manager);
        client = new OkHttpClient();
        bt_out = (ImageButton)findViewById(R.id.manager_out);
        listView = (ListView)findViewById(R.id.manager_listview);
        initListView();
        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initListView()
    {
        list = new MemberHelper(UserManagerActivity.this).getMembers();
        listView.setAdapter(new ListAdapter(UserManagerActivity.this,R.layout.item_user,list));
    }

    private class ListAdapter extends ArrayAdapter<User>
    {
        private int resourceId;
        public ListAdapter(Context context,int resourceId,List<User> objects)
        {
            super(context,resourceId,objects);
            this.resourceId = resourceId;
        }
        public View getView(int position,View convertView,ViewGroup parent)
        {
            User user = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null)
            {
                view = LayoutInflater.from(getContext()).inflate(resourceId,null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView)view.findViewById(R.id.manager_item_name);
                viewHolder.sno = (TextView)view.findViewById(R.id.manager_item_sno);
                viewHolder.action = (Button)view.findViewById(R.id.manager_item_action);
                view.setTag(viewHolder);
            }
            else
            {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.name.setText(user.getName());
            viewHolder.sno.setText(user.getSno());
            if(user.getPid() == 1)
            {
                viewHolder.action.setVisibility(View.INVISIBLE);
            }
            if(user.getPid() < 3)
            {
                viewHolder.action.setBackgroundResource(R.drawable.manager_bt_red);
                viewHolder.action.setText("撤销管理员");
            }
            viewHolder.action.setOnClickListener(new ManagerOnclickListener(position));
            return  view;
        }
        class ViewHolder
        {
            TextView name;
            TextView sno;
            Button action;
        }
    }

    private class ManagerOnclickListener implements View.OnClickListener
    {
        private int position;

        public  ManagerOnclickListener(int position)
        {
            this.position = position;
        }
        public void onClick(View v)
        {
            if(SignConfig.isNetworkConnected(UserManagerActivity.this) == false)
            {
                Toast.makeText(UserManagerActivity.this,"凑 ~ ~ 没联网",Toast.LENGTH_SHORT).show();
                return ;
            }
            manager(position);
            handler.sendEmptyMessageDelayed(1,2000);
        }
    }

    //添加/撤销管理员
    public void manager(int position)
    {
        User user = list.get(position);
        int pid  = user.getPid() == 2 ? 7 : 2;
        User admin = new UserHelper(UserManagerActivity.this).getUser();
        user.setPid(pid);
        list.set(position,user);
        Gson gson = new Gson();
        String json = gson.toJson(new UserSign(user.getSno(),admin.getSno(),admin.getAccess_token()));
        RequestBody body = RequestBody.create(MEDIA_TYPE,json);
        Request request = new Request.Builder()
                .url(SignConfig.URLUSERMANAGER)
                .post(body)
                .build();
        client.newCall(request).enqueue(new ManagerCallBack());
    }

    class ManagerCallBack implements Callback
    {
        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("response result", "failed to sign--------");
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
                Log.i("JSON",result);
                Gson gson = new Gson();
                OkHttpUtilResponse<UserSign> obj =
                        gson.fromJson(result,new TypeToken<OkHttpUtilResponse<UserSign>>(){}.getType());
                int code = Integer.parseInt(obj.getErrcode());
                if(code == SignConfig.HTTPERRORCODEOK)
                {
                    new MemberHelper(UserManagerActivity.this).update(list);
                }
                else
                {
                    Looper.prepare();
                    Toast.makeText(UserManagerActivity.this,"设置失败",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }
    }

}
