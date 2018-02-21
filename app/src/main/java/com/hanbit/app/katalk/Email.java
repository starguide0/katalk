package com.hanbit.app.katalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by starguide on 2017-11-18.
 */

public class Email {
    private Context ctx;
    private Activity act;

    public Email(Context ctx, Activity act) {
        this.ctx = ctx;
        this.act = act;
    }

    public void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+email));
        intent.setType("text/plain");
        intent.putExtra(intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello!!");
        intent.putExtra(Intent.EXTRA_TEXT, "Good byte!!");
        ctx.startActivity(intent.createChooser(intent, "example"));
    }
}
