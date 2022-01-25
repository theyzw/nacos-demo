package com.yk.common.core.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.PhoneUtil;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ye
 * @date 2018/10/18 18:44
 */
public class CheckUtil {

    private final static Pattern MOBILE_PATTERN = Pattern
        .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");

    private final static Pattern MATCH_MOBILE_PATTERN = Pattern
        .compile("(?<!\\d)(?:(?:1[3-9]\\d{9})|(?:861[3-9]\\d{9}))(?!\\d)");

    private final static Pattern EMAIL_PATTERN = Pattern
        .compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)" + "*\\" + ".)*[a-z]{2,3}$");

    private final static Pattern IP_PATTERN = Pattern
        .compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\." + "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

    private final static Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]+");

    private final static Pattern YYYYMMDD_PATTERN = Pattern.compile(
        "((\\d{2}(([02468][048])|([13579][26]))[\\-](" + "(((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|" +
            "(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|" + "([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|" + "([1" +
            "-2][0-9])))))|(\\d{2}(([02468][1235679])|" + "([13579][01345789]))[\\-]((((0?[13578])|(1[02])"
            + ")[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|" + "(11))[\\-]((0?[1-9])|([1-2][0-9])|(30))"
            + ")|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))");

    private final static Pattern WECHAT_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]{5,19}$");

    private final static Pattern PHONE_PATTERN = Pattern.compile("[0-9—-]{5,13}$");

    /**
     * 支持港澳台
     */
    private static final String[] I18N_SUPPORT = new String[]{"852", "853", "886"};

    private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,20}$");

    /**
     * 统一社会信用代码，15位或18位数字或字母
     */
    private final static Pattern CREDIT_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{18}");

    /**
     * 手机号校验
     *
     * @param mobile 手机号
     * @return true or false
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null) {
            return false;
        }

        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * 手机号校验，支持港澳台
     *
     * @param mobile 手机号
     * @return true or false
     */
    public static boolean isI18nMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }

        if (PhoneUtil.isMobile(mobile)) {
            return true;
        }

        if (StringUtils.startsWithAny(mobile, I18N_SUPPORT)) {
            if (NumberUtil.isNumber(mobile)) {
                return true;
            }
            return false;
        }

        return false;
    }

    public static boolean startWithI18n(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        return StringUtils.startsWithAny(mobile, I18N_SUPPORT);
    }

    /**
     * 非手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isNotMobile(String mobile) {
        return !isMobile(mobile);
    }

    /**
     * 电话校验。5-13位的数字或“-”、“—”
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (phone == null) {
            return false;
        }

        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 文本中是否存在手机号
     *
     * @param text
     * @return
     */
    public static boolean hasMobile(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        return MATCH_MOBILE_PATTERN.matcher(text).find();
    }

    /**
     * 邮箱校验
     *
     * @param email 邮箱
     * @return true or false
     */
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * ip校验
     *
     * @param ip ip
     * @return
     */
    public static boolean isIp(String ip) {
        if (ip == null) {
            return false;
        }

        return IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 中文校验
     *
     * @param text
     * @return
     */
    public static boolean isChinese(String text) {
        if (text == null) {
            return false;
        }

        return CHINESE_PATTERN.matcher(text).matches();
    }

    /**
     * yyyy-MM-dd校验
     *
     * @param text
     * @return
     */
    public static boolean isYyyyMMdd(String text) {
        if (text == null) {
            return false;
        }

        return YYYYMMDD_PATTERN.matcher(text).matches();
    }

    /**
     * 微信账号校验 6—20个字母、数字、下划线和减号，必须以字母开头（不区分大小写），不支持设置中文
     *
     * @param text
     * @return
     */
    public static boolean isWechat(String text) {
        if (text == null) {
            return false;
        }

        return WECHAT_PATTERN.matcher(text).matches();
    }

    /**
     * 密码校验 8—20个字符，包含字母和数字
     *
     * @param text
     * @return
     */
    public static boolean isPassword(String text) {
        if (text == null) {
            return false;
        }

        return PASSWORD_PATTERN.matcher(text).matches();
    }

    /**
     * 统一社会信用代码，15位或18位数字或字母
     *
     * @param text
     * @return
     */
    public static boolean isCreditCode(String text) {
        if (text == null) {
            return false;
        }

        return CREDIT_CODE_PATTERN.matcher(text).matches();
    }

    public static void main(String[] args) {
        System.out.println(isCreditCode("2dfl-llllllll3l"));
        System.out.println(isCreditCode("2dfll2325433llll3l"));
        System.out.println(isCreditCode("2dfll23254-3llll3l"));
    }

}
