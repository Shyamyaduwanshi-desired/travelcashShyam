package constant;

import android.content.Context;
import android.content.SharedPreferences;

public class SignUpPreference {
    private static SignUpPreference signUpPreference;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private SignUpPreference(Context context) {
        mSharedPreferences = context.getSharedPreferences("registration_data", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SignUpPreference getInstance(Context context) {
        if (signUpPreference == null) {
            signUpPreference = new SignUpPreference(context.getApplicationContext());
        }
        return signUpPreference;
    }

    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.apply();
    }

    public String getValue(String key) {
        return mSharedPreferences.getString(key, "NA");
    }

    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }
}

