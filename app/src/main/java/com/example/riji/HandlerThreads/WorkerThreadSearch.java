package com.example.riji.HandlerThreads;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.riji.BulletPoint_related.BulletPoint;
import com.example.riji.BulletPoint_related.BulletPointDAO;
import com.example.riji.Database;
import com.example.riji.Day_related.Day;
import com.example.riji.Day_related.DayDAO;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkerThreadSearch extends HandlerThread {
    private static final String TAG = WorkerThreadSearch.class.getSimpleName();
    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private Callback mCallback;
    private DayDAO mDayDao;
    private BulletPointDAO mBPDao;

    public WorkerThreadSearch(Handler responseHandler, Callback callback, Context context) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        this.mDayDao = Database.getDatabase(context).getDayDAO();
        this.mBPDao = Database.getDatabase(context).getBulletPointDAO();
    }

    public void queueDay(int id) {
        // Log.i(TAG, "year: " + year + " month: " + month + " day: " + day + " added to the day queue");
        mWorkerHandler.obtainMessage(id)
                .sendToTarget();
    }

    public void queueSearch(String term) {
        Message message = mWorkerHandler.obtainMessage();
        message.obj = term;
        message.sendToTarget();
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
                int id = msg.what;
                handleDayRequest(id);
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
                String term = (String) msg.obj;
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

    private void handleDayRequest(final int id) {

        final Day day1 = mDayDao.findDayById(id);
        final long day_id = mDayDao.getDayId(day1.getYear(), day1.getMonth(), day1.getDay());
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onDayFound(day1, day_id);
            }
        });
    }


    private void handleSearchRequest(final String term) {

        final List<BulletPoint> bullets = mBPDao.search(term);
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSearchFound(bullets);
            }
        });
    }

    public interface Callback {
        void onDayFound(Day day, long day_id);

        void onSearchFound(List<BulletPoint> bullets);
    }


}