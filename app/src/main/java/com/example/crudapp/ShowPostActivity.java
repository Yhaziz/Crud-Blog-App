// ShowPostActivity.java
package com.example.crudapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.bumptech.glide.Glide;
import com.example.crudapp.architecture.PostViewModel;

public class ShowPostActivity extends AppCompatActivity {

    TextView postTitleView, postDescView, postAuthorView, postDateView, upvoteCountView, downvoteCountView;
    ImageView postImageView; // New ImageView
    Button deleteBtn, editBtn;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        // Find UI elements
        postTitleView = findViewById(R.id.title_text_view);
        postDescView = findViewById(R.id.desc_text_view);
        postAuthorView = findViewById(R.id.author_text_view);
        postDateView = findViewById(R.id.date_text_view);
        upvoteCountView = findViewById(R.id.upvote_count_view);
        downvoteCountView = findViewById(R.id.downvote_count_view);
        postImageView = findViewById(R.id.post_image_view); // Initialize ImageView
        deleteBtn = findViewById(R.id.delete_btn);
        editBtn = findViewById(R.id.edit_btn);

        // Make text views scrollable if needed
        postDescView.setMovementMethod(new ScrollingMovementMethod());
        postTitleView.setMovementMethod(new ScrollingMovementMethod());

        // Initialize ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Get data from Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.POST_TITLE);
        String desc = intent.getStringExtra(Constants.POST_DESC);
        String author = intent.getStringExtra(Constants.POST_AUTHOR);
        String date = intent.getStringExtra(Constants.POST_DATE);
        int upvotes = intent.getIntExtra(Constants.POST_UPVOTE_COUNT, 0);
        int downvotes = intent.getIntExtra(Constants.POST_DOWNVOTE_COUNT, 0);
        String imageUriString = intent.getStringExtra(Constants.POST_IMAGE_URI);

        // Set data to views
        postTitleView.setText(title);
        postDescView.setText(desc);
        postAuthorView.setText(author);
        postDateView.setText(date);
        upvoteCountView.setText(String.valueOf(upvotes));
        downvoteCountView.setText(String.valueOf(downvotes));

        // Load image using Glide
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.placeholder_image) // Ensure placeholder_image exists
                    .error(R.drawable.error_image) // Ensure error_image exists
                    .into(postImageView);
        } else {
            // Set a placeholder image if no image is available
            postImageView.setImageResource(R.drawable.placeholder_image); // Ensure placeholder_image exists
        }

        int id = intent.getIntExtra(Constants.POST_ID, -1);

        // Edit Button Click Listener
        editBtn.setOnClickListener(view -> {

            Intent editIntent = new Intent(this, AddUpdatePostActivity.class);
            editIntent.putExtra(Constants.ACTION_TYPE, Constants.ACTION_TYPE_EDIT);
            editIntent.putExtra(Constants.POST_ID, id);
            editIntent.putExtra(Constants.POST_TITLE, title);
            editIntent.putExtra(Constants.POST_DESC, desc);
            editIntent.putExtra(Constants.POST_AUTHOR, author);
            editIntent.putExtra(Constants.POST_DATE, date);
            editIntent.putExtra(Constants.POST_UPVOTE_COUNT, upvotes);
            editIntent.putExtra(Constants.POST_DOWNVOTE_COUNT, downvotes);
            editIntent.putExtra(Constants.POST_IMAGE_URI, imageUriString); // Pass image URI

            startActivity(editIntent);
            finish();

        });

        // Delete Button Click Listener
        deleteBtn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete this post?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                PostModel post = new PostModel(title, desc, author, date, imageUriString, upvotes, downvotes);
                post.setId(id);
                postViewModel.delete(post);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Do nothing
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });

    }
}
