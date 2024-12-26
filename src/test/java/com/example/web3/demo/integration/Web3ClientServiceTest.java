package com.example.web3.demo.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Web3ClientServiceTest {

    @Autowired
    private Web3ClientService web3ClientService;

    @Test
    void web3ClientService() {
        assertNotNull(web3ClientService.getWeb3j());
    }

}