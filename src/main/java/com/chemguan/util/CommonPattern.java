package com.chemguan.util;

import java.util.regex.Pattern;

public final class CommonPattern {
    /**
     * 邮箱格式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9]+([._\\-]*[a-zA-Z0-9])*@([a-zA-Z0-9]+[-a-zA-Z0-9]*[a-zA-Z0-9]+.){1,63}[a-zA-Z0-9]+$");

    /**
     * 汉字模式
     */
    private static final Pattern CHINESE_PATTERN = Pattern
            .compile("[\\u4e00-\\u9fa5]");

    /**
     * 中国手机号码格式
     */
    private static final Pattern CHINA_PHONE_PATTERN = Pattern
            .compile("^((13[0-9])|(147)|(15[0-9])|17[0-9]|(18[0-9]))\\d{8}$");

    /**
     * 中国身份证号码格式
     */
    private static final Pattern IDENTITY_NUM_PATTERN = Pattern
            .compile("(^[0-9]{15}$)|(^[0-9]{18}$)|(^[0-9]{17}[Xx]$)");

    private CommonPattern() {
    }

    /**
     * 是否符合Email格式
     *
     * @param email
     * @return
     */
    public static boolean isEmailFormat(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 是否符合中国电话格式
     *
     * @param
     * @return
     */
    public static boolean isChinaPhoneFormat(String chinaPhone) {
        return CHINA_PHONE_PATTERN.matcher(chinaPhone).matches();
    }

    /**
     * 是否符合中国身份证号码格式
     *
     * @param
     * @return
     */
    public static boolean isIdentityNumFormat(String identitynum) {
        return IDENTITY_NUM_PATTERN.matcher(identitynum).matches();
    }
}