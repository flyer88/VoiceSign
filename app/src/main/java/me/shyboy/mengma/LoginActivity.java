package me.shyboy.mengma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        isLogined();
        setContentView(R.layout.activity_login);

        et_sno = (EditText)findViewById(R.id.login_sno);
        et_password = (EditText)findViewById(R.id.login_password);
        btn_login = (Button)findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onStop()
    {
        super.onStop();
        Log.i("login","login success,->main.");
        this.finish();
    }
}
