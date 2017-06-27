package invites.sample.com.criminalintent;

import android.support.v4.app.Fragment;
/**
 * Created by mpudota on 6/11/17.
 */

public class CrimeCameraActivity extends SignleFragmentActivity {

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        Fragment fragment = new CrimeCameraFragment();
        return fragment;
    }
}
