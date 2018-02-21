package com.hanbit.app.katalk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by starguide on 2017-11-18.
 */

public class Phone {
    private Context ctx;
    private Activity act;

    public Phone(Context ctx, Activity act) {
        this.ctx = ctx;
        this.act = act;
    }

    public void dial(String phoneNo){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNo));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public void call(String phoneNo){
        Log.d(null, phoneNo);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNo));
        if(ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CALL_PHONE}, 2);
            return;
        }
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
