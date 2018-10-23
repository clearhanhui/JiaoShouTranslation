package com.hanhui.jiaoshoutranslation;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class WordFragment extends Fragment {

    String url_path="";//请求地址
    String jinshan_api_path = "http://dict-co.iciba.com/api/dictionary.php";
    String jinshan_key = "6A39E8BFBA90906A6E6170ABB73A9715";
    String query;
    String url_sound=""; //音频地址
    String [] sp_args = {
            "英语 -> 中文",
            "中文 -> 英语"
    };
    String eUrl_sound;
    String aUrl_sound;
    Spinner sp_wd;
    Button btn_tsl;
    Button btn_clr;
    Button btn_e_sound;
    Button btn_a_sound;
    TextView tv;
    TextView tv_a;
    TextView tv_e;
    TextView tv02;
    EditText et_wd;
    MediaPlayer player = new MediaPlayer();

    boolean isEn = true; //true代表英译汉，false汉译英

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
               String string = (String) msg.obj;
               Gson gson = new Gson();
                if(isEn){
                    try{
                        com.hanhui.jiaoshoutranslation.jinshan_yh.JsonRootBean bean = gson.fromJson(string,com.hanhui.jiaoshoutranslation.jinshan_yh.JsonRootBean.class);
                        List<com.hanhui.jiaoshoutranslation.jinshan_yh.Symbols> list = bean.getSymbols();
                        //String voice_info = "美音 [ "+list.get(0).getPh_am()+" ]\n英音 [ "+list.get(0).getPh_en()+" ]\n";
                        eUrl_sound = list.get(0).getPh_en_mp3();
                        aUrl_sound = list.get(0).getPh_am_mp3();
                        //Log.i("ddd", "handleMessage: ========="+eUrl_sound+aUrl_sound+list.get(0).getPh_tts_mp3());
                        String mean_info = "";
                        for (int i = 0; i < list.get(0).getParts().size();i++){
                            mean_info += ("\n"+list.get(0).getParts().get(i).getPart()+list.get(0).getParts().get(i).getMeans());
                        }
                        url_sound = list.get(0).getPh_am_mp3();
                       // Log.i("dddd", "handleMessage: =========="+url_sound);

                        try {
                            player.setDataSource(aUrl_sound);
                            btn_a_sound.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                        }
                        player.reset();
                        try {
                            player.setDataSource(eUrl_sound);
                            btn_e_sound.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                        }
                        player.reset();

                        tv_a.setText("美."+list.get(0).getPh_am());//美音
                        tv_e.setText("英."+list.get(0).getPh_en());//英音
                        tv.setText(mean_info.replace("[","  ").replace("]",""));
                    }catch (java.lang.NullPointerException e){
                        Toast.makeText(getActivity(),"对不起(＞人＜)，小的查不到~~",Toast.LENGTH_SHORT).show();
                        //flag = 1;
                        //btn_translation.performClick();
                    }
                }else{
                    try{
                        String info = "";
                        com.hanhui.jiaoshoutranslation.jishan_hy.JsonRootBean bean = gson.fromJson(string,com.hanhui.jiaoshoutranslation.jishan_hy.JsonRootBean.class);
                        List<com.hanhui.jiaoshoutranslation.jishan_hy.Symbols> symbolsList = bean.getSymbols();
                        for (int i = 0; i < symbolsList.size(); i++){
                            url_sound = symbolsList.get(i).getPh_am_mp3()+"\n"+symbolsList.get(i).getPh_en_mp3()+"\n"+symbolsList.get(i)+"\n"+symbolsList.get(i).getPh_tts_mp3()+"\n"+symbolsList.get(i).getSymbol_mp3();
                            //info += "[读音."+symbolsList.get(i).getWord_symbol()+"  ";
                            List<com.hanhui.jiaoshoutranslation.jishan_hy.Parts> partsList = symbolsList.get(i).getParts();
                            for (int j = 0; j < partsList.size(); j++){
                                //info += "词性."+partsList.get(j).getPart_name()+"  ";
                                List<com.hanhui.jiaoshoutranslation.jishan_hy.Means> meansList = partsList.get(j).getMeans();
                                for (int k = 0; k < meansList.size(); k++){
                                    info += "  "+meansList.get(k).getWord_mean()+"\n";
                                }
                            }
                            //Log.i("dddd", "handleMessage: =========="+url_sound);
                        }
                        tv02.setText("\n"+info);
                    }catch (java.lang.NullPointerException e){
                        Toast.makeText(getActivity(),"对不起(＞人＜)，小的查不到~~",Toast.LENGTH_SHORT).show();
                        //flag = 1;
                        //btn_translation.performClick();
                    }
                }

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_word, container, false);
        btn_clr = view.findViewById(R.id.btn_word_clear);
        btn_tsl = view.findViewById(R.id.btn_word_translate);
        tv = view.findViewById(R.id.tv_word_output);
        et_wd = view.findViewById(R.id.et_word_input);
        sp_wd = view.findViewById(R.id.sp_word);
        btn_a_sound = view.findViewById(R.id.btn_am);
        btn_e_sound = view.findViewById(R.id.btn_em);
        tv_a = view.findViewById(R.id.tv_am);
        tv_e = view.findViewById(R.id.tv_em);
        tv02 = view.findViewById(R.id.tv_word_output02);
        //btn_wd_sound = view.findViewById(R.id.btn_wd_start);

        et_wd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_e.setText("");
                tv.setText("");
                tv_a.setText("");
                tv02.setText("");
                btn_a_sound.setVisibility(View.INVISIBLE);
                btn_e_sound.setVisibility(View.INVISIBLE);
            }
        });

        ArrayAdapter<?> adapter =new ArrayAdapter<Object>(getActivity(), R.layout.item_spinner,sp_args);
        sp_wd.setAdapter(adapter);

        //重写回车键输入相应事件
        et_wd.setOnEditorActionListener( new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_ACTION_SEARCH){
                    btn_tsl.performClick();
                }
                return false;
            }
        });

        //监听
        MyListener listener = new MyListener();
        btn_tsl.setOnClickListener(listener);
        btn_clr.setOnClickListener(listener);
        btn_e_sound.setOnClickListener(listener);
        btn_a_sound.setOnClickListener(listener);
        //btn_wd_sound.setOnClickListener(listener);
        sp_wd.setOnItemSelectedListener(sp_listener);

        return view;
    }

    AdapterView.OnItemSelectedListener sp_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
            //获取选择的项的值
            String sInfo=adapter.getItemAtPosition(position).toString();
            et_wd.setHint("请输入"+sInfo.substring(0,1)+"文");
        }
        @Override//什么都没选
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };



    /**
     * 停止音乐播放的方法 MediaPlayer
     * */
    public void stopMusic(){
        if (player!=null) {
            player.pause();   //暂停
            player.seekTo(0);  //将进度条移动回最初的位置
            player.stop();		//停止播放
        }
    }


    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view){

            switch (view.getId()){
                case R.id.btn_word_translate:
                    //获取输入数据，转格式
                    String q = et_wd.getText().toString();
                    try{
                        query = URLEncoder.encode(q,"utf-8");
                    }catch(UnsupportedEncodingException e){
                        e.printStackTrace();
                    }
                    //query = et.getText().toString();

                    //判断是为空
                    if(TextUtils.isEmpty(query.trim())){
                        Toast.makeText(getActivity(),"小主想查什么呀",Toast.LENGTH_SHORT).show();
                        return ;
                    }

                    //判断手机是否联网
                    ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if(networkInfo == null || !networkInfo.isAvailable()) {
                        Toast.makeText(getActivity(),"对不起(＞人＜)，小的无法联网",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //判断是汉译英，还是英译汉
                    String sp_select = sp_wd.getSelectedItem().toString();
                    switch(sp_select){
                        case "英语 -> 中文":

                            //利用正则表达式判断是否是正确格式的单词
                            Pattern pattern_en = Pattern.compile("[a-zA-Z]+");
                            if(!pattern_en.matcher(q.replace(" ", "")).matches()){
                                Toast.makeText(getActivity(),"小主输入的格式不对啦",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //大写换小写
                            q = q.toLowerCase();
                            query = query.toLowerCase();

                            isEn = true;
                            break;
                        case "中文 -> 英语":

                            //利用正则表达式判断是否是正确格式的单词
                            Pattern pattern_zh = Pattern.compile("[\u4e00-\u9fa5]+");
                            if(!pattern_zh.matcher(q.replace(" ", "")).matches()){
                                Toast.makeText(getActivity(),"小主输入的格式不对啦",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            isEn = false;
                            break;
                    }

                    //拼接url
                    url_path = jinshan_api_path+"?w="+query+"&type=json&key="+jinshan_key;

                    //开启子线程，获取数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String str = HttpUtils.getJson(url_path);
                            Message message = new Message();
                            message.what = 1;
                            message.obj = str;
                            handler.sendMessage(message);
                        }
                    }).start();
                    break;

                case R.id.btn_word_clear:
                    //清空
                    tv.setText("");
                    et_wd.setText("");
                    btn_a_sound.setVisibility(View.INVISIBLE);
                    btn_e_sound.setVisibility(View.INVISIBLE);
                    break;

                case R.id.btn_am:
                    stopMusic();
                    player.reset();	//重置播放器
                    try {
                        player.setDataSource(aUrl_sound);
//				准备播放
                        player.prepare();
//				开始播放
                        player.start();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        //Toast.makeText( getActivity(),"对不起(＞人＜),找不到发音QAQ", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btn_em:
                    stopMusic();
                    player.reset();	//重置播放器
                    try {
                        //Log.i("dddd", "onClick: ++++++"+eUrl_sound);
                        player.setDataSource(eUrl_sound);
//				准备播放
                        player.prepare();
//				开始播放
                        player.start();
                    } catch (Exception e) {
                        //Toast.makeText( getActivity(),"对不起(＞人＜),找不到发音QAQ", Toast.LENGTH_SHORT).show();
                        //e.printStackTrace();
                    }
                    break;
            }

//            if (view.getId() == R.id.btn_word_translate){
//
//                //获取输入数据，转格式
//                String q = et_wd.getText().toString();
//                try{
//                    query = URLEncoder.encode(q,"utf-8");
//                }catch(UnsupportedEncodingException e){
//                    e.printStackTrace();
//                }
//                //query = et.getText().toString();
//
//                //判断是为空
//                if(TextUtils.isEmpty(query.trim())){
//                    Toast.makeText(getActivity(),"小主想查什么呀",Toast.LENGTH_SHORT).show();
//                    return ;
//                }
//
//                //判断手机是否联网
//                ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(CONNECTIVITY_SERVICE);
//                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//                if(networkInfo == null || !networkInfo.isAvailable()) {
//                    Toast.makeText(getActivity(),"对不起(＞人＜)，小的无法联网",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //判断是汉译英，还是英译汉
//                String sp_select = sp_wd.getSelectedItem().toString();
//                switch(sp_select){
//                    case "English -> 中文":
//
//                        //利用正则表达式判断是否是正确格式的单词
//                        Pattern pattern_en = Pattern.compile("[a-zA-Z]+");
//                        if(!pattern_en.matcher(q.replace(" ", "")).matches()){
//                            Toast.makeText(getActivity(),"小主输入的格式不对啦",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        //大写换小写
//                        q = q.toLowerCase();
//                        query = query.toLowerCase();
//
//                        isEn = true;
//                        break;
//                    case "中文 -> English":
//
//                        //利用正则表达式判断是否是正确格式的单词
//                        Pattern pattern_zh = Pattern.compile("[\u4e00-\u9fa5]+");
//                        if(!pattern_zh.matcher(q.replace(" ", "")).matches()){
//                            Toast.makeText(getActivity(),"小主输入的格式不对啦",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        isEn = false;
//                        break;
//                }
//
//                //拼接url
//                url_path = jinshan_api_path+"?w="+query+"&type=json&key="+jinshan_key;
//
//                //开启子线程，获取数据
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String str = HttpUtils.getJson(url_path);
//                        Message message = new Message();
//                        message.what = 1;
//                        message.obj = str;
//                        handler.sendMessage(message);
//                    }
//                }).start();
//
//            }else if(view.getId() == R.id.btn_word_clear) {
//                //清空
//                tv.setText("");
//                et_wd.setText("");
//            }
//           else if(view.getId() == R.id.btn_wd_start){
//
//            }
        }
    }
}
