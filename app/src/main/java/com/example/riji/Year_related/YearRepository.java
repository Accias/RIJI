package com.example.riji.Year_related;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.riji.Database;
import com.example.riji.Year_related.Year;
import com.example.riji.Year_related.YearDAO;
import com.example.riji.Year_related.YearRepository;

import java.util.List;

public class YearRepository {
    private YearDAO mYearDao;
    private LiveData<List<Year>> mAllYears;

    YearRepository(Application application) {
        Database db = Database.getDatabase(application);
        mYearDao = db.getYearDAO();
        mAllYears = mYearDao.getAllYears();
    }

    LiveData<List<Year>> getAllYears() {
        return mAllYears;
    }


    void insertYear(Year year) {
        new YearRepository.insertAsyncTask(mYearDao).execute(year);
    }

    private static class insertAsyncTask extends AsyncTask<Year, Void, Void> {

        private YearDAO mAsyncTaskDao;

        insertAsyncTask(YearDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Year... params) {
            mAsyncTaskDao.insertYear(params[0]);
            return null;
        }
    }

}
