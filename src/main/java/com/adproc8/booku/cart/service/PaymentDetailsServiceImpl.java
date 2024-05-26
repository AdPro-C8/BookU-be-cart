package com.adproc8.booku.cart.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.adproc8.booku.cart.dto.CreateTransactionHistoryDto;
import com.adproc8.booku.cart.enums.PaymentStatus;
import com.adproc8.booku.cart.model.Book;
import com.adproc8.booku.cart.model.PaymentDetails;
import com.adproc8.booku.cart.repository.PaymentDetailsRepository;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private static final String CREATE_TRANSACTION_HISTORY_PATH = "/api/history/createPurchaseHistory";

    private final String historyHost;

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final RestClient restClient;

    @Autowired
    public PaymentDetailsServiceImpl(
        PaymentDetailsRepository paymentDetailsRepository,
        RestClient restClient,
        @Value("${api.history-host}") String historyHost)
    {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.restClient = restClient;
        this.historyHost = historyHost;
    }

    private void saveToTransactionHistory(PaymentDetails paymentDetails, String authHeader)
    throws RestClientException
    {
        int totalPrice = paymentDetails.getCart()
                .getBooks()
                .stream()
                .mapToInt(Book::getPrice)
                .sum();

        Date purchaseDate = new Date(Calendar.getInstance().getTimeInMillis());

        CreateTransactionHistoryDto createTransactionHistoryDto = CreateTransactionHistoryDto.builder()
                .bookIds(List.copyOf(paymentDetails.getCart().getBookIds()))
                .userId(paymentDetails.getCart().getUserId())
                .purchaseDate(purchaseDate)
                .totalPrice(totalPrice)
                .build();

        restClient.post()
                .uri(historyHost + CREATE_TRANSACTION_HISTORY_PATH)
                .header("Authorization", authHeader)
                .body(createTransactionHistoryDto)
                .retrieve()
                .toBodilessEntity();
    }

    public PaymentDetails save(PaymentDetails paymentDetails) {
        if (paymentDetails.getDeliveryAddress().isEmpty())
            throw new IllegalArgumentException("Delivery address cannot be empty");

        return paymentDetailsRepository.save(paymentDetails);
    }

    public PaymentDetails save(PaymentDetails paymentDetails, String authHeader)
    throws RestClientException
    {
        if (paymentDetails.getDeliveryAddress().isEmpty())
            throw new IllegalArgumentException("Delivery address cannot be empty");

        PaymentStatus status = paymentDetails.getPaymentStatus();

        if (status.equals(PaymentStatus.SUCCESS)) {
            paymentDetails.getCart().setActive(false);
            saveToTransactionHistory(paymentDetails, authHeader);
        } else {
            paymentDetails.getCart().setActive(false);
        }

        return paymentDetailsRepository.save(paymentDetails);
    }

    public Optional<PaymentDetails> findById(UUID id) {
        return paymentDetailsRepository.findById(id);
    }

    public Optional<PaymentDetails> findByUserId(UUID userId) {
        return paymentDetailsRepository.findByUserId(userId);
    }

    public void deleteById(UUID id) {
        paymentDetailsRepository.deleteById(id);
    }

    public void deleteByIdAndUserId(UUID id, UUID userId) throws IllegalArgumentException {
        paymentDetailsRepository.deleteByIdAndCart_UserId(id, userId);
    }
}
