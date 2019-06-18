package com.example.riji.Day_related;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.riji.Month_related.Month;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "days", foreignKeys = {@ForeignKey(entity = Month.class, parentColumns = "id", childColumns = "month_id", onDelete = CASCADE)},
        indices = {@Index(value = "month_id")})
//one to many relationship with month class using foreign key

//since all database classes are very similar, refer to BulletPoint folder for commenting references. Method names should be self explanatory
public class Day {

    @ColumnInfo(name = "day")
    public int day;
    @ColumnInfo(name = "month")
    public int month;
    @ColumnInfo(name = "year")
    public int year;
    @ColumnInfo(name = "weekDate")
    public int weekDate;

    @ColumnInfo(name = "note")
    public String note;

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

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(int weekDate) {
        this.weekDate = weekDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}