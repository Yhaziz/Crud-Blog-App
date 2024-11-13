// PostListAdapter.java
package com.example.crudapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crudapp.architecture.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private List<PostModel> postData = new ArrayList<>();
    private Context context;
    private PostViewModel postViewModel;

    public void updatePostList(List<PostModel> postData) {
        this.postData.clear();
        this.postData.addAll(postData);
        notifyDataSetChanged();
    }

    public PostListAdapter(PostViewModel postViewModel) {
        this.postViewModel = postViewModel;
    }

    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.ViewHolder holder, int position) {
        final PostModel post = postData.get(holder.getAdapterPosition());
        holder.postTitle.setText(post.getTitle());
        holder.postDesc.setText(post.getDescription());
        holder.upvote_count.setText(String.valueOf(post.getUpvote()));
        holder.downvote_count.setText(String.valueOf(post.getDownvote()));

        // Load image using Glide
        if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
            Uri imageUri = Uri.parse(post.getImageUri());
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.placeholder_image) // Ensure placeholder_image exists
                    .error(R.drawable.error_image) // Ensure error_image exists
                    .into(holder.postImage);
        } else {
            // Set a placeholder image if no image is available
            holder.postImage.setImageResource(R.drawable.placeholder_image);
        }

        holder.showPostButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowPostActivity.class);
            intent.putExtra(Constants.POST_ID, post.getId());
            intent.putExtra(Constants.POST_TITLE, post.getTitle());
            intent.putExtra(Constants.POST_DESC, post.getDescription());
            intent.putExtra(Constants.POST_AUTHOR, post.getAuthor());
            intent.putExtra(Constants.POST_DATE, post.getDate());
            intent.putExtra(Constants.POST_UPVOTE_COUNT, post.getUpvote());
            intent.putExtra(Constants.POST_DOWNVOTE_COUNT, post.getDownvote());
            intent.putExtra(Constants.POST_IMAGE_URI, post.getImageUri()); // Pass image URI
            context.startActivity(intent);
        });

        holder.upvote_btn.setOnClickListener(view -> {
            post.setUpvote(post.getUpvote() + 1);
            postViewModel.update(post);
        });

        holder.downvote_btn.setOnClickListener(view -> {
            post.setDownvote(post.getDownvote() + 1);
            postViewModel.update(post);
        });

    }

    @Override
    public int getItemCount() {
        return postData != null ? postData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postTitle, postDesc, upvote_count, downvote_count;
        public ImageView postImage; // New ImageView
        public Button showPostButton;
        public ImageButton upvote_btn, downvote_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.postTitle = itemView.findViewById(R.id.post_title);
            this.postDesc = itemView.findViewById(R.id.post_desc);
            this.postImage = itemView.findViewById(R.id.post_item_image); // Initialize ImageView
            this.showPostButton = itemView.findViewById(R.id.show_post_btn);
            this.upvote_btn = itemView.findViewById(R.id.upvote_btn);
            this.downvote_btn = itemView.findViewById(R.id.downvote_btn);
            this.upvote_count = itemView.findViewById(R.id.upvote_count);
            this.downvote_count = itemView.findViewById(R.id.downvote_count);
        }
    }

}
