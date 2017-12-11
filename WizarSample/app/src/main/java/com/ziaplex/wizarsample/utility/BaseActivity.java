package com.ziaplex.wizarsample.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ziaplex.wizarsample.R;

public class BaseActivity extends AppCompatActivity {

    private CloseActivityBroadcast BroadcastCloseAll;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BroadcastCloseAll = new CloseActivityBroadcast();
        IntentFilter intentFilter = new IntentFilter("baseActivity");
        registerReceiver(BroadcastCloseAll, intentFilter);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BroadcastCloseAll);
    }

    public class CloseActivityBroadcast extends BroadcastReceiver {

        public void onReceive(Context arg0, Intent intent) {
            if (intent.getIntExtra("closeAll", 0) == 1)
                finish();
        }
    }

    public void addBaseContentView(View view) {
        LinearLayout v = findViewById(R.id.container);
        if (v != null)
            v.addView(view);
    }
}
