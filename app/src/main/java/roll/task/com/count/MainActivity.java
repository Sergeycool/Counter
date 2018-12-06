package roll.task.com.count;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());


        mValue = mSharedPref.getInt("value",mValue);
        mLastTime = mSharedPref.getString("last_time", mLastTime);

        mCounter.setText(String.valueOf(mValue));
        mTvLastTime.setText(mLastTime);


    }


    @OnClick(R.id.button_start)
    void startService() {

        startService(new Intent(this, ServiceCounter.class));

    }

    @OnClick(R.id.button_stop)
    void storService() {

        stopService(new Intent(this, ServiceCounter.class));


    }
}
