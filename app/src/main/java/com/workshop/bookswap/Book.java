package com.workshop.bookswap;

public class Book {
    private String title, imageUrl;
    private int price;

    public Book() {
        // Required empty constructor
    }

    public Book(String title, String imageUrl, int price) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }
}
