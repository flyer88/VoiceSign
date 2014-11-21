package me.shyboy.mengma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.Common.UserSign;
import me.shyboy.mengma.adapter.MainPagerAdapter;
import me.shyboy.mengma.database.SignHelper;
import me.shyboy.mengma.database.UserHelper;
import me.shyboy.mengma.listener.PagerTabOnClickListener;
import me.shyboy.mengma.methods.OkHttpUtil;
import me.shyboy.mengma.methods.SnoGenerator;
import me.shyboy.mengma.sinvoice.LogHelper;
import me.shyboy.mengma.sinvoice.SinVoicePlayer;
import me.shyboy.mengma.sinvoice.SinVoiceRecognition;


public class MainActivity extends Activity implements SinVoiceRecognition.Listener, SinVoicePlayer.Listener{

    private List<TextView> tabs;
    private LinearLayout settingVoice,settingQr,settingNewSign,settingNewAdmin,settingLogout,settingSignNfc,settingSignList;
    private ViewPager pager;
    private List<View> pagers;
    private TextView tab_sign,tab_news,tab_kown,tab_setting;
    private ImageView tab_line;
    private int currentIndex = 0;// 当前页卡编号，
    private int bmpW;// 动画图片宽度
    private SinVoiceRecognition mRecognition;
    private SinVoicePlayer voicePlayer;
    private User user;
    //page_sign
    private Button sign_btn;
    private boolean isLogout = false;
    private boolean isPlaying = false;
    private boolean isRec = false;
    private final static String TAG = "MainActivity";
    private final static int MAX_NUMBER = 5;
    private final static int MSG_SET_RECG_TEXT = 1;
    private final static int MSG_RECG_START = 2;
    private final static int MSG_RECG_END = 3;

    private final static String CODEBOOK = "0123-";

    private Handler mHanlder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        user = new UserHelper(MainActivity.this).getUser();
        pager = (ViewPager)findViewById(R.id.main_pager);
        tab_line = (ImageView)findViewById(R.id.tab_line);
        mHanlder = new RegHandler(MainActivity.this);
        initTabLine();
        initTab();
        initList();
        initVoicePlayer();
        initPagerSetting();
        MainPagerAdapter adapter = new MainPagerAdapter(pagers);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new PagerChangeListener());

        //isLogined();
    }
    //初始化底部Tab

    private void initTab()
    {
        tab_sign = (TextView)findViewById(R.id.tab_sign);
        tab_news = (TextView)findViewById(R.id.tab_news);
        tab_kown = (TextView)findViewById(R.id.tab_know);
        tab_setting = (TextView)findViewById(R.id.tab_setting);

        //设置监听器

        tab_sign.setOnClickListener(new PagerTabOnClickListener(0,pager));
        tab_news.setOnClickListener(new PagerTabOnClickListener(1,pager));
        tab_kown.setOnClickListener(new PagerTabOnClickListener(2,pager));
        tab_setting.setOnClickListener(new PagerTabOnClickListener(3,pager));

    }
    //初始化setting页面
    private void initPagerSetting()
    {
        initRecognition();
        settingLogout = (LinearLayout)pagers.get(3).findViewById(R.id.setting_logout);
        settingNewSign = (LinearLayout)pagers.get(3).findViewById(R.id.setting_new_sign);
        settingNewAdmin = (LinearLayout)pagers.get(3).findViewById(R.id.setting_new_admin);
        settingVoice = (LinearLayout)pagers.get(3).findViewById(R.id.setting_receive_voice);
        settingQr = (LinearLayout)pagers.get(3).findViewById(R.id.setting_get_qr);
        settingSignNfc = (LinearLayout)pagers.get(3).findViewById(R.id.setting_sign_nfc);
        settingSignList = (LinearLayout)pagers.get(3).findViewById(R.id.setting_sign_list);
        //退出登录
        settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogout = true;
                new UserHelper(MainActivity.this).logout();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        if(user.getPid() < 4 )
        {
            settingVoice.setVisibility(View.VISIBLE);
            settingQr.setVisibility(View.VISIBLE);
            settingSignNfc.setVisibility(View.VISIBLE);
            settingVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SignConfig.isNetworkConnected(MainActivity.this) == false)
                    {
                        Toast.makeText(MainActivity.this,"凑 ~ ~ 没联网",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    if(isRec)
                    {
                        ((TextView)v.findViewById(R.id.setting_receive_voice_text)).setText("接收签到声波");
                        isRec = false;
                        mRecognition.stop();
                    }
                    else
                    {
                        ((TextView)v.findViewById(R.id.setting_receive_voice_text)).setText("终止声波识别");
                        isRec = true;
                        mRecognition.start();
                    }
                }
            });
            //二维码签到
            settingQr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SignConfig.isNetworkConnected(MainActivity.this) == false)
                    {
                        Toast.makeText(MainActivity.this,"凑 ~ ~ 没联网",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }
            });
            //NFC获取一卡通信息
            settingSignNfc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"如果需要使用NFC签到,请将一卡通放到手机背后!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(user.getPid() < 3)
        {
            settingNewSign.setVisibility(View.VISIBLE);
            settingNewSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,NewSignActivity.class);
                    startActivity(intent);
                }
            });
            settingSignList.setVisibility(View.VISIBLE); //签到列表
            settingSignList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SignConfig.isNetworkConnected(MainActivity.this) == false)
                    {
                        Toast.makeText(MainActivity.this,"凑 ~ ~ 没联网",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    new OkHttpUtil(MainActivity.this).list();
                }
            });
        }
        if(user.getPid() < 2)
        {
            settingNewAdmin.setVisibility(View.VISIBLE);
        }

    }
    //初始化VoicePlayer
    private void initVoicePlayer()
    {
        voicePlayer = new SinVoicePlayer(CODEBOOK);
        voicePlayer.setListener(this);
        sign_btn = (Button)pagers.get(0).findViewById(R.id.sign_btn);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying)
                {
                    sign_btn.setText("声波签到");
                    isPlaying = false;
                    voicePlayer.stop();
                }else
                {
                    sign_btn.setText("终止签到");
                    isPlaying = true;
                    voicePlayer.play(new SnoGenerator().encode(user.getSno().substring(5)),true,1000);
                    //Toast.makeText(MainActivity.this,)
                }
            }
        });
    }
    //把ViewPager 的四个页面加载到List
    private void initList()
    {
        tabs = new ArrayList<TextView>();
        tabs.add(tab_sign);
        tabs.add(tab_news);
        tabs.add(tab_kown);
        tabs.add(tab_setting);

        pagers = new ArrayList<View>();

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        pagers.add(inflater.inflate(R.layout.pager_sign, null));
        pagers.add(inflater.inflate(R.layout.pager_news,null));
        pagers.add(inflater.inflate(R.layout.pager_kown,null));
        pagers.add(inflater.inflate(R.layout.pager_setting,null));

    }
    //获取底部滑条
    private void initTabLine()
    {
        WindowManager wm=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        bmpW = wm.getDefaultDisplay().getWidth()/4;//手机屏幕的宽度
        DisplayMetrics dm = new DisplayMetrics();
        tab_line.setLayoutParams(new LinearLayout.LayoutParams(bmpW,8));
        tab_line.setMinimumWidth(bmpW);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        tab_line.setImageMatrix(matrix);// 设置动画初始位置
    }

    //初始化识别
    private void initRecognition()
    {
        mRecognition = new SinVoiceRecognition(CODEBOOK);
        mRecognition.setListener(this);
    }

