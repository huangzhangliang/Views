package com.webapps.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import com.webapps.view.R;
import com.webapps.view.view.sliding.SlidingCardLayout;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by leon on 17/1/10.
 */

public class SlidingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String,SlidingCardLayout> cardLayoutMap = new TreeMap<String, SlidingCardLayout>();
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        for (int i = 0;i<coordinatorLayout.getChildCount();i++){
            SlidingCardLayout slidingCardLayout = (SlidingCardLayout) coordinatorLayout.getChildAt(i);
            coordinatorLayout.onLayoutChild(slidingCardLayout,0);
        }
    }
}
