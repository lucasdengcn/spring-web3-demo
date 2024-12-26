/* (C) 2024 */ 

package com.example.web3.demo.integration.account;

import static org.junit.jupiter.api.Assertions.*;

import com.example.web3.demo.model.WalletAccount;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.utils.Convert;

@SpringBootTest
class AccountClientServiceTest {

    String testKey = "dbda1821b80551c9d65939329250298aa3472ba22feea921c0cf5d620ea67b97";

    @Autowired
    private AccountClientService accountClientService;

    @Test
    void should_get_account_balance_correct() {
        WalletAccount walletAccount =
                WalletAccount.builder().privateKey(testKey).build();
        BigInteger balance = accountClientService.GetAccountBalanceInWei(walletAccount);
        assertTrue(balance.compareTo(BigInteger.ZERO) > 0);
    }

    @Test
    void should_get_account_balance_in_ether_correct() {
        WalletAccount walletAccount =
                WalletAccount.builder().privateKey(testKey).build();
        BigInteger balance = accountClientService.GetAccountBalanceInWei(walletAccount);
        BigDecimal etherBalance = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        assertTrue(etherBalance.compareTo(BigDecimal.ZERO) > 0);
    }
}
