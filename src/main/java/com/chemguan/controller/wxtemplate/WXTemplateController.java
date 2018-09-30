package com.chemguan.controller.wxtemplate;

import com.chemguan.util.wxlogin.WeixinUtil;
import com.chemguan.util.wxtemplate.Template;
import com.chemguan.util.wxtemplate.TemplateParam;
import com.chemguan.util.wxtemplate.sendTemplateMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ShiWei on 2018-07-12.
 */
@Controller
@RequestMapping("pc")
public class WXTemplateController {


    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appsecret;

    @Value("${wx.token_url}")
    public String token_url;

    @Value("${wx.template_url}")
    private static String templateurl;

    /**
     * 发送模板消息
     * @return
     */
    @RequestMapping("sendtemplatemsg")
    public void sendTemplateMsg(HttpServletRequest request){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Template temcus=new Template();
        //模板号
        temcus.setTemplateId("brzM-osD6JS8eJEE0TiOEvdURgOy-YfnN0PxsUCykHY");
        temcus.setTopColor("#00DD00");
        temcus.setToUser("");
        temcus.setUrl("");
        List<TemplateParam> parascus=new ArrayList<TemplateParam>();
        parascus.add(new TemplateParam("first","蟹蟹卿分红","#0044BB"));
        parascus.add(new TemplateParam("keyword2","蟹蟹卿","#0044BB"));
        parascus.add(new TemplateParam("keyword3","获取分红提醒","#0044BB"));
        parascus.add(new TemplateParam("keyword4","",""));
        parascus.add(new TemplateParam("keyword5",format.format(new Date()),"#0044BB"));
        parascus.add(new TemplateParam("remark","感谢使用","#AAAAAA"));
        temcus.setTemplateParamList(parascus);
        String tokenstr=WeixinUtil.getAccessToken(token_url,appid,appsecret);
        boolean result= sendTemplateMsg.sendMsg(tokenstr, temcus,templateurl);
    }

}
