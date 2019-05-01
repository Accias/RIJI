package com.example.riji;

import android.os.AsyncTask;

import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

public class findSpecificDayAsyncTask extends AsyncTask<Date,Void, Day> {
    private DayDAO mAsyncTaskDao;
    private AsyncResponse delegate = null;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(Day output);
    }

    findSpecificDayAsyncTask(DayDAO dao,AsyncResponse delegate) {
        mAsyncTaskDao = dao;
        this.delegate=delegate;
    }

    @Override
    protected Day doInBackground(final Date... params) {
        int year = params[0].year;
        int month = params[0].month;
        int day = params[0].day;
        Day theDay = mAsyncTaskDao.findSpecificDayNoLive(year,month,day);
        return theDay;
    }

    @Override
    protected void onPostExecute(Day day) {
        delegate.processFinish(day);
    }

}

class Date{
    public int year, month,day;
    Date(int year,int month, int day){
        this.year=year;
        this.month=month;
        this.day=day;
    }
}