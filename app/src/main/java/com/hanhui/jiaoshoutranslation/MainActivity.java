package com.hanhui.jiaoshoutranslation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager vp;
    PagerSlidingTabStrip tabStrip;
    DrawerLayout drawerLayout;
    //EditText et_in;
    Button btn_menu;
    Button btn_trans;
    Button btn_daily;
    Button btn_news;
    Button btn_about;
    List<Fragment> fragList = new ArrayList<Fragment>();
    public static String URLTitles[]={
            "译词",
            "译句",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //ManageActivity.addActivity(this);
        vp = findViewById(R.id.m_vp);
        tabStrip = findViewById(R.id.m_tabs);
        drawerLayout = findViewById(R.id.main_drawer);
        btn_menu = findViewById(R.id.btn_menu);
        btn_trans = findViewById(R.id.btn_01);
        btn_daily = findViewById(R.id.btn_02);
        btn_news = findViewById(R.id.btn_03);
        btn_about = findViewById(R.id.btn_04);

        Fragment wordFragment = new WordFragment();
        fragList.add(wordFragment);
        Fragment sentenceFragment = new SentenceFragment();
        fragList.add(sentenceFragment);

        VPAdapter adapter = new VPAdapter(getSupportFragmentManager(), URLTitles, fragList);
        vp.setAdapter(adapter);
        tabStrip.setViewPager(vp);

        ButtonListener listener = new ButtonListener();
        btn_trans.setOnClickListener(listener);
        btn_daily.setOnClickListener(listener);
        btn_news.setOnClickListener(listener);
        btn_menu.setOnClickListener(listener);
        btn_about.setOnClickListener(listener);

//        vp.setOnPageChangeListener(vplistener);
////        //((TextView)((LinearLayout) tabStrip.getChildAt(0)).getChildAt(1)).setBackgroundColor(getResources().getColor(R.color.Google_red));
//        ((TextView)((LinearLayout) tabStrip.getChildAt(0)).getChildAt(0)).setTextColor(getResources().getColor(R.color.Google_blue));
    }



    ViewPager.OnPageChangeListener vplistener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            updateTextStyle(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
    private void updateTextStyle(int position) {
        LinearLayout tabsContainer = (LinearLayout) tabStrip.getChildAt(0);
        for(int i=0; i< tabsContainer.getChildCount(); i++) {
            TextView textView = (TextView) tabsContainer.getChildAt(i);
            if(position == i) {
                textView.setTextSize(15);
            } else {
                //textView.setBackgroundColor(getResources().getColor(R.color.white));
                //textView.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }


    class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent();

            //跳转界面
            switch (view.getId()) {
                case R.id.btn_menu:
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;
                case R.id.btn_01:
                    drawerLayout.closeDrawers();
                    break;
                case R.id.btn_02:
                    intent.setClass(MainActivity.this, DailyJinShan.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                    break;
                case R.id.btn_03:
                    intent.setClass(MainActivity.this, News.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                    break;
                case R.id.btn_04:
                    intent.setClass(MainActivity.this, About.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();

            }
        }
    }


}
