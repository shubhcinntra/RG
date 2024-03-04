package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.rgelectric.model.DataItemInSalesCard;

import java.util.List;

@Dao
public interface ItemInSalesCardDao {

   // Getting ALL Data
    @Query("SELECT * FROM item_list_in_sales")
    List<DataItemInSalesCard> getAll();
    @Query("DELETE  FROM item_list_in_sales")
    void deleteAll();

  //Inserting All Data At once

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DataItemInSalesCard> data);


}
