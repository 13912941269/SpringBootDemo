package com.chemguan.util;

import org.beetl.core.Configuration;
import org.beetl.core.Function;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class Tools{
	//生成指定长度的随机数字
	public static String randomcode(int length){
		String[] num = {"0","1","2","3","4","5","6","7","8","9"};
		Random rand = new Random();//创建Random类的对象rand  
        int index = 0;
        StringBuffer sbf = new StringBuffer();
		for(int i=0;i<length;i++){
			index = rand.nextInt(num.length-1);
			sbf.append(num[index]);
		}
		return sbf+"";
	}




	//生成13位支付编号
	public static String ordernumber(){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String dat=sdf1.format(new Date());
		String[] num ="0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		Random rand = new Random();//创建Random类的对象rand  
        int index = 0;
        StringBuffer sbf = new StringBuffer();
		for(int i=0;i<5;i++){
			index = rand.nextInt(num.length-1);
			sbf.append(num[index]);
		}
		return dat+sbf;
	}
	
	
	/**
	 * 生成14位随机编号
	 */
	public static String productCode(){
		String pcode="";		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyMMdd");
		String dat=sdf1.format(new Date());
		String[] num ="0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
        Random rand = new Random();//创建Random类的对象rand  
        int index = 0;
		StringBuffer sbf = new StringBuffer();
        for (int i=0; i<8; ++i){  
           index = rand.nextInt(num.length-1);
			sbf.append(num[index]);
        }  
		pcode=dat+sbf;
		return pcode;
	}






	public static int surplusdays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);
		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if(year1 != year2){//不同年
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++){
				if(i%4==0 && i%100!=0 || i%400==0){
					timeDistance += 366;
				}else {//不是闰年
					timeDistance += 365;
				}
			}
			return timeDistance + (day2-day1) ;
		}else{//同一年
			return day2-day1;
		}

	}



	/**
	 * 获取当前时间 HHmmss
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("HHmmss");
		String s = outFormat.format(now);
		return s;
	}



	/**
	 * 金额double转分
	 * @return String
	 */
	public static String exchangemoney(String money) {
		float sessionmoney = Float.parseFloat(money);
		String finalmoney = String.format("%.2f", sessionmoney);
		finalmoney = Integer.parseInt(finalmoney.replace(".", ""))+"";
		return finalmoney;
	}



	/**
	 * 获取编码字符集
	 * @param request
	 * @param response
	 * @return String
	 */

	public static String getCharacterEncoding(HttpServletRequest request,HttpServletResponse response) {

		if(null == request || null == response) {
			return "utf-8";
		}
		String enc = request.getCharacterEncoding();
		if(null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}

		if(null == enc || "".equals(enc)) {
			enc = "utf-8";
		}

		return enc;
	}


	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	
	public static Boolean checkContain(String str,String containStr) {
		System.out.println("111");
		Boolean type=true;
		if(!str.contains(containStr)){
			type=false;
		}
		return type;
	}

	public static void main(String[] args) throws IOException {
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(surplusdays(sdf.parse("2009-01-11"),sdf.parse("2009-01-12")));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("templates/codetemplate/");
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template temp = gt.getTemplate("service.html");
		System.out.println(temp.render());


	}
}
