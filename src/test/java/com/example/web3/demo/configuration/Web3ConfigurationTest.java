/* (C) 2024 */ 

package com.example.web3.demo.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"web3.url=http://localhost:8545/", "web3.chainId=31337"})
class Web3ConfigurationTest {

    @Autowired
    private Web3Configuration web3Configuration;

    @Autowired
    private Web3Properties web3Properties;

    @Test
    void should_init_properties_correctly() {
        assertEquals("http://localhost:8545/", web3Properties.getUrl());
        assertEquals("31337", web3Properties.getChainId());
    }
}
