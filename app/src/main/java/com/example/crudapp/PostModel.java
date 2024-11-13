// PostModel.java
package com.example.crudapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class PostModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String author;
    private String date;
    private int upvote;
    private int downvote;
    private String imageUri; // New field for image URI

    // Constructor with all fields
    public PostModel(String title, String description, String author, String date, String imageUri, int upvote, int downvote) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.imageUri = imageUri;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    // Constructor without upvote and downvote (ignored by Room)
    @Ignore
    public PostModel(String title, String description, String author, String date, String imageUri) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.imageUri = imageUri;
    }

    // Getters and Setters
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { // Added setter for id
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
}
