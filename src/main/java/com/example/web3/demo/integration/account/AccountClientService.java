/* (C) 2024 */ 

package com.example.web3.demo.integration.account;

import com.example.web3.demo.model.WalletAccount;
import java.io.IOException;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

@Slf4j
@Service
public class AccountClientService {

    private final Web3j web3j;

    public AccountClientService(Web3j web3j) {
        this.web3j = web3j;
    }

    /**
     *
     * @param walletAccount
     * @return
     */
    public BigInteger GetAccountBalanceInWei(WalletAccount walletAccount) {
        try {
            Credentials credentials = Credentials.create(walletAccount.getPrivateKey());
            EthGetBalance sent = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .send();
            return sent.getBalance();
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
