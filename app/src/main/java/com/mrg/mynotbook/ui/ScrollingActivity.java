package com.mrg.mynotbook.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mrg.mynotbook.R;
import com.mrg.mynotbook.adapter.IndexAdapter;
import com.xiaomi.ad.AdListener;
import com.xiaomi.ad.NativeAdInfoIndex;
import com.xiaomi.ad.NativeAdListener;
import com.xiaomi.ad.adView.StandardNewsFeedAd;
import com.xiaomi.ad.common.pojo.AdError;
import com.xiaomi.ad.common.pojo.AdEvent;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    private String TAG="mrg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        ViewPager viewById = (ViewPager) findViewById(R.id.viewPager);
         ArrayList<View> views = new ArrayList<>();
        TextView textView = new TextView(this);
        textView.setText("asdfasdasdfd");
        views.add(new Button(this));
        views.add(new TextView(this));
        views.add(new Button(this));
        views.add(textView);
        views.add(new Button(this));
        final IndexAdapter indexAdapter = new IndexAdapter(this);
        viewById.setAdapter(indexAdapter);








    }
}
