package me.shyboy.mengma.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.widget.Toast;

public class MyNfcAdapter {
    NfcAdapter nfcAdapter;
    Context context;
    public MyNfcAdapter(Context context){
       this.context = context;
    }
    public boolean init(){
        try{
            this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        }
        catch (Exception e){
            Toast.makeText(context,"wrong",Toast.LENGTH_SHORT).show();
        }


            if (nfcAdapter == null) {
                Toast.makeText(context, "设备不支持NFC！", Toast.LENGTH_SHORT).show();

                return false;
            }
            if (!nfcAdapter.isEnabled()) {
                Toast.makeText(context,"请在系统设置中先启用NFC功能！",Toast.LENGTH_SHORT).show();
                //得到是否检测到ACTION_TECH_DISCOVERED触发
                return false;
            }
        return true;
    }

    public String processNfcAdapterIntent(Intent intent){
            //ACTION_TECH_DISCOVERED
            Toast.makeText(context,"正在签到！",Toast.LENGTH_SHORT).show();
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
                //处理该intent
                return processIntent(intent);
            }else{
                return null;
            }
    }

    private String processIntent(Intent intent) {
            //取出封装在intent中的TAG
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            boolean auth = false;
            String metaInfo = "";//学号信息
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                mfc.connect();
                auth = mfc.authenticateSectorWithKeyA(15,
                        MifareClassic.KEY_DEFAULT);
                if (auth) {
                    // 读取扇区中的块
                    byte[] data = mfc.readBlock(60);

                    char c[] = bytesToHexString(data).toCharArray();

                    for(int i = 3 ; i < 18 ; i+=2){
                        metaInfo +=c[i];
                    }
                } else {
                    metaInfo += ":验证失败\n";
                }
                System.out.println(metaInfo);
                return metaInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return metaInfo;
        }

    private String bytesToHexString(byte[] src) {
            StringBuilder stringBuilder = new StringBuilder("0x");
            if (src == null || src.length <= 0) {
                return null;
            }
            char[] buffer = new char[2];
            for (int i = 0; i < src.length; i++) {
                buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
                buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
                stringBuilder.append(buffer);
            }
            return stringBuilder.toString();
        }


}
