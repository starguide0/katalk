package com.hanbit.app.katalk;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by starguide on 2017-11-25.
 */

public class JavascriptInterface {
    Context ctx;

    public JavascriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @android.webkit.JavascriptInterface // android.webkit.JavascriptInterface 이름과 클래스 이름을 정확히 맞춰야 한다
    public void showToast(String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    @android.webkit.JavascriptInterface // android.webkit.JavascriptInterface 이름과 클래스 이름을 정확히 맞춰야 한다
    public void sendSms(String phone, String msg){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, msg, null, null);
    }
}
