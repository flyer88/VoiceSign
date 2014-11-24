package me.shyboy.mengma;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import me.shyboy.mengma.Common.User;
import me.shyboy.mengma.QRcode.zxing.encoding.EncodingHandler;
import me.shyboy.mengma.database.UserHelper;


public class QRcodeActivity extends Activity {
    ImageView qrImgImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        qrImgImageView=(ImageView)findViewById(R.id.iv_qr_image);
        User user = new UserHelper(QRcodeActivity.this).getUser();
        String str = "voicesign-"+user.getSno()+"-"+user.getAccess_token();
        try {
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(str, 350);
            qrImgImageView.setImageBitmap(qrCodeBitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }

    }
}
