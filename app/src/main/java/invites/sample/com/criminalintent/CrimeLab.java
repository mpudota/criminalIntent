package invites.sample.com.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by mpudota on 4/8/17.
 */

public class CrimeLab {


    public static CrimeLab get(Context c) {

        if (crimeLab == null) {
            crimeLab = new CrimeLab(c.getApplicationContext());
        }
        return crimeLab;
    }

    private static CrimeLab crimeLab;
    private Context mContext;
    private ArrayList<Crime> mCrimes;
    CriminalIntentJsonParser criminalIntentJsonParser;
    String filename = "crimes.json";
    private final String TAG = "sample.criminalintent";

    private CrimeLab (Context context) {
        mContext = context;
        criminalIntentJsonParser = new CriminalIntentJsonParser(mContext, filename);

        try {
            mCrimes = criminalIntentJsonParser.loadCrimes();
        } catch (JSONException e) {
            mCrimes = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            mCrimes = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID uuid) {
        for ( Crime c : mCrimes) {
            if (c.getUuid().equals(uuid))
                return c;
        }
        return null;
    }

    public void addCrime (Crime c) {
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }

    public boolean saveCrime() {
        try {
            criminalIntentJsonParser.saveCrimes(mCrimes);
            Log.d(TAG, " is saving data to crime.json file");
            return true;
        }  catch (IOException e) {
            Log.d(TAG, " is not saving data to crime.json file");
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            Log.d(TAG, " is not saving data to crime.json file");
            e.printStackTrace();
            return false;
        }

    }
}
