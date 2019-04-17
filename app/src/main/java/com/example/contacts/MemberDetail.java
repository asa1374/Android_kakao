package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        Context _this = MemberDetail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getInt("seq")+"";
        ImageView memberimg = findViewById(R.id.profile);
        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView addr = findViewById(R.id.addr);

        MemberDetaail mdetail = new MemberDetaail(_this);

        Toast.makeText(_this,seq,Toast.LENGTH_LONG).show();
        mdetail.seq = seq;

        name.setText((String)new Index.ISupplier() {
            @Override
            public Object get() {
                return mdetail.get().name;
            }
        }.get());
        email.setText((String)new Index.ISupplier() {
            @Override
            public Object get() {
                return mdetail.get().email;
            }
        }.get());
        phone.setText((String)new Index.ISupplier() {
            @Override
            public Object get() {
                return mdetail.get().phone;
            }
        }.get());
        addr.setText((String)new Index.ISupplier() {
            @Override
            public Object get() {
                return mdetail.get().addr;
            }
        }.get());

        Img im = new Img(_this);
        im.seq = seq;
        memberimg.setImageDrawable(
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
    }

    private class MemberDetailQuery extends Index.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberDetailQuery(Context _this) {
            super(_this);
            helper = new Index.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberDetaail extends MemberDetailQuery{
        String seq;
        public MemberDetaail(Context _this) {
            super(_this);
        }

        public Index.Member get(){

            Index.Member member = null;
            Cursor c = this.getDatabase().rawQuery(
                    String.format("SELECT * FROM %s" +
                            " WHERE %s LIKE '%s'",Index.MEMBERS,Index.MSEQ,seq),null
            );
            if(c != null){
                while (((Cursor) c).moveToNext()) {
                    member = new Index.Member();
                    member.seq = Integer.parseInt(c.getString(c.getColumnIndex(Index.MSEQ)));
                    member.name = c.getString(c.getColumnIndex(Index.MNAME));
                    member.pw = c.getString(c.getColumnIndex(Index.MPW));
                    member.email = c.getString(c.getColumnIndex(Index.MEMAIL));
                    member.phone = c.getString(c.getColumnIndex(Index.MPHONE));
                    member.addr = c.getString(c.getColumnIndex(Index.MADDR));
                }
            }else{
                Toast.makeText(_this, "등록된회원이없다",Toast.LENGTH_LONG).show();
            }
            return member;
        }
    }
    private  class ImgQuery extends Index.QueryFactory{
        SQLiteOpenHelper helper;
        public ImgQuery(Context _this) {
            super(_this);
            helper = new Index.SQLiteHelper(_this);
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
                                    " WHERE %s LIKE '%s'",Index.MPHOTO,Index.MEMBERS
                            ,Index.MSEQ,seq), null);
            if(c != null ){
                if ( c.moveToNext()) {
                    res = c.getString(c.getColumnIndex(Index.MPHOTO));
                }
            }else{
                Toast.makeText(_this, "사진이 없다",Toast.LENGTH_LONG).show();
            }
            return res;
        };
    }


}
