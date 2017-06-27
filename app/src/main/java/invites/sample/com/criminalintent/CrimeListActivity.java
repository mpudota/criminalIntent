package invites.sample.com.criminalintent;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

public class CrimeListActivity extends SignleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        Fragment fragment = new CrimeListFragment();
        return fragment;
    }

    @Override
    public int getLayoutResourceId () {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment) == null) {
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getUuid());
            startActivity(intent);
        }
        else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();


            Fragment oldDetail = fm.findFragmentById(R.id.detail_fragment);
            Fragment newDetail = CrimeFragment.newInstance(crime.getUuid());

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }
            ft.add(R.id.detail_fragment, newDetail);
            ft.commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.activity_main);
        listFragment.updateUI();

    }
}
