package williamha.com.wagchallenge.repo;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import williamha.com.wagchallenge.managers.StoreManager;


public class StackProfileRepo {

    private final String USER_ENDPOINT = "https://api.stackexchange.com/2.2/users?site=stackoverflow";
    private StackProfileTask asyncTask;

    public interface AsyncResponse {
        void fetchCompleted(String result);
    }

    public StackProfileRepo(AsyncResponse delegate, Context context) {
        asyncTask = new StackProfileTask(new WeakReference<>(context));
        asyncTask.delegate = delegate;
    }

    public void startProfileFetch(String page) {
        asyncTask.execute(USER_ENDPOINT + String.format("&page=%s", page), page);
    }

    private static class StackProfileTask extends AsyncTask<String, String, String> {

        private AsyncResponse delegate = null;
        private WeakReference<Context> context;
        private String page;

        public StackProfileTask(WeakReference<Context> context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                this.page = params[1];
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

            StoreManager storeManager = new StoreManager(this.context.get());
            storeManager.setDataForPage(page, result);
            delegate.fetchCompleted(result);
        }
    }
}
