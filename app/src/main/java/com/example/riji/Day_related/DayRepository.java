package com.example.riji.Day_related;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.riji.Database;

import java.util.List;

class DayRepository {
    private DayDAO mDayDao;
    private LiveData<List<Day>> mAllDays;

    DayRepository(Application application) {
        Database db = Database.getDatabase(application);
        mDayDao = db.getDayDAO();
        mAllDays = mDayDao.getAllDays();
    }

    LiveData<List<Day>> getAllDays() {
        return mAllDays;
    }

    LiveData<Day> getSpecificDay(int year, int month, int day){
        return mDayDao.findSpecificDay(year,month,day);
    }



    void insertDay(Day day) {
        new DayRepository.insertAsyncTask(mDayDao).execute(day);
    }

    private static class insertAsyncTask extends AsyncTask<Day, Void, Void> {

        private DayDAO mAsyncTaskDao;

        insertAsyncTask(DayDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Day... params) {
            mAsyncTaskDao.insertDay(params[0]);
            return null;
        }
    }
}


