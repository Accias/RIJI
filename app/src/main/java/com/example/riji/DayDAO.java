package com.example.riji;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DayDAO {
    @Insert
    void insertDay(Day day);

    @Update
    void updateDay(Day day);

    @Delete
    void deleteDay(Day day);

    @Query("SELECT * FROM days ORDER BY id")
    List<Day> getAllDays();

    @Query("SELECT * FROM days WHERE year=:year ORDER BY id")
    List<Day> findDayInYear(final int year);

    @Query("SELECT * FROM days WHERE year=:year&month=:month ORDER BY id")
    List<Day> findDayInMonth(final int year,final int month);

    @Query("SELECT * FROM days WHERE year=:year&month=:month&day=:day ORDER BY id")
     List<Day> findSpecificDay(final int year, final int month, final int day);



}
