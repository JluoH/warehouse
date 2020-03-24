package com.teoxoft.test.TestTaskFromTEOXOFT.service;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Product;
import com.teoxoft.test.TestTaskFromTEOXOFT.entity.ProductId;
import com.teoxoft.test.TestTaskFromTEOXOFT.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProduct(Product product) {
        productRepository.delete(product);
    }

    public int getAmountOfProductsInCategory(String categoryName) {
        return productRepository.countByProductIdCategory(categoryName);
    }

    public Page<Product> getAllProductsAsPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getAllProductsInCategoryAsPage(String category, Pageable pageable) {
        return productRepository.findByProductIdCategory(category, pageable);
    }

    public Product getProductByCategoryAndName(String category, String name) {
        return productRepository.findById(new ProductId(category, name)).get();
    }
}
