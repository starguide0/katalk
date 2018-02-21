package com.hanbit.app.katalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class Intro extends AppCompatActivity {

    final static public int[] photos ={
            R.drawable.cupcake,
            R.drawable.donut,
            R.drawable.eclair,
            R.drawable.froyo,
            R.drawable.gingerbread,
            R.drawable.honeycomb
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        final Context ctx = Intro.this;

        Toast.makeText(ctx, "Intro", Toast.LENGTH_SHORT).show();

        SQLiteHelper helper = new SQLiteHelper(ctx);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, Login.class));
            }
        });

        findViewById(R.id.join_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, Join.class));
            }
        });
    }

    static String DB_NAME = "hanbit.db";
    static String MEM_TABLE = "Member";
    static String MEM_SEQ = "Seq";
    static String MEM_NAME = "Name";
    static String MEM_PWD = "Pass";
    static String MEM_EMAIL = "Email";
    static String MEM_PHONE= "Phone";
    static String MEM_ADDR = "Address";
    static String MEM_PHOTO= "Photo";

    static class Member{int Seq;String Name,Password,Email,Phone,Address,Photo;};
    static interface LoginService{public void execute();}
    static interface AddService{public void execute(Member m);}
    static interface ListService{public List<?> execute();}
    static interface DetailService{public Object execute();}
    static interface UpdateService{public void execute();}
    static interface DeleteService{public void execute();}

    static abstract class QueryFactory{
        Context ctx;

        public QueryFactory(Context ctx) {this.ctx = ctx;}
        public abstract SQLiteDatabase getDatabase();
    }

    static class SQLiteHelper extends SQLiteOpenHelper{
//초기 자동생성에서 나온 함수
//        public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//            super(context, name, factory, version);
//        }

        public SQLiteHelper(Context ctx) {
            super(ctx, DB_NAME, null, 1);   // factory 이 null 이면 안드로이드 내부 에 있는 걸 사용하지 않음
//            this.getWritableDatabase();  // DB Create 임, null 값이므로 this 에 write 할 DB 를 get 한다.
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s " +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT );",
                    MEM_TABLE,MEM_SEQ, MEM_NAME,MEM_PWD,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO
                    ));
            for(int i=0; i<5; i++) {
                db.execSQL(String.format("  INSERT INTO %s (%s,%s,%s,%s,%s,%s) " +
                        " VALUES ('%s','%s','%s','%s','%s','%s');",
                        MEM_TABLE,MEM_NAME,MEM_PWD,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO,
                        "홍길동"+i,"1","Hong"+i+"@test.com", "010-1234-567"+i, "서울"+i, "hong"+i));
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
