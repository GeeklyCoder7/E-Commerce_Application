package com.example.e_commerceapp.models;

public class CategoryModel {
    String categoryIconImage, categoryName, categoryId;

    public CategoryModel(String categoryIconImage, String categoryName, String categoryId) {
        this.categoryIconImage = categoryIconImage;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public CategoryModel() {

    }

    public String getCategoryIconImage() {
        return categoryIconImage;
    }

    public void setCategoryIconImage(String categoryIconImage) {
        this.categoryIconImage = categoryIconImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
