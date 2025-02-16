package com.dev.walletrestapi.controllers;

import com.dev.walletrestapi.models.Wallet;
import com.dev.walletrestapi.requests.ChangeBalanceRequest;
import com.dev.walletrestapi.services.implementations.WalletServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletServiceImpl walletService;

    public WalletController(WalletServiceImpl walletService) {
        this.walletService = walletService;
    }


    @PostMapping(value = "/wallet")
    public ResponseEntity<String> changeBalance(@RequestBody ChangeBalanceRequest body) {

        if(body.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("The amount of the operation must be greater than 0!");
        }

        String resultMessage = walletService.changeBalance(body.getWalletId(), body.getType(), body.getAmount());

        return new ResponseEntity<>(resultMessage, HttpStatus.OK);

    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<String> viewBalance(@PathVariable("WALLET_UUID") UUID walletId) {

        Wallet wallet = walletService.getWalletById(walletId);

        return new ResponseEntity<>(String.valueOf(wallet.getBalance()), HttpStatus.OK);
    }

}
