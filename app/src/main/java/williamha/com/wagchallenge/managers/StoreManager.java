package williamha.com.wagchallenge.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import williamha.com.wagchallenge.R;

public class StoreManager {

    private Context context;

    public StoreManager(Context context) {
        this.context = context;
    }

    public void setDataForPage(String page, String data) {
        String key = String.format(context.getString(R.string.user_data_shared_prefs_key), page);
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public @Nullable
    String getDataForPage(String page) {
        String key = String.format(context.getString(R.string.user_data_shared_prefs_key), page);
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, null);
    }
}
