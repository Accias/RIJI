package com.example.riji.Day_related;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//since all database classes are very similar, refer to BulletPoint folder for commenting references. Method names should be self explanatory
public class DayViewModel extends AndroidViewModel {
    private DayRepository mRepository;
    private LiveData<List<Day>> mAllDays;

    public DayViewModel(Application application) {
        super(application);
        mRepository = new DayRepository(application);
        mAllDays = mRepository.getAllDays();
    }

    public LiveData<List<Day>> getAllDays() {
        return mAllDays;
    }

    public LiveData<Day> getSpecificDay(int year, int month, int day) {
        return mRepository.getSpecificDay(year, month, day);
    }

    public void insert(Day day) {
        mRepository.insertDay(day);
    }
}
