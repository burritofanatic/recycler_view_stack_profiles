package williamha.com.wagchallenge.repo;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class StackProfileRepo {

    private final String USER_ENDPOINT = "https://api.stackexchange.com/2.2/users?site=stackoverflow";

    public interface AsyncResponse {
        void fetchCompleted(String result);
    }

    private StackProfileTask asyncTask = new StackProfileTask();

    public StackProfileRepo(AsyncResponse delegate) {
        asyncTask.delegate = delegate;
    }

    public void startProfileFetch(String page) {
        asyncTask.execute(USER_ENDPOINT + String.format("&page=%s", page));
    }

    private static class StackProfileTask extends AsyncTask<String, String, String> {

        private AsyncResponse delegate = null;

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
}
