package com.adproc8.booku.cart.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.cart.dto.PurchaseDetailsRequestDto;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.PurchaseDetails;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;
import com.adproc8.booku.cart.service.PurchaseDetailsService;

@RestController
@RequestMapping("/purchase-details")
class PurchaseDetailsController {

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
    @ResponseStatus(HttpStatus.OK)
    PurchaseDetails createPurchaseDetails(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody PurchaseDetailsRequestDto purchaseDetailsDto,
        @AuthenticationPrincipal User user)
    {
        String deliveryAddress = purchaseDetailsDto.getDeliveryAddress();

        Cart cart = cartService
            .findByUserId(user.getId(), authHeader)
            .orElseThrow();

        PurchaseDetails purchaseDetails = PurchaseDetails.builder()
            .deliveryAddress(deliveryAddress)
            .cart(cart)
            .build();

        purchaseDetailsService.save(purchaseDetails);

        return purchaseDetails;
    }
}
