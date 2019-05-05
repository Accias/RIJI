package com.example.riji.BulletPoint_related;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
    this is the bulletpoint viewmodel, which is another abstract layer over the UI.
    It contains all database methods from the BulletPoint DAO.
    The view model retains data whenever an activity is destroyed, and returns the data when a new
    activity is created.
 */
public class BulletPointViewModel extends AndroidViewModel {
    //data
    private BulletPointRepository mRepository;
    private LiveData<List<BulletPoint>> mAllBulletPoints;

    public BulletPointViewModel(Application application) {
        super(application);
        mRepository = new BulletPointRepository(application);
        mAllBulletPoints = mRepository.getAllBulletPoints();
    }

    public LiveData<List<BulletPoint>> getAllBulletPoints() {
        return mAllBulletPoints;
    }

    public void insert(BulletPoint bp) {
        mRepository.insertBulletPoint(bp);
    }
}