package com.imronreviady.simplestore.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.imronreviady.simplestore.viewobject.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Query("DELETE FROM Category")
    void deleteAllCategory();

    @Query("DELETE FROM Category WHERE id = :id")
    void deleteCategoryById(String id);

    @Query("SELECT c.* FROM Category c, CategoryMap cm WHERE c.id = cm.categoryId AND cm.mapKey = :value ORDER BY cm.sorting asc")
    LiveData<List<Category>> getCategoryByKeyTest (String value);

    @Query("SELECT c.* FROM Category c, CategoryMap cm WHERE c.id = cm.categoryId AND cm.mapKey = :value ORDER BY cm.sorting asc limit:loadFromDB")
    LiveData<List<Category>> getCategoryByKeyTestByLimit (String value,String loadFromDB);

}