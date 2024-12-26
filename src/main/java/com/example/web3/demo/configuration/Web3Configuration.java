package com.example.web3.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
public class Web3Configuration implements DisposableBean {

    @Autowired
    private Web3Properties web3Properties;

    @Bean
    public Web3j web3j(){
        //
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        // HTTPService or UnixIpcService
        Web3j web3j = Web3j.build(new HttpService(web3Properties.getUrl()),1000, scheduler);
        //
        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("Web3ClientVersion: {}", clientVersion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return web3j;
    }

    @Override
    public void destroy() throws Exception {
        web3j().shutdown();
    }
}
