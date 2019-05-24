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

public class WorkerThreadTableOfYears extends HandlerThread {
    private static final String TAG = WorkerThreadTableOfYears.class.getSimpleName();
    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private WorkerThreadTableOfYears.Callback mCallback;
    private YearDAO mYearDao;

    public WorkerThreadTableOfYears(Handler responseHandler, WorkerThreadTableOfYears.Callback callback, Context context) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        this.mYearDao = Database.getDatabase(context).getYearDAO();
    }

    public void queueYears() {
        Log.i(TAG, "added to the years queue");
        mWorkerHandler.obtainMessage()
                .sendToTarget();
    }

    public void prepareHandlerYears() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handleYearsRequest();
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    private void handleYearsRequest() {

        final LiveData<List<Year>> years = mYearDao.getAllYears();
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onYearsFound(years);
            }
        });
    }

    public interface Callback {

        void onYearsFound(LiveData<List<Year>> years);
    }
}
