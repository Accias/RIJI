package com.example.riji;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointDAO;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//MyWorkerThread.java
class MyWorkerThread extends HandlerThread {
    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private static final String TAG = MyWorkerThread.class.getSimpleName();
    private Map<ImageView, String> mRequestMap = new HashMap<>();
    private Callback mCallback;
    private DayDAO mDayDao;
    private BulletPointDAO mBPDao;

    public interface Callback {
        void onDayFound(Day day, long day_id);
        void onBPFound(LiveData<List<BulletPoint>> bullets);
    }

    MyWorkerThread(Handler responseHandler, Callback callback, DayDAO mDayDao, BulletPointDAO mBPDao) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        this.mDayDao = mDayDao;
        this.mBPDao = mBPDao;
    }

    void queueDay(int year, int month, int day) {
        Log.i(TAG, "year: " + year + " month: " + month + " day: " + day + " added to the day queue");
        mWorkerHandler.obtainMessage(year, month, day)
                .sendToTarget();
    }

    void queueBP(long day_id) {
        Log.i(TAG, "day_id: " + day_id +  " added to the BP queue");
        mWorkerHandler.obtainMessage((int)day_id)
                .sendToTarget();
    }

    void prepareHandlerBP() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int day_id = msg.what;

                handleBPRequest(day_id);
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    void prepareHandlerDay() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int year = msg.what;
                int month = msg.arg1;
                int day = msg.arg2;
                handleDayRequest(year, month, day);
                try {
                    msg.recycle(); //it can work in some situations
                } catch (IllegalStateException e) {
                    mWorkerHandler.removeMessages(msg.what); //if recycle doesnt work we do it manually
                }
                return true;
            }
        });
    }

    private void handleDayRequest(final int year, final int month, final int day) {

        final Day day1 = mDayDao.findSpecificDayNoLive(year, month, day);
        final long day_id = mDayDao.getDayId(year,month,day);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDayFound(day1,day_id);
            }
        });
    }

    private void handleBPRequest(final int day_id)
    {
        final LiveData<List<BulletPoint>> bullets= mBPDao.findBulletPointsForDay(day_id);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onBPFound(bullets);
            }
        });
    }

}