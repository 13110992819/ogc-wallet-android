package com.cdkj.baselibrary.utils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Created by cdkj on 2017/7/19.
 */

public class BigDecimalUtils {

    public static int intValue(BigDecimal b) {
        if (b != null) {
            return b.intValue();
        }

        return 0;
    }

    public static double doubleValue(BigDecimal b) {
        if (b != null) {
            return b.doubleValue();
        }

        return 0;
    }

    //减法
    public static BigDecimal subtract(BigDecimal b, BigDecimal b1) {
        if (b != null && b1 != null) {
            return b.subtract(b1);
        }

        return BigDecimal.ZERO;
    }

    //乘法
    public static BigDecimal multiply(BigDecimal b, BigDecimal b1) {
        if (b != null && b1 != null) {
            return b.multiply(b1);
        }

        return BigDecimal.ZERO;
    }

    //加法
    public static BigDecimal add(BigDecimal b, BigDecimal b1) {
        if (b != null && b1 != null) {
            return b.add(b1);
        }

        return BigDecimal.ZERO;
    }

    /**
     * 除法计算
     *
     * @param b
     * @param b1
     * @param sca
     * @return
     */
    public static BigDecimal div(BigDecimal b, BigDecimal b1, int sca) {

        if (b == null || b1 == null) {
            return BigDecimal.ZERO;
        }

        if (BigDecimalUtils.compareToZERO(b) || BigDecimalUtils.compareToZERO(b)) {
            return BigDecimal.ZERO;
        }

        return b.divide(b1, sca, ROUND_HALF_UP);

    }

    /**
     * 判断b是否大于b1
     *
     * @param b
     * @param b1
     * @return
     */
    public static boolean compareTo(BigDecimal b, BigDecimal b1) {
        if (b == null || b1 == null) return false;
        return b.compareTo(b1) == 1;
    }

    /**
     * 判断b是否大于等于b1
     *
     * @param b
     * @param b1
     * @return
     */
    public static boolean compareTo2(BigDecimal b, BigDecimal b1) {
        if (b == null || b1 == null) return false;
        return b.compareTo(b1) == 1 || b.compareTo(b1) == 0;
    }

    /**
     * 判断b是否小于0
     *
     * @return
     */
    public static boolean compareToZERO(BigDecimal b) {
        if (b == null) return false;
        return b.compareTo(BigDecimal.ZERO) == 1;
    }

}
