package com.teoxoft.test.TestTaskFromTEOXOFT.controller;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.Product;
import com.teoxoft.test.TestTaskFromTEOXOFT.entity.User;
import com.teoxoft.test.TestTaskFromTEOXOFT.service.ProductService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.teoxoft.test.TestTaskFromTEOXOFT.entity.Role.USER;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${testTaskFromTEOXOFT.pathToImages}")
    private String pathToImages;

    @GetMapping("/products")
    public String products(ModelMap modelMap,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "sort", required = false) String sort,
                           @PageableDefault Pageable pageable) {
        Page<Product> products;
        if (category == null || category.isEmpty()) {
            products = productService.getAllProductsAsPage(pageable);
        } else {
            products = productService.getAllProductsInCategoryAsPage(category, pageable);
        }
        modelMap.addAttribute("products", products);
        modelMap.addAttribute("imagePrefix", pathToImages);
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
    public String editProduct(ModelMap modelMap,
                              @RequestParam(value = "category", required = false) String category,
                              @RequestParam(value = "name", required = false) String name) {
        if ((category == null || category.isEmpty()) && (name == null || name.isEmpty())) {
            modelMap.addAttribute("product", new Product());
        } else if ((category != null && !category.isEmpty()) && (name == null || name.isEmpty())) {
            Product product = new Product();
            product.setCategory(category);
            modelMap.addAttribute("product", product);
        } else {
            modelMap.addAttribute("product", productService.getProductByCategoryAndName(category, name));
        }
        return "editProduct";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editProduct")
    public String addOrEditProduct(@ModelAttribute Product product,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "image", required = false) MultipartFile image) throws UnsupportedEncodingException {
        try {
            if (!image.isEmpty()) {
                validateImage(image);
                saveImage(product.getCategory() + product.getName() + ".jpg", image);
                product.setImage(product.getCategory() + product.getName());
            }
        } catch (RuntimeException e) {
            bindingResult.reject(e.getMessage());
            return "editProduct";
        }
        productService.addProduct(product);
        return "redirect:/products?category=" + URLEncoder.encode(product.getCategory(), "UTF-8");
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/editProduct", params = {"remove", "category", "name"})
    public String removeProduct(@RequestParam("category") String category,
                                @RequestParam("name") String name) throws UnsupportedEncodingException {
        productService.removeProduct(productService.getProductByCategoryAndName(category, name));
        return "redirect:/products?category=" + URLEncoder.encode(category, "UTF-8");
    }

    private void validateImage(MultipartFile image) {
        if (!image.getContentType().equals("image/jpeg")) {
            throw new RuntimeException("Разрешены только изображения формата JPG");
        }
    }

    private void saveImage(String filename, MultipartFile image) throws RuntimeException {
        try {
            File file = new File(pathToImages + filename);
            FileUtils.writeByteArrayToFile(file, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Невозможно сохранить изображение", e);
        }
    }
}