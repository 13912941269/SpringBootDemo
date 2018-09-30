package com.chemguan.util.wxpay;

import com.chemguan.util.OKHttpUtil;
import com.chemguan.util.Tools;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WXPayUtil {

	/**
	 *
	 * @param pdesc  商品描述
	 * @param attach 附加数据
	 * @param paymoney 支付金额
	 * @param openid 用户
	 *
	 */
	public Map payForOrder(HttpServletRequest request,
							String pdesc, String attach,
						   String paymoney,String openid,
						   String appid,String partner,
						   String notify_url,String partnerkey,String createorder_url){
		Map parammap = new HashMap<>();

		//10位随机数
		String nonce_str = Tools.getCurrTime() + Tools.randomcode(4);
		//商户订单号
		String out_trade_no = Tools.productCode();
		//金额转化为分为单位
		String finalmoney = Tools.exchangemoney(paymoney);
		//订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		//交易类型
		String trade_type = "JSAPI";


		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", partner);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", pdesc);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", finalmoney);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);
		String sign = RequestHandler.createSign(packageParams,partnerkey);



		//准备订单详情扩展字符串(二)
		String xml="<xml>"+
				"<appid>"+appid+"</appid>"+
				"<mch_id>"+partner+"</mch_id>"+
				"<nonce_str>"+nonce_str+"</nonce_str>"+
				"<sign>"+sign+"</sign>"+
				"<body><![CDATA["+pdesc+"]]></body>"+
				"<attach>"+attach+"</attach>"+
				"<out_trade_no>"+out_trade_no+"</out_trade_no>"+
				"<total_fee>"+finalmoney+"</total_fee>"+
				"<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
				"<notify_url>"+notify_url+"</notify_url>"+
				"<trade_type>"+trade_type+"</trade_type>"+
				"<openid>"+openid+"</openid>"+
				"</xml>";

		
		//用于接收订单详情扩展字符串(package的prepay_id)
		JSONObject jsonObject = new OKHttpUtil().xmlPost(xml, createorder_url);
		String prepay_id  ="";
		if(jsonObject!=null){
			//String return_code  = (String) jsonObject.get("return_code");
			try {
				prepay_id  = (String) jsonObject.get("prepay_id");
			}catch (Exception e){}
			if(prepay_id.equals("")){
				parammap.put("ErrorMsg", "统一支付接口获取预支付订单出错:"+jsonObject.toString());
			}else{
				parammap.put("ErrorMsg", "统一支付下单成功！");
			}
		}


		String timestamp = Tools.getTimeStamp();

		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		finalpackage.put("appId", appid);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonce_str);
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		String finalsign = RequestHandler.createSign(finalpackage,partnerkey);


		parammap.put("appid", appid);
		parammap.put("timeStamp", timestamp);
		parammap.put("nonceStr", nonce_str);
		parammap.put("package", packages);
		parammap.put("sign", finalsign);
		return parammap;
	}
}
