package com.chemguan.util.wxtemplate;

import com.chemguan.util.OKHttpUtil;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2018-07-16.
 */
public class sendTemplateMsg {

    //微信公众号发消息给用户
    public static boolean sendMsg(String token,Template template,String templateurl){
        boolean flag=false;
        //拼接地址获取token
        String requestUrl=templateurl;
        requestUrl=requestUrl.replace("ACCESS_TOKEN", token);
        //获取返回结果
        JSONObject jsonResult= OKHttpUtil.httpPost(requestUrl,template.toJSON());
        if(jsonResult!=null){
            int errorCode=jsonResult.getInt("errcode");
            String errorMessage=jsonResult.getString("errmsg");
            if(errorCode==0){
                flag=true;
            }else{
                flag=false;
            }
        }
        return flag;
    }
}
