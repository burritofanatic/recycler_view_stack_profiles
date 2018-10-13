package williamha.com.wagchallenge.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import williamha.com.wagchallenge.managers.StoreManager;
import williamha.com.wagchallenge.model.Badges;
import williamha.com.wagchallenge.model.StackProfile;
import williamha.com.wagchallenge.repo.StackProfileRepo;

public class StackProfileViewModel extends AndroidViewModel implements StackProfileRepo.AsyncResponse {
    private MutableLiveData<List<StackProfile>> profiles;

    public StackProfileViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<List<StackProfile>> getProfiles() {
        if (profiles == null) {
            profiles = new MutableLiveData<>();
            loadProfiles();
        }
        return profiles;
    }

    private void loadProfiles() {

        String page = "1";

        if (isNetworkAvailable()) {
            StackProfileRepo repo = new StackProfileRepo(this, getApplication().getApplicationContext());
            repo.startProfileFetch(page);
        } else {
            StoreManager storeManager = new StoreManager(getApplication().getApplicationContext());
            String result = storeManager.getDataForPage(page);
            if (result != null) {
                fetchCompleted(result);
            }
        }
    }

    @Override
    public void fetchCompleted(String result) {
        Log.d("Response: ", "> " + result);

        List<StackProfile> stackProfiles = new ArrayList<>();

        try {
            JSONObject resultJSON = new JSONObject(result);

            JSONArray items = resultJSON.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject profile = (JSONObject) items.get(i);

                JSONObject badgesJSON = profile.getJSONObject("badge_counts");

                int goldCount = badgesJSON.getInt("gold");
                int silverCount = badgesJSON.getInt("silver");
                int bronzeCount = badgesJSON.getInt("bronze");

                Badges badges = new Badges(goldCount, silverCount, bronzeCount);

                String displayName = profile.getString("display_name");
                String imageUrl = profile.getString("profile_image");

                StackProfile stackProfile = new StackProfile(displayName, badges, imageUrl);
                stackProfiles.add(stackProfile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        profiles.setValue(stackProfiles);
    }


    // Helpers
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
