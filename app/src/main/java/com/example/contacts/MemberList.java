package com.example.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.contacts.Index.*;

import java.util.ArrayList;
import java.util.List;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        Context _this = MemberList.this;
        ListView memberList = findViewById(R.id.memberList);
        List list = new List(_this);

        memberList.setAdapter(
                new MemberAdapter(_this,(ArrayList<Index.Member>)new Index.ISupplier() {
                    @Override
                    public Object get() {
                        return list.get();
                    }
                }.get())
        );

        memberList.setOnItemClickListener(
                (AdapterView<?> p,View v,int i,long l)->{
                    Member m = (Member)memberList.getItemAtPosition(i);
                    Intent intent = new Intent(_this,MemberDetail.class);
                    intent.putExtra("seq",m.seq);
                    startActivity(intent);
                });
        memberList.setOnItemLongClickListener(
                (AdapterView<?> p,View v,int i,long l)->{
                    Member m = (Member)memberList.getItemAtPosition(i);
                    new AlertDialog.Builder(_this).setTitle("DELETE")
                            .setMessage("정말 삭제 하시겠습니까?")
                            .setPositiveButton(
                                    android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //삭제쿼리 실행
                                            startActivity(new Intent(_this,MemberList.class));
                                        }
                                    }
                            ).setNegativeButton(
                            android.R.string.no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //삭제 취소
                                }
                            }
                    );
                    return true;
                });
    }

    private class ListQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
        }
        @Override
        public SQLiteDatabase getDatabase() {return helper.getReadableDatabase();}
    }
    private class List extends ListQuery{

        public List(Context _this) {super(_this);}

        public ArrayList<Member> get(){
            ArrayList<Member> list = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(
                    "SELECT * FROM MEMBER", null
            );
            Member m = null;
            if(c != null){
                while (((Cursor) c).moveToNext()) {
                    m = new Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MSEQ)));
                    m.name = c.getString(c.getColumnIndex(MNAME));
                    m.pw = c.getString(c.getColumnIndex(MPW));
                    m.email = c.getString(c.getColumnIndex(MEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MPHONE));
                    m.addr = c.getString(c.getColumnIndex(MADDR));
                    m.photo = c.getString(c.getColumnIndex(MPHOTO));
                    list.add(m);
                }
            }else{
                Toast.makeText(_this, "등록된회원이없다",Toast.LENGTH_LONG).show();
            }
            return list;
        };
    }
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Member> list;
        LayoutInflater inflater;
        Context _this;

        public MemberAdapter(Context _this,ArrayList<Member> list) {
            this._this = _this;
            this.list = list;
            this.inflater = LayoutInflater.from(_this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if (v== null){
                v = inflater.inflate(R.layout.member_item,null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            Img im = new Img(_this);
            im.seq = list.get(i).seq+"";
            holder.photo.setImageDrawable(
                            getResources()
                                    .getDrawable(
                                            getResources()
                                                    .getIdentifier(
                                                            _this.getPackageName()+":drawable/"+
                                                            (String) new Index.ISupplier() {
                                                                @Override
                                                                public Object get() {
                                                                    return im.get();
                                                                }
                                                            }.get(),
                                                            null,
                                                            null),
                                            _this.getTheme()
                                            )
                                    );
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);

            return v;
        }
    }

    static class ViewHolder{
        ImageView photo;
        TextView name,phone;
    }

    private  class ImgQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public ImgQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class Img extends ImgQuery{
        String seq;
        public Img(Context _this) {
            super(_this);
        }
        public String get(){
            String res = "";
            Cursor c = this.getDatabase().rawQuery(
                    String.format("SELECT %s FROM %s" +
                            " WHERE %s LIKE '%s'",MPHOTO,MEMBERS
                                                ,MSEQ,seq), null);
            if(c != null && c.moveToNext()){
                res = c.getString(c.getColumnIndex(MPHOTO));
            }else{
                Toast.makeText(_this, "사진이 없다",Toast.LENGTH_LONG).show();
            }
            return res;
        };
    }
}
