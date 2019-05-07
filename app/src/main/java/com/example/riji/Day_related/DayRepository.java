package com.example.riji.Day_related;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.riji.AfterDBOperationListener;
import com.example.riji.Database;

import java.lang.ref.WeakReference;
import java.util.List;

public class DayRepository {
    private DayDAO mDayDao;
    private LiveData<List<Day>> mAllDays;
    private AfterDBOperationListener delegate;

    DayRepository(Application application) {
        Database db = Database.getDatabase(application);
        mDayDao = db.getDayDAO();
        mAllDays = mDayDao.getAllDays();
    }

    LiveData<List<Day>> getAllDays() {
        return mAllDays;
    }

    public void setDelegate(AfterDBOperationListener delegate) {
        this.delegate = delegate;
    }

    void insertDay(Day day) {
        new DayRepository.insertAsyncTask(mDayDao, delegate).execute(day);
    }

    private static class insertAsyncTask extends AsyncTask<Day, Void, Integer> {

        private DayDAO mAsyncTaskDao;
        private WeakReference<AfterDBOperationListener> asyncDelegate;

        insertAsyncTask(DayDAO dao, AfterDBOperationListener afterDBOperationListener) {
            mAsyncTaskDao = dao;
            asyncDelegate = new WeakReference<>(afterDBOperationListener);
        }

        @Override
        protected Integer doInBackground(final Day... params) {
            mAsyncTaskDao.insertDay(params[0]);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            final AfterDBOperationListener delegate = asyncDelegate.get();
            if (delegate != null)
                delegate.afterDBOperation(result);
        }
    }
}


