package com.codingbad.project.uber.uberApp.services;

import com.codingbad.project.uber.uberApp.dto.WalletTransactionDto;
import com.codingbad.project.uber.uberApp.entities.WalletTransaction;
import org.springframework.stereotype.Service;


public interface WalletTransactionService {

    void createNewWalletTransaction(WalletTransaction walletTransaction);


}
