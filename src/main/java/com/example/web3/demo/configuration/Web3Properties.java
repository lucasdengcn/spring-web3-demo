package com.example.web3.demo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("web3")
public class Web3Properties {

    private String url;
    private String chainId;
    //

}
