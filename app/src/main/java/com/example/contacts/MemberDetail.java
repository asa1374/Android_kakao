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
import static com.example.contacts.Index.*;

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
        mdetail.seq = seq;

        Member m = (Member)new ISupplier() {
            @Override
            public Object get() {
                return mdetail.get();
            }
        }.get();


        name.setText(m.name);
        email.setText(m.email);
        phone.setText(m.phone);
        addr.setText(m.addr);

        Img im = new Img(_this);
        im.seq = seq;
        memberimg.setImageDrawable(
                    getResources()
                            .getDrawable(
                                    getResources()
                                            .getIdentifier(
                                                    _this.getPackageName()+":drawable/"+
                                                            (String) new ISupplier() {
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

        findViewById(R.id.listBtn).setOnClickListener(v->{
            startActivity(new Intent(_this,MemberList.class));
        });
        findViewById(R.id.callBtn).setOnClickListener(v->{
            startActivity(new Intent(_this,MemberList.class));
        });
        findViewById(R.id.dialBtn).setOnClickListener(v->{
            startActivity(new Intent(_this,MemberList.class));
        });
        findViewById(R.id.smsBtn).setOnClickListener(v->{
            startActivity(new Intent(_this,MemberList.class));
        });
        findViewById(R.id.emailBtn).setOnClickListener(v->{
            startActivity(new Intent(_this,MemberList.class));
        });
        findViewById(R.id.albumBtn).setOnClickListener(v->{
            startActivity(new Intent(_this,MemberList.class));
        });
        findViewById(R.id.updateBtn).setOnClickListener(v->{
            Intent intent2 = new Intent(_this, MemberUpdate.class);
            intent2.putExtra("spec",
                    m.seq+","+
                            m.name+","+
                            m.pw+","+
                            m.email+","+
                            m.phone+","+
                            m.addr+","+
                            m.photo);
            startActivity(intent2);
        });

    }

    private class MemberDetailQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public MemberDetailQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
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

        public Member get(){

            Member member = null;
            Cursor c = this.getDatabase().rawQuery(
                    String.format("SELECT * FROM %s" +
                            " WHERE %s LIKE '%s'",MEMBERS,MSEQ,seq),null
            );
            if(c != null && c.moveToNext()){
                    member = new Member();
                    member.seq = Integer.parseInt(c.getString(c.getColumnIndex(MSEQ)));
                    member.name = c.getString(c.getColumnIndex(MNAME));
                    member.pw = c.getString(c.getColumnIndex(MPW));
                    member.email = c.getString(c.getColumnIndex(MEMAIL));
                    member.phone = c.getString(c.getColumnIndex(MPHONE));
                    member.addr = c.getString(c.getColumnIndex(MADDR));
                    member.photo = c.getString(c.getColumnIndex(MPHOTO));
            }else{
                Toast.makeText(_this, "등록된회원이없다",Toast.LENGTH_LONG).show();
            }
            return member;
        }
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
