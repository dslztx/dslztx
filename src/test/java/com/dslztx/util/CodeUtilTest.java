package com.dslztx.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author dslztx
 * @date 2015年08月11日
 */
public class CodeUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtilTest.class);

    @Test
    public void encodeTest() {
        try {
            byte[] a = CodeUtil.encode("好", CodeUtil.CodeMethod.UTF8);
            byte[] b = new byte[3];
            b[0] = (byte) 0xe5;
            b[1] = (byte) 0xa5;
            b[2] = (byte) 0xbd;
            assertTrue(Arrays.equals(a, b));

            byte[] c = CodeUtil.encode("好", CodeUtil.CodeMethod.GBK);
            byte[] d = new byte[2];
            d[0] = (byte) 0xba;
            d[1] = (byte) 0xc3;
            assertTrue(Arrays.equals(c, d));
        } catch (Exception e) {
            logger.error("", e);
            fail();
        }
    }

    @Test
    public void decodeTest() {
        try {
            byte[] b = new byte[3];
            b[0] = (byte) 0xe5;
            b[1] = (byte) 0xa5;
            b[2] = (byte) 0xbd;
            String str = CodeUtil.decode(b, CodeUtil.CodeMethod.UTF8);
            assertTrue(str.equals("好"));

            byte[] d = new byte[2];
            d[0] = (byte) 0xba;
            d[1] = (byte) 0xc3;
            String str1 = CodeUtil.decode(d, CodeUtil.CodeMethod.GBK);
            assertTrue(str1.equals("好"));
        } catch (Exception e) {
            logger.error("", e);
            fail();
        }
    }
}