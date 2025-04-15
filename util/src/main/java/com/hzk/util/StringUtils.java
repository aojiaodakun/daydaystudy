package com.hzk.util;


import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    private static final String SHA1PRNG = "SHA1PRNG";
    private final static String[] empty_string_array = new String[0];
    private final static String DOT = ".";
    // 是，
    private final static String YES = "1";
    // NO
    private final static String NO = "0";
    /**
     * true 数据值
     */
    private final static String TRUE_VALUE="true";

    public static boolean isEmpty(String string) {
        return (isNull(string) || isBlank(string));
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String getEmpty() {
        return "";
    }

    public static boolean isNull(String string) {
        return string == null;
    }

    public static boolean isNotNull(String string) {
        return !isNull(string);
    }

    public static boolean isBlank(String string) {
        return isNotNull(string) && (string.trim().length() == 0);
    }

    /**
     * 是否数字串
     * 此方法弃用，空字符串也会判断为true，存在漏洞
     * @param str
     * @return
     */
    @Deprecated
    public static boolean isNumericString(String str) {
        boolean isNumericString = false;
        if (str != null) {
            isNumericString = true;
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    isNumericString = false;
                    break;
                }
            }
        }
        return isNumericString;
    }

    /**
     * 判断是否数字串
     * @param str
     * @return
     */
    public static boolean isNumberString(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return isNumericString(str);
    }

    /**
     * 是否数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        boolean isNumeric = false;
        if (str != null) {
            if (str.indexOf(DOT) == str.lastIndexOf(DOT)) {
                // 只有一个小数点
                String tempString = str.replace(DOT, "");
                isNumeric = isNumericString(tempString);
            }

        }
        return isNumeric;
    }

    /**
     * 消除字符串头尾空字符，如果是null字符串，返回""
     *
     * @param string
     * @return 消除空格后的字符串
     */
    public static String trim(String string) {
        if (isNull(string)) {
            return getEmpty();
        }
        return string.trim();
    }

    public static String[] split(String value, boolean withoutEmpty, char... delim) {
        if (value == null || value.length() == 0) {
            if (withoutEmpty) {
                return empty_string_array;
            }
            return new String[] { "" };
        }
        if (delim.length > 0) {
            List<String> ret = new ArrayList<String>();
            String split = new StringBuilder().append(delim).toString();
            char[] chs = value.toCharArray();
            StringBuilder token = new StringBuilder();
            for (int i = 0, n = chs.length; i < n; i++) {
                char ch = chs[i];
                if (split.indexOf(ch) != -1) {
                    if (withoutEmpty) {
                        String s = token.toString().trim();
                        if (s.length() > 0) {
                            ret.add(s);
                        }
                    } else {
                        ret.add(token.toString());
                    }
                    token.setLength(0);
                } else {
                    token.append(ch);
                }
            }
            if (split.indexOf(chs[chs.length - 1]) != -1) {
                if (!withoutEmpty) {
                    ret.add("");
                }
            } else {
                if (withoutEmpty) {
                    String s = token.toString().trim();
                    if (s.length() > 0) {
                        ret.add(s);
                    }
                } else {
                    ret.add(token.toString());
                }
            }
            return ret.toArray(new String[ret.size()]);
        } else {
            if (withoutEmpty) {
                value = value.trim();
            }
            return value.length() == 0 ? empty_string_array : new String[] { value };
        }
    }

    private static String randomWord(boolean randomIndicator, int min, int max) {
        String str = "";
        int range = min;
        String[] arr = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D",
                "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
                "Z" };
        SecureRandom secRamdom = null;
        try {
            secRamdom = SecureRandom.getInstance(SHA1PRNG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (secRamdom == null) {
            secRamdom = new SecureRandom();
        }

        /*
        if (secRamdom == null) {
            if (randomIndicator) {
                range = (int) (Math.round(Math.random() * (max - min)) + min);
            }
            for (int i = 0; i < range; i++) {
                int pos = (int) Math.round(Math.random() * (arr.length - 1));
                str += arr[pos];
            }
            return str;
        } else
        */

        {
            // 随机产生
            if (randomIndicator) {
                range = (int) (Math.round(random(secRamdom) * (max - min)) + min);
            }
            for (int i = 0; i < range; i++) {
                int pos = (int) Math.round(random(secRamdom) * (arr.length - 1));
                str += arr[pos];
            }
        }
        return str;
    }

    /**
     * 随机生成一个长度在min的一个数字和字母组合的字串
     *
     * @param min
     * @return
     */
    public static String randomWord(int min)  {
        return randomWord(false, min, min);
    }

    public static double random(SecureRandom secRamdom) {
        if (secRamdom != null) {
            BigDecimal result = new BigDecimal(Math.abs(secRamdom.nextInt())).divide(new BigDecimal(10000000))
                    .divide(new BigDecimal(1000));

            return result.multiply(new BigDecimal(10)).doubleValue() - result.multiply(new BigDecimal(10)).intValue();
        } else {
            return 1.0;
        }
    }

    /**
     * 随机生成一个长度在min，max之间的一个数字组合的字串
     *
     * @param randomIndicator
     * @param min
     * @param max
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String randomNumber(boolean randomIndicator, int min, int max) {
        String str = "";
        int range = min;
        String[] arr = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", };
        SecureRandom secRamdom = null;
        try {
            secRamdom = SecureRandom.getInstance(SHA1PRNG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (secRamdom == null) {
            // 随机产生
            if (randomIndicator) {
                range = (int) (Math.round(Math.random() * (max - min)) + min);
            }
            for (int i = 0; i < range; i++) {
                int pos = (int) Math.round(Math.random() * (arr.length - 1));
                str += arr[pos];
            }
        } else {
            // 随机产生
            if (randomIndicator) {
                range = (int) (Math.round(random(secRamdom) * (max - min)) + min);
            }
            for (int i = 0; i < range; i++) {
                int pos = (int) Math.round(random(secRamdom) * (arr.length - 1));
                str += arr[pos];
            }
        }
        return str;
    }

    /**
     * 随机生成一个长度在min的一个数字组合的字串
     *
     * @param min
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String randomNumber(int min) {
        return randomNumber(false, min, min);
    }


    /**
     * 根据到前字符串得到boolean值，默认false
     *
     * @param value
     * @return
     */
    public static boolean getBooleanValue(String value) {
        return getBooleanValue(value, false);
    }

    /**
     * 根据到前字符串得到boolean值，默认为defaultValue
     *
     * @param value
     * @return
     */
    public static boolean getBooleanValue(String value, boolean defaultValue) {
        boolean returnValue = defaultValue;

        if (YES.equals(value)) {
            returnValue = true;
        }else if(TRUE_VALUE.equalsIgnoreCase(value)){
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * 返回对象的Obj
     * @param StringObj
     * @return
     */
    public static String getStringValue(Object StringObj){
        String returnString=null;
        if(StringObj instanceof String){
            returnString=(String)StringObj;
        }else if(StringObj!=null){
            returnString=String.valueOf(StringObj);
        }
        return returnString;
    }

    /**
     * 返回对象的Obj,如果为空，就用defaultValue代替
     * @param StringObj
     * @return
     */
    public static String getStringValue(Object StringObj,String defaultValue){
        String returnString=defaultValue;
        if(StringObj instanceof String){
            returnString=(String)StringObj;
        }else if(StringObj!=null){
            returnString=String.valueOf(StringObj);
        }
        return returnString;
    }
}
