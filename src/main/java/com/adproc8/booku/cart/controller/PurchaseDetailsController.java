package com.adproc8.booku.cart.controller;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import com.adproc8.booku.cart.dto.CreatePurchaseDetailsRequestDto;
import com.adproc8.booku.cart.dto.CreatePurchaseDetailsResponseDto;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.PurchaseDetails;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;
import com.adproc8.booku.cart.service.PurchaseDetailsService;

@RestController
@RequestMapping("/purchase")
class PurchaseDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseDetailsController.class);

    private final CartService cartService;
    private final PurchaseDetailsService purchaseDetailsService;

    @Autowired
    PurchaseDetailsController(CartService cartService, PurchaseDetailsService purchaseDetailsService) {
        this.cartService = cartService;
        this.purchaseDetailsService = purchaseDetailsService;
    }

    @GetMapping("/{purchaseDetailsId}")
    ResponseEntity<PurchaseDetails> getPurchaseDetailsById(
        @PathVariable UUID purchaseDetailsId,
        @AuthenticationPrincipal User user)
    {
        PurchaseDetails purchaseDetails = purchaseDetailsService
                .findById(purchaseDetailsId)
                .orElseThrow();
        UUID purchaseDetailsUserId = purchaseDetails
                .getCart()
                .getUserId();
        UUID userId = user.getId();

        if (!userId.equals(purchaseDetailsUserId))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(purchaseDetails);
    }

    @PostMapping("")
    ResponseEntity<CreatePurchaseDetailsResponseDto> createPurchaseDetails(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody CreatePurchaseDetailsRequestDto purchaseDetailsDto,
        @AuthenticationPrincipal User user)
    {
        Optional<String> optionalDeliveryAddress =
                Optional.ofNullable(purchaseDetailsDto.getDeliveryAddress());

        if (optionalDeliveryAddress.isEmpty())
            return ResponseEntity.badRequest().build();

        String deliveryAddress = purchaseDetailsDto.getDeliveryAddress();

        Cart cart;
        try {
            cart = cartService.findByUserId(user.getId(), authHeader);
        } catch (RestClientResponseException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        PurchaseDetails purchaseDetails = PurchaseDetails.builder()
                .deliveryAddress(deliveryAddress)
                .cart(cart)
                .build();

        purchaseDetails = purchaseDetailsService.save(purchaseDetails);

        UUID purchaseDetailsId = purchaseDetails.getId();
        CreatePurchaseDetailsResponseDto responseDto =
                new CreatePurchaseDetailsResponseDto(purchaseDetailsId);

        return ResponseEntity.ok().body(responseDto);
    }
}
