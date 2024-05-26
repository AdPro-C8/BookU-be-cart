package com.adproc8.booku.cart.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.cart.dto.CreatePaymentDetailsRequestDto;
import com.adproc8.booku.cart.dto.CreatePaymentDetailsResponseDto;
import com.adproc8.booku.cart.dto.UpdatePaymentDetailsAddressRequestDto;
import com.adproc8.booku.cart.dto.UpdatePaymentDetailsStatusDto;
import com.adproc8.booku.cart.enums.PaymentStatus;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.PaymentDetails;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;
import com.adproc8.booku.cart.service.PaymentDetailsService;

@RestController
@RequestMapping("/payment")
class PaymentDetailsController {

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
    @ResponseStatus(HttpStatus.OK)
    PaymentDetails getPaymentDetails(@AuthenticationPrincipal User user) {
        UUID userId = user.getId();
        PaymentDetails paymentDetails = paymentDetailsService
                .findByUserId(userId)
                .orElseThrow();

        return paymentDetails;
    }

    @PostMapping("")
    ResponseEntity<CreatePaymentDetailsResponseDto> createPaymentDetails(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody CreatePaymentDetailsRequestDto paymentDetailsDto,
        @AuthenticationPrincipal User user)
    {
        UUID userId = user.getId();
        Optional<String> optionalDeliveryAddress =
                Optional.ofNullable(paymentDetailsDto.getDeliveryAddress());

        if (optionalDeliveryAddress.isEmpty())
            return ResponseEntity.badRequest().build();

        String deliveryAddress = paymentDetailsDto.getDeliveryAddress();

        Cart cart = cartService.findByUserId(userId, authHeader);

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .deliveryAddress(deliveryAddress)
                .cart(cart)
                .build();

        paymentDetails = paymentDetailsService.save(paymentDetails);

        UUID paymentDetailsId = paymentDetails.getId();
        CreatePaymentDetailsResponseDto responseDto =
                new CreatePaymentDetailsResponseDto(paymentDetailsId);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deletePaymentDetails(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        UUID userId = user.getId();
        paymentDetailsService.deleteByIdAndUserId(id, userId);
    }

    @PatchMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    void updatePaymentDetailsAddress(
        @AuthenticationPrincipal User user,
        @RequestBody UpdatePaymentDetailsAddressRequestDto paymentDetailsDto)
    {
        UUID userId = user.getId();
        PaymentDetails paymentDetails = paymentDetailsService
                .findByUserId(userId)
                .orElseThrow();

        Optional.ofNullable(paymentDetailsDto.getDeliveryAddress())
                .ifPresent(deliveryAddress -> paymentDetails.setDeliveryAddress(deliveryAddress));

        paymentDetailsService.save(paymentDetails);
    }

    @PatchMapping("/status")
    ResponseEntity<Void> updatePaymentDetailsStatus(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody UpdatePaymentDetailsStatusDto paymentDetailsDto)
    {
        UUID userId = user.getId();
        Optional<String> optionalStatusString =
                Optional.ofNullable(paymentDetailsDto.getPaymentStatus());

        if (optionalStatusString.isEmpty())
            return ResponseEntity.badRequest().build();

        String statusString = optionalStatusString.get();
        PaymentStatus status = PaymentStatus.valueOf(statusString);

        PaymentDetails paymentDetails = paymentDetailsService
                .findByUserId(userId)
                .orElseThrow();

        paymentDetails.setPaymentStatus(status);

        paymentDetailsService.save(paymentDetails, authHeader);

        return ResponseEntity.ok().build();
    }
}
