package com.hanhui.jiaoshoutranslation;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class SentenceFragment extends Fragment {

    String baidu_api_path = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    String url_path;
    String baidu_appid = "20180719000186936";
    String baidu_key = "HrkxMhST6zMhWjebRtpi";
    String from_lang = "auto";
    String to_lang = "zh";
    //String query;
    String salt = "123";//随机数，干嘛的？
    String sign = "";
    String [] sp_args = {
            "中文",
            "英语",
            "德语",
            "日语",
            "法语",
            "文言"
    };

    Spinner sp_stc_from;
    Spinner sp_stc_to;
    Button btn_tsl;
    Button btn_clr;
    TextView tv;
    EditText et_stc;
    Button btn_exg;
    boolean flag = false;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                String string = (String) msg.obj;
                //Log.i("dddddddd", "handleMessage============: "+string);
                Gson gson = new Gson();
                com.hanhui.jiaoshoutranslation.baidu.JsonRootBean bean = gson.fromJson(string,com.hanhui.jiaoshoutranslation.baidu.JsonRootBean.class);
                List<com.hanhui.jiaoshoutranslation.baidu.Trans_result> list = bean.getTrans_result();
                tv.setText(list.get(0).getDst());
            }
        }
    };





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_sentence, container, false);

        sp_stc_from = view.findViewById(R.id.sp_sentence01);
        sp_stc_to = view.findViewById(R.id.sp_sentence02);
        btn_clr = view.findViewById(R.id.btn_sentence_clear);
        btn_tsl = view.findViewById(R.id.btn_sentence_translate);
        btn_exg = view.findViewById(R.id.btn_exg);
        tv = view.findViewById(R.id.tv_sentence_output);
        et_stc = view.findViewById(R.id.et_sentence_input);
        et_stc.setHorizontallyScrolling(false);
        et_stc.setMaxLines(7);
//
//        ArrayAdapter<?> adapter01 =new ArrayAdapter<Object>(getActivity(), R.layout.item_spinner,sp_args);
//        ArrayAdapter<?> adapter02 =new ArrayAdapter<Object>(getActivity(), R.layout.item_spinner,sp_args);

        ArrayAdapter<?> adapter =new ArrayAdapter<Object>(getActivity(), R.layout.item_spinner,sp_args);
        sp_stc_from.setAdapter(adapter);
        sp_stc_to.setAdapter(adapter);

        //重写回车键输入相应事件
        et_stc.setOnEditorActionListener( new TextView.OnEditorActionListener(){
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
        btn_exg.setOnClickListener(listener);
        sp_stc_to.setSelection(1,true);

        sp_stc_from.setOnItemSelectedListener(sp_listener);

        return view;
    }

    AdapterView.OnItemSelectedListener sp_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
            //获取选择的项的值
            if(position == 5){
                et_stc.setHint("请输入文言文");
                return;
            }
            String sInfo=adapter.getItemAtPosition(position).toString();
            et_stc.setHint("请输入"+sInfo.substring(0,1)+"文");
        }
        @Override//什么都没选
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_sentence_translate) {
                //获取输入数据，转格式
                String q = et_stc.getText().toString().replace("\n","");
                Log.i("tag", "onClick: ======="+q);

//                try{
//                    query = URLEncoder.encode(q,"utf-8");
//                }catch(UnsupportedEncodingException e){
//                    e.printStackTrace();
//                }
//                //query = et.getText().toString();


                //判断是为空
                //if(TextUtils.isEmpty(q.trim())){
                if(TextUtils.isEmpty(q.replace(" ", ""))){
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
                String sp_select1 = sp_stc_from.getSelectedItem().toString();
                String sp_select2 = sp_stc_to.getSelectedItem().toString();
                switch(sp_select1){
                    case "中文":
                        q = q.toLowerCase();//大写换小写;
                        from_lang = "zh";
                        break;
                    case "英语":
                        from_lang ="en";
                        break;
                    case "德语":
                        from_lang ="de";
                        break;
                    case "日语":
                        from_lang ="jp";
                        break;
                    case"法语":
                        from_lang ="fra";
                        break;
                    case"文言":
                        from_lang ="wyw";
                        break;
                }
                switch(sp_select2){
                    case "中文":
                        q = q.toLowerCase();//大写换小写;
                        to_lang = "zh";
                        break;
                    case "英语":
                        to_lang ="en";
                        break;
                    case "德语":
                        to_lang ="de";
                        break;
                    case "日语":
                        to_lang ="jp";
                        break;
                    case"法语":
                        to_lang ="fra";
                        break;
                    case"文言":
                        to_lang ="wyw";
                        break;
                }

                //p拼接url
                sign = MD5.MD5Encode(baidu_appid+q+salt+baidu_key);
                url_path = baidu_api_path+"?q="+q+"&from="+from_lang+"&to="+to_lang+"&appid="+baidu_appid+"&salt="+salt+"&sign="+sign;

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

            }else if(view.getId() == R.id.btn_sentence_clear){
                tv.setText("");
                et_stc.setText("");
            }else if (view.getId() == R.id.btn_exg){
                //获取选中位置
                int f,t;
                for (f = 0; f < sp_args.length; f++){
                    if (sp_args[f] == sp_stc_from.getSelectedItem().toString()){ break; }
                }
                for (t = 0; t < sp_args.length; t++){
                    if (sp_args[t] == sp_stc_to.getSelectedItem().toString()){ break; }
                }
                //int f = sp_stc_from.getId();
                //int t = sp_stc_to.getId();
                Log.i("dddddd", t+"onClick:+=+=+=+== "+f);
                sp_stc_from.setSelection(t,true);
                sp_stc_to.setSelection(f,true);
                if(!TextUtils.isEmpty(et_stc.getText().toString().replace("\n","").replace(" ",""))) { btn_tsl.performClick(); }
            }
        }
    }
}
