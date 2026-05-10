package ed.lab.ed1labo04.service;

import ed.lab.ed1labo04.entity.CartEntity;
import ed.lab.ed1labo04.entity.CartItemEntity;
import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CreateCartItemRequest;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.repository.CartRepository;
import ed.lab.ed1labo04.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CartEntity createCart(CreateCartRequest createCartRequest) {

        if (createCartRequest == null ||
                createCartRequest.getCartItems() == null ||
                createCartRequest.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart items are required");
        }

        List<ProductEntity> products = new ArrayList<ProductEntity>();

        for (CreateCartItemRequest itemRequest : createCartRequest.getCartItems()) {

            if (itemRequest.getProductId() == null) {
                throw new IllegalArgumentException("Product id is required");
            }

            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Optional<ProductEntity> optionalProduct =
                    productRepository.findById(itemRequest.getProductId());

            if (optionalProduct.isEmpty()) {
                throw new IllegalArgumentException("Product not found");
            }

            ProductEntity productEntity = optionalProduct.get();

            if (productEntity.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough inventory");
            }

            products.add(productEntity);
        }

        CartEntity cartEntity = new CartEntity();
        List<CartItemEntity> cartItems = new ArrayList<CartItemEntity>();
        double totalPrice = 0;

        for (int i = 0; i < createCartRequest.getCartItems().size(); i++) {

            CreateCartItemRequest itemRequest =
                    createCartRequest.getCartItems().get(i);

            ProductEntity productEntity = products.get(i);

            productEntity.setQuantity(
                    productEntity.getQuantity() - itemRequest.getQuantity()
            );

            productRepository.save(productEntity);

            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setProductId(productEntity.getId());
            cartItemEntity.setName(productEntity.getName());
            cartItemEntity.setPrice(productEntity.getPrice());
            cartItemEntity.setQuantity(itemRequest.getQuantity());

            totalPrice = totalPrice + productEntity.getPrice() * itemRequest.getQuantity();

            cartItems.add(cartItemEntity);
        }

        cartEntity.setCartItems(cartItems);
        cartEntity.setTotalPrice(totalPrice);

        return cartRepository.save(cartEntity);
    }

    public Optional<CartEntity> getCartById(Long id) {
        return cartRepository.findById(id);
    }
}