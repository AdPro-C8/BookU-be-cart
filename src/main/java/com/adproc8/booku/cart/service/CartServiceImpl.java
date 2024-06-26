package com.adproc8.booku.cart.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.adproc8.booku.cart.dto.BookIdsDto;
import com.adproc8.booku.cart.model.Book;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.repository.CartRepository;

@Service
class CartServiceImpl implements CartService {

    private static final String BOOK_GET_BATCH_PATH = "/book/get-multiple";
    private static final ParameterizedTypeReference<List<Book>> BOOK_LIST_TYPE =
            new ParameterizedTypeReference<List<Book>>() {};

    private final String bookListHost;

    private final CartRepository cartRepository;
    private final RestClient restClient;

    @Autowired
    CartServiceImpl(
        CartRepository cartRepository,
        RestClient restClient,
        @Value("${api.booklist-host}") String bookListHost)
    {
        this.cartRepository = cartRepository;
        this.restClient = restClient;
        this.bookListHost = bookListHost;
    }

    private List<Book> findBooksByCart(Cart cart, String authHeader)
    throws RestClientException
    {
        List<Book> books = restClient.post()
                .uri(bookListHost + BOOK_GET_BATCH_PATH)
                .header("Authorization", authHeader)
                .body(new BookIdsDto(cart.getBookIds()))
                .retrieve()
                .toEntity(BOOK_LIST_TYPE)
                .getBody();

        return books;
    }

    public Cart save(Cart cart, String authHeader)
    throws RestClientException
    {
        List<Book> books = findBooksByCart(cart, authHeader);
        Set<UUID> bookIds = books.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());

        Set<UUID> cartBookIds = cart.getBookIds()
                .stream()
                .filter(bookId -> bookIds.contains(bookId))
                .collect(Collectors.toSet());
        cart.setBookIds(cartBookIds);

        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(UUID id, String authHeader)
    throws RestClientException
    {
        Optional<Cart> result = cartRepository.findById(id);

        if (result.isEmpty()) {
            return result;
        }

        Cart cart = result.get();

        List<Book> books = findBooksByCart(cart, authHeader);
        cart.setBooks(books);

        return Optional.of(cart);
    }

    public Cart findByUserId(UUID userId, String authHeader)
    throws RestClientException
    {
        Optional<Cart> result = cartRepository.findByUserId(userId);

        if (result.isEmpty()) {
            Cart cart = Cart.builder()
                    .userId(userId)
                    .build();
            return save(cart, authHeader);
        }

        Cart cart = result.get();

        List<Book> books = findBooksByCart(cart, authHeader);
        cart.setBooks(books);

        return cart;
    }

    public void deleteById(UUID cartId) {
        cartRepository.deleteById(cartId);
    }
}
