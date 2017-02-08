package com.webapps.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.webapps.view.R;
import com.webapps.view.databinding.ActivityWaveBinding;

/**
 * Created by leon on 17/1/16.
 */

public class WaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWaveBinding waveBinding = DataBindingUtil.setContentView(this,R.layout.activity_wave);
        waveBinding.waveView.startAnimation();
    }
}
