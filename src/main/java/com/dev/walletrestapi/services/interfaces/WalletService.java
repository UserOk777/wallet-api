package com.dev.walletrestapi.services.interfaces;

import com.dev.walletrestapi.enums.OperationType;
import com.dev.walletrestapi.models.Wallet;
import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

    String changeBalance(UUID walletId, OperationType operationType, BigDecimal amount);
    Wallet getWalletById(UUID walletId);
    boolean contains(UUID walletId);
}
