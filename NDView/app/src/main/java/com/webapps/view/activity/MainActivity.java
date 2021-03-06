package com.webapps.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.webapps.view.R;
import com.webapps.view.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    ImageButton mFab;
    View mFab;
    ActivityMainBinding mActivityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mActivityMainBinding.toDiscrollAnimator.setOnClickListener(this);
        mActivityMainBinding.toPathAnimator.setOnClickListener(this);
        mActivityMainBinding.toBoucdingAnimator.setOnClickListener(this);
        mActivityMainBinding.toSlidingAnimator.setOnClickListener(this);
        mActivityMainBinding.toWaveAnimator.setOnClickListener(this);
        mActivityMainBinding.toSweetPaneListView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.toPathAnimator:
                intent = new Intent(this,AnimatorPathActivity.class);
                startActivity(intent);
                break;
            case R.id.toDiscrollAnimator:
                intent = new Intent(this,DiscrollActivity.class);
                startActivity(intent);
                break;
            case R.id.toBoucdingAnimator:
                intent = new Intent(this,BoucdingActivity.class);
                startActivity(intent);
                break;
            case R.id.toSlidingAnimator:
                intent = new Intent(this,SlidingActivity.class);
                startActivity(intent);
                break;
            case R.id.toWaveAnimator:
                intent = new Intent(this,WaveActivity.class);
                startActivity(intent);
                break;
            case R.id.toSweetPaneListView:
                intent = new Intent(this,SweetPaneActivity.class);
                startActivity(intent);
                break;
        }
    }
}
