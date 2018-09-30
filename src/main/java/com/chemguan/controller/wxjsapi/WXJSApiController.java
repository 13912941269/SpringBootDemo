package com.chemguan.controller.wxjsapi;

import com.chemguan.util.wxjsapi.wxjspai;
import com.chemguan.util.wxlogin.WeixinUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ShiWei on 2018-07-12.
 */
@Controller
@RequestMapping("pc")
public class WXJSApiController {


    @Value("${wx.ticket_url}")
    private String ticketurl;


    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appsecret;


    @Value("${wx.token_url}")
    public String token_url;

    /**
     * jsapi
     * @return
     */
    @RequestMapping("wxjsapitest")
    public String wxJsApiTest(HttpServletRequest request, ModelMap model){
        String url="";
        String accessToken= WeixinUtil.getAccessToken(token_url,appid,appsecret);
        wxjspai api=new wxjspai();
        Map map = api.jsapiparam(appid,ticketurl,url,accessToken);
        //随机串
        model.put("nonceStr", map.get("nonceStr"));
        //时间戳
        model.put("timestamp", map.get("timestamp"));
        //签名
        model.put("signature", map.get("signature"));

        model.put("appid", map.get("appid"));
        return "";
    }

}
