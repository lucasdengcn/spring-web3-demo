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
        System.out.printf("%f\n", MathUtils.decimalShift);
    }

    @Test
    void sqrtToPrice() {
        // Create a BigInteger value
        BigInteger bigInteger = new BigInteger("79197132494366457731461945975");
        BigDecimal price = MathUtils.sqrtToPrice(bigInteger);
        System.out.printf("%f", price);
    }

    @Test
    void numberOfETH() {
    }
}