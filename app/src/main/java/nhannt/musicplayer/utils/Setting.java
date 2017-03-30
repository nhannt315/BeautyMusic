package nhannt.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 * Created by nhannt on 03/03/2017.
 */

public class Setting {
    private static Setting mInstance;
    private SharedPreferences preferences;
    private Context context;

    public static Setting getInstance() {
        if (mInstance == null)
            mInstance = new Setting();
        return mInstance;
    }

    // Initialize the internal setting
    public void init(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Reset all setting to default
     */
    public void reset() {
        this.preferences.edit().clear().commit();
    }

    //Query method

    /**
     * Get Boolean setting
     */
    public boolean get(String key, boolean defaultValue) {
        if (preferences == null)
            return defaultValue;
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * Get String setting
     */
    public String get(String key, String defaultValue) {
        if (preferences == null)
            return defaultValue;
        return preferences.getString(key, defaultValue);
    }

    /**
     * Get int setting
     */
    public int get(String key, int defaultValue) {
        if (preferences == null)
            return defaultValue;
        return preferences.getInt(key, defaultValue);
    }

    /**
     * put boolean setting
     *
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        if (preferences == null) return;
        new PutBooleanAsync(key, value).execute();
    }

    private class PutBooleanAsync extends AsyncTask<Void, Void, Void> {
        private boolean value;
        private String key;

        public PutBooleanAsync(String key, boolean value) {
            this.value = value;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
            return null;
        }
    }


    /**
     * Put int setting
     *
     * @param key
     * @param value
     */
    public void put(String key, int value) {
        if (preferences == null) return;
        new PutIntAsync(key, value).execute();
    }

    private class PutIntAsync extends AsyncTask<Void, Void, Void> {
        private int value;
        private String key;

        public PutIntAsync(String key, int value) {
            this.value = value;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.commit();
            return null;
        }
    }

    /**
     * Put String setting
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if (preferences == null) return;
        new PutStringAsync(key,value).execute();
    }
    private class PutStringAsync extends AsyncTask<Void,Void,Void>{
        private String value;
        private String key;
        public PutStringAsync(String key,String value){
            this.key = key;
            this.value = value;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
            return null;
        }
    }

    private Setting() {
    }
}
