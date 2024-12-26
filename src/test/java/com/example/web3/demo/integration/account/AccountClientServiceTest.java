package com.example.web3.demo.integration.account;

import com.example.web3.demo.model.WalletAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountClientServiceTest {

    String testKey = "dbda1821b80551c9d65939329250298aa3472ba22feea921c0cf5d620ea67b97";

    @Autowired
    private AccountClientService accountClientService;

    @Test
    void should_get_account_balance_correct() {
        WalletAccount walletAccount = WalletAccount.builder().privateKey(testKey).build();
        BigInteger balance = accountClientService.GetAccountBalanceInWei(walletAccount);
        assertTrue(balance.intValue() > 0);
    }

    @Test
    void should_get_account_balance_in_ether_correct() {
        WalletAccount walletAccount = WalletAccount.builder().privateKey(testKey).build();
        BigInteger balance = accountClientService.GetAccountBalanceInWei(walletAccount);
        BigDecimal etherBalance = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        assertTrue(etherBalance.doubleValue() > 0);
    }

}