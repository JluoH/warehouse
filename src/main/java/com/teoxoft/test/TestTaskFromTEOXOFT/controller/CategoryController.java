package com.teoxoft.test.TestTaskFromTEOXOFT.controller;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Category;
import com.teoxoft.test.TestTaskFromTEOXOFT.entity.User;
import com.teoxoft.test.TestTaskFromTEOXOFT.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.teoxoft.test.TestTaskFromTEOXOFT.entity.Role.USER;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String categories() {
        return "redirect:/categories";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categoryList", categoryService.getAllCategories());
        model.addAttribute("addCategory", false);
        if (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().equals(USER)) {
            model.addAttribute("regularUser", true);
        }
        return "categories";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/categories", params = "addCategory")
    public String addCategory(Model model) {
        model.addAttribute("addCategory", true);
        model.addAttribute("categoryList", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category());
        return "categories";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/categories", params = "edit")
    public String editCategory(Model model,
                               @RequestParam("edit") String editableCategory) {
        model.addAttribute("editableCategory", editableCategory);
        model.addAttribute("categoryList", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category());
        return "categories";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/categories")
    public String addOrEditCategory(@RequestParam(value = "edit", required = false) String editableCategory,
                                    @ModelAttribute Category newCategory,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "categories";
        }
        if (editableCategory != null && !editableCategory.isEmpty()) {
            categoryService.updateCategoryName(newCategory.getName(), editableCategory);
        }
        categoryService.addCategory(newCategory);
        return "redirect:/categories";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/categories", params = "remove")
    public String removeCategory(@RequestParam("remove") String removableCategory) {
        Category category = new Category();
        category.setName(removableCategory);
        categoryService.removeCategory(category);
        return "redirect:/categories";
    }
}