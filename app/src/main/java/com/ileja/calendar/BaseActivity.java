package com.ileja.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Created by chentao on 16/5/16.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        RefWatcher refWatcher = MainApp.getRefWatcher();
        refWatcher.watch(this);
    }
}
