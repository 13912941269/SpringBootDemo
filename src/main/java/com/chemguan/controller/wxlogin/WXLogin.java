package com.chemguan.controller.wxlogin;

import com.chemguan.util.wxlogin.EmojiFilter;
import com.chemguan.util.wxlogin.SNSUserInfo;
import com.chemguan.util.wxlogin.WeixinOauth2;
import com.chemguan.util.wxlogin.WeixinUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by ShiWei on 2018-07-13.
 */
public class WXLogin {

    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appsecret;

    @Value("${wx.token_url}")
    public String token_url;

    @Value("${wx.oauth2token_url}")
    public String oauth2token_url;

    @Value("${wx.userinfo_url}")
    public String userinfo_url;

    @Value("${wx.usersub_url}")
    public String usersub_url;

    /**
     *微信登陆获取用户信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("mobile/getUser")
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reurl = request.getParameter("reurl");
        WeixinUtil w = new WeixinUtil();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        EmojiFilter ef = new EmojiFilter();
        // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            //请跟换自己的appid和密钥
            WeixinOauth2 weixinOauth2 = w.getOauth2AccessToken(appid.trim(),appsecret.trim(), code,oauth2token_url);
            // 网页授权接口访问凭证
            String accessToken = weixinOauth2.getAccessToken();
            // 用户标识
            String openId = weixinOauth2.getOpenId();
            // 获取用户信息
            SNSUserInfo snsUserInfo = w.getUserInfo(accessToken, openId,userinfo_url);
            //自己写的sql语句根据oppenid查询用户是否存在
            //String username=emojiFilter.filterEmoji(snsUserInfo.getNickname());
            //String openid=(snsUserInfo.getOpenId());
            //String headimgurl=(snsUserInfo.getHeadImgUrl());
            //不存在创建用户
            if (true) {


            }else{

                //存在更新用户信息
            }
            HttpSession session = request.getSession();
            session.setAttribute("openid", snsUserInfo.getOpenId());
            if (reurl != null) {
                response.sendRedirect(reurl);
            }else{
                response.sendRedirect("index.do");
            }
        }
    }
}
