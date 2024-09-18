package com.codingbad.project.uber.uberApp.dto;

import com.codingbad.project.uber.uberApp.entities.Ride;
import com.codingbad.project.uber.uberApp.entities.Wallet;
import com.codingbad.project.uber.uberApp.entities.enums.TransactionMethod;
import com.codingbad.project.uber.uberApp.entities.enums.TransactionType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    private RideDto ride;
    private String transactionId;
    private WalletDto wallet;

    private LocalDateTime timeStamp;
}
