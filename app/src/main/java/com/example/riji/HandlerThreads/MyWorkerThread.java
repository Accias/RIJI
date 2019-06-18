package com.example.riji.HandlerThreads;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointDAO;
import com.example.riji.Database;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
    Custom handlerthread class for asynchronous database queries and returns.
    There is one for each activity.
 */
public class MyWorkerThread extends HandlerThread {
    private static final String TAG = MyWorkerThread.class.getSimpleName();
    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private Callback mCallback;
    private DayDAO mDayDao;
    private BulletPointDAO mBPDao;

    public MyWorkerThread(Handler responseHandler, Callback callback, Context context) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        //get DAOs
        this.mDayDao = Database.getDatabase(context).getDayDAO();
        this.mBPDao = Database.getDatabase(context).getBulletPointDAO();
    }

    //am not really sure how these methods work yet
    public void queueDay(int year, int month, int day) {
        Log.i(TAG, "year: " + year + " month: " + month + " day: " + day + " added to the day queue");
        mWorkerHandler.obtainMessage(year, month, day)
                .sendToTarget();
    }

    public void queueBP(long day_id) {
        Log.i(TAG, "day_id: " + day_id + " added to the BP queue");
        mWorkerHandler.obtainMessage((int) day_id)
                .sendToTarget();
    }

    public void queueSearch(String term){
       Message message= mWorkerHandler.obtainMessage();
       message.obj=term;
       message.sendToTarget();
    }

    public void prepareHandlerBP() {
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

    public void prepareHandlerSearch() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.MICROSECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               String term=(String)msg.obj;
                handleSearchRequest(term);
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

        //query
        final Day day1 = mDayDao.findSpecificDayNoLive(year, month, day);
        final long day_id = mDayDao.getDayId(year, month, day);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                //return result to activity
                mCallback.onDayFound(day1, day_id);
            }
        });
    }

    private void handleBPRequest(final int day_id) {
        //query
        final LiveData<List<BulletPoint>> bullets = mBPDao.findBulletPointsForDay(day_id);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                //return result to activity
                mCallback.onBPFound(bullets);
            }
        });
    }
    private void handleSearchRequest(final String term) {
        //query
        final List<BulletPoint> bullets=mBPDao.search(term);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                //return result to activity
                mCallback.onSearchFound(bullets);
            }
        });
    }

    //activity implements interface so the data can be returned
    public interface Callback {
        void onDayFound(Day day, long day_id);

        void onBPFound(LiveData<List<BulletPoint>> bullets);

        void onSearchFound(List<BulletPoint> bullets);
    }


}