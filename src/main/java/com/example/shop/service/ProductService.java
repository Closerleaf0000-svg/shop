package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    // 新增商品
    public void addProduct(Product product) {

        productMapper.addProduct(product);
    }

    // 查詢所有有庫存的商品
    public List<Product> findAll() {

        return productMapper.findAll();
    }

    // 商品名稱搜尋
    public List<Product> search(String keyword) {

        return productMapper.search(keyword);
    }

    // 查詢單一商品
    public Product findByProductId(
            String productId) {

        return productMapper.findByProductId(
                productId);
    }

    // 扣除商品庫存
    public void decreaseQuantity(
            String productId,
            Integer quantity) {

        productMapper.decreaseQuantity(
                productId,
                quantity);
    }
}