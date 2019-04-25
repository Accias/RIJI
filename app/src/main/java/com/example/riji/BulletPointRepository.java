package com.example.riji;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class BulletPointRepository {
    private BulletPointDAO mBulletPointDao;
    private LiveData<List<BulletPoint>> mAllBulletPoints;

    BulletPointRepository(Application application) {
        Database db = Database.getDatabase(application);
        mBulletPointDao = db.getBulletPointDAO();
        mAllBulletPoints = mBulletPointDao.getAllBulletPoints();
    }

    LiveData<List<BulletPoint>> getAllBulletPoints() {
        return mAllBulletPoints;
    }

    void insertBulletPoint(BulletPoint bullet) {
        new insertAsyncTask(mBulletPointDao).execute(bullet);
    }

    private static class insertAsyncTask extends AsyncTask<BulletPoint, Void, Void> {

        private BulletPointDAO mAsyncTaskDao;

        insertAsyncTask(BulletPointDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BulletPoint... params) {
            mAsyncTaskDao.insertBulletPoint(params[0]);
            return null;
        }
    }
}