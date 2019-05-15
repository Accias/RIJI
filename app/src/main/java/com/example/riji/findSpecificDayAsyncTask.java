package com.example.riji;

import android.os.AsyncTask;

import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

import java.lang.ref.WeakReference;

public class findSpecificDayAsyncTask extends AsyncTask<Date, Void, Day> {
    private DayDAO mAsyncTaskDao;
    private AsyncResponse delegate;
    private WeakReference<AfterDBOperationListener> asyncDelegate;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(Day output);
    }

    findSpecificDayAsyncTask(DayDAO dao,AfterDBOperationListener afterDBOperationListener, AsyncResponse delegate) {
        mAsyncTaskDao = dao;
        this.delegate = delegate;
        asyncDelegate = new WeakReference<>(afterDBOperationListener);
    }

    @Override
    protected Day doInBackground(final Date... params) {
        int year = params[0].year;
        int month = params[0].month;
        int day = params[0].day;
        return mAsyncTaskDao.findSpecificDayNoLive(year, month, day);
    }

    @Override
    protected void onPostExecute(Day day) {
        final AfterDBOperationListener delegate1 = asyncDelegate.get();
        if (delegate1 != null)
            delegate1.afterDBOperation(day);
        delegate.processFinish(day);
    }

}

class Date {
    public int year, month, day;

    Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}