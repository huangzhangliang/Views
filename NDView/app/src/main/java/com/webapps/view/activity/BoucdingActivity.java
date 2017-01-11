package com.webapps.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.webapps.view.R;
import com.webapps.view.databinding.ActivityBoucdingBinding;
import com.webapps.view.view.boucding.BoucdingMenu;

/**
 * Created by leon on 17/1/4.
 */

public class BoucdingActivity extends AppCompatActivity {


    private BoucdingMenu mMenu;
    private ActivityBoucdingBinding mActivityBoucdingBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBoucdingBinding = DataBindingUtil.setContentView(this, R.layout.activity_boucding);
    }

    public void showAndDis(View view){
        if (mMenu != null && mMenu.isShow()){
            mMenu.dismiss();
        }else {
            mMenu = BoucdingMenu.makeMenu(mActivityBoucdingBinding.fl,R.layout.layout_boucding).show();
        }
    }

}
