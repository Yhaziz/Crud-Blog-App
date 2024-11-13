package com.example.crudapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudapp.architecture.PostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PostViewModel postViewModel;
    private FloatingActionButton addPostBtn, searchBtn;
    private EditText searchInput;
    private PostListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        addPostBtn = findViewById(R.id.fab_add_post_btn);
        searchBtn = findViewById(R.id.fab_search);
        searchInput = findViewById(R.id.search_input);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        adapter = new PostListAdapter(postViewModel);

        postViewModel.getAllPosts().observe(this, posts -> adapter.updatePostList(posts));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Toggle visibility of search input when search button is clicked
        searchBtn.setOnClickListener(view -> {
            if (searchInput.getVisibility() == View.GONE) {
                searchInput.setVisibility(View.VISIBLE);
            } else {
                searchInput.setVisibility(View.GONE);
                adapter.updatePostList(postViewModel.getAllPosts().getValue());
            }
        });

        // Filter posts when text changes in search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterPosts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        addPostBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddUpdatePostActivity.class);
            intent.putExtra(Constants.ACTION_TYPE, Constants.ACTION_TYPE_ADD);
            startActivity(intent);
        });
    }

    private void filterPosts(String query) {
        List<PostModel> filteredList = new ArrayList<>();
        if (postViewModel.getAllPosts().getValue() != null) {
            for (PostModel post : postViewModel.getAllPosts().getValue()) {
                if (post.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(post);
                }
            }
        }
        adapter.updatePostList(filteredList);
    }
}
