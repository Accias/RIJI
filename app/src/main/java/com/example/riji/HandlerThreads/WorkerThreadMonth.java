package com.example.riji.HandlerThreads;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.riji.Database;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;
import com.example.riji.Month_related.Month;
import com.example.riji.Month_related.MonthDAO;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkerThreadMonth extends HandlerThread {
    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private static final String TAG = MyWorkerThread.class.getSimpleName();
    private WorkerThreadMonth.Callback mCallback;
    private DayDAO mDayDao;
    private MonthDAO mMonthDao;

    public interface Callback {
        void onDayFound(LiveData<List<Day>> days);

        void onMonthFound(Month month, long month_id);
    }

    public WorkerThreadMonth(Handler responseHandler, WorkerThreadMonth.Callback callback, Context context) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        this.mDayDao = Database.getDatabase(context).getDayDAO();
        this.mMonthDao = Database.getDatabase(context).getMonthDAO();
    }

    public void queueDay(int year, int month) {
        Log.i(TAG, "year: " + year + " month: " + month + " added to the day queue");
        mWorkerHandler.obtainMessage(year, month)
                .sendToTarget();
    }

    public void queueMonth(int year, int month) {
        Log.i(TAG, "year: " + year + " month: " + month + " added to the month queue");
        mWorkerHandler.obtainMessage(year, month)
                .sendToTarget();
    }

    public void prepareHandlerDay() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int year = msg.what;
                int month = (int) msg.obj;
                handleDayRequest(year, month);
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    public void prepareHandlerMonth() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int year = msg.what;
                int month = (int) msg.obj;
                handleMonthRequest(year, month);
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    private void handleDayRequest(final int year, final int month) {

        final LiveData<List<Day>> days = mDayDao.findDayInMonth(year, month);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDayFound(days);
            }
        });
    }

    private void handleMonthRequest(final int year, final int month) {

        final Month month1 = mMonthDao.findSpecificMonthNoLive(year, month);
        final long month_id = mMonthDao.getMonthId(year, month);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMonthFound(month1, month_id);
            }
        });
    }
}
