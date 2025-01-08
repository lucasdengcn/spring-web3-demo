package com.example.web3.demo.integration;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * https://blog.uniswap.org/uniswap-v3-math-primer
 */
public class MathUtils {

    public static BigDecimal denominator = BigDecimal.TWO.pow(192);
    public static BigDecimal USDC_WETH_SHIFT = BigDecimal.valueOf(10).pow(12);

    public static BigDecimal pow(BigDecimal base, int exponent) {
        if (exponent >= 0) {
            return base.pow(exponent);
        } else {
            BigDecimal result = base.pow(Math.abs(exponent));
            return BigDecimal.ONE.divide(result, 96, RoundingMode.HALF_UP);
        }
    }

    /**
     * USDC Price means the amount of USDC that represents 1 WETH.
     *
     * sqrtPriceX96 = sqrtPrice * 2^96
     * price = sqrtPrice^2 = (sqrtPriceX96 / 2^96)^2 = sqrtPriceX96^2 / 2^192
     *
     * @param sqrtPriceX96
     * @return
     */
    public static BigDecimal getUSDCPrice(BigInteger sqrtPriceX96) {
        BigDecimal numerator = new BigDecimal(sqrtPriceX96).pow(2);
        BigDecimal ratio = numerator.divide(denominator, 96, RoundingMode.HALF_UP);
        // depend on token pair decimal places
        BigDecimal price = USDC_WETH_SHIFT.divide(ratio,4, RoundingMode.HALF_UP);
        return price;
    }

    public static BigDecimal numberOfETH(BigInteger weiValue){
        return Convert.fromWei(weiValue.toString(), Convert.Unit.ETHER);
    }
}
