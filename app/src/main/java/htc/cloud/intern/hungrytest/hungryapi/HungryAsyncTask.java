package htc.cloud.intern.hungrytest.hungryapi;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public final static String numField = "num_rec";
    public final static String jsonArrayName = "businesses";

    public final static int numRec = 30;

    private AsyncResponse responseDelegate;
    private URL url;

    public void setResponseDelegate(AsyncResponse rd){
        responseDelegate = rd;
    }

    @Override
    protected JSONArray doInBackground(UserState... userStates) {

        InputStream inputStream;
        HttpsURLConnection urlConnection;
        BufferedReader bufferedReader;
        String tempString;
        String jsonString;
        JSONArray jsonArray = null;

        // Get top recommendations from API
        try {
            url = new URL(apiURL+serviceName+
                    "?"+useridField+"="+userStates[0].mDeviceID+
                    "&"+locationField+"="+userStates[0].mCurrentLocation+
                    "&"+numField+"="+numRec);

            urlConnection = (HttpsURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            jsonString = new String();
            while ((tempString = bufferedReader.readLine()) != null) {
                jsonString += tempString;
            }
            bufferedReader.close();
            jsonArray = (new JSONObject(jsonString)).getJSONArray(jsonArrayName);

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

        Log.i("hungry-api", "API: "+jsonArray.length());
        responseDelegate.onPostExecute(jsonArray);

    }

}
