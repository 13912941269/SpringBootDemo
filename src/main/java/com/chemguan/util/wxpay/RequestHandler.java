package com.chemguan.util.wxpay;


import org.apache.commons.codec.digest.DigestUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RequestHandler {
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(SortedMap<String, String> packageParams,String key) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		return sign;
	}



	/**
	 * 红包生成签名
	 * */
	public static String createSendRedPackOrderSign(SendRedPack redPack,String key){
		StringBuffer sign = new StringBuffer();
		sign.append("act_name=").append(redPack.getAct_name());
		sign.append("&client_ip=").append(redPack.getClient_ip());
		sign.append("&mch_billno=").append(redPack.getMch_billno());
		sign.append("&mch_id=").append(redPack.getMch_id());
		sign.append("&nonce_str=").append(redPack.getNonce_str());
		sign.append("&re_openid=").append(redPack.getRe_openid());
		sign.append("&remark=").append(redPack.getRemark());
		sign.append("&send_name=").append(redPack.getSend_name());
		sign.append("&total_amount=").append(redPack.getTotal_amount());
		sign.append("&total_num=").append(redPack.getTotal_num());
		sign.append("&wishing=").append(redPack.getWishing());
		sign.append("&wxappid=").append(redPack.getWxappid());
		sign.append("&key=").append(key);
		return DigestUtils.md5Hex(sign.toString()).toUpperCase();
	}
}
