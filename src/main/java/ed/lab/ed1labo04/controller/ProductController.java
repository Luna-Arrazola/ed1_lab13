package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CreateProductRequest;
import ed.lab.ed1labo04.model.UpdateProductRequest;
import ed.lab.ed1labo04.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(
            @RequestBody CreateProductRequest createProductRequest) {

        try {
            ProductEntity productEntity =
                    productService.createProduct(createProductRequest);

            return ResponseEntity.status(201).body(productEntity);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProduct(
            @PathVariable long id) {

        Optional<ProductEntity> productEntity =
                productService.getProductById(id);

        if (productEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productEntity.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(
            @PathVariable long id,
            @RequestBody UpdateProductRequest updateProductRequest) {

        try {

            ProductEntity productEntity =
                    productService.updateProduct(id, updateProductRequest);

            return ResponseEntity.ok(productEntity);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest().build();
        }
    }
}