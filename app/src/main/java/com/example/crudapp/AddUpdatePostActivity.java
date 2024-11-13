// AddUpdatePostActivity.java
package com.example.crudapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.crudapp.architecture.PostViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUpdatePostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;

    EditText titleEditText, descEditText, authorEditText;
    Button submitPostBtn, selectImageBtn;
    ImageView selectedImageView;
    private PostViewModel postViewModel;
    private Uri selectedImageUri = null;

    // ActivityResultLauncher for selecting image
    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            selectedImageView.setImageURI(selectedImageUri);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        // Initialize ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Find UI elements
        titleEditText = findViewById(R.id.title_edit_text);
        descEditText = findViewById(R.id.desc_edit_text);
        authorEditText = findViewById(R.id.author_edit_text);
        submitPostBtn = findViewById(R.id.submit_post_btn);
        selectImageBtn = findViewById(R.id.select_image_btn);
        selectedImageView = findViewById(R.id.selected_image_view);

        // Handle Intent for Edit action
        Intent intent = getIntent();
        String actionType = intent.getStringExtra(Constants.ACTION_TYPE);

        if (Constants.ACTION_TYPE_EDIT.equals(actionType)) {
            titleEditText.setText(intent.getStringExtra(Constants.POST_TITLE));
            descEditText.setText(intent.getStringExtra(Constants.POST_DESC));
            authorEditText.setText(intent.getStringExtra(Constants.POST_AUTHOR));
            String imageUriString = intent.getStringExtra(Constants.POST_IMAGE_URI);
            if (imageUriString != null && !imageUriString.isEmpty()) {
                selectedImageUri = Uri.parse(imageUriString);
                selectedImageView.setImageURI(selectedImageUri);
            }
        }

        // Select Image Button Click Listener
        selectImageBtn.setOnClickListener(view -> {
            if (hasImagePermission()) {
                openImageSelector();
            } else {
                requestImagePermission();
            }
        });

        // Submit Button Click Listener
        submitPostBtn.setOnClickListener(view -> {

            String title = titleEditText.getText().toString().trim();
            String desc = descEditText.getText().toString().trim();
            String author = authorEditText.getText().toString().trim();
            String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : "";

            String curDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

            PostModel post;
            if (Constants.ACTION_TYPE_ADD.equals(actionType)) {
                post = new PostModel(title, desc, author, curDate, imageUriString);
            } else {
                int id = intent.getIntExtra(Constants.POST_ID, -1);
                post = new PostModel(title, desc, author, curDate, imageUriString,
                        intent.getIntExtra(Constants.POST_UPVOTE_COUNT, 0),
                        intent.getIntExtra(Constants.POST_DOWNVOTE_COUNT, 0));
                post.setId(id);
            }

            String result;
            if (Constants.ACTION_TYPE_ADD.equals(actionType)) {
                result = postViewModel.insert(post);
            } else {
                result = postViewModel.update(post);
            }

            if (Constants.SUCCESS.equals(result)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Check if the app has the necessary image permissions
    private boolean hasImagePermission() {
        if (Build.VERSION.SDK_INT >= 33) { // Android 13 and above
            return ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES")
                    == PackageManager.PERMISSION_GRANTED;
        } else { // Below Android 13
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Request the necessary image permissions
    private void requestImagePermission() {
        if (Build.VERSION.SDK_INT >= 33) { // Android 13 and above
            requestPermissions(new String[]{"android.permission.READ_MEDIA_IMAGES"},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else { // Below Android 13
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    // Open Image Selector
    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    // Handle Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0) {
                boolean granted;
                if (Build.VERSION.SDK_INT >= 33) { // Android 13 and above
                    granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                } else { // Below Android 13
                    granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                }

                if (granted) {
                    openImageSelector();
                } else {
                    Toast.makeText(this, "Permission denied to access images", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission request was cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
