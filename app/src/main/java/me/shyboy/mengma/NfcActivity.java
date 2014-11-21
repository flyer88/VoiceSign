package me.shyboy.mengma;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.Common.UserSign;
import me.shyboy.mengma.database.UserHelper;
import me.shyboy.mengma.methods.OkHttpUtil;
import me.shyboy.mengma.nfc.MyNfcAdapter;


/**
 * Created by flyer on 14-11-20.
 */
public class NfcActivity extends Activity {

    MyNfcAdapter myNfcAdapter;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nfc);
        user = new UserHelper(this).getUser();
        myNfcAdapter = new MyNfcAdapter(this);

        myNfcAdapter.init();//初始化

        if (user.getPid()<4) {//验证
            String  sno =myNfcAdapter.processNfcAdapterIntent(getIntent());
            //Toast.makeText(NfcActivity.this,sno+"签到成功!", Toast.LENGTH_SHORT).show();
            User user = new UserHelper(NfcActivity.this).getUser();
            UserSign userSign = new UserSign(sno,user.getSno(),user.getAccess_token());
            new OkHttpUtil(NfcActivity.this).Sign(userSign);

            finish();
        } else {
            //Toast.makeText(NfcActivity.this,myNfcAdapter.processNfcAdapterIntent(getIntent()), Toast.LENGTH_SHORT).show();
            Toast.makeText(NfcActivity.this, "小样,你想用你自己的手机签到么?哈哈哈哈哈", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    protected void onResume() {
        super.onResume();
//        myNfcAdapter.init();//初始化
//        if (user.getPid()<4) {//验证
//            String  sno =myNfcAdapter.processNfcAdapterIntent(getIntent());
//            Toast.makeText(NfcActivity.this,sno+"签到成功!", Toast.LENGTH_SHORT).show();
//            finish();
//        } else {
//            //Toast.makeText(NfcActivity.this,myNfcAdapter.processNfcAdapterIntent(getIntent()), Toast.LENGTH_SHORT).show();
//            Toast.makeText(NfcActivity.this, "小样,你想用你自己的手机签到么?哈哈哈哈哈", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }
}
