package com.example.riji.Day_related;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.riji.Month_related.Month;

@Entity(tableName = "days", foreignKeys = {@ForeignKey(entity = Month.class, parentColumns = "id", childColumns = "month_id")},
        indices = {@Index(value = "month_id")})
public class Day {

    @ColumnInfo(name = "day")
    public int day;
    @ColumnInfo(name = "month")
    public int month;
    @ColumnInfo(name = "year")
    public int year;
    @ColumnInfo(name = "weekDate")
    public int weekDate;

    @ColumnInfo(name = "month_id")
    public long month_id;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    public Day(int day, int month, int year, int weekDate, final long month_id) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.weekDate = weekDate;
        this.month_id = month_id;
    }

    public long getId() {
        return id;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getWeekDate() {
        return weekDate;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setWeekDate(int weekDate) {
        this.weekDate = weekDate;
    }
}