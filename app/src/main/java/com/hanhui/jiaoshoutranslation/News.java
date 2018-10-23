package com.hanhui.jiaoshoutranslation;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hanhui.jiaoshoutranslation.R;
import com.hanhui.jiaoshoutranslation.news.Articles;
import com.hanhui.jiaoshoutranslation.news.JsonRootBean;

import java.util.List;



public class News extends AppCompatActivity {

    //public static AppCompatActivity newsActivity;


    String url_news_source = "";
    String url_path = "https://newsapi.org/v2/top-headlines?sources=cnn&apiKey=6ac2710669dd46da8c42f3607453cda6";
    Button btn_trans;
    Button btn_daily;
    Button btn_news;
    Button btn_about;
    Button btn_back;
    DrawerLayout newsDrawer;
    List<Articles> list;
    ListView lv;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                String string = (String) msg.obj;
                //Log.i("+++++++", "handleMessage:+++++++++++++++++++++ "+string);
                Gson gson = new Gson();
                JsonRootBean bean = gson.fromJson(string, JsonRootBean.class);
                list = bean.getArticles();
                ArticleAdapter adapter = new ArticleAdapter(News.this,list);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(listener);
            }
        }
    };


    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            url_news_source = list.get(i).getUrl();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url_news_source);
            intent.setData(content_url);
            startActivity(intent);
            //Toast.makeText(News.this,url_news_source,Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent();
            intent.setClass(MainActivity.this,SecondActivity.class);
            intent.putExtra("all",s);
            startActivity(intent);*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        getSupportActionBar().hide();
        ManageActivity.addActivity(this);
        //newsActivity = this;
        btn_trans = findViewById(R.id.btn_01);
        btn_daily = findViewById(R.id.btn_02);
        btn_news = findViewById(R.id.btn_03);
        btn_about = findViewById(R.id.btn_04);
        btn_back = findViewById(R.id.btn_news_back);
        newsDrawer = findViewById(R.id.news_drawer);
        lv = findViewById(R.id.lv_news);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str_json = HttpUtils.getJson(url_path);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = str_json;
                handler.sendMessage(msg);
            }
        }).start();



        ButtonListener listener = new ButtonListener();
        btn_trans.setOnClickListener(listener);
        btn_daily.setOnClickListener(listener);
        btn_news.setOnClickListener(listener);
        btn_about.setOnClickListener(listener);
        btn_back.setOnClickListener(listener);

    }

    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent();

            //跳转界面
            switch (view.getId()) {
                case R.id.btn_01:
//                    intent.setClass(News.this, MainActivity.class);
//                    startActivity(intent);
                    newsDrawer.closeDrawers();
                    ManageActivity.finishAllActivity();
                    break;
                case R.id.btn_02:
                    //News.newsActivity.finish();
                    intent.setClass(News.this, DailyJinShan.class);
                    startActivity(intent);
                    newsDrawer.closeDrawers();
                    break;
                case R.id.btn_03:
                    newsDrawer.closeDrawers();
                    break;
                case R.id.btn_04:
                    //About.aboutActivity.finish();
                    intent.setClass(News.this, About.class);
                    startActivity(intent);
                    newsDrawer.closeDrawers();
                    break;
                case R.id.btn_news_back:
                    finish();
            }
        }
    }
}
