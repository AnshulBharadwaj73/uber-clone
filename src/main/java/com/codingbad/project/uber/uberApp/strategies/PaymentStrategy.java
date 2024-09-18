package com.codingbad.project.uber.uberApp.strategies;

import com.codingbad.project.uber.uberApp.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
