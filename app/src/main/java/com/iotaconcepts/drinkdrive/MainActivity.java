package com.iotaconcepts.drinkdrive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iotaconcepts.drinkdrive.R;
import com.iotaconcepts.drinkdrive.OneFragment;
import com.iotaconcepts.drinkdrive.TwoFragment;

public class MainActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    SessionManager session;
    String name, number;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        //Get user's login data from session
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        number = user.get(SessionManager.KEY_EMAIL);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Emergency !");
        adapter.addFragment(new TwoFragment(), "BAC");
        adapter.addFragment(new ThreeFragment(), "Search Ride");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // In case you have an item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();


        if(id == R.id.action_logout)  //Logout
        {
            session.logoutUser();
        }

        if(id == R.id.action_feedback)  //Feedback
        {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        if(id == R.id.action_share)  //Share
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        if(id == R.id.action_about)  //About
        {
            Intent i = new Intent(MainActivity.this, About.class);
            startActivity(i);
        }

        if (id == R.id.action_disclaimer)  //Disclaimer
        {
            Intent i = new Intent(MainActivity.this, Disclaimer.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}