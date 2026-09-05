package com.example.shop.controller;

import com.example.shop.dto.ProductRequest;
import com.example.shop.entity.Product;
import com.example.shop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 查詢所有有庫存的商品
    @GetMapping("/list")
    public List<Product> getProducts() {

        return productService.findAll();
    }

    // 商品名稱搜尋
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam String keyword) {

        return productService.search(keyword);
    }

    // 查詢單一商品
    @GetMapping("/{id}")
    public Product getProductById(
            @PathVariable String id) {

        return productService.findByProductId(id);
    }

    // 新增商品只有 RULER 可以使用
    @PostMapping
    public String addProduct(
            @Valid @RequestBody ProductRequest request,
            HttpSession session) {

        // 取得目前登入者的角色
        String role = (String) session.getAttribute("role");

        // 沒登入或不是 RULER
        if (!"RULER".equals(role)) {

            return "沒有權限新增商品";
        }

        // DTO → Entity
        Product product = new Product();
        product.setProductId(request.getProductId());
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        // RULER 才可以新增
        productService.addProduct(product);

        return "商品新增成功";
    }
}