package com.example.web3.demo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

@Slf4j
@Service
public class Web3ClientService implements DisposableBean {

    private final Web3j web3j;

    public Web3ClientService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    @Override
    public void destroy() throws Exception {
        web3j.shutdown();
    }


}
