package com.dev.walletrestapi.requests;

import com.dev.walletrestapi.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

public class ChangeBalanceRequest {
    private UUID walletId;
    private OperationType type;
    private BigDecimal amount;

    public ChangeBalanceRequest(){}

    public ChangeBalanceRequest(UUID walletId,OperationType type, BigDecimal amount) {
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public OperationType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
