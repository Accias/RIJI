package com.example.riji.Day_related;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "days")
public class Day {

    @ColumnInfo(name = "day")
    public int day;
    @ColumnInfo(name = "month")
    public int month;
    @ColumnInfo(name = "year")
    public int year;
    @ColumnInfo(name = "weekDate")
    public int weekDate;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    public Day(int day, int month, int year, int weekDate){
        this.day=day;
        this.month=month;
        this.year=year;
        this.weekDate=weekDate;
    }

    public long getId(){
        return id;
    }

    public int getDay(){
        return day;
    }

    public int getMonth(){
        return month;
    }

    public int getYear(){
        return year;
    }

    public int getWeekDate(){
        return weekDate;
    }

    public void setDay(int day){
        this.day=day;
    }

    public void setMonth(int month){
        this.month=month;
    }

    public void setYear(int year){
        this.year=year;
    }

    public void setWeekDate(int weekDate){
        this.weekDate=weekDate;
    }
}