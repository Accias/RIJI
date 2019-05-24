package com.example.riji.Month_related;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.riji.Year_related.Year;

@Entity(tableName = "months", foreignKeys = {@ForeignKey(entity = Year.class, parentColumns = "id", childColumns = "year_id")},
        indices = {@Index(value = "year_id")})
public class Month {

    //month starts from 1(january)
    @ColumnInfo(name = "month")
    public int month;
    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "year_id")
    public long year_id;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    public Month(int month, int year, final long year_id) {
        this.month = month;
        this.year = year;
        this.year_id = year_id;
    }

    public long getId() {
        return id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getYear_id() {
        return year_id;
    }

}
