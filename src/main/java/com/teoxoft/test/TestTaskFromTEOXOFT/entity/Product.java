package com.teoxoft.test.TestTaskFromTEOXOFT.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    @EmbeddedId
    private ProductId productId;
    private String image;
    private String description;
    private float price;
    private int amount;

    public Product() {
        this.productId = new ProductId();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) description = "";
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return productId.getName();
    }

    public void setName(String name) {
        this.productId.setName(name);
    }

    public String getCategory() {
        return productId.getCategory();
    }

    public void setCategory(String category) {
        this.productId.setCategory(category);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
