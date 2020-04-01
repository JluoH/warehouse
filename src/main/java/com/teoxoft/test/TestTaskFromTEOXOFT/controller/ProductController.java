package com.teoxoft.test.TestTaskFromTEOXOFT.controller;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Product;
import com.teoxoft.test.TestTaskFromTEOXOFT.entity.User;
import com.teoxoft.test.TestTaskFromTEOXOFT.service.CategoryService;
import com.teoxoft.test.TestTaskFromTEOXOFT.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.teoxoft.test.TestTaskFromTEOXOFT.entity.Role.USER;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Value("${testTaskFromTEOXOFT.pathToImages}")
    private String imagesDir;

    @GetMapping("/products")
    public String products(ModelMap modelMap,
                           @RequestParam(value = "category", defaultValue = "") String category,
                           @RequestParam(value = "sort", defaultValue = "") String sort,
                           @PageableDefault Pageable pageable) {
        Page<Product> products;
        if (category.isEmpty()) {
            products = productService.getAllProductsAsPage(pageable);
        } else {
            products = productService.getAllProductsInCategoryAsPage(category, pageable);
        }
        modelMap.addAttribute("products", products);
        modelMap.addAttribute("imagePrefix", imagesDir);
        modelMap.addAttribute("categoryParameter", category);
        modelMap.addAttribute("currentPage", pageable.getPageNumber());
        modelMap.addAttribute("currentSort", sort);
        modelMap.addAttribute("pageSize", products.getSize());
        if (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().equals(USER))
            modelMap.addAttribute("regularUser", true);
        return "products";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/editProduct", params = "edit")
    public String addOrEditProduct(ModelMap modelMap,
                              @RequestParam(value = "name", defaultValue = "") String name,
                              @RequestParam(value = "category", defaultValue = "") String category) {
        if (category.isEmpty() && name.isEmpty()) {
            modelMap.addAttribute("product", new Product());
        } else if (!category.isEmpty() && name.isEmpty()) {
            Product product = new Product();
            product.setCategory(category);
            modelMap.addAttribute("product", product);
        } else {
            modelMap.addAttribute("product", productService.getProductByCategoryAndName(category, name));
        }
        modelMap.addAttribute("categoryList", categoryService.getAllCategories());
        return "editProduct";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editProduct")
    public String addOrEditProduct(@ModelAttribute Product product,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws UnsupportedEncodingException {
        String[] categories = product.getCategory().split(",");
        String[] products = product.getName().split(",");
        String newCategory = categories[0];
        String newProduct = products[0];
        String oldCategory = "";
        product.setCategory(newCategory);
        if (categories.length > 1) {
            oldCategory = categories[1];
        }
        String oldProduct = "";
        if (products.length > 1) {
            product.setName(newProduct);
            oldProduct = products[1];
        }
        if (!oldProduct.isEmpty() && !oldCategory.isEmpty()
                && (!oldCategory.equals(newCategory) || !oldProduct.equals(newProduct))) {
            productService.updateCategoryAndNameOfProduct(newCategory, oldCategory, newProduct, oldProduct);
        }
        try {
            if (!imageFile.isEmpty()) {
                if (!imageFile.getContentType().equals("image/jpeg")) {
                    throw new RuntimeException("Разрешены только изображения формата JPG");
                }
                try {
                    Path path = Paths.get("src/main/resources/static" + imagesDir + newCategory + newProduct + ".jpg");
                    if (Files.notExists(path.getParent())) {
                        Files.createDirectories(path.getParent());
                    }
                    Files.write(path, imageFile.getBytes(), StandardOpenOption.CREATE);
                } catch (IOException e) {
                    throw new RuntimeException("Невозможно сохранить изображение", e);
                }
                product.setImage(newCategory + newProduct);
            }
        } catch (RuntimeException e) {
            bindingResult.reject(e.getMessage());
            return "/editProduct";
        }
        productService.addProduct(product);
        return "redirect:/products?category=" + URLEncoder.encode(newCategory, "UTF-8");
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/editProduct", params = {"remove", "name", "category"})
    public String removeProduct(@RequestParam("name") String name,
                                @RequestParam("category") String category) throws UnsupportedEncodingException {
        productService.removeProduct(productService.getProductByCategoryAndName(category, name));
        return "redirect:/products?category=" + URLEncoder.encode(category, "UTF-8");
    }
}