package invites.sample.com.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by mpudota on 4/8/17.
 */

public class Crime {

    private static final String JSON_ID = "id";
    private final static String JSON_NAME = "name";
    private static final String JSON_DATE = "date";
    private final static String JSON_SOlved = "solved";
    private final static String JSON_SUSPECT = "suspect";

    public Crime() {

        uuid = UUID.randomUUID();
        date = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        uuid = UUID.fromString(json.getString(JSON_ID));

        if (json.has(JSON_NAME)) {
            title = json.getString(JSON_NAME);
        }
        solved = json.getBoolean(JSON_SOlved);
        date = new Date(json.getString(JSON_DATE));
        if(json.has(JSON_SUSPECT)) {
            suspect = json.getString(JSON_SUSPECT);
        }
    }

    private String title;
    private UUID uuid;
    private Date date;
    private boolean solved;

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    private String suspect;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return title;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, uuid.toString());
        json.put(JSON_NAME, title);
        json.put(JSON_SOlved, solved);
        json.put(JSON_SUSPECT, suspect);
        json.put(JSON_DATE, date.toString());
        return json;
    }
    public void deleteFromJson () {
        JSONObject jsonObject = new JSONObject();
        jsonObject.remove(JSON_ID);
        jsonObject.remove(JSON_NAME);
        jsonObject.remove(JSON_DATE);
        jsonObject.remove(JSON_SOlved);

    }
}
