package com.codingbad.project.uber.uberApp.dto;

import com.codingbad.project.uber.uberApp.entities.User;
import com.codingbad.project.uber.uberApp.entities.WalletTransaction;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
public class WalletDto {

    private Long id;

    private UserDto user;

    private Double balance;

    private List<WalletTransactionDto> transactions;
}
