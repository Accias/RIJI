package com.example.riji.Month_related;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.riji.Database;

import java.util.List;

//since all database classes are very similar, refer to BulletPoint folder for commenting references. Method names should be self explanatory
public class MonthRepository {
    private MonthDAO mMonthDao;
    private LiveData<List<Month>> mAllMonths;

    public MonthRepository(Application application) {
        Database db = Database.getDatabase(application);
        mMonthDao = db.getMonthDAO();
        mAllMonths = mMonthDao.getAllMonths();
    }

    LiveData<List<Month>> getAllMonths() {
        return mAllMonths;
    }

    public void updateMonth(Month month) {
        new MonthRepository.updateAsyncTask(mMonthDao).execute(month);
    }

    void insertMonth(Month month) {
        new MonthRepository.insertAsyncTask(mMonthDao).execute(month);
    }

    private static class insertAsyncTask extends AsyncTask<Month, Void, Void> {

        private MonthDAO mAsyncTaskDao;

        insertAsyncTask(MonthDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Month... params) {
            mAsyncTaskDao.insertMonth(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Month, Void, Void> {

        private MonthDAO mAsyncTaskDao;

        updateAsyncTask(MonthDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Month... params) {
            mAsyncTaskDao.updateMonth(params[0]);
            return null;
        }
    }


}
