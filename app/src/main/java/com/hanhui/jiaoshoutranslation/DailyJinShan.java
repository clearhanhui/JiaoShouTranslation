package com.hanhui.jiaoshoutranslation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanhui.jiaoshoutranslation.daily.JsonRootBean;
import com.squareup.picasso.Picasso;

public class DailyJinShan extends AppCompatActivity {

    //public static AppCompatActivity dailyAcctivity;

    String daily_jinshan_path = "http://open.iciba.com/dsapi/";

    ImageView iv ;
    TextView tv_zh;
    TextView tv_en;
    TextView tv_date;
    TextView tv_author;
    Button btn_trans;
    Button btn_daily;
    Button btn_news;
    Button btn_about;
    Button btn_back;
    DrawerLayout dailyDrawer;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                String string = (String) msg.obj;
                Gson gson = new Gson();
                JsonRootBean bean = gson.fromJson(string,JsonRootBean.class);
                tv_en.setText(bean.getContent());
                tv_zh.setText(bean.getNote());
                Picasso.with(DailyJinShan.this).load(bean.getPicture()).into(iv);
                tv_date.setText(bean.getDateline());
                tv_author.setText(bean.getTranslation());
                String daily_voice_path = bean.getTts();//音频地址
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_jinshan);
        getSupportActionBar().hide();
        //dailyAcctivity = this;
        ManageActivity.addActivity(this);

        iv = findViewById(R.id.iv_big);
        tv_zh = findViewById(R.id.tv_daily_zh);
        tv_en = findViewById(R.id.tv_daily_en);
        tv_date = findViewById(R.id.tv_date);
        tv_author = findViewById(R.id.tv_daily_author);
        btn_trans = findViewById(R.id.btn_01);
        btn_daily = findViewById(R.id.btn_02);
        btn_news = findViewById(R.id.btn_03);
        btn_back = findViewById(R.id.btn_daily_back);
        btn_about = findViewById(R.id.btn_04);
        dailyDrawer = findViewById(R.id.daily_drawer);


        //开启子线程，获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.getJson(daily_jinshan_path);
                Message message = new Message();
                message.what = 1;
                message.obj = str;
                handler.sendMessage(message);
            }
        }).start();

        //iv.setOnClickListener(clickListener);

        ButtonListener listener = new ButtonListener();
        btn_trans.setOnClickListener(listener);
        btn_daily.setOnClickListener(listener);
        btn_news.setOnClickListener(listener);
        btn_about.setOnClickListener(listener);
        btn_back.setOnClickListener(listener);


    }

//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            finish();
//        }
//    };

    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent();

            //跳转界面
            switch (view.getId()) {
                case R.id.btn_01:
//                    intent.setClass(DailyJinShan.this, MainActivity.class);
//                    startActivity(intent);
                    dailyDrawer.closeDrawers();
                    ManageActivity.finishAllActivity();
                    break;
                case R.id.btn_02:
                    dailyDrawer.closeDrawers();
                    break;
                case R.id.btn_03:
                    //News.newsActivity.finish();//
                    intent.setClass(DailyJinShan.this, News.class);
                    startActivity(intent);
                    dailyDrawer.closeDrawers();
                    break;
                case R.id.btn_04:
                    //About.aboutActivity.finish();
                    intent.setClass(DailyJinShan.this, About.class);
                    startActivity(intent);
                    dailyDrawer.closeDrawers();
                case R.id.btn_daily_back:
                    finish();
            }
        }
    }
}
