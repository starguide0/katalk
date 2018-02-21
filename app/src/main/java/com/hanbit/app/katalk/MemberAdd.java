package com.hanbit.app.katalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.hanbit.app.katalk.Intro.*;
import static com.hanbit.app.katalk.Intro.MEM_ADDR;
import static com.hanbit.app.katalk.Intro.MEM_EMAIL;
import static com.hanbit.app.katalk.Intro.MEM_NAME;
import static com.hanbit.app.katalk.Intro.MEM_PHONE;
import static com.hanbit.app.katalk.Intro.MEM_PHOTO;
import static com.hanbit.app.katalk.Intro.MEM_PWD;
import static com.hanbit.app.katalk.Intro.MEM_TABLE;

public class MemberAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);
        final Context ctx = MemberAdd.this;
        final EditText name_input = findViewById(R.id.name_input);
        final EditText email_input = findViewById(R.id.email_input);
        final EditText phone_input = findViewById(R.id.phone_input);
        final EditText dob_input = findViewById(R.id.dob_input);

        Toast.makeText(ctx, "MemberAdd", Toast.LENGTH_SHORT).show();

        findViewById(R.id.member_add_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Member m = new Member();
                m.Name = name_input.getText().toString();
                m.Address = dob_input.getText().toString();
                m.Email = email_input.getText().toString();
                m.Phone = phone_input.getText().toString();

                final MemberItemAdd ins = new MemberItemAdd(ctx);
                new Intro.AddService() {
                    @Override
                    public void execute(Member m) {
                        ins.execute(m);
                    }
                }.execute(m);
                startActivity(new Intent(ctx, MemberList.class));
            }
        });

        findViewById(R.id.member_add_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_input.setText("");
                email_input.setText("");
                phone_input.setText("");
                dob_input.setText("");

                startActivity(new Intent(ctx, MemberList.class));
            }
        });
    }

    private abstract class InsertQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public InsertQuery(Context ctx) {
            super(ctx);
            helper = new SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class MemberItemAdd extends InsertQuery{

        public MemberItemAdd(Context ctx) {
            super(ctx);
        }

        public void execute(Member member){
            String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES ('%s','%s','%s','%s','%s','%s') ;",
                    MEM_TABLE,
                    MEM_PWD, MEM_NAME, MEM_EMAIL, MEM_PHONE, MEM_ADDR, MEM_PHOTO,
                    "1", member.Name, member.Email,member.Phone, member.Address, "default_profile");

            this.getDatabase().execSQL(sql);
        }
    }
}

