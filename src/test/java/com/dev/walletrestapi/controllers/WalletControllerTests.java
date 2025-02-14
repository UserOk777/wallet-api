package com.dev.walletrestapi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.dev.walletrestapi.enums.OperationType;
import com.dev.walletrestapi.models.Wallet;
import com.dev.walletrestapi.requests.ChangeBalanceRequest;
import com.dev.walletrestapi.services.implementations.WalletServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

@WebMvcTest(WalletController.class)
public class WalletControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletServiceImpl walletService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testChangeBalance() throws Exception {
//        UUID walletId = UUID.randomUUID();
//        ChangeBalanceRequest request = new ChangeBalanceRequest(walletId, OperationType.DEPOSIT, 100.0);
//        String expectedMessage = "Balance changed successfully";
//
//        when(walletService.changeBalance(eq(walletId), eq(OperationType.DEPOSIT), anyDouble())).thenReturn(expectedMessage);

        UUID walletId = UUID.randomUUID();
        ChangeBalanceRequest request = new ChangeBalanceRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100.0));
        String expectedMessage = "Balance changed successfully";

        when(walletService.changeBalance(eq(walletId), eq(OperationType.DEPOSIT), any(BigDecimal.class))).thenReturn(expectedMessage);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    public void testChangeBalanceWithNegativeAmount() throws Exception {
        UUID walletId = UUID.randomUUID();
        ChangeBalanceRequest request = new ChangeBalanceRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(-50.0));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(
                        result -> assertEquals(
                                "The amount of the operation must be greater than 0!",
                                result.getResolvedException().getMessage()
                        )
                );
    }

    @Test
    public void testViewBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(500.0));

        when(walletService.getWalletById(walletId)).thenReturn(wallet);

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    public void testWithdrawBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        ChangeBalanceRequest request = new ChangeBalanceRequest(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(50.0));
        String expectedMessage = "Balance changed successfully";

        when(walletService.changeBalance(eq(walletId), eq(OperationType.WITHDRAW), any(BigDecimal.class))).thenReturn(expectedMessage);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }
}
