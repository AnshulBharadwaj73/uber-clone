package com.codingbad.project.uber.uberApp.services;

import com.codingbad.project.uber.uberApp.entities.Payment;
import com.codingbad.project.uber.uberApp.entities.Ride;
import com.codingbad.project.uber.uberApp.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride Ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
