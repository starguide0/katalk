package com.hanbit.app.katalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.hanbit.app.katalk.Intro.MEM_ADDR;
import static com.hanbit.app.katalk.Intro.MEM_EMAIL;
import static com.hanbit.app.katalk.Intro.MEM_NAME;
import static com.hanbit.app.katalk.Intro.MEM_PHONE;
import static com.hanbit.app.katalk.Intro.MEM_PHOTO;
import static com.hanbit.app.katalk.Intro.MEM_PWD;
import static com.hanbit.app.katalk.Intro.MEM_SEQ;
import static com.hanbit.app.katalk.Intro.MEM_TABLE;
import static com.hanbit.app.katalk.Intro.Member;
import static com.hanbit.app.katalk.Intro.ListService;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context ctx = MemberList.this;
        final ListView listView = findViewById(R.id.list_item);

        Toast.makeText(ctx, "MemberList", Toast.LENGTH_SHORT).show();

        String tmp = "";
        final MemberItemList mlt = new MemberItemList(ctx);

//        final ArrayList<Member> l = mlt.list();
//        for (Member m : l) {
//            tmp += m.Name + ",";
//        }
//        Toast.makeText(ctx, tmp, Toast.LENGTH_LONG).show();

        ArrayList<Member> memberList = (ArrayList<Member>)new ListService() {
            @Override
            public List<?> execute() {
                return mlt.list();
            }
        }.execute();

        listView.setAdapter(new MemberItem(ctx, memberList));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member selectedMember = (Member)listView.getItemAtPosition(position);
//                Toast.makeText(ctx, selectedMember.Name, Toast.LENGTH_LONG);
                Log.v("MemberList.onCreate()",selectedMember.Name);
                Intent intent = new Intent(ctx, MemberDetail.class);
                intent.putExtra(MEM_SEQ, selectedMember.Seq);
                intent.putExtra("POSITION", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Member selectedMember = (Member)listView.getItemAtPosition(position);
                new AlertDialog.Builder(ctx)
                        .setTitle("DELETE")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton(
                                android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                        Log.v("OnClickListener",String.valueOf(selectedMember.Seq));

                                        final MemberItemDelete del = new MemberItemDelete(ctx);
                                        new Intro.DeleteService() {
                                            @Override
                                            public void execute() {
                                                del.execute(String.valueOf(selectedMember.Seq));
                                            }
                                        }.execute();
                                        startActivity(new Intent(ctx, MemberList.class));
                                    }
                                }
                        )
                        .setNegativeButton(
                                android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                        )
                        .show();


//                Toast.makeText(ctx, selectedMember.Name, Toast.LENGTH_LONG);
                return true;
            }
        });

        findViewById(R.id.member_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx, MemberAdd.class));
            }
        });

    }

    // DB item 로딩 [
    private abstract class ListQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberItemList extends ListQuery{
        public MemberItemList(Context ctx) {
            super(ctx);
        }


        public ArrayList<Member> list(){
            ArrayList<Member> members = new ArrayList<>();
            String sql = String.format("SELECT %s,%s,%s,%s,%s,%s,%s FROM %s",
                    MEM_SEQ, MEM_NAME, MEM_PWD, MEM_EMAIL, MEM_PHONE, MEM_ADDR, MEM_PHOTO, MEM_TABLE);

            Cursor cusor = this.getDatabase().rawQuery(sql, null);
            Member member = null;
            if(cusor != null){
                while (cusor.moveToNext()){
                    member = new Member();
                    member.Seq = Integer.parseInt(cusor.getString(cusor.getColumnIndex(MEM_SEQ)));
                    member.Name = cusor.getString(cusor.getColumnIndex(MEM_NAME));
                    member.Password = cusor.getString(cusor.getColumnIndex(MEM_PWD));
                    member.Email = cusor.getString(cusor.getColumnIndex(MEM_EMAIL));
                    member.Phone = cusor.getString(cusor.getColumnIndex(MEM_PHONE));
                    member.Address = cusor.getString(cusor.getColumnIndex(MEM_ADDR));
                    member.Photo = cusor.getString(cusor.getColumnIndex(MEM_PHOTO));
                    members.add(member);
                }
            }
            return members;
        }
    }

    class MemberItem extends BaseAdapter{
        ArrayList<Member> list;
        LayoutInflater inflater;

        public MemberItem(Context ctx, ArrayList<Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(ctx);
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View v, ViewGroup parent) {
            ViewHoader holder;

            if(v==null) {
                v = inflater.inflate(R.layout.member_item, null);
                holder = new ViewHoader();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHoader) v.getTag();
            }
            holder.photo.setImageResource(Intro.photos[i]);
            holder.name.setText(list.get(i).Name);
            holder.phone.setText(list.get(i).Phone);

            return v;
        }
    }

    static class ViewHoader{
        ImageView photo;
        TextView name;
        TextView phone;
    }
    // ]

    // DB item 삭제 [
    private abstract class DeleteQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public DeleteQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class MemberItemDelete extends DeleteQuery{
        public MemberItemDelete(Context ctx) {
            super(ctx);
        }

        public void execute(String seq) {
            String sql = String.format("DELETE FROM %s WHERE %s = '%s';",
                    MEM_TABLE, MEM_SEQ, seq);
            this.getDatabase().execSQL(sql);
            this.getDatabase().close(); // 삭제할때만 close() 한다??
        }

    }
    // ]

    // DB item 추가 [
    private abstract class InsertQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public InsertQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class MemberItemInsert extends InsertQuery {

        public MemberItemInsert(Context ctx) {
            super(ctx);
        }


        public void execute() {
//            final MemberItemList mlt = new MemberItemList(ctx);
//            ArrayList<Member> memberList = (ArrayList<Member>)new ListService() {
//                @Override
//                public List<?> execute() {
//                    return mlt.list();
//                }
//            }.execute();
//            int s =0;
//            for(Member m : memberList){
//                if(m.Seq != s){
//                    break;
//                }
//                s++;
//            }
            String sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s) VALUES ('%s','%s','%s','%s','%s');",
                    MEM_TABLE, MEM_NAME, MEM_PWD, MEM_EMAIL, MEM_PHONE, MEM_ADDR, MEM_PHOTO,
                    "Test", "p", "test@test.com","012-3456-7890", "Korea", "ok");

            this.getDatabase().execSQL(sql);
        }

    }
    // ]
}
