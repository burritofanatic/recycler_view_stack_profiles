package williamha.com.wagchallenge.view;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import williamha.com.wagchallenge.R;
import williamha.com.wagchallenge.model.Badges;
import williamha.com.wagchallenge.model.StackProfile;

/**
 * A simple {@link Fragment} subclass.
 */

public class AvatarFragment extends Fragment implements AsyncResponse {

    StackProfileTask asyncTask = new StackProfileTask();

    public AvatarFragment() {
        // Required empty public constructor
    }

    public static AvatarFragment newInstance() {
        return new AvatarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTask.delegate = this;
        asyncTask.execute("https://api.stackexchange.com/2.2/users?site=stackoverflow");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avatar, container, false);
    }

    @Override
    public void fetchCompleted(String result) {
        Log.d("Response: ", "> " + result);

        List<StackProfile> stackProfiles = new ArrayList<StackProfile>();

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

    }
}

class StackProfileTask extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        delegate.fetchCompleted(result);
    }
}