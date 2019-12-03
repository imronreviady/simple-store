package com.imronreviady.simplestore.db;


import com.imronreviady.simplestore.viewobject.Shop;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Shop> shops);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Shop shop);

    @Query("DELETE FROM SHOP")
    void deleteAll();

    @Query("SELECT * FROM SHOP")
    LiveData<Shop> getShopById();

    @Query("DELETE FROM SHOP WHERE id = :id")
    void deleteShopById(String id);

}
