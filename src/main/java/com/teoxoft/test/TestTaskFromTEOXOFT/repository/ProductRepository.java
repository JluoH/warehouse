package com.teoxoft.test.TestTaskFromTEOXOFT.repository;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Product;
import com.teoxoft.test.TestTaskFromTEOXOFT.entity.ProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductId> {
    Page<Product> findByProductIdCategory(String category, Pageable pageable);

    int countByProductIdCategory(String category);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Product SET productId.category = ?1, productId.name = ?3 WHERE productId.category = ?2 AND productId.name =?4")
    void updateCategoryAndNameOfProduct(String newCategory, String oldCategory, String newName, String oldName);
}
