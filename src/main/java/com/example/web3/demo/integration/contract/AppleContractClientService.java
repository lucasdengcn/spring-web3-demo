package com.example.web3.demo.integration.contract;

import com.example.demo.contracts.applecontractv2.AppleContractV2;
import com.example.web3.demo.integration.CredentialUtils;
import com.example.web3.demo.model.WalletAccount;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppleContractClientService implements DisposableBean {

    private final Web3j web3j;
    private final String contractAddress;
    private final List<Disposable> disposableList = new ArrayList<>();

    /**
     *
     * @param web3j
     */
    public AppleContractClientService(Web3j web3j) {
        this.web3j = web3j;
        this.contractAddress = "0xe7f1725E7734CE288F8367e1Bb143E90bb3F0512";
        this.SubscribePriceChangedEvents();
    }

    /**
     * listen price changed event on the networks
     */
    private void SubscribePriceChangedEvents(){
        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, this.contractAddress);
        filter.addSingleTopic(EventEncoder.encode(AppleContractV2.PRICECHANGEDSUCCESS_EVENT));
        //
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            AppleContractV2.PriceChangedSuccessEventResponse eventResponse = AppleContractV2.getPriceChangedSuccessEventFromLog(event);
            log.info("PriceChangedSuccessEvent: {}, {}", eventResponse.newPrice, eventResponse.oldPrice);
            // store event into db.
        });
        disposableList.add(disposable);
    }

    /**
     * build contract instance
     *
     * @param walletAccount
     * @return
     * @throws CipherException
     * @throws IOException
     */
    @NotNull
    private AppleContractV2 buildContractInstance(WalletAccount walletAccount) throws CipherException, IOException {
        Credentials credentials = CredentialUtils.getWalletCredentials(walletAccount);
        return AppleContractV2.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    /**
     * call contract functions: appleCount, price0.
     *
     * @param walletAccount
     * @return
     */
    public Pair<BigInteger, BigInteger> GetAppleCount(WalletAccount walletAccount){
        try {
            AppleContractV2 appleContractV2 = buildContractInstance(walletAccount);
            BigInteger appleCount = appleContractV2.appleCount().send();
            BigInteger price0 = appleContractV2.price0().send();
            log.info("appleCount: {}, price0: {}", appleCount, price0);
            return new ImmutablePair<>(appleCount, price0);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * call contract function: price1
     *
     * @param walletAccount
     * @return
     */
    public BigInteger GetPrice1(WalletAccount walletAccount){
        try {
            AppleContractV2 appleContractV2 = buildContractInstance(walletAccount);
            return appleContractV2.price1().send();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * call contract function: changePrice
     *
     * @param walletAccount
     * @param price1
     * @return
     */
    public AppleContractV2.PriceChangedSuccessEventResponse ChangePrice(WalletAccount walletAccount, BigInteger price1) {
        try {
            AppleContractV2 appleContractV2 = buildContractInstance(walletAccount);
            TransactionReceipt receipt = appleContractV2.changePrice(price1).send();
            // log.info("receipt: {}", receipt);
            Log log1 = receipt.getLogs().getFirst();
            return AppleContractV2.getPriceChangedSuccessEventFromLog(log1);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() throws Exception {
        disposableList.forEach(Disposable::dispose);
    }
}
