package com.example.riji.Day_related;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//since all database classes are very similar, refer to BulletPoint folder for commenting references. Method names should be self explanatory
@Dao
public interface DayDAO {
    @Insert
    void insertDay(Day day);

    @Update
    void updateDay(Day day);

    @Delete
    void deleteDay(Day day);

    @Query("DELETE FROM days")
    void deleteAll();

    @Query("SELECT * FROM days ORDER BY id")
    LiveData<List<Day>> getAllDays();

    @Query("SELECT * FROM days WHERE year=:year ORDER BY id")
    LiveData<List<Day>> findDayInYear(final int year);

    @Query("SELECT * FROM days WHERE year=:year AND month=:month ORDER BY id")
    LiveData<List<Day>> findDayInMonth(final int year, final int month);

    @Query("SELECT * FROM days WHERE year=:year AND month=:month AND day=:day ORDER BY id")
    LiveData<Day> findSpecificDay(final int year, final int month, final int day);

    @Query("SELECT * FROM days WHERE year= :year AND month= :month AND day= :day")
    Day findSpecificDayNoLive(final int year, final int month, final int day);

    @Query("SELECT id FROM days WHERE year=:year AND month=:month AND day=:day")
    int getDayId(final int year, final int month, final int day);

    @Query("SELECT * FROM days WHERE id= :id")
    Day findDayById(final int id);
}
