package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import static com.example.contacts.Index.*;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);

        Context _this = MemberUpdate.this;
        String[] spec = getIntent()
                .getStringExtra("spec")
                .split(",")
                ;

        ImageView photo = findViewById(R.id.profile);
        photo.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(
                                                _this.getPackageName()+":drawable/"
                                                        +spec[6],
                                                null,
                                                null),
                                _this.getTheme()
                        )
        );
        EditText name = findViewById(R.id.name);
        name.setText(spec[1]);

        EditText changePhone = findViewById(R.id.changePhone);
        changePhone.setText(spec[4]);

        EditText changeEmail = findViewById(R.id.changeEmail);
        changeEmail.setText(spec[3]);

        EditText changeAddress = findViewById(R.id.changeAddress);
        changeAddress.setText(spec[5]);

        findViewById(R.id.cancelBtn).setOnClickListener((v)->{
            startActivity(new Intent(_this,MemberDetail.class));
        });

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Member changeMember = new Member();
                changeMember.seq = Integer.parseInt(spec[0]);
/*                changeMember.photo =
                        (changeProfilePhoto.getText().toString().equals(""))?
                                arr[5]
                                :
                                changeProfilePhoto.getText().toString()
                ;*/

                Intent intent = new Intent(_this,MemberDetail.class);
                intent.putExtra(MEMBERS,spec[0]);
                startActivity(intent);
            }

    });
    }
    private class UpdateQuery extends QueryFactory{
        SQLiteOpenHelper helper;

        public UpdateQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class Update extends UpdateQuery{
        SQLiteDatabase db;
        public Update(Context _this) {
            super(_this);
        }

        public void  accept(Member m){
            String sql = String.format(
                    " UPDATE %s SET %s = '%s',"+
                            " %s = '%s',"+
                            " %s = '%s',"+
                            " %s = '%s',"+
                            " %s = '%s'"+
                            " WHERE %s LIKE '%s'" ,
                    MEMBERS,
                    MPHOTO,
                    m.photo,
                    MNAME,
                    m.name,
                    MPHONE,
                    m.photo,
                    MEMAIL,
                    m.email,
                    MADDR,
                    m.addr,
                    MSEQ,
                    m.seq);
            Log.d("실행할 SQL:",sql);
            this.getDatabase().execSQL(sql);
        }
    }
}
