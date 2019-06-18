package com.example.riji.Year_related;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "years")
//since all database classes are very similar, refer to BulletPoint folder for commenting references. Method names should be self explanatory
public class Year {

    @ColumnInfo(name = "year")
    public int year;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    public Year(int year) {
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
