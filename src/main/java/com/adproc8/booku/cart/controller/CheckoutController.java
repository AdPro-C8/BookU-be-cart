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
import com.adproc8.booku.cart.service.CheckoutService;
import com.adproc8.booku.cart.service.UserService;

@RestController
@RequestMapping("/checkout")
class CheckoutController {

    private final CheckoutService checkoutService;
    private final UserService userService;

    @Autowired
    CheckoutController(CheckoutService checkoutService, UserService userService) {
        this.checkoutService = checkoutService;
        this.userService = userService;
    }

    @GetMapping("/{checkoutId}")
    Checkout getCheckoutById(
        @PathVariable UUID checkoutId,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        Checkout checkout = checkoutService.findById(checkoutId)
            .orElseThrow();

        String checkoutOwnerName = checkout.getCart()
            .getUser()
            .getUsername();

        if (!checkoutOwnerName.equals(username))
            throw new NoSuchElementException("No value present");

        return checkout;
    }

    @PostMapping("")
    Checkout postCheckout(
        @RequestBody CheckoutForm form,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        Cart cart = userService
            .findByUsername(userDetails.getUsername())
            .orElseThrow()
            .getCart();

        Checkout newCheckout = Checkout.builder()
            .cart(cart)
            .deliveryAddress(form.getDeliveryAddress())
            .paymentStatus(PaymentStatus.PENDING)
            .build();

        checkoutService.save(newCheckout);

        return newCheckout;
    }
}
