package com.chemguan.util.wxlogin;


import com.chemguan.util.OKHttpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class WeixinUtil {
    public static String TOKEN="";

    public static Long DATETIME=(long)0;

	/** 
	 * 获取access_token 
	 *
	 * @return 
	 */  
	public static String getAccessToken(String token_url,String appid,String appsecret) {
        String tokenstr="";
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        if(StringUtils.isEmpty(TOKEN)){
            JSONObject jsonObject = OKHttpUtil.httpGet(requestUrl);
            tokenstr=jsonObject.getString("access_token");
            TOKEN=tokenstr;
            DATETIME=(new Date()).getTime();
        }else{
            long time = new Date().getTime();
            if((time-DATETIME)/1000>1200){
                JSONObject jsonObject = OKHttpUtil.httpGet(requestUrl);
                tokenstr=jsonObject.getString("access_token");
                TOKEN=tokenstr;
                DATETIME=(new Date()).getTime();
            }else{
                tokenstr=TOKEN;
            }
        }
        return tokenstr;
	}



	
    /**
     * 获取用户信息
     * 
     * @param accessToken 接口访问凭证
     * @param openId 用户标识
     * @return WeixinUserInfo
     */
    public static SNSUserInfo getUserInfo(String openId,String accessToken,String userinfo_url) {
    	 SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = userinfo_url;
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 获取用户信息
        JSONObject jsonObject = OKHttpUtil.httpGet(requestUrl);
        if (null != jsonObject) {
            try {
            	snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(jsonObject.getInt("sex"));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
            } catch (Exception e) {
            	 snsUserInfo = null;
                 int errorCode = jsonObject.getInt("errcode");
                 String errorMsg = jsonObject.getString("errmsg");
                 System.out.println("获取用户信息失败:"+errorCode+errorMsg);
            }
        }
        return snsUserInfo ;
    }






    /**
     * 判断用户是否关注
     *
     * @param openId 用户标识
     * @return WeixinUserInfo
     */
    public static Boolean getUserInfoGZ(String openId,String accessToken,String usersub_url) {
        String requestUrl = usersub_url;
        //String accessToken = getAccessToken();
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        JSONObject jsonObjects = OKHttpUtil.httpGet(requestUrl);
        Boolean subscribe=true;
        try {
            int subtype=jsonObjects.getInt("subscribe");
            if(subtype!=1){
                subscribe=false;
            }
        } catch (Exception e) {

        }
        return subscribe ;
    }




	
	
    /**
     * 获取网页授权凭证
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinOauth2 getOauth2AccessToken(String appId, String appSecret, String code,String oauth2token_url) {
        WeixinOauth2 wat = null;
        // 拼接请求地址
        String requestUrl = oauth2token_url;
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        if(code!=null){
        	requestUrl = requestUrl.replace("CODE", code);
        }
        // 获取网页授权凭证
        JSONObject jsonObject = OKHttpUtil.httpGet(requestUrl);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                System.out.println("获取网页授权凭证失败"+errorCode+errorMsg);
            }
        }
        return wat;
    }
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
}  

