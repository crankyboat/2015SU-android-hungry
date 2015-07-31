package htc.cloud.intern.hungrytest.hungryapi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import htc.cloud.intern.hungrytest.UserState;

/**
 * Created by intern on 7/30/15.
 */
public class FeedbackAsyncTask extends AsyncTask<UserState, Void, Void> {

    public final static String apiURL = "https://recornot.herokuapp.com/";
    public final static String serviceName = "feedback";
    public final static String useridField = "user_id";
    public final static String feedbackField = "feedbacks"; //fedbacks=[...]
    private URL url;

    @Override
    protected Void doInBackground(UserState... userStates) {

        HttpsURLConnection urlConnection;
        OutputStreamWriter outputStreamWriter;

        // Construct JSONstring
        // Input = { userid, JSONArray of business_id:clicks pairs }
//        String jsonString = new String();
//        jsonString = userStates[0].getFeedbackAndClear();


        // PUT to db
        try {
            url = new URL(apiURL+serviceName
                    +"?"+useridField+"="+userStates[0].mDeviceID
                    +"&"+feedbackField+"=["+userStates[0].getFeedbackAndClear()+"]");
            urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//            urlConnection.setRequestMethod("PUT");
//            urlConnection.setRequestProperty("Content-Type", "application/json");

//            outputStreamWriter= new OutputStreamWriter(urlConnection.getOutputStream());
//            outputStreamWriter.write(jsonString);
//            outputStreamWriter.flush();
            Log.i("FeedbackAsyncTask", "(PUT) Response Code " + urlConnection.getResponseCode());

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String tempString;
            String responseString  = new String();
            while ((tempString = bufferedReader.readLine()) != null) {
                responseString += tempString;
            }
            bufferedReader.close();
            Log.i("FeedbackAsyncTask", responseString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
