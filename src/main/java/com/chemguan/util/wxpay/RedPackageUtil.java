package com.chemguan.util.wxpay;

import com.chemguan.util.Tools;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.UUID;

import static com.chemguan.util.wxpay.RequestHandler.createSendRedPackOrderSign;

/**
 * Created by Administrator on 2018-07-16.
 */
public class RedPackageUtil {

    /**
     * 发送现金红包
     * @throws
     */
    public String sendRedPack(String re_openid, String money,String partner,String partnerkey,String redpackageurl,
                              String appid){
        //金额转化为分为单位
        int total_amount=Integer.parseInt(Tools.exchangemoney(money));
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = ia.getHostAddress(); // 获取本机IP地址
        SendRedPack sendRedPack = new SendRedPack();
        sendRedPack.setNonce_str(uuid);
        sendRedPack.setMch_id(partner);
        sendRedPack.setMch_billno(Tools.ordernumber());
        sendRedPack.setWxappid(appid);
        sendRedPack.setSend_name("蟹蟹卿".trim());
        sendRedPack.setTotal_num(1);
        sendRedPack.setAct_name("商家返利".trim());
        sendRedPack.setWishing("恭喜发财".trim());
        sendRedPack.setRemark("商家返利".trim());
        sendRedPack.setClient_ip(ip);
        sendRedPack.setRe_openid(re_openid);
        sendRedPack.setTotal_amount(total_amount);
        String sign = createSendRedPackOrderSign(sendRedPack,partnerkey);
        sendRedPack.setSign(sign);

        XMLUtils xmlUtils= new XMLUtils();
        xmlUtils.xstream().alias("xml", sendRedPack.getClass());
        String xml = xmlUtils.xstream().toXML(sendRedPack);
        String response = ssl(partner,redpackageurl, xml);
        /*Map<String, String> map = null;
        try {
            map = xmlUtils.parseXml(response);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return response;
    }





    /**
     * 发送请求
     * */
    private String ssl(String partner,String url,String data){
        StringBuffer message = new StringBuffer();
        try {
            String mchId = partner;
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            String name="";
            try {
                name = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            name=name+"/apiclient_cert.p12";
            FileInputStream instream = new FileInputStream(new File(name));
            keyStore.load(instream, mchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpPost httpost = new HttpPost(url);
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return message.toString();
    }
}
