package com.example.riji;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BulletPointViewModel extends AndroidViewModel {
    private BulletPointRepository mRepository;
    private LiveData<List<BulletPoint>> mAllBulletPoints;

    public BulletPointViewModel(Application application) {
        super(application);
        mRepository = new BulletPointRepository(application);
        mAllBulletPoints = mRepository.getAllBulletPoints();
    }

    LiveData<List<BulletPoint>> getAllBulletPoints() { return mAllBulletPoints; }

    public void insert(BulletPoint bp) { mRepository.insertBulletPoint(bp); }
}