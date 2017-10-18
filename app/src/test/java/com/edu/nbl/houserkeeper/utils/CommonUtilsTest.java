package com.edu.nbl.houserkeeper.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/8/10.
 */
public class CommonUtilsTest {
    @Test
    public void getFileSize() throws Exception {
        String str = CommonUtils.getFileSize(1024L*1024*1024*1024);
        System.out.println("str="+str);
    }

}