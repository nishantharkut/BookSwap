//package com.workshop.bookswap.models;
//
//public class Book {
//    private String id;
//    private String title;
//    private String author;
//    private Double price;
//    private String imageUrl;
//    private String category;
//    private long timestamp;
//    private String condition;
//    private boolean isAuction;
//    private long auctionEndTime;
//    private boolean isFeatured;
//
//    // New fields
//    private String sellerId;
//    private String sellerName;
//    private boolean isSold;
//
//    // Empty constructor (needed for Firebase)
//    public Book() {}
//
//    public Book(String id, String title, String author, double price, String imageUrl, String category,
//                String condition, boolean isAuction, long auctionEndTime, long timestamp) {
//        this.id = id;
//        this.title = title;
//        this.author = author;
//        this.price = price;
//        this.imageUrl = imageUrl;
//        this.category = category;
//        this.condition = condition;
//        this.isAuction = isAuction;
//        this.auctionEndTime = auctionEndTime;
//        this.timestamp = timestamp;
//    }
//
//    // Getters
//    public String getId() { return id; }
//    public String getTitle() { return title; }
//    public String getAuthor() { return author; }
//    public double getPrice() { return price; }
//    public String getImageUrl() { return imageUrl; }
//    public String getCategory() { return category; }
//    public String getCondition() { return condition; }
//    public boolean isAuction() { return isAuction; }
//    public long getTimestamp() { return timestamp; }
//    public long getAuctionEndTime() { return auctionEndTime; }
//    public boolean isFeatured() { return isFeatured; }
//    public String getSellerId() { return sellerId; }
//    public String getSellerName() { return sellerName; }
//    public boolean isSold() { return isSold; }
//
//    // Setters
//    public void setFeatured(boolean isFeatured) { this.isFeatured = isFeatured; }
//    public void setId(String id) { this.id = id; }
//    public void setTitle(String title) { this.title = title; }
//    public void setAuthor(String author) { this.author = author; }
//    public void setPrice(double price) { this.price = price; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//    public void setCategory(String category) { this.category = category; }
//    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
//    public void setCondition(String condition) { this.condition = condition; }
//    public void setAuction(boolean auction) { isAuction = auction; }
//    public void setAuctionEndTime(long auctionEndTime) { this.auctionEndTime = auctionEndTime; }
//    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
//    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
//    public void setSold(boolean sold) { isSold = sold; }
//}

package com.workshop.bookswap.models;
import com.google.firebase.Timestamp;
public class Book {
    private String id;
    private String title;
    private String author;
    private Double price;
    private String imageUrl;
    private String category;
    private Timestamp timestamp;
    private String condition;

    private boolean isAuction;
    private long auctionEndTime;
    private boolean featured;
    private boolean isFeatured;
    private String sellerId;
    private String sellerName;
    private boolean isSold;

    // Empty constructor (needed for Firebase)
    public Book() {}

    // Full constructor
    public Book(String id, String title, String author, double price, String imageUrl, String category,
                String condition, boolean isAuction, long auctionEndTime, Timestamp timestamp,
                boolean isFeatured, boolean featured, String sellerId, String sellerName, boolean isSold) {
        this.id = id;
        this.featured = featured;
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.condition = condition;
        this.isAuction = isAuction;
        this.auctionEndTime = auctionEndTime;
        this.timestamp = timestamp;
        this.featured = isFeatured;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.isSold = isSold;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public String getCondition() { return condition; }
    public boolean isAuction() { return isAuction; }
    public Timestamp getTimestamp() { return timestamp; }
    public long getAuctionEndTime() { return auctionEndTime; }
    public boolean isFeatured() { return featured; }
    public boolean featured() {return isFeatured; }
    public String getSellerId() { return sellerId; }
    public String getSellerName() { return sellerName; }
    public boolean isSold() { return isSold; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsFeatured(boolean isFeatured) {this.isFeatured = isFeatured;}
    public void setPrice(double price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setAuction(boolean isAuction) { this.isAuction = isAuction; }
    public void setAuctionEndTime(long auctionEndTime) { this.auctionEndTime = auctionEndTime; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public void setSold(boolean isSold) { this.isSold = isSold; }
}
