package com.example.web3.demo.integration.swap;

import com.example.web3.demo.integration.CredentialUtils;
import com.example.web3.demo.model.WalletAccount;
import com.google.common.collect.Maps;
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
import java.util.Map;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class UniswapPoolClientService implements DisposableBean {

    private final Web3j web3j;
    private final List<Disposable> disposableList = new ArrayList<>();

    private Map<String, String> tokenMaps = Maps.newHashMap();

    public UniswapPoolClientService(Web3j web3j) {
        this.web3j = web3j;
        //
        tokenMaps.put("0x7fb0A4f081b3F5Ed84759A8675094a71e713b802", "USDT_USDC");
        tokenMaps.put("0xE75e7eBcD8B0e9c0810C3Af537FD44B7e3dd92E3", "USDT_WBTC");
        tokenMaps.put("0x2FBaa56Da111E060055690f6f8d442AE335A62cc", "USDC_WBTC");
        //
        tokenMaps.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String address, String name) {
                SubscribeSwapEvents(address, name);
                SubscribeMintEvents(address, name);
                SubscribeBurnEvents(address, name);
                SubscribeCollectEvents(address, name);
                SubscribeFlashEvents(address, name);
            }
        });
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
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PoolContract.SWAP_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PoolContract.SwapEventResponse eventResponse = UniswapV3PoolContract.getSwapEventFromLog(event);
            log.info("{}, swap event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

    public void SubscribeMintEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PoolContract.Mint_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PoolContract.MintEventResponse eventResponse = UniswapV3PoolContract.getMintEventFromLog(event);
            log.info("{}, Mint event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

    public void SubscribeBurnEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PoolContract.Burn_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PoolContract.BurnEventResponse eventResponse = UniswapV3PoolContract.getBurnEventFromLog(event);
            log.info("{}, Burn event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

    public void SubscribeCollectEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PoolContract.Collect_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PoolContract.CollectEventResponse eventResponse = UniswapV3PoolContract.getCollectEventFromLog(event);
            log.info("{}, Collect event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

    public void SubscribeFlashEvents(String address, String name) {
        EthFilter filter =
                new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, address);
        filter.addSingleTopic(EventEncoder.encode(UniswapV3PoolContract.Flash_EVENT));
        Disposable disposable = web3j.ethLogFlowable(filter).subscribe(event -> {
            UniswapV3PoolContract.FlashEventResponse eventResponse = UniswapV3PoolContract.getFlashEventFromLog(event);
            log.info("{}, Flash event: {}", name, eventResponse);
        });
        disposableList.add(disposable);
    }

}
