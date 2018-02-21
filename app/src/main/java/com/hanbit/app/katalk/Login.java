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
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context ctx = Login.this;
        final EditText id = findViewById(R.id.id_et);
        final EditText pwd = findViewById(R.id.password_et);
        final MemberExist exist = new MemberExist(ctx);

        Toast.makeText(ctx, "Login", Toast.LENGTH_SHORT).show();

        findViewById(R.id.login_on_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etID = findViewById(R.id.id_et);
                final EditText etPass = findViewById(R.id.password_et);
                final String inputID = etID.getText().toString();
                final String inputPass = etPass.getText().toString();
                Intro.SQLiteHelper helper = new Intro.SQLiteHelper(ctx);

                new Intro.LoginService(){

                    @Override
                    public void execute() {
                        boolean longOk = exist.execute(inputID, inputPass);
                        if(longOk == true) {
                           startActivity(new Intent(ctx,MemberList.class));
                        }else {
//                            startActivity(new Intent(ctx,Login.class));
                            etID.setText("");
                            etPass.setText("");
                        }
                    }
                }.execute();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 로그인 정보 비교
                startActivity(new Intent(ctx, Intro.class));
            }
        });
    }


    private class LoginQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberExist extends LoginQuery{

        public MemberExist(Context ctx) {
            super(ctx);
        }

        public boolean execute(String seq,String pass){
            return super
                    .getDatabase()
                    .rawQuery(String.format(" SELECT * FROM %s WHERE %s = '%s' AND " +
                            " %s = '%s';", Intro.MEM_TABLE, Intro.MEM_SEQ, seq, Intro.MEM_PWD, pass),null)
                    .moveToNext();
        }
    }
}
