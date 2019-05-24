package com.example.riji.HandlerThreads;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.riji.Database;
import com.example.riji.Month_related.Month;
import com.example.riji.Month_related.MonthDAO;
import com.example.riji.Year_related.Year;
import com.example.riji.Year_related.YearDAO;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkerThreadYear extends HandlerThread {
    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private static final String TAG = MyWorkerThread.class.getSimpleName();
    private WorkerThreadYear.Callback mCallback;
    private YearDAO mYearDao;
    private MonthDAO mMonthDao;

    public interface Callback {
        void onMonthsFound(LiveData<List<Month>> months);
        void onYearFound(Year year,int year_id);
    }

    public WorkerThreadYear(Handler responseHandler, WorkerThreadYear.Callback callback, Context context) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        this.mYearDao = Database.getDatabase(context).getYearDAO();
        this.mMonthDao = Database.getDatabase(context).getMonthDAO();
    }


    public void queueMonths(int year) {
        Log.i(TAG, "year: " + year + " added to the month queue");
        mWorkerHandler.obtainMessage(year)
                .sendToTarget();
    }

    public void queueYear(int year) {
        Log.i(TAG, "year: " + year + " added to the year queue");
        mWorkerHandler.obtainMessage(year)
                .sendToTarget();
    }

    public void prepareHandlerMonths() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int year = msg.what;
                handleMonthsRequest(year);
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    void prepareHandlerYear(){
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int year = msg.what;
                handleYearRequest(year);
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    private void handleYearRequest(final int year) {

        final Year year1 = mYearDao.findSpecificYearNoLive(year);
        final int year_id=mYearDao.getYearId(year);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onYearFound(year1,year_id);
            }
        });
    }

    private void handleMonthsRequest(final int year) {

        final LiveData<List<Month>> months = mMonthDao.findMonthInYear(year);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMonthsFound(months);
            }
        });
    }
}
