/* (C) 2024 */ 

package com.example.web3.demo.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WalletAccount {
    //
    private String privateKey;
    //
    private String password;
    //
    private String mnemonic;
    //
    private String content;
}
