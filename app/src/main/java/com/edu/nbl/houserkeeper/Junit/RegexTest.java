package com.edu.nbl.houserkeeper.Junit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/10.
 * 正则表达式 593863495@qq.com
 */

public class RegexTest {
    public static boolean checkEmail(){
        String email = "593863495@qq.com";
        String regex = "[a-zA-Z0-9]+@+[a-zA-Z0-9]+.[a-z]{2,4}";
        boolean flag = email.matches(regex);
        return flag;
    }
    public static boolean checkPhone(){
        String number = "18361886521";
        String regex = "\\d{11}";//digital "\\d"=[0-9]
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        boolean flag = matcher.matches();
        return flag;
    }
}
