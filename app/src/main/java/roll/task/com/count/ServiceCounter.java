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

    public ServiceCounter() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        SharedPreferences.Editor editor = mSharedPref.edit();

        //todo in new method code below
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate= dateFormat.format(date);


        editor.putString("last_time", formattedDate);
        editor.apply();

        mInfinityAdder.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mFlag = false;
        mInfinityAdder.interrupt();
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt("value", mValue);
        editor.apply();
    }

    private class InfinityAdder extends Thread {


        @Override
        public void run() {
            super.run();


            mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
            mValue = mSharedPref.getInt("value", mValue);


            while (mFlag) {

                mValue++;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }


    }

}


