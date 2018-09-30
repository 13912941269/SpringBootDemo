package com.chemguan.controller.wxpay;

import com.chemguan.util.wxpay.RedPackageUtil;
import com.chemguan.util.wxpay.WXPayUtil;
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
public class WXPayController {


    @Value("${wx.redpackage_url}")
    private String redpackageurl;


    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appsecret;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    @Value("${notifyurl}")
    private String notify_url;

    @Value("${wx.createorder_url}")
    private String createorder_url;

    /**
     * 支付测试
     * @return
     */
    @RequestMapping("paytest")
    public String PayTest(HttpServletRequest request, ModelMap model){
        WXPayUtil pay=new WXPayUtil();
        Map map = pay.payForOrder(request, "商品购买", "", "0.1", "123",appid,partner,notify_url,partnerkey,createorder_url);
        model.put("appid",map.get("appid"));
        model.put("timeStamp", map.get("timestamp"));
        model.put("nonceStr",map.get("nonceStr"));
        model.put("package",map.get("package"));
        model.put("sign",map.get("sign"));
        model.put("ErrorMsg",map.get("ErrorMsg"));
        return "wxpay/wxpay";
    }



    /**
     * 发红包测试
     * @return
     */
    @RequestMapping("redpackagetest")
    public void redpackagetest(HttpServletRequest request, ModelMap model){
        RedPackageUtil red=new RedPackageUtil();
        String mapss = red.sendRedPack("","0.01",partner,partnerkey,redpackageurl,appid);
        if(mapss!=null){
            System.out.println(mapss);
        }
    }
}
