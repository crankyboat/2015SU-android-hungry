package htc.cloud.intern.hungrytest.hungryapi;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by intern on 7/28/15.
 */
public interface AsyncResponse {

    void onPostExecute(AsyncTask<?, ?, ?> asyncTask, ArrayList<?> arrayList);

    void onNoExecute(AsyncTask<?, ?, ?> asyncTask, String message);

}
