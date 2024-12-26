package com.example.web3.demo.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.*;
import org.web3j.crypto.exception.CipherException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WalletUtilsTest {

    String testPwdFake = "p@sSw0rd!";

    @Autowired
    private WalletService walletService;

    @Test
    void step1_should_generate_mnemonic_success(){
        String mnemonic = walletService.generateMnemonic();
        System.out.printf("%s\n", mnemonic);
    }

    @Test
    void step2_should_generate_keypair_with_mnemonic_success() {
        try {
            String mnemonic = walletService.generateMnemonic();
            ECKeyPair keyPair = walletService.generateEcKeyPairWithMnemonic(mnemonic, testPwdFake);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void step3_should_generate_wallet_success(){
        try {
            String mnemonic = walletService.generateMnemonic();
            ECKeyPair keyPair = walletService.generateEcKeyPairWithMnemonic(mnemonic, testPwdFake);
            WalletFile walletFile = Wallet.createStandard(testPwdFake, keyPair);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_generate_wallet_file_success() {
        try {
            String mnemonic = walletService.generateMnemonic();
            ECKeyPair keyPair = walletService.generateEcKeyPairWithMnemonic(mnemonic, testPwdFake);
            WalletUtils.generateWalletFile(testPwdFake, keyPair, new File("temp"), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_generate_wallet_string_success() {
        try {
            String json = walletService.generateWalletFileString(testPwdFake);
            assertNotNull(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_generate_wallet_string2_success() {
        try {
            String mnemonic = walletService.generateMnemonic();
            String json = walletService.generateWalletFileString(mnemonic, testPwdFake);
            assertNotNull(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
