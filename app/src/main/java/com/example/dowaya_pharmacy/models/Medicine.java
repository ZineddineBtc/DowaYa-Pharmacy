package com.example.dowaya_pharmacy.models;

public class Medicine {
    private String id, name, dose, photo, priceRange, description,
            searchHistoryTime, createdHistoryTime;
    private boolean isAvailable;

    public Medicine(){}

    public Medicine(String name) {
        this.name = name;
    }

    public Medicine(String name, String description, String priceRange) {
        this.name = name;
        this.description = description;
        this.priceRange = priceRange;
    }

    public Medicine(String id, String name, String description, String priceRange) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceRange = priceRange;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPrice(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchHistoryTime() {
        return searchHistoryTime;
    }

    public void setSearchHistoryTime(String searchHistoryTime) {
        this.searchHistoryTime = searchHistoryTime;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getCreatedHistoryTime() {
        return createdHistoryTime;
    }

    public void setCreatedHistoryTime(String createdHistoryTime) {
        this.createdHistoryTime = createdHistoryTime;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
