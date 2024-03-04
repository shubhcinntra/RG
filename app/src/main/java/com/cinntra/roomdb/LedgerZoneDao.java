package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.rgelectric.model.DataLedgerGroup;
import com.cinntra.rgelectric.model.DataZoneGroup;

import java.util.List;

@Dao
public interface LedgerZoneDao {

   // Getting ALL Data
    @Query("SELECT * FROM ledger_zone")
    List<DataLedgerGroup> getAll();
    @Query("DELETE  FROM ledger_zone")
    void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DataZoneGroup> data);


}
