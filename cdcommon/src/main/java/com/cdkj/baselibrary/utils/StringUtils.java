package com.cdkj.baselibrary.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by cdkj on 2017/6/9.
 */

public class StringUtils {

    public static String SPACE_SYMBOL = " ";

    /**
     * 区号显示转换 0086 -》  +86
     *
     * @param code
     * @return
     */
    public static String transformShowCountryCode(String code) {
        if (TextUtils.isEmpty(code) || code.length() < 2) return "";
        return "+" + code.substring(2, code.length());
    }

    /**
     * 手机号显示转换 132*****1111
     *
     * @param phone
     * @return
     */
    public static String ttransformShowPhone(String phone) {

        if (TextUtils.isEmpty(phone)) {
            return "";
        }

        if (phone.length() > 8) {
            return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        }

        if (phone.length() > 3) {
            int midLeng = phone.length() / 2;
            return phone.substring(0, midLeng - 1) + "**" + phone.substring(midLeng + 1, phone.length());
        }

        return phone;
    }


    /**
     * 合并字符串中的连续空格
     *
     * @param str
     * @return
     */
    public static String mergeSpace(String str) {
        String regex = "\\s+";
        if (!TextUtils.isEmpty(str)) {
            return str.replaceAll(regex, SPACE_SYMBOL);
        }
        return str;
    }

    /**
     * 合并字符串中的连续空格
     *
     * @param str
     * @return
     */
    public static String mergeSpace(String str, String replace) {
        String regex = "\\s+";
        if (!TextUtils.isEmpty(str)) {
            return str.replaceAll(regex, replace);
        }
        return str;
    }


    /**
     * 请求参数json格式转换，公共参数添加
     *
     * @param requestMap
     * @return
     */
    public static String getRequestJsonString(Map requestMap) {

        if (requestMap == null) {
            return "";
        }

        requestMap.put("client", "android");
        requestMap.put("systemCode", AppConfig.SYSTEMCODE);
        requestMap.put("companyCode", AppConfig.COMPANYCODE);
        requestMap.put("token", SPUtilHelper.getUserToken());
        requestMap.put("language", SPUtilHelper.getLanguage());
        String jsonString = JSON.toJSONString(requestMap);
        LogUtil.BIGLOG("JSON 转换__:        " + jsonString);
        return jsonString;
    }


    public static double parseDouble(String s) {


        try {
            return Double.valueOf(s);
        } catch (Exception e) {

        }
        return 0;
    }


    public static List<String> splitAsList(String s, String sp) {

        List<String> strings = new ArrayList<>();

        if (!TextUtils.isEmpty(s)) {
            strings = Arrays.asList(s.split(sp));
        }

        return strings;
    }

    public static ArrayList<String> splitAsArrayList(String s, String sp) {

        ArrayList<String> strings = new ArrayList<>();

        if (!TextUtils.isEmpty(s)) {
            strings.addAll(Arrays.asList(s.split(sp)));
        }

        return strings;
    }


    /**
     * 切割获取广告图片
     *
     * @param s
     * @return
     */
    public static List<String> splitBannerList(String s) {
        return splitAsList(s, "\\|\\|");
    }

    /**
     * 切割获取广告图片
     *
     * @param s
     * @return
     */
    public static ArrayList<String> splitBannerArrayList(String s) {

        ArrayList arrayList = new ArrayList();

        arrayList.addAll(splitAsList(s, "\\|\\|"));

        return arrayList;
    }

    public static String subStringEnd(String s, int start) {
        if (TextUtils.isEmpty(s) || start < 0) {
            return s;
        }
        return s.substring(start, s.length() - 1);

    }

    //int前面补零
    public static String frontCompWithZoreString(Object sourceDate, int formatLength) {
        try {
            String newString = String.format("%0" + formatLength + "d", sourceDate);
            return newString;
        } catch (Exception e) {
            return sourceDate.toString();
        }
    }

    /**
     * list装换为字符串
     *
     * @param list
     * @return
     */
    public static String listToString(List<?> list, String sep1) {

        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null || list.get(i).equals("")) {
                continue;
            }
            // 如果值是list类型则调用自己
            if (list.get(i) instanceof List) {
                sb.append(listToString((List<?>) list.get(i), sep1));
                if (i != list.size() - 1) {
                    sb.append(sep1);
                }

            } /*else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    if (i != list.size() - 1) {
                        sb.append(sep1);
                    }

                }*/ else {
                sb.append(list.get(i));
                if (i != list.size() - 1) {
                    sb.append(sep1);
                }

            }
        }

        return sb.toString();
    }

    /**
     * list装换为字符串
     *
     * @param list
     * @return
     */
    public static String listToString(List<?> list) {

        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null || list.get(i).equals("")) {
                continue;
            }
            // 如果值是list类型则调用自己
            if (list.get(i) instanceof List) {
                sb.append(listToString((List<?>) list.get(i)));

            } /*else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    if (i != list.size() - 1) {
                        sb.append(sep1);
                    }

                }*/ else {
                sb.append(list.get(i));
            }
        }

        return sb.toString();
    }


    //判断email格式是否正确
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
//        String str = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-z]{2,}$";
        Pattern p = Pattern.compile(str);

        Matcher m = p.matcher(email);

        return m.matches();
    }


    //设置价格输入
    public static void editSetPriceInputState(EditText editText) {

        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});

    }


    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
/*    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1
                        + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return  sb.toString();
    }*/


    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";


    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(final CharSequence input) {
        return isMatch(REGEX_IP, input);
    }


    /**
     * 字符过滤
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 百分比
     *
     * @param money
     * @return
     */
    public static String showformatPercentage(float money) {
//获取格式化对象
//        NumberFormat nt = NumberFormat.getPercentInstance();
//        //设置百分数精确度2即保留两位小数
//        nt.setMinimumFractionDigits(0);
        DecimalFormat nt = new DecimalFormat("0.00%");
        BigDecimal bigDecimal = new BigDecimal(money);
        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP); //四舍五入
        return nt.format(bigDecimal);
    }

    /**
     * 字符串为空返回0不为空返回原字符
     *
     * @param string
     * @return
     */
    public static String checkEmptyReturnZeor(String string) {
        if (TextUtils.isEmpty(string)) {
            return "0";
        }
        return string;
    }

    /**
     * 检测position是否在list中越界
     *
     * @param list
     * @param position
     * @return
     */
    public static boolean checkPostionCrossingInList(List list, int position) {

        if (list == null || list.size() == 0 || position < 0 || position > list.size()) {
            return true;
        }

        return false;

    }

}
