package com.ziaplex.wizarsample.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class BaseActivity extends AppCompatActivity {

    private CloseActivityBroadcast BroadcastCloseAll;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BroadcastCloseAll = new CloseActivityBroadcast();
        IntentFilter intentFilter = new IntentFilter("baseActivity");
        registerReceiver(BroadcastCloseAll, intentFilter);
        addBaseContentView(onCreateMessage());
        addBaseContentView(UI.createCustomHorizontalSeparator(this));
    }

    public View onCreateMessage() {
        return new View(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exit();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {
        Intent intent = new Intent("baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);
    }
}
