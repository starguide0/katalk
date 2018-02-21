package com.hanbit.app.katalk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Join extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        final Context ctx = Join.this;

        Toast.makeText(ctx, "Join", Toast.LENGTH_SHORT).show();

        findViewById(R.id.join_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 회원 가입 저장
                startActivity(new Intent(ctx, Intro.class));
            }
        });

        findViewById(R.id.join_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, Intro.class));
            }
        });
    }
}
