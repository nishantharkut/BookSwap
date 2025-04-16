package com.workshop.bookswap;

public class HelperClass {
    String name, roll_number, email, password;

    // Default constructor (needed for Firebase)
    public HelperClass() {
    }

    // Constructor matching the correct order
    public HelperClass(String name, String roll_number, String email, String password) {
        this.name = name;
        this.roll_number = roll_number;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoll_number() { return roll_number; }
    public void setRoll_number(String roll_number) { this.roll_number = roll_number; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


}
