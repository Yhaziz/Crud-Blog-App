package com.example.crudapp.architecture;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.crudapp.PostModel;

import java.util.List;

public class PostRepository {

    private PostDao postDao;
    private LiveData<List<PostModel>> allPosts;

    public PostRepository(Context context) {
        PostDatabase db = PostDatabase.getInstance(context);
        postDao = db.postDao();
        allPosts = postDao.getAll();
    }

    public LiveData<List<PostModel>> getAllPosts() {
        return allPosts;
    }

    public void insert(PostModel post){
        PostDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                postDao.insert(post);
            }
        });
    }

    public void update(PostModel post){
        PostDatabase.databaseWriteExecutor.execute(()->{
            postDao.update(post);
        });
    }

    public void delete(PostModel post){
        PostDatabase.databaseWriteExecutor.execute(()->{
            postDao.delete(post);
        });
    }


}
