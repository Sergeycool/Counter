package roll.task.com.count;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceCounter extends Service {

    private SharedPreferences mSharedPref;
    private int mValue;
    private InfinityAdder mInfinityAdder = new InfinityAdder();
    private boolean mFlag = true;
    private String mLastTime = "first run";

    public ServiceCounter() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        mValue = intent.getIntExtra(MainActivity.PARAM_COUNT, 0);
        mLastTime = intent.getStringExtra((MainActivity.PARAM_TIME));
        writeNewTime();

        mInfinityAdder.start();

        return super.onStartCommand(intent, flags, startId);
    }

    public void writeNewTime() {

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        SharedPreferences.Editor editor = mSharedPref.edit();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        mLastTime = dateFormat.format(date);
        editor.putString("last_time", mLastTime);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mFlag = false; //stop Thread
        mInfinityAdder.interrupt();
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt("value", mValue);
        editor.apply();

        //update time
        Intent intentTime = new Intent(MainActivity.BROADCAST_ACTION);
        intentTime.putExtra(MainActivity.PARAM_TIME, mLastTime);
        sendBroadcast(intentTime);
    }

    private class InfinityAdder extends Thread {

        @Override
        public void run() {
            super.run();

//            mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
//            mValue = mSharedPref.getInt("value", mValue);

            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);

            while (mFlag) {

                mValue++;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                intent.putExtra(MainActivity.PARAM_COUNT, mValue);
                sendBroadcast(intent);

            }
        }


    }

}


