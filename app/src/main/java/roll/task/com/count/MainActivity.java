package roll.task.com.count;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.counter)
    TextView mCounter;
    @BindView(R.id.last_time)
    TextView mTvLastTime;

    private SharedPreferences mSharedPref;
    private int mValue = 0;
    private String mLastTime = "first run";
    public final static String PARAM_COUNT = "count";
    public final static String PARAM_TIME = "time";
    public final static String BROADCAST_ACTION = "roll.task.com.count";
    BroadcastReceiver mBroadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());

        mValue = mSharedPref.getInt("value", mValue);
        mLastTime = mSharedPref.getString("last_time", mLastTime);

        initialBroadcastReceiver();

        //set value after launch
        mCounter.setText(String.valueOf(mValue));
        mTvLastTime.setText(mLastTime);

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, ServiceCounter.class));
    }

    public void initialBroadcastReceiver() {

        mBroadcast = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                mValue = intent.getIntExtra(PARAM_COUNT, mValue);
                mLastTime = intent.getStringExtra(PARAM_TIME);

                mCounter.setText(String.valueOf(mValue));
                if (mLastTime != null) mTvLastTime.setText(mLastTime);


            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(mBroadcast, intentFilter);
    }


    @OnClick(R.id.button_start)
    void startService() {

        startService(new Intent(this, ServiceCounter.class)
                .putExtra(PARAM_COUNT, mValue)
                .putExtra(PARAM_TIME, mLastTime));

    }

    @OnClick(R.id.button_stop)
    void stopService() {

        stopService(new Intent(this, ServiceCounter.class));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcast);
    }
}
