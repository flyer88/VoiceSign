package me.shyboy.mengma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.database.UserHelper;
import me.shyboy.mengma.methods.OkHttpUtil;


public class LoginActivity extends Activity {
    private EditText et_sno;
    private EditText et_password;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isLogined();
        setContentView(R.layout.layout_welcome);
        welHandler.sendEmptyMessageDelayed(1, 3000);
    }

    //检测是否已经登录
    private void isLogined()
    {
        OkHttpUtil okHttpUtil = new OkHttpUtil(LoginActivity.this);
        User user = okHttpUtil.login();

        if(user == null)
        {
            Log.i("login","login failed,when start first.");
        }
        else
        {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void initLogin()
    {
        et_sno = (EditText)findViewById(R.id.login_sno);
        et_password = (EditText)findViewById(R.id.login_password);
        btn_login = (Button)findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SignConfig.isNetworkConnected(LoginActivity.this) == false)
                {
                    Toast.makeText(LoginActivity.this,"凑 ~ ~ 没联网",Toast.LENGTH_SHORT).show();
                    return ;
                }
                String sno = et_sno.getText().toString();
                String pwd = et_password.getText().toString();
                if(sno.length() != SignConfig.SNOLENGTH)
                {
                    Toast.makeText(LoginActivity.this,"学号应该包含"+SignConfig.SNOLENGTH+"位数字",Toast.LENGTH_SHORT).show();
                    return ;

                }
                if (pwd.length() < SignConfig.PASSWORDMINLENGTH || pwd.length() > SignConfig.PASSWORDMAXLENGTH)
                {
                    Toast.makeText(LoginActivity.this,"密码应该包含"+SignConfig.PASSWORDMINLENGTH+"-"
                                    +SignConfig.PASSWORDMAXLENGTH+"个字符",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User(sno,"",pwd,"",7);
                UserHelper helper= new UserHelper(LoginActivity.this);
                helper.update(user);
                OkHttpUtil http = new OkHttpUtil(LoginActivity.this);
                http.login();
            }
        });

        ((TextView)findViewById(R.id.link_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.flappyant.com/VoiceSignListener/public/register");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
    }
    Handler welHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            setContentView(R.layout.activity_login);
            initLogin();
        }

    };
    @Override
    public void onStop()
    {
        super.onStop();
    }
}
