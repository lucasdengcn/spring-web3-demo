package com.example.web3.demo.integration.swap;

import com.example.web3.demo.integration.CredentialUtils;
import com.example.web3.demo.model.WalletAccount;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UniswapPositionManagerClientService implements DisposableBean {

    private final Web3j web3j;
    private final List<Disposable> disposableList = new ArrayList<>();

    public UniswapPositionManagerClientService(Web3j web3j) {
        this.web3j = web3j;
        this.SubscribeIncreaseEvents("0x5FC8d32690cc91D4c39d9d3abcBD16989F875707", "");
        this.SubscribeDecreaseEvents("0x5FC8d32690cc91D4c39d9d3abcBD16989F875707", "");
        this.SubscribeCollectEvents("0x5FC8d32690cc91D4c39d9d3abcBD16989F875707", "");
    }

    public void destroy() throws Exception {
        disposableList.forEach(Disposable::dispose);
    }

    @NotNull
    private UniswapV3PositionManagerContract buildContractInstance(WalletAccount walletAccount, String contractAddress) throws CipherException, IOException {
        Credentials credentials = CredentialUtils.getWalletCredentials(walletAccount);
        return UniswapV3PositionManagerContract.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    /**
     * listen swap event on the pool
     */
    public void SubscribeIncreaseEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PositionManagerContract.IncreaseLiquidity_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PositionManagerContract.IncreaseLiquidityEventResponse eventResponse = UniswapV3PositionManagerContract.getIncreaseLiquidityEventFromLog(event);
            log.info("{}, Increase event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

    public void SubscribeDecreaseEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PositionManagerContract.DecreaseLiquidity_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PositionManagerContract.DecreaseLiquidityEventResponse eventResponse = UniswapV3PositionManagerContract.getDecreaseLiquidityEventFromLog(event);
            log.info("{}, Decrease event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

    public void SubscribeCollectEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PositionManagerContract.Collect_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PositionManagerContract.CollectEventResponse eventResponse = UniswapV3PositionManagerContract.getCollectEventFromLog(event);
            log.info("{}, Collect event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

}
