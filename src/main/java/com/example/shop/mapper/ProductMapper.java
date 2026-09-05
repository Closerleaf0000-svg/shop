package com.example.shop.mapper;

import com.example.shop.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

        // 新增商品
        void addProduct(Product product);

        // 查詢所有有庫存的商品
        List<Product> findAll();

        // 商品名稱搜尋
        List<Product> search(
                        @Param("keyword") String keyword);

        // 查詢單一商品
        Product findByProductId(
                        @Param("productId") String productId);

        // 扣除商品庫存
        void decreaseQuantity(
                        @Param("productId") String productId,
                        @Param("quantity") Integer quantity);
}