# BookSwap – Campus Book Exchange Platform

---

**BookSwap** is an Android application designed to facilitate peer-to-peer book exchanges within university campuses. Users can list textbooks or novels for sale or auction, browse available listings, and engage in real-time chats with sellers. The app leverages Firebase services for backend functionalities and Cloudinary for image hosting.

## Table of Contents

---

## ✨ Features

- **User Authentication**: Sign up and log in using Firebase Authentication.
- **Book Listings**: Users can list books for sale or auction, including uploading images via Cloudinary.
- **Real-time Chat**: One-to-one messaging between buyers and sellers using Firebase Realtime Database.
- **Guest Mode**: Allows browsing of home and profile sections without full access.
- **Auction System**: Timer-based auctions with future enhancements planned.
- **Profile Management**: Users can view and manage their profiles.

---

## 🛠 Tech Stack

| Component | Technology |
| --- | --- |
| **Language** | Java |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **UI Framework** | Android XML Layouts |
| **Backend Services** | Firebase Authentication, Firestore, Realtime Database |
| **Image Hosting** | Cloudinary |
| **State Management** | LiveData |
| **Image Loading** | Picasso |

---

## 🏗 System Architecture

The application follows the MVVM architecture pattern:

- **Model**: Represents the data layer, including data classes and Firebase interactions.
- **ViewModel**: Acts as a bridge between the Model and View, exposing LiveData for UI updates.
- **View**: Comprises Activities and Fragments that observe LiveData and render UI accordingly.

**Backend Services**:

- **Firebase Authentication**: Manages user sign-up and login processes.
- **Cloud Firestore**: Stores book listings and auction details.
- **Firebase Realtime Database**: Handles user profiles and messaging data.
- **Cloudinary**: Facilitates image uploads and hosting.

---

## 📁 Folder Structure

```
BookSwap/
├── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   ├── java/com/workshop/bookswap/
│   │   ├── activities/
│   │   │   ├── LoginActivity.java
│   │   │   ├── SignupActivity.java
│   │   │   ├── MainActivity.java
│   │   │   ├── BookDetailsActivity.java
│   │   │   └── ChatActivity.java
│   │   ├── adapters/
│   │   │   ├── BookAdapter.java
│   │   │   ├── FeaturedBookAdapter.java
│   │   │   ├── ChatAdapter.java
│   │   │   └── MessageAdapter.java
│   │   ├── fragments/
│   │   │   ├── HomeFragment.java
│   │   │   ├── SellFragment.java
│   │   │   ├── AuctionFragment.java
│   │   │   ├── MessagesFragment.java
│   │   │   └── ProfileFragment.java
│   │   ├── helpers/
│   │   │   └── CloudinaryHelper.java
│   │   ├── models/
│   │   │   ├── Book.java
│   │   │   ├── User.java
│   │   │   ├── ChatUser.java
│   │   │   ├── Message.java
│   │   │   └── Chat.java
│   │   └── viewmodel/
│   │       └── BookViewModel.java
│   └── res/
│       ├── layout/
│       ├── drawable/
│       ├── values/
│       └── menu/
├── build.gradle
└── settings.gradle

```

---

## 🗃 Data Models

### Book.java

```java
public class Book {
    private String id;
    private String title;
    private String author;
    private double price;
    private String imageUrl;
    private String category;
    private String condition;
    private boolean isFeatured;
    private boolean isAuction;
    private long auctionEndTime;
    private long timestamp;
    // Getters and setters...
}

```

### User.java

```java
public class User {
    private String uid;
    private String name;
    private String rollNumber;
    private String email;
    private String profileImageUrl;
    // Getters and setters...
}

```

### Chat.java

```java
public class Chat {
    private String chatId;
    private List<String> participants;
    // Getters and setters...
}

```

### Message.java

```java
public class Message {
    private String senderId;
    private String text;
    private long timestamp;
    // Getters and setters...
}

```

---

## 🔄 API & Data Flow

### Book Upload Process

1. **User Action**: Fills in book details in `SellFragment` and selects an image.
2. **Image Upload**: `CloudinaryHelper` uploads the image to Cloudinary and retrieves the URL.
3. **Data Storage**: Book details, along with the image URL, are stored in Firestore under the `books` collection.

### Chat Initialization

1. **User Action**: Buyer clicks on a listed book and selects the chat option.
2. **Chat Setup**: `ChatActivity` checks for an existing chat between the buyer and seller in Realtime Database.
3. **Messaging**: If no chat exists, a new chat node is created. Messages are exchanged under `/chats/{senderId}_{receiverId}/messages/`.

### Book Fetching

- `BookViewModel` listens to Firestore collections:
    - **Featured Books**: Filters books where `isFeatured` is `true`.
    - **Recent Books**: Orders books by `timestamp` in descending order.
    - **Auction Books**: Retrieves books from the `auctions` collection.

---

## 👥 User Flow

1. **Authentication**:
    - **Sign Up**: New users register via `SignupActivity`.
    - **Login**: Existing users log in via `LoginActivity`.
    - **Guest Mode**: Users can opt to browse as guests with limited access.
2. **Browsing Books**:
    - **HomeFragment**: Displays featured, recent, and auction books.
    - **BookDetailsActivity**: Provides detailed information about a selected book.
3. **Listing Books**:
    - **SellFragment**: Allows users to list books for sale or auction, including image uploads.
4. **Messaging**:
    - **MessagesFragment**: Shows existing chat threads.
    - **ChatActivity**: Facilitates real-time messaging between buyers and sellers.
5. **Profile Management**:
    - **ProfileFragment**: Displays user information and provides logout functionality.

---

## ⚙️ Setup Instructions

1. **Clone the Repository**:
    
    ```bash
    git clone https://github.com/yourusername/BookSwap.git
    cd BookSwap
    
    ```
    
2. **Firebase Configuration**:
    - Create a Firebase project.
    - Enable **Authentication**, **Cloud Firestore**, and **Realtime Database**.
    - Download the `google-services.json` file and place it in the `app/` directory.
3. **Cloudinary Setup**:
    - Sign up at [Cloudinary](https://cloudinary.com/).
    - Obtain your **Cloud Name**, **API Key**, and **API Secret**.
    - Configure these credentials in `CloudinaryHelper.java`.
4. **Build the Project**:
    - Open the project in Android Studio.
    - Sync Gradle and build the project.
5. **Run the App**:
    - Deploy the app to an emulator or physical device running Android 6.0 or higher.

---

## 🤝 Contributing

We welcome contributions from the community!

1. **Fork the Repository**:
    
    Click on the "Fork" button at the top right of this page.
    
2. **Create a Feature Branch**:
    
    ```bash
    git checkout -b feature/YourFeatureName
    
    ```
    
3. **Commit Your Changes**:
    
    ```bash
    git commit -m "Add YourFeatureName"
    
    ```
    
4. **Push to Your Fork**:
    
    ```bash
    git push origin feature/YourFeatureName
    
    ```
    
5. **Create a Pull Request**:
    
    Submit a pull request detailing your changes for review.
    

**Coding Standards**:

- Follow Java naming conventions.
- Ensure code is well-documented.
- Maintain the MVVM architecture pattern.

---

## 📄 License

This project is licensed under the [MIT License](https://www.notion.so/LICENSE).

---

*Happy swapping! 📚*

---