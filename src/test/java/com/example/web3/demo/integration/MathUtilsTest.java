package com.example.web3.demo.integration;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void pow() {
        BigDecimal val = MathUtils.pow(BigDecimal.valueOf(10), -12);
        System.out.printf("%f\n", val);
        System.out.printf("%f\n", MathUtils.denominator);
        System.out.printf("%f\n", MathUtils.USDC_WETH_SHIFT);
    }

    @Test
    void sqrtToPrice() {
        // Create a BigInteger value
        BigInteger bigInteger = new BigInteger("2018382873588440326581633304624437");
        BigDecimal price = MathUtils.getUSDCPrice(bigInteger);
        System.out.printf("%f", price);
    }

    @Test
    void numberOfETH() {
    }
}