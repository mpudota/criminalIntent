package invites.sample.com.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by mpudota on 6/8/17.
 */

public class CriminalIntentJsonParser {

    private Context mContext;
    private String mFilename;

    public CriminalIntentJsonParser(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes
                = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(stringBuilder.toString()).nextValue();

            for(int i=0; i < jsonArray.length(); i++) {
                crimes.add(new Crime(jsonArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws IOException, JSONException {

        JSONArray jsonArray = new JSONArray();
        for (Crime c: crimes) {
            jsonArray.put(c.toJSON());

            Writer writer = null;

            try {
                OutputStream out = mContext
                        .openFileOutput(mFilename, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(jsonArray.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                if (writer != null);
                writer.close();
            }
        }

    }

    public void deleteCrimes (ArrayList<Crime> crimes) {
        JSONObject jsonObject = new JSONObject();


    }
}
