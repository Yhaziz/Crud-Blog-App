package com.example.crudapp.architecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.crudapp.Constants;
import com.example.crudapp.PostModel;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository postRepository;
    private LiveData<List<PostModel>> allPosts;

    // Constructor for the ViewModel
    public PostViewModel(@NonNull Application application) {
        super(application);
        // Create the repository and get all the posts
        this.postRepository = new PostRepository(application);
        this.allPosts = postRepository.getAllPosts();
    }

    // Insert a new post
    public String insert(PostModel post) {
        // Validate the post and return an error message if it fails
        String res = validatePost(post);
        if (!res.equals(Constants.SUCCESS))
            return res;

        // Insert the post using the repository
        postRepository.insert(post);
        return Constants.SUCCESS;
    }

    // Update an existing post
    public String update(PostModel post) {
        // Validate the post and return an error message if it fails
        String res = validatePost(post);
        if (!res.equals(Constants.SUCCESS))
            return res;

        // Update the post using the repository
        postRepository.update(post);
        return Constants.SUCCESS;
    }

    // Delete a post
    public void delete(PostModel post) {
        // Delete the post using the repository
        postRepository.delete(post);
    }

    // Get all posts
    public LiveData<List<PostModel>> getAllPosts() {
        return allPosts;
    }

    // Validate a post and return an error message if it fails
    public String validatePost(PostModel post) {
        if (post.getTitle().isEmpty()) {
            return "Le titre du blog ne peut pas être vide.";
        }
        if (post.getDescription().isEmpty()) {
            return "La description du blog ne peut pas être vide .";
        }
        if (post.getAuthor().isEmpty()) {
            return "L'auteur du blog ne peut pas être vide.";
        }
        if (post.getTitle().length() >= 100) {
            return "La longueur du titre du blog doit être inférieure à 10 caractères";
        }
        if (post.getAuthor().length() >= 100) {
            return "Le nom de l'auteur du blog doit comporter moins de 8 caractères";
        }

        // Return success if validation passes
        return Constants.SUCCESS;
    }

}
