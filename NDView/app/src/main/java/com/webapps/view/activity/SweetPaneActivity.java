package com.webapps.view.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webapps.view.R;
import com.webapps.view.databinding.ActivitySweetPaneBinding;
import com.webapps.view.view.SweetPaneListView;

/**
 * Created by leon on 17/1/16.
 */

public class SweetPaneActivity extends AppCompatActivity implements SweetPaneListView.OnPositionChangedListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySweetPaneBinding sweetPaneBinding = DataBindingUtil.setContentView(this,R.layout.activity_sweet_pane);
        sweetPaneBinding.sweetPaneListView.setAdapter(new DummyAdapter());
        sweetPaneBinding.sweetPaneListView.setCacheColorHint(Color.GREEN);
        sweetPaneBinding.sweetPaneListView.setOnPositionChangedListener(this);
    }

    private class DummyAdapter extends BaseAdapter{

        private int mNumDummies = 100;

        @Override
        public int getCount() {
            return mNumDummies;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(SweetPaneActivity.this).inflate(R.layout.sweet_pane_view,parent,false);
            }
            TextView textView = (TextView) convertView;
            textView.setText("" + position);
            return convertView;
        }
    }


    @Override
    public void onPositionChanged(SweetPaneListView listView, int position, View scrollBarPanelView) {
        ((TextView)scrollBarPanelView).setText("位置："+position);
    }
}
