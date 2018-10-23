package com.hanhui.jiaoshoutranslation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class About extends AppCompatActivity {

   // public static AppCompatActivity aboutActivity;
    Button btn_trans;
    Button btn_daily;
    Button btn_news;
    Button btn_about;
    Button btn_back;
    DrawerLayout referenceDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reference);
        getSupportActionBar().hide();
        ManageActivity.addActivity(this);
        //aboutActivity = this;

        btn_trans = findViewById(R.id.btn_01);
        btn_daily = findViewById(R.id.btn_02);
        btn_news = findViewById(R.id.btn_03);
        btn_about = findViewById(R.id.btn_04);
        btn_back = findViewById(R.id.btn_reference_back);
        referenceDrawer = findViewById(R.id.reference_drawer);

        ButtonListener listener = new ButtonListener();
        btn_trans.setOnClickListener(listener);
        btn_daily.setOnClickListener(listener);
        btn_news.setOnClickListener(listener);
        btn_back.setOnClickListener(listener);
        btn_about.setOnClickListener(listener);

    }

    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent();

            //跳转界面
            switch (view.getId()) {
                case R.id.btn_01:
//                    intent.setClass(About.this, MainActivity.class);
//                    startActivity(intent);
                    referenceDrawer.closeDrawers();
                    ManageActivity.finishAllActivity();
                    break;
                case R.id.btn_02:
                    //DailyJinShan.dailyAcctivity.finish();
                    intent.setClass(About.this, DailyJinShan.class);
                    startActivity(intent);
                    referenceDrawer.closeDrawers();
                    break;
                case R.id.btn_03:
                    //News.newsActivity.finish();
                    intent.setClass(About.this, News.class);
                    startActivity(intent);
                    referenceDrawer.closeDrawers();
                    break;
                case R.id.btn_04:
                    referenceDrawer.closeDrawers();
                    break;
                case R.id.btn_reference_back:
                    finish();
            }
        }
    }
}
