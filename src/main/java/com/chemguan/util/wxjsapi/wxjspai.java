package com.chemguan.util.wxjsapi;

import com.chemguan.util.OKHttpUtil;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by ShiWei on 2018-07-16.
 */
public class wxjspai {

    /**
     * jsapi所需参数
     * @param url
     * @return
     */
    public Map jsapiparam(String appid,String ticketurl,String url,String accessToken){
        String requestUrl=ticketurl;
        requestUrl=requestUrl.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = OKHttpUtil.httpGet(requestUrl);
        String ticket=jsonObject.getString("ticket");
        Map<String, String> ret = Wxsign.sign(ticket, url);
        ret.put("appid",appid);
        return ret;
    }
}
