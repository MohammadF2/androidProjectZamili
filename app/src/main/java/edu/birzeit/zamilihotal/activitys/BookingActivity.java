package edu.birzeit.zamilihotal.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.adabter.FragmentPagerAdapterP;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapterP(getSupportFragmentManager(),
                BookingActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void profileClick(MenuItem item) {
        Intent intent = new Intent(BookingActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void BookingClick(MenuItem item) {
        Intent intent = new Intent(BookingActivity.this, BookingActivity.class);
        startActivity(intent);
    }
    public void SearchClick(MenuItem item) {
        Intent intent = new Intent(BookingActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
