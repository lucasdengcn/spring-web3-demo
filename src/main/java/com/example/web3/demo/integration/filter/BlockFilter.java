package com.example.web3.demo.integration.filter;

import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

@Slf4j
@Service
public class BlockFilter implements DisposableBean {

    private final Web3j web3j;
    private final Disposable disposable;

    public BlockFilter(Web3j web3j) {
        this.web3j = web3j;
        this.disposable = subscribe();
    }

    public Disposable subscribe(){
        return web3j.blockFlowable(false).subscribe(block -> {
            log.info("block: {}", block);
        });
    }

    @Override
    public void destroy() throws Exception {
        try {
            disposable.dispose();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}