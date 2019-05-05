package com.example.riji.Year_related;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.riji.Year_related.Year;
import com.example.riji.Year_related.YearRepository;

import java.util.List;

public class YearViewModel extends AndroidViewModel {
    private YearRepository mRepository;
    private LiveData<List<Year>> mAllYears;

    public YearViewModel(Application application) {
        super(application);
        mRepository = new YearRepository(application);
        mAllYears = mRepository.getAllYears();
    }

    LiveData<List<Year>> getAllYears() {
        return mAllYears;
    }

    public void insert(Year year) {
        mRepository.insertYear(year);
    }

}
