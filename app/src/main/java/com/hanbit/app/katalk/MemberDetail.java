package com.hanbit.app.katalk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import static com.hanbit.app.katalk.Intro.*;
import static com.hanbit.app.katalk.Intro.MEM_ADDR;
import static com.hanbit.app.katalk.Intro.MEM_EMAIL;
import static com.hanbit.app.katalk.Intro.MEM_NAME;
import static com.hanbit.app.katalk.Intro.MEM_PHONE;
import static com.hanbit.app.katalk.Intro.MEM_PHOTO;
import static com.hanbit.app.katalk.Intro.MEM_PWD;
import static com.hanbit.app.katalk.Intro.MEM_SEQ;
import static com.hanbit.app.katalk.Intro.MEM_TABLE;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;

        Intent intent = getIntent();
        final int seq = intent.getExtras().getInt(MEM_SEQ);
        final int position = intent.getExtras().getInt("POSITION");
        Log.v("넘어온데이터", Integer.toString(seq));
        final MemberItem item = new MemberItem(ctx);
        final Member member = (Member)new DetailService(){
            @Override
            public Object execute() {
                return item.getItem(seq);
            }
        }.execute();

        final String spec = member.Seq+","
                +member.Name + ","
                +member.Address+","
                +member.Phone+","
                +member.Email+","
                +member.Photo+","
                +String.valueOf(position);

        ImageView v = findViewById(R.id.detail_image);
        TextView detailEmail = findViewById(R.id.email_show);
        TextView detailPhone = findViewById(R.id.phone_show);
        TextView detailAddr = findViewById(R.id.dob_show);
        TextView detailName = findViewById(R.id.name_show);

        detailEmail.setText(member.Email);
        detailPhone.setText(member.Phone);
        detailAddr.setText(member.Address);
        detailName.setText(member.Name);
        v.setImageResource(Intro.photos[position]);

        final Phone phone = new Phone(ctx, this);
        final Email email = new Email(ctx, this);

        findViewById(R.id.dial_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.dial(member.Phone);
            }
        });

        findViewById(R.id.call_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.call(member.Phone);
            }
        });

        findViewById(R.id.sms_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SMS 는 WebView 을 사용해야한다
                // Test
                startActivity(new Intent(ctx, Network.class));
            }
        });

        findViewById(R.id.email_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.sendEmail(member.Email);
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MemberUpdate.class);
                intent.putExtra("MEMBER", spec);
                startActivity(intent);
            }
        });

        findViewById(R.id.list_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberList.class));
            }
        });

        findViewById(R.id.map_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, Maps.class);
                intent.putExtra("addr", member.Address);
                startActivity(intent);
            }
        });
    }

    private abstract class ItemQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public ItemQuery(Context ctx) {
            super(ctx);
            helper = new SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberItem extends MemberDetail.ItemQuery {
        public MemberItem(Context ctx) {
            super(ctx);
        }

        public Member getItem(int index){

            String sql = String.format("SELECT * FROM %s WHERE %s = '%d'",
                    MEM_TABLE, MEM_SEQ, index);

            Cursor cusor = this.getDatabase().rawQuery(sql, null);
            Intro.Member member = null;
            if(cusor.moveToNext()){
                member = new Intro.Member();
                member.Seq = cusor.getInt(cusor.getColumnIndex(MEM_SEQ));
                member.Name = cusor.getString(cusor.getColumnIndex(MEM_NAME));
                member.Password = cusor.getString(cusor.getColumnIndex(MEM_PWD));
                member.Email = cusor.getString(cusor.getColumnIndex(MEM_EMAIL));
                member.Phone = cusor.getString(cusor.getColumnIndex(MEM_PHONE));
                member.Address = cusor.getString(cusor.getColumnIndex(MEM_ADDR));
                member.Photo = cusor.getString(cusor.getColumnIndex(MEM_PHOTO));
            }
            return member;
        }
    }
}
