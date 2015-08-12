package htc.cloud.intern.hungrytest.hungryapi;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import htc.cloud.intern.hungrytest.UserState;

/**
 * Created by intern on 7/28/15.
 */
public class HungryAsyncTask extends AsyncTask<UserState, Void, JSONArray> {

    public final static String apiURL = "https://recornot.herokuapp.com/";
    public final static String serviceName = "get_recommendation";
    public final static String useridField = "user_id";
    public final static String locationField = "coordinate";
    public final static String startRank = "start_rank";
    public final static String numField = "num_rec";
    public final static String jsonArrayName = "businesses";

    public final static int curRank = 0;
    public final static int numRec = 100;

    private AsyncResponse responseDelegate;
    private URL url;

    public void setResponseDelegate(AsyncResponse rd){
        responseDelegate = rd;
    }

    @Override
    protected JSONArray doInBackground(UserState... userStates) {

        InputStream inputStream;
        HttpsURLConnection urlConnection;
        StringWriter strWriter;
        String jsonString;
        JSONArray jsonArray = null;

        // Get top recommendations from API
        double startTime, endTime;
        startTime = System.currentTimeMillis();
        Log.i("hungry-api", "Starting....");

        try {
            url = new URL(apiURL+serviceName+
                    "?"+useridField+"="+userStates[0].mDeviceID+
                    "&"+locationField+"="+userStates[0].mCurrentLocation+
                    "&"+startRank+"="+curRank+
                    "&"+numField+"="+numRec);

            urlConnection = (HttpsURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            strWriter = new StringWriter();
            IOUtils.copy(inputStream, strWriter, (String)null);
            jsonString = strWriter.toString();
            jsonArray = (new JSONObject(jsonString)).getJSONArray(jsonArrayName);

            endTime = System.currentTimeMillis();
            Log.i("hungry-api", "API: "+jsonArray.length()+", "+(endTime-startTime)/1000+" sec");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    protected void onPostExecute(JSONArray jsonArray) {
        responseDelegate.onPostExecute(jsonArray);
    }

}
