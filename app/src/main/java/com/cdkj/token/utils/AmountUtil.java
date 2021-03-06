package com.cdkj.token.utils;


import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.token.R;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import static com.cdkj.token.utils.LocalCoinDBUtils.getLocalCoinUnit;

/**
 * Created by lei on 2017/10/20.
 */

public class AmountUtil {

    public static final int ETH_UNIT_UNIT = 18;
    public static final int ETHSCALE = 8;
    public static final int ALLSCALE = 8;
    public static final int SCALE_4 = 4;


    /**
     * 货币单位转换
     *
     * @param amount
     * @param
     * @return
     */
    public static String transformFormatToString(BigDecimal amount, String coinSymbol, int scale) {
        if (amount == null) {
            return "0";
        }

        if (TextUtils.isEmpty(coinSymbol)) {
            return formatCoinAmount(BigDecimalUtils.div(amount, BigDecimal.TEN.pow(18), scale));
        }

        return formatCoinAmount(BigDecimalUtils.div(amount, getLocalCoinUnit(coinSymbol), scale));
    }

    /**
     * 货币单位转换
     *
     * @param amount
     * @param
     * @return
     */
    public static String toMinWithUnit(BigDecimal amount, String coinSymbol, int scale) {
        if (amount == null) {
            return "0" + coinSymbol;
        }

        if (TextUtils.isEmpty(coinSymbol)) {
            return formatCoinAmount(BigDecimalUtils.div(amount, BigDecimal.TEN.pow(18), scale));
        }

        return formatCoinAmount(BigDecimalUtils.div(amount, getLocalCoinUnit(coinSymbol), scale)) + coinSymbol;
    }

    /**
     * 货币单位转换
     *
     * @param amount
     * @param
     * @return
     */
    public static String transformFormatToString2(BigDecimal amount, String coinSymbol, int scale) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        }

        if (TextUtils.isEmpty(coinSymbol)) {
            return formatCoinAmount(BigDecimalUtils.div(amount, BigDecimal.TEN.pow(18), scale));
        }


        return formatCoinAmount(BigDecimalUtils.div(amount, getLocalCoinUnit(coinSymbol), scale));
    }

    /**
     * 货币单位转换
     *
     * @param amountString
     * @param
     * @return
     */
    public static String transformForRequest(String amountString, String coinSymbol) {
        if (TextUtils.isEmpty(amountString)) {
            return "0";
        }
        return transformForRequest(new BigDecimal(amountString), coinSymbol);
    }

    /**
     * 货币单位转换
     *
     * @param amount
     * @param
     * @return
     */
    public static String transformForRequest(BigDecimal amount, String coinSymbol) {
        if (amount == null) {
            return "0";
        }

        if (TextUtils.isEmpty(coinSymbol)) {
            return "0";
        }

        return formatCoinAmount(BigDecimalUtils.multiply(amount, getLocalCoinUnit(coinSymbol)));
    }


    /**
     * 货币单位转换
     *
     * @param amount
     * @param
     * @return
     */
    public static String transformFormatToString(BigDecimal amount, BigDecimal unit, int scale) {
        if (amount == null) {
            return "0";
        }
        return formatCoinAmount(BigDecimalUtils.div(amount, unit, scale));
    }


    /**
     * BigInteger
     *
     * @param amount
     * @return
     */
    public static BigInteger bigIntegerFormat(BigDecimal amount, String coin) {

        if (amount == null) {
            return BigInteger.ZERO;
        }

        return amount.multiply(getLocalCoinUnit(coin)).toBigInteger();
    }

    /**
     * BigInteger
     *
     * @param amount
     * @return
     */
    public static BigDecimal bigDecimalFormat(BigDecimal amount, String coin) {
        return bigDecimalFormat(amount, getLocalCoinUnit(coin));
    }

    /**
     * BigInteger
     *
     * @param amount
     * @return
     */
    public static BigDecimal bigDecimalFormat(BigDecimal amount, BigDecimal coinUnit) {

        if (amount == null) {
            return BigDecimal.ZERO;
        }

        return amount.multiply(coinUnit);
    }

    /**
     * 格式化输出的金额格式，最多8位小数
     *
     * @param s
     * @return
     */
    public static String scale(String s, int scale) {
        String amount[] = s.split("\\.");
        if (amount.length > 1) {
            if (amount[1].length() > scale) {
                return amount[0] + "." + amount[1].substring(0, scale);
            } else {
                return amount[0] + "." + amount[1];
            }
        } else {
            return s;
        }
    }


    public static String formatCoinAmount(BigDecimal money) {
        DecimalFormat df = new DecimalFormat("#######0.########");
        String showMoney = df.format(money);

        return showMoney;
    }

    public static String formatCoinAmount(float money) {
        DecimalFormat df = new DecimalFormat("#######0.########");
        String showMoney = df.format(money);

        return showMoney;
    }

    public static String formatInt(double money) {
        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format(money);

        return showMoney.substring(0, showMoney.length() - 1).split("\\.")[0];
    }

    public static String formatDoubleDiv(String money, int scale) {

        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format((Double.parseDouble(money) / scale));

        return showMoney.substring(0, showMoney.length() - 1);
    }


    public static String formatBizType(String bizType) {
        switch (bizType) {
            case "charge":
                return StringUtil.getString(R.string.biz_type_charge);

            case "withdraw":
                return StringUtil.getString(R.string.bill_type_withdraw);

            case "tradefee":
                return StringUtil.getString(R.string.biz_type_tradefee);

            case "withdrawfee":
                return StringUtil.getString(R.string.biz_type_withdrawfee);
            case "lhlc_invest": // 量化理财投资
                return StringUtil.getString(R.string.lhlc_invest);
            case "lhlc_repay": // 量化理财还款
                return StringUtil.getString(R.string.lhlc_repay);
            default:
                return "";

        }
    }


    public static String formatBillStatus(String status) {

        switch (status) {

            case "1":
                return StringUtil.getString(R.string.biz_status_daiduizhang);

            case "3":
                return StringUtil.getString(R.string.biz_status_yiduiyiping);

            case "4":
                return StringUtil.getString(R.string.biz_status_zhangbuping);

            case "5":
                return StringUtil.getString(R.string.biz_status_yiduibuping);

            case "6":
                return StringUtil.getString(R.string.biz_status_wuxuduizhang);

            default:
                return "";
        }

    }


//      new BigDecimal(1).divide()

    public static String divideDoubleToBigDecimal(double v1, BigDecimal v2, int scale) {
        if (v1 == 0 || v2 == null)
            return "";
        BigDecimal divide = new BigDecimal(v1).divide(v2,scale+1,BigDecimal.ROUND_HALF_UP);
        return scale(divide.toString(), scale);
    }

    public static String divideBigDecimalToDouble(BigDecimal v1, double v2, int scale) {
        if (v2 == 0 || v1 == null)
            return "";
        BigDecimal divide = v1.divide(new BigDecimal(v2),scale+1,BigDecimal.ROUND_HALF_UP);

        return scale(divide.toString(), scale);
    }
    public static String multiplyBigDecimalToDouble(BigDecimal v1, double v2, int scale) {
        if (v2 == 0 || v1 == null)
            return "";
        BigDecimal divide = v1.multiply(new BigDecimal(v2));
        return scale(divide.toString(), scale);
    }

}
