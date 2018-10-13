package williamha.com.wagchallenge.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import williamha.com.wagchallenge.model.Badges;
import williamha.com.wagchallenge.model.StackProfile;
import williamha.com.wagchallenge.repo.StackProfileRepo;

public class StackProfileViewModel extends ViewModel implements StackProfileRepo.AsyncResponse {
    private MutableLiveData<List<StackProfile>> profiles;

    public LiveData<List<StackProfile>> getProfiles() {
        if (profiles == null) {
            profiles = new MutableLiveData<>();
            loadProfiles();
        }
        return profiles;
    }

    private void loadProfiles() {
        StackProfileRepo repo = new StackProfileRepo(this);
        repo.startProfileFetch("1");
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
}
