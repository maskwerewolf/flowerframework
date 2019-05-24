

package com.github.maskwerewolf.mysql.utils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/10
 */
public class StringUtils {


    public static boolean isEmpty(String string) {
        return null == string || string.length() == 0 || "".equals(string);
    }

    public static boolean isEmpty(Object string) {
        return null == string || string.toString().length() == 0 || "".equals(string);
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isNotEmpty(Object string) {
        return !isEmpty(string);
    }

    public static boolean isNumeric(String string) {
        if (null == string || string.length() == 0) {
            return false;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isDigit(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Integer parseInteger(String string, Integer defaultValue) {
        if (isNumeric(string)) {
            return Integer.valueOf(string);
        }
        return defaultValue;
    }

    public static String matcher(String regex, String string) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(string);
        if (m.find()) {
            String result = m.group(1);
            return result;
        }
        return "";
    }


    public static LinkedList<String> matcherAll(String regex, String string) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(string);
        LinkedList<String> result = new LinkedList<>();
        while (m.find()) {
            result.add(m.group(1));
        }
        return result;
    }





}
