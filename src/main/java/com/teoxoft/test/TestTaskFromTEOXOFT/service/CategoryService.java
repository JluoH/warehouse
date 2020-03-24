package com.teoxoft.test.TestTaskFromTEOXOFT.service;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Category;
import com.teoxoft.test.TestTaskFromTEOXOFT.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void removeCategory(Category category) {
        categoryRepository.delete(category);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        categoryList.forEach(category ->
                category.setCountOfProducts(productService.getAmountOfProductsInCategory(category.getName())));
        return categoryList;
    }

}
