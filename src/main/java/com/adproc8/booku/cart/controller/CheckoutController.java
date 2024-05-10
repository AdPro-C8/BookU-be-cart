package com.adproc8.booku.cart.controller;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adproc8.booku.cart.enums.PaymentStatus;
import com.adproc8.booku.cart.form.CheckoutForm;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.Checkout;
import com.adproc8.booku.cart.service.CartService;
import com.adproc8.booku.cart.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
class CheckoutController {

    private final CheckoutService checkoutService;
    private final CartService cartService;

    @Autowired
    CheckoutController(CheckoutService checkoutService, CartService cartService) {
        this.checkoutService = checkoutService;
        this.cartService = cartService;
    }

    @GetMapping("/{checkoutId}")
    Checkout getCheckoutById(
        @PathVariable UUID checkoutId,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        Checkout checkout = checkoutService
            .findById(checkoutId)
            .orElseThrow();
        String checkoutOwnerName = checkout
            .getCart()
            .getId()
            .getUsername();

        String username = userDetails.getUsername();

        if (!username.equals(checkoutOwnerName))
            throw new NoSuchElementException("No value present");

        return checkout;
    }

    @PostMapping("")
    Checkout postCheckout(
        @RequestBody CheckoutForm form,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        Cart cart = cartService
            .findByUsername(userDetails.getUsername())
            .orElseThrow();

        String deliveryAddress = form.getDeliveryAddress();

        Checkout newCheckout = Checkout.builder()
            .cart(cart)
            .deliveryAddress(deliveryAddress)
            .paymentStatus(PaymentStatus.PENDING)
            .build();

        checkoutService.save(newCheckout);

        return newCheckout;
    }
}
