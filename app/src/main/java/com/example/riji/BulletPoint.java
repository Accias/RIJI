package com.example.riji;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "bulletpoints",foreignKeys = @ForeignKey(entity=Day.class,parentColumns = "id",childColumns = "day_id"))
public class BulletPoint {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "bulletType")
    public int bulletType;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "day_id")
    public long day_id;

    public BulletPoint(int bulletType, String note){
        this.bulletType=bulletType;
        this.note=note;
    }

    public long getId(){
        return id;
    }
    public int getBulletType(){
        return bulletType;
    }

    public String getNote(){
        return note;
    }

    public void setBulletType(int bulletType){
        this.bulletType=bulletType;
    }

    public void setNote(String note){
        this.note=note;
    }
}