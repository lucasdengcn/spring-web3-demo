/* (C) 2024 */ 

package com.example.web3.demo.wallet;

import static org.web3j.crypto.Hash.sha256;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.exception.CipherException;

@Slf4j
@Service
public class WalletService {

    private static final SecureRandom secureRandom = new SecureRandom();
    private final ObjectMapper objectMapper;

    public WalletService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @NotNull public String generateMnemonic() {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        return MnemonicUtils.generateMnemonic(initialEntropy);
    }

    @NotNull public ECKeyPair generateEcKeyPairWithMnemonic(String mnemonic, String password) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        return ECKeyPair.create(sha256(seed));
    }

    /**
     *
     * @throws CipherException
     * @throws JsonProcessingException
     */
    public String generateWalletFileString(String password) throws Exception {
        String mnemonic = generateMnemonic();
        ECKeyPair ecKeyPair = generateEcKeyPairWithMnemonic(mnemonic, password);
        WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
        String json = objectMapper.writeValueAsString(walletFile);
        return json;
    }

    /**
     *
     * @throws CipherException
     * @throws JsonProcessingException
     */
    public String generateWalletFileString(String mnemonic, String password) throws Exception {
        ECKeyPair ecKeyPair = generateEcKeyPairWithMnemonic(mnemonic, password);
        WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
        String json = objectMapper.writeValueAsString(walletFile);
        return json;
    }
}
