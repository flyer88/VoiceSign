package me.shyboy.mengma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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

import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.adapter.MainPagerAdapter;
import me.shyboy.mengma.listener.PagerTabOnClickListener;
import me.shyboy.mengma.methods.OkHttpUtil;
import voice.decoder.VoiceRecognition;
import voice.decoder.VoiceRecognitionListener;
import voice.encoder.VoicePlayer;


public class MainActivity extends Activity{

    private List<TextView> tabs;
    private ViewPager pager;
    private List<View> pagers;
    private TextView tab_sign,tab_news,tab_kown,tab_setting;
    private ImageView tab_line;
    private int currentIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private VoiceRecognition mRecognition;
    private User user;
    //page_sign
    private Button sign_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        pager = (ViewPager)findViewById(R.id.main_pager);
        tab_line = (ImageView)findViewById(R.id.tab_line);
        initTabLine();
        initTab();
        initList();
        initSign();
        sign_btn = (Button)pagers.get(0).findViewById(R.id.sign_btn);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoicePlayer vp = new VoicePlayer();
                vp.play(encode(4),10,1000);
                //Toast.makeText(MainActivity.this,"lalala",Toast.LENGTH_SHORT).show();
            }
        });
        MainPagerAdapter adapter = new MainPagerAdapter(pagers);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new PagerChangeListener());

        //isLogined();
    }

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

    private void initSign()
    {
        initRecognition();
        ((Button)pagers.get(0).findViewById(R.id.sign_rec)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognition.start();
            }
        });

        ((Button)pagers.get(0).findViewById(R.id.sign_rec_stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognition.stop();
            }
        });

    }

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
        mRecognition = new VoiceRecognition();
        mRecognition.setListener(new VoiceRecognitionListener()
        {
            @Override
            public void onRecognitionStart() {
            }

            public void onRecognitionEnd(int _recogStatus, String _val)
            {
                if(_recogStatus == VoiceRecognition.Status_Success)
                {
                    Toast.makeText(MainActivity.this,_val,Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public static String encode(int uid)
    {

            String s = Integer.toHexString(uid);
            while(s.length() < 11)
            {
                s = "0" + s;
            }
            return s;
    }
}
