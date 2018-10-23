package com.hanhui.jiaoshoutranslation;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static String getJson (String path){
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //创建容器
        try {
            URL url = new URL(path);//封装url
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect(); //连接
            InputStream inputStream = connection.getInputStream(); //w文件流
            byte []buf = new byte[1024];
            int hasRead = 0;
            while ((hasRead = inputStream.read(buf)) != -1){
                baos.write(buf,0,hasRead);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return baos.toString();
    }
}