//pageChange
    private class PagerChangeListener implements ViewPager.OnPageChangeListener
   {
       // 当滑动状态改变时调用
       @Override
       public void onPageScrollStateChanged(int arg0) {
       }

       // 当当前页面被滑动时调用
       @Override
       public void onPageScrolled(int arg0, float arg1, int arg2) {

       }

       // 当新的页面被选中时调用
       @Override
       public void onPageSelected(int arg0) {

           if (arg0 < 0 || arg0 > pagers.size() - 1
                   || currentIndex == arg0) {
               return;
           }

           Animation animation = new TranslateAnimation(bmpW*currentIndex, bmpW*arg0, 0, 0);//显然这个比较简洁，只有一行代码。
           animation.setFillAfter(true);// True:图片停在动画结束位置
           animation.setDuration(200);
           tab_line.startAnimation(animation);

           for(int i = 0; i < tabs.size();i++)
           {
               tabs.get(i).setTextColor(Color.rgb(102,102,102));
           }
           tabs.get(arg0).setTextColor(Color.rgb(90, 149, 248));
           currentIndex = arg0;

       }
   }
    private static class RegHandler extends Handler {
        private StringBuilder mTextBuilder = new StringBuilder();
        //解码具体处理
        private Context context;

        public RegHandler(Context context)
        {
            this.context = context;
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_RECG_TEXT:
                    char ch = (char) msg.arg1;
                    mTextBuilder.append(ch);
                    break;

                case MSG_RECG_START:
                    mTextBuilder.delete(0, mTextBuilder.length());
                    break;

                case MSG_RECG_END:
                    LogHelper.d(TAG, "recognition end");
                    String nums = new SnoGenerator().decode(mTextBuilder.toString());
                    if(nums != null)
                    {
                        String sno = "12108";
                        sno += nums;
                        User user = new UserHelper(context).getUser();
                        UserSign userSign = new UserSign(sno,user.getSno(),user.getAccess_token());
                        //Toast.makeText(context,sno,Toast.LENGTH_SHORT).show();
                        new OkHttpUtil(context).Sign(userSign);
                    }
                    mTextBuilder.delete(0, mTextBuilder.length());
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onRecognitionStart() {
        mHanlder.sendEmptyMessage(MSG_RECG_START);
        Log.i("reg voice","start ***************");
    }

    @Override
    public void onRecognition(char ch) {
        mHanlder.sendMessage(mHanlder.obtainMessage(MSG_SET_RECG_TEXT, ch, 0));
        Log.i("reg voice","ing ***************");
    }

    @Override
    public void onRecognitionEnd() {
        mHanlder.sendEmptyMessage(MSG_RECG_END);
        Log.i("reg voice","end ***************");
    }

    @Override
    public void onPlayStart() {
        LogHelper.d(TAG, "start play");
    }

    @Override
    public void onPlayEnd() {
        LogHelper.d(TAG, "stop play");
    }

    public void onStop()
    {
        super.onStop();
        if(isPlaying)
        {
            sign_btn.setText("声波签到");
            voicePlayer.stop();

        }
        if(isLogout)
        {
            this.finish();
        }
    }

}
