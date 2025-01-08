package com.example.web3.demo.integration.swap;

import com.example.demo.contracts.applecontractv2.AppleContractV2;
import com.example.web3.demo.integration.CredentialUtils;
import com.example.web3.demo.model.WalletAccount;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.contracts.applecontractv2.AppleContractV2.PRICECHANGEDSUCCESS_EVENT;

@Service
@Slf4j
public class UniswapClientService implements DisposableBean {

    private final Web3j web3j;
    private final List<Disposable> disposableList = new ArrayList<>();

    public UniswapClientService(Web3j web3j) {
        this.web3j = web3j;
        this.SubscribeSwapEvents("0x7fb0A4f081b3F5Ed84759A8675094a71e713b802", "USDT_USDC");
        this.SubscribeSwapEvents("0xE75e7eBcD8B0e9c0810C3Af537FD44B7e3dd92E3", "USDT_WBTC");
        this.SubscribeSwapEvents("0x2FBaa56Da111E060055690f6f8d442AE335A62cc", "USDC_WBTC");
    }

    public void destroy() throws Exception {
        disposableList.forEach(Disposable::dispose);
    }

    @NotNull
    private UniswapV3PoolContract buildContractInstance(WalletAccount walletAccount, String contractAddress) throws CipherException, IOException {
        Credentials credentials = CredentialUtils.getWalletCredentials(walletAccount);
        return UniswapV3PoolContract.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    /**
     * listen swap event on the pool
     */
    public void SubscribeSwapEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PoolContract.SwapEventResponse eventResponse = UniswapV3PoolContract.getSwapEventFromLog(event);
            log.info("{}, swap event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

}
