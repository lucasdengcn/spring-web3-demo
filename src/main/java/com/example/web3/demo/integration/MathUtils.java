package com.example.web3.demo.integration;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MathUtils {

    public static BigDecimal denominator = BigDecimal.TWO.pow(192);
    public static BigDecimal decimalShift = pow(BigDecimal.valueOf(10),-12);

    public static BigDecimal pow(BigDecimal base, int exponent) {
        if (exponent >= 0) {
            return base.pow(exponent);
        } else {
            BigDecimal result = base.pow(Math.abs(exponent));
            return BigDecimal.ONE.divide(result, 96, RoundingMode.HALF_UP);
        }
    }

    public static BigDecimal sqrtToPrice(BigInteger sqrt) {
        BigDecimal numerator = new BigDecimal(sqrt).pow(2);
        BigDecimal ratio = numerator.divide(denominator, 96, RoundingMode.HALF_UP);
        ratio = ratio.multiply(decimalShift);
        return ratio;
    }

    public static BigDecimal numberOfETH(BigInteger weiValue){
        return Convert.fromWei(weiValue.toString(), Convert.Unit.ETHER);
    }
}
