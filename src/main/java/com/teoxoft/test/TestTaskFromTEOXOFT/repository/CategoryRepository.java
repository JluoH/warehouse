package com.teoxoft.test.TestTaskFromTEOXOFT.repository;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE Category SET name = ?1 WHERE name = ?2")
    void updateCategoryName(String newName, String oldName);
}