package com.example.riji.Month_related;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//since all database classes are very similar, refer to BulletPoint folder for commenting references. Method names should be self explanatory
@Dao
public interface MonthDAO {
    @Insert
    void insertMonth(Month month);

    @Update
    void updateMonth(Month month);

    @Delete
    void deleteMonth(Month month);

    @Query("DELETE FROM months")
    void deleteAll();

    @Query("SELECT * FROM months ORDER BY id")
    LiveData<List<Month>> getAllMonths();

    @Query("SELECT * FROM months WHERE year=:year ORDER BY id")
    LiveData<List<Month>> findMonthInYear(final int year);

    @Query("SELECT * FROM months WHERE year=:year AND month=:month ORDER BY id")
    LiveData<Month> findSpecificMonth(final int year, final int month);

    @Query("SELECT * FROM months WHERE year=:year AND month=:month ORDER BY id")
    Month findSpecificMonthNoLive(final int year, final int month);

    @Query("SELECT id FROM months WHERE year=:year AND month=:month ORDER BY id")
    int getMonthId(final int year, final int month);

}
