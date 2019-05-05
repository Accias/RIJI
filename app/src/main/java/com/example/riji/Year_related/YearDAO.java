package com.example.riji.Year_related;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface YearDAO {
    @Insert
    void insertYear(Year year);

    @Update
    void updateYear(Year year);

    @Delete
    void deleteYear(Year year);

    @Query("DELETE FROM years")
    void deleteAll();

    @Query("SELECT * FROM years ORDER BY id")
    LiveData<List<Year>> getAllYears();

    @Query("SELECT * FROM years WHERE year=:year ORDER BY id")
    LiveData<Year> findSpecificYear(final int year);

    @Query("SELECT * FROM years WHERE year=:year ORDER BY id")
    Year findSpecificYearNoLive(final int year);

    @Query("SELECT id FROM years WHERE year=:year ORDER BY id")
    int getYearId(final int year);

}
