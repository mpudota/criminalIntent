package invites.sample.com.criminalintent;



import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class SignleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_main);

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.activity_main, fragment).commit();
        }
    }

    protected int getLayoutResourceId () {
        return R.layout.fragment_activity;
    }
}


