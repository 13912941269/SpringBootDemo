package com.chemguan.util;

import net.sf.json.JSONObject;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import java.io.IOException;

/**
 * Created by ShiWei on 2018-06-12.
 */
public class OKHttpUtil {

    private static OkHttpClient client = OKHttpSingle.getInstance();


    /**
     * xmlpost
     * @throws IOException
     */
    public static JSONObject xmlPost(String xmlstr,String url){
        MediaType mediaType = MediaType.parse("application/xml");
        RequestBody body = RequestBody.create(mediaType, xmlstr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/xml")
                .build();
        String jsonstr = null;
        JSONObject jsonObject =null;
        try {
            Response response = client.newCall(request).execute();
            jsonstr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotEmpty(jsonstr)){
            jsonObject=JSONObject.fromObject(jsonstr);
        }
        return  jsonObject;
    }




    /**
     * httpGet
     * @throws IOException
     */
    public static JSONObject httpGet(String url){
        Request request = new Request.Builder().url(url).build();
        String jsonstr = null;
        JSONObject jsonObject =null;
        try {
            Response response = client.newCall(request).execute();
            jsonstr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotEmpty(jsonstr)){
            jsonObject=JSONObject.fromObject(jsonstr);
        }
        return jsonObject;
    }




    /**
     * httpGet
     * @throws IOException
     */
    public static JSONObject httpPost(String url,String json){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        String jsonstr = null;
        JSONObject jsonObject =null;
        try {
            Response response = client.newCall(request).execute();
            jsonstr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotEmpty(jsonstr)){
            jsonObject=JSONObject.fromObject(jsonstr);
        }
        return jsonObject;
    }

    public static void main(String[] args) throws IOException {

    }
}
