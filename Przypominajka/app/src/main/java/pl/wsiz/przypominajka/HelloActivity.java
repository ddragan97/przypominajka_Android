package pl.wsiz.przypominajka;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayout;

public class HelloActivity extends FragmentActivity {

    private static final int countPagers = 3;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        viewPager = findViewById(R.id.helloPager);
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Aktualne");
                        tab.setIcon(R.drawable.list_ico_1);
                        break;
                    }
                    case 1: {
                        tab.setText("Dodaj");
                        tab.setIcon(R.drawable.add_ico_2);
                        break;
                    }
                    case 2: {
                        tab.setText("Dane");
                        tab.setIcon(R.drawable.profile_ico_3);
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            finish();
        }
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CurrentlyActivity();
                case 1:
                    return new AddNewActivity();
                case 2:
                    return new ProfileActivity();
            }
            return new CurrentlyActivity();
        }

        @Override
        public int getItemCount() {
            return countPagers;
    }
    }

}
