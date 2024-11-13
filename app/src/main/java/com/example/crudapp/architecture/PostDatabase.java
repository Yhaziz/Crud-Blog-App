// PostDatabase.java
package com.example.crudapp.architecture;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.crudapp.PostModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PostModel.class}, version = 2) // Updated version to 2
public abstract class PostDatabase extends RoomDatabase {

    private static PostDatabase instance;
    private static final String DB_NAME = "post_db";
    public static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public abstract PostDao postDao();

    public static synchronized PostDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PostDatabase.class, DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    // Define migration from version 1 to 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Add the imageUri column with default value as empty string
            database.execSQL("ALTER TABLE post_table ADD COLUMN imageUri TEXT NOT NULL DEFAULT ''");
        }
    };
}
