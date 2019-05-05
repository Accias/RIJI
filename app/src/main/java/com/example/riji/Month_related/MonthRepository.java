package com.example.riji.Month_related;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.riji.Database;

import java.util.List;

public class MonthRepository {
    private MonthDAO mMonthDao;
    private LiveData<List<Month>> mAllMonths;

    MonthRepository(Application application) {
        Database db = Database.getDatabase(application);
        mMonthDao = db.getMonthDAO();
        mAllMonths = mMonthDao.getAllMonths();
    }

    LiveData<List<Month>> getAllMonths() {
        return mAllMonths;
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


}
