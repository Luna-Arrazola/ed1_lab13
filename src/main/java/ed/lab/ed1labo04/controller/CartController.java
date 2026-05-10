package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.entity.CartEntity;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartEntity> createCart(
            @RequestBody CreateCartRequest createCartRequest) {

        try {
            CartEntity cartEntity = cartService.createCart(createCartRequest);
            return ResponseEntity.status(201).body(cartEntity);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartEntity> getCart(@PathVariable long id) {

        Optional<CartEntity> cartEntity = cartService.getCartById(id);

        if (cartEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartEntity.get());
    }
}