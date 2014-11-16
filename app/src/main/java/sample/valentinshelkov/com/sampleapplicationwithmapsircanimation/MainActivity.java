package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.valentinshelkov.sampleapplicationwithmapsircanimation.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.adapter.RootPagerAdapter;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.contentprovider.DictProvider;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.data.MessageData;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment.AnimationFragment;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment.IrcFragment;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment.MapsFragment;


public class MainActivity extends FragmentActivity {
    private static final String[] TABS_NAMES = {
            IrcFragment.class.getSimpleName(),
            MapsFragment.class.getSimpleName(),
            AnimationFragment.class.getSimpleName()
    };
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager rootPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootPager = (ViewPager) findViewById(R.id.rootPager);
        rootPager.setAdapter(new RootPagerAdapter(getSupportFragmentManager(), this));
        rootPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //ignored
            }

            @Override
            public void onPageSelected(int i) {
                getActionBar().setTitle(TABS_NAMES[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //ignored
            }
        });
    }
}
