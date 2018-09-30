package com.chemguan.util.wxlogin;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmojiFilter {


    /**
     * 检测是否有emoji字符
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
    	//包含空格
        if (StringUtils.isBlank(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            int x=0;
            //不包含emoji表情
            if (isEmojiCharacter(codePoint)) {
            	x++;
            	//如果x等于字符串的长度直接返回true
            	if(x==len){
            		return true;
            		//如果x不等于字符串的长度返回false
            	}else{
            		return false;
            	}
            }
        }
        return false;
    }

    //正常范围内的字符串
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || 
                (codePoint == 0x9) ||                            
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    
    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
    	//source=" "+source;
        if (containsEmoji(source)) {
            return source.trim();//不包含emoji表情，直接返回原字符
        }
        //定义一个接受非emoji表情的字符串
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
        	//挨个拿出字符串中的字符
            char codePoint = source.charAt(i);
            //不是emoji表情
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                //追加进buf
                buf.append(codePoint);
            } 
        }
       
        if (buf == null) { //找到emoji表情，整个字符串全是emoji表情
        	Date currentTime = new Date();
           	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
           	String dateString = formatter.format(currentTime);
           	//防止报错纯emoji表情名字的字符串将使用时间代替
            return dateString;
        } else {//如果没有找到 emoji表情，则返回源字符串
            if (buf.length() == len) {//全部是正常中文//这里的意义在于尽可能少的toString，因为会重新生成字符串
            	buf = null;
                return source.trim();
            } else {//包含一部分emoji表情
                return buf.toString().trim();
            }
        }
        
    }
}
