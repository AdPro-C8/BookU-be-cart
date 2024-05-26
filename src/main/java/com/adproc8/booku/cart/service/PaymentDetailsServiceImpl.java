package com.adproc8.booku.cart.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.adproc8.booku.cart.dto.CreateTransactionHistoryDto;
import com.adproc8.booku.cart.dto.PatchBookDto;
import com.adproc8.booku.cart.dto.PatchBooksByIdDto;
import com.adproc8.booku.cart.enums.PaymentStatus;
import com.adproc8.booku.cart.model.Book;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.PaymentDetails;
import com.adproc8.booku.cart.repository.PaymentDetailsRepository;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private static final String CREATE_TRANSACTION_HISTORY_PATH = "/api/history/createPurchaseHistory";
    private static final String BOOK_PATH = "/book";

    private final String historyHost;
    private final String bookListHost;

    private final CartService cartService;
    private final RestClient restClient;
    private final PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    public PaymentDetailsServiceImpl(
        CartService cartService,
        RestClient restClient,
        PaymentDetailsRepository paymentDetailsRepository,
        @Value("${api.history-host}") String historyHost,
        @Value("${api.booklist-host}") String bookListHost)
    {
        this.cartService = cartService;
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.restClient = restClient;
        this.historyHost = historyHost;
        this.bookListHost = bookListHost;
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

    private void updateBooksDownloadCount(Collection<Book> books, String authHeader) {
        List<PatchBookDto> updateBookDtos = books.stream()
                .map(book -> PatchBookDto.builder()
                    .id(book.getId())
                    .downloadCount(1 + book.getDownloadCount())
                    .build())
                .collect(Collectors.toList());

        PatchBooksByIdDto updateBooksByIdDto = new PatchBooksByIdDto(updateBookDtos);

        restClient.patch()
                .uri(bookListHost + BOOK_PATH)
                .header("Authorization", authHeader)
                .body(updateBooksByIdDto)
                .retrieve()
                .toBodilessEntity();
    }

    public PaymentDetails save(PaymentDetails paymentDetails) {
        if (paymentDetails.getDeliveryAddress().isEmpty())
            throw new IllegalArgumentException("Delivery address cannot be empty");

        return paymentDetailsRepository.save(paymentDetails);
    }

    public PaymentDetails save(PaymentDetails paymentDetails, String authHeader)
    throws RestClientException, NoSuchElementException
    {
        if (paymentDetails.getDeliveryAddress().isEmpty())
            throw new IllegalArgumentException("Delivery address cannot be empty");

        PaymentStatus status = paymentDetails.getPaymentStatus();

        if (status.equals(PaymentStatus.CANCELLED)) {
            paymentDetails.getCart().setActive(false);
        } else if (status.equals(PaymentStatus.SUCCESS)) {
            Cart cart = paymentDetails.getCart();
            cart = cartService
                    .findById(cart.getId(), authHeader)
                    .get();

            paymentDetails.getCart().setActive(false);

            List<Book> books = cart.getBooks();

            saveToTransactionHistory(paymentDetails, authHeader);
            updateBooksDownloadCount(books, authHeader);
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
