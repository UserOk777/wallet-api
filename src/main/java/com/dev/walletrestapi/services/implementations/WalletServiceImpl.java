package com.dev.walletrestapi.services.implementations;

import com.dev.walletrestapi.enums.OperationType;
import com.dev.walletrestapi.models.Wallet;
import com.dev.walletrestapi.repositories.WalletRepository;
import com.dev.walletrestapi.services.interfaces.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public String changeBalance(UUID walletId, OperationType type, BigDecimal amount) {
        Wallet wallet = getWalletById(walletId);

        if(type.equals(OperationType.WITHDRAW) && wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in the account!");
        }

        BigDecimal newBalance = type.apply(wallet.getBalance(), amount);

        wallet.setBalance(newBalance);

        return "Account transaction: " + type;
    }

    @Override
    @Transactional(readOnly = true)
    public Wallet getWalletById(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean contains(UUID walletId) {return walletRepository.existsById(walletId);}

}
