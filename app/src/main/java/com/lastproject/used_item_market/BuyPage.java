package com.lastproject.used_item_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

//살려면 buy 사용자 기준 1인칭이 사용자

//내가 사고 싶은 목록

public class BuyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);

        Back();
    }

    void Back(){
        ImageButton back = (ImageButton)findViewById(R.id.xbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}