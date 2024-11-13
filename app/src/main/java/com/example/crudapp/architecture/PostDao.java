package com.example.crudapp.architecture;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.crudapp.PostModel;

import java.util.List;

@Dao
public interface PostDao {

    @Query("Select * FROM post_table")
    LiveData<List<PostModel>> getAll();

    @Insert
    void insert(PostModel post);

    @Delete
    void delete(PostModel post);

    @Update
    void update(PostModel post);

}
