package com.workshop.bookswap.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
    private String rollNumber;
    private String university;

    public User() {} // Needed for Firebase

    public User(String id, String name, String email, String profileImageUrl, String rollNumber, String university) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.rollNumber = rollNumber;
        this.university = university;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getRollNumber() { return rollNumber; }
    public String getUniversity() { return university; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public void setUniversity(String university) { this.university = university; }
}
