package com.hanbit.app.katalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.hanbit.app.katalk.Intro.*;
import static com.hanbit.app.katalk.Intro.MEM_ADDR;
import static com.hanbit.app.katalk.Intro.MEM_EMAIL;
import static com.hanbit.app.katalk.Intro.MEM_NAME;
import static com.hanbit.app.katalk.Intro.MEM_PHONE;
import static com.hanbit.app.katalk.Intro.MEM_PHOTO;
import static com.hanbit.app.katalk.Intro.MEM_PWD;
import static com.hanbit.app.katalk.Intro.MEM_TABLE;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context ctx = MemberUpdate.this;

        Intent intent = getIntent();
        String spec = intent.getStringExtra("MEMBER");

        final String[] element = spec.split(",");
        final EditText nameInput = findViewById(R.id.name_input);
        final EditText addrInput = findViewById(R.id.addr_input);
        final EditText phoneInput = findViewById(R.id.phone_input);
        final EditText emailInput = findViewById(R.id.email_input);
        ImageView photo = findViewById(R.id.photo);

        nameInput.setHint(element[1]);
        addrInput.setHint(element[2]);
        phoneInput.setHint(element[3]);
        emailInput.setHint(element[4]);
Log.i("넘어온 이미지",String.valueOf(Intro.photos[Integer.parseInt(element[6])]));
        int profile = getResources().getIdentifier("@drawable/cupcake", "drawable", this.getPackageName());
Log.i("Profile Num:", String.valueOf(profile));
//        photo.setImageDrawable(getResources().getDrawable(profile, ctx.getTheme()));
//        photo.setImageResource(Intro.photos[Integer.parseInt(element[6])]);

        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MemberItemUpdate ins = new MemberItemUpdate(ctx);

                new UpdateService(){
                    @Override
                    public void execute() {
                        Member m = new Member();
                        m.Seq = Integer.parseInt(element[0]);

                        m.Name = (nameInput.getText().toString().equals("") == true) ? element[1] : nameInput.getText().toString();
                        m.Address = (addrInput.getText().toString().equals("") == true) ? element[2] : addrInput.getText().toString();
                        m.Phone = (phoneInput.getText().toString().equals("") == true) ? element[3] : phoneInput.getText().toString();
                        m.Email = (emailInput.getText().toString().equals("") == true) ? element[4] : emailInput.getText().toString();
                        m.Photo = element[5];
                        Log.i("Update", m.Seq+","+m.Name+","+m.Address+","+m.Phone+","+m.Email+","+m.Photo);
                        ins.execute(m);
                    }
                }.execute();
                startActivity(new Intent(ctx, MemberList.class));
            }
        });
    }

    private abstract class UpdateQuery extends QueryFactory {
        SQLiteOpenHelper helper;
        public UpdateQuery(Context ctx) {
            super(ctx);
            helper = new SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class MemberItemUpdate extends UpdateQuery{

        public MemberItemUpdate(Context ctx) {
            super(ctx);
        }

        public void execute(Member member){
            String sql = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s'" +
                            "WHERE %s = '%s'",
                    MEM_TABLE,
                    MEM_NAME, member.Name,
                    MEM_EMAIL, member.Email,
                    MEM_PHONE, member.Phone,
                    MEM_ADDR, member.Address,
                    MEM_PHOTO, member.Photo,
                    MEM_SEQ, member.Seq);

            this.getDatabase().execSQL(sql);
        }
    }
}
