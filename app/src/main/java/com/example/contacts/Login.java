package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Context _this = Login.this;
        findViewById(R.id.loginBtn).setOnClickListener((v)->{

            EditText userID = findViewById(R.id.userID);
            EditText password = findViewById(R.id.password);
            ItemExist it = new ItemExist(_this);
            it.id = userID.getText().toString();
            it.pw = password.getText().toString();
            if(it.test()) {
                startActivity(new Intent(_this, MemberList.class));
            }else {
                Toast.makeText(_this,"잘못입력했다.",Toast.LENGTH_LONG).show();
            }
        });
    }
    private class LoginQuery extends Index.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context _this) {
            super(_this);
            helper = new Index.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemExist extends LoginQuery{
        String id,pw;
        public ItemExist(Context _this) {
            super(_this);
        }
        public boolean test(){
            return super
                    .getDatabase()
                    .rawQuery(String.format(
                            "SELECT * FROM %s " +
                                    " WHERE %s LIKE '%s' AND %s LIKE '%s'",
                            Index.MEMBERS,
                            Index.MSEQ,
                            id,
                            Index.MPW,
                            pw
                    ),null)
                    .moveToNext();
        }
    }
}
