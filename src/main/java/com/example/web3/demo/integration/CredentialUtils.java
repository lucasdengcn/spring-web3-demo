package com.example.web3.demo.integration;

import com.example.web3.demo.model.WalletAccount;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;

import java.io.IOException;

public class CredentialUtils {

    public static Credentials getWalletCredentials(WalletAccount walletAccount) throws IOException, CipherException {
        Credentials credentials = null;
        if (walletAccount.getPrivateKey() != null) {
            credentials = Credentials.create(walletAccount.getPrivateKey());
        } else {
            if (walletAccount.getMnemonic() != null) {
                credentials = WalletUtils.loadCredentials(walletAccount.getPassword(), walletAccount.getMnemonic());
            } else {
                credentials = WalletUtils.loadJsonCredentials(walletAccount.getPassword(), walletAccount.getContent());
            }
        }
        return credentials;
    }

}
