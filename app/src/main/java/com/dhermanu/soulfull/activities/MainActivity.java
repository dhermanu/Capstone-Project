package com.dhermanu.soulfull.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.fragments.FavoriteActivityFragment;
import com.dhermanu.soulfull.fragments.MainActivityFragment;

import java.util.ArrayList;
import java.util.List;

import static com.dhermanu.soulfull.R.id.viewpager;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.FragmentCallBack{
    public static String EXTRA_DATA = "com.extra.data";
    public static String EXTRA_LAT = "com.extra.lat";
    public static String EXTRA_LONG = "com.extra.long";
    public static String EXTRA_MAPS = "com.extra.maps";
    public static String EXTRA_CITY = "com.extra.city";
    public static String EXTRA_SAVE = "com.extra.save";

    private static final int REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView tittleText;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.BLACK);
        toolbar.setSubtitleTextColor(android.graphics.Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tittleText = (TextView) findViewById(R.id.locationTitle);

        viewPager = (ViewPager) findViewById(viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }



    @Override
    public void onTextUpdated(String text, boolean changeColor) {
        tittleText.setText(text);
        if(changeColor){
            tittleText.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorAccent));
        }

        else
            tittleText.setTextColor(ContextCompat.getColor(getApplication(), R.color.black));
    }

    public void searchLocation(View view){
        int pos = viewPager.getCurrentItem();
        Fragment MainActivityFragment = adapter.getItem(pos);
        if(pos == 0)
            ((MainActivityFragment)MainActivityFragment).openAutoCompleteActivity();
    }

    private void setupViewPager(ViewPager viewPager) {
        final String tabTittleMain = "Search";
        final String tabTittleBook = "Bookmark";
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainActivityFragment(), tabTittleMain);
        adapter.addFragment(new FavoriteActivityFragment(), tabTittleBook);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Fragment main, favorite;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(main == null)
                        main = new MainActivityFragment();
                    return main;
                case 1:
                    if(favorite == null)
                        favorite= new FavoriteActivityFragment();
                    return favorite;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

