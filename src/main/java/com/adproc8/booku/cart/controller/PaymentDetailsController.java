package com.adproc8.booku.cart.controller;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import com.adproc8.booku.cart.dto.CreatePaymentDetailsRequestDto;
import com.adproc8.booku.cart.dto.CreatePaymentDetailsResponseDto;
import com.adproc8.booku.cart.dto.UpdatePaymentDetailsRequestDto;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.PaymentDetails;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;
import com.adproc8.booku.cart.service.PaymentDetailsService;

@RestController
@RequestMapping("/payment")
class PaymentDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentDetailsController.class);

    private final CartService cartService;
    private final PaymentDetailsService paymentDetailsService;

    @Autowired
    PaymentDetailsController(
        CartService cartService,
        PaymentDetailsService paymentDetailsService)
    {
        this.cartService = cartService;
        this.paymentDetailsService = paymentDetailsService;
    }

    @GetMapping("")
    ResponseEntity<PaymentDetails> getPaymentDetails(
        @AuthenticationPrincipal User user)
    {
        UUID userId = user.getId();
        PaymentDetails paymentDetails = paymentDetailsService
                .findByUserId(userId)
                .orElseThrow();

        return ResponseEntity.ok(paymentDetails);
    }

    @PostMapping("")
    ResponseEntity<CreatePaymentDetailsResponseDto> createPaymentDetails(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody CreatePaymentDetailsRequestDto paymentDetailsDto,
        @AuthenticationPrincipal User user)
    {
        Optional<String> optionalDeliveryAddress =
                Optional.ofNullable(paymentDetailsDto.getDeliveryAddress());

        if (optionalDeliveryAddress.isEmpty())
            return ResponseEntity.badRequest().build();

        String deliveryAddress = paymentDetailsDto.getDeliveryAddress();

        Cart cart;
        try {
            cart = cartService.findByUserId(user.getId(), authHeader);
        } catch (RestClientException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .deliveryAddress(deliveryAddress)
                .cart(cart)
                .build();

        try {
            paymentDetails = paymentDetailsService.save(paymentDetails);
        } catch (DataIntegrityViolationException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UUID paymentDetailsId = paymentDetails.getId();
        CreatePaymentDetailsResponseDto responseDto =
                new CreatePaymentDetailsResponseDto(paymentDetailsId);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("")
    ResponseEntity<Void> deletePaymentDetails(
        @AuthenticationPrincipal User user)
    {
        UUID userId = user.getId();
        paymentDetailsService.deleteByUserId(userId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("")
    ResponseEntity<Void> updatePaymentDetails(
        @PathVariable UUID paymentDetailsId,
        @AuthenticationPrincipal User user,
        @RequestBody UpdatePaymentDetailsRequestDto paymentDetailsDto)
    {
        UUID userId = user.getId();
        PaymentDetails paymentDetails = paymentDetailsService
                .findByUserId(userId)
                .orElseThrow();

        Optional.ofNullable(paymentDetailsDto.getDeliveryAddress())
                .ifPresent(deliveryAddress -> paymentDetails.setDeliveryAddress(deliveryAddress));
        Optional.ofNullable(paymentDetailsDto.getPaymentStatus())
                .ifPresent(paymentStatus -> paymentDetails.setPaymentStatus(paymentStatus));

        paymentDetailsService.save(paymentDetails);

        return ResponseEntity.ok().build();
    }
}
