package com.example.web3.demo.integration.contract;

import com.example.demo.contracts.applecontractv2.AppleContractV2;
import com.example.web3.demo.model.WalletAccount;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppleContractClientServiceTest {

    // for testing purpose
    public static final String testKey = "dbda1821b80551c9d65939329250298aa3472ba22feea921c0cf5d620ea67b97";

    @Autowired
    private AppleContractClientService appleContractClientService;

    @Test
    void should_return_contract_counts_and_price0_correct() {
        // for testing purpose
        WalletAccount walletAccount = WalletAccount.builder().privateKey(testKey).build();
        Pair<BigInteger, BigInteger> pair = appleContractClientService.GetAppleCount(walletAccount);
        assertEquals(100, pair.getKey().intValue());
        assertEquals(10, pair.getValue().intValue());
    }

    @Test
    void should_return_contract_price1_correct() {
        // for testing purpose
        WalletAccount walletAccount = WalletAccount.builder().privateKey(testKey).build();
        BigInteger result = appleContractClientService.GetPrice1(walletAccount);
        assertTrue(result.intValue() > 0);
    }

    @Test
    void should_update_contract_price1_correct() {
        // for testing purpose
        WalletAccount walletAccount = WalletAccount.builder().privateKey(testKey).build();
        AppleContractV2.PriceChangedSuccessEventResponse response = appleContractClientService.ChangePrice(walletAccount, BigInteger.valueOf(40));
        assertEquals(40, response.newPrice.intValue());
        //
        BigInteger result = appleContractClientService.GetPrice1(walletAccount);
        assertEquals(40, result.intValue());
    }
}