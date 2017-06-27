package invites.sample.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by mpudota on 6/3/17.
 */

public class CrimePagerActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ArrayList<Crime> mCrimes;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getUuid());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);

        for (int i=0; i < mCrimes.size() ; i++ ) {
            if (crimeId.equals(mCrimes.get(i).getUuid())) {
                mViewPager.setCurrentItem(i);
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Crime crime = mCrimes.get(position);
                if (crime.getTitle() != null) {
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
