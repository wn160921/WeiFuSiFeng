package com.example.a25564.weifusifeng;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.a25564.weifusifeng.bean.user;

import org.litepal.crud.DataSupport;

public class MainActivity extends AppCompatActivity {
    Button temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp= (Button) findViewById(R.id.temp);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(user.class);

            }
        });

    }
}
