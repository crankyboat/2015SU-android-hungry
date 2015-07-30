package htc.cloud.intern.hungrytest.hungryapi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by intern on 7/30/15.
 */
public class FeedbackAsyncTask extends AsyncTask<JSONArray, Void, Void> {

    public final static String apiURL = "https://recornot.herokuapp.com/";
    public final static String serviceName = "feedback";

    @Override
    protected Void doInBackground(JSONArray... jsonArrays) {

        URL url;
        HttpsURLConnection urlConnection;
        OutputStreamWriter outputStreamWriter;

        // Construct JSONstring
        // Input = { userid, JSONArray of business_id:clicks pairs }
        String jsonString = new String();

        // PUT to db
        try {
            url = new URL(apiURL+serviceName);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            outputStreamWriter= new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(jsonString);
            outputStreamWriter.flush();
            Log.i("FeedbackAsyncTask", "(PUT) Response Code " + urlConnection.getResponseCode());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
