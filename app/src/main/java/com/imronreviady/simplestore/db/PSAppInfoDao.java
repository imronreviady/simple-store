package com.imronreviady.simplestore.db;

import com.imronreviady.simplestore.viewobject.PSAppInfo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PSAppInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PSAppInfo PSAppInfo);

    @Query("DELETE FROM PSAppInfo")
    void deleteAll();
}
