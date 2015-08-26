package htc.cloud.intern.hungrytest.hungryapi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import htc.cloud.intern.hungrytest.PlaceState;
import htc.cloud.intern.hungrytest.UserState;

/**
 * Created by intern on 7/28/15.
 */
public class HungryAsyncTask extends AsyncTask<UserState, Void, JSONArray> {

    public final static String apiURL = "https://recornot.herokuapp.com/";
    public final static String serviceName = "recommendation";
    public final static String useridField = "user_id";
    public final static String locationField = "coordinate";
    public final static String startRank = "start_rank";
    public final static String numField = "num_rec";
    public final static String jsonArrayName = "businesses";
    public final static String zoomField = "zoom_level";
    public final static int numRec = 100;

    private int currentRank;
    private int currentZoom;
    private AsyncResponse responseDelegate;
    private URL url;

    public HungryAsyncTask(AsyncResponse delegate, int zoom) {
        responseDelegate = delegate;
        currentZoom = zoom;
    }

    public int getCurrentZoom() {
        return currentZoom;
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
//                    "&"+startRank+"="+currentRank+
//                    "&"+numField+"="+numRec+
                    "&"+zoomField+"="+currentZoom);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    protected void onPostExecute(JSONArray jsonArray) {

        if (jsonArray==null) {
            responseDelegate.onNoExecute(this, "");
            return;
        }

        ArrayList<PlaceState> likelyPlaces = new ArrayList<PlaceState>();

        double maxDistance = 0.0;
        JSONObject business;
        String id;
        String name;
        String address;
        int rank;
        double rating;
        double distance;
        LatLng latlng;
        String imgSrc;
        String category;
        String phoneNum;
        String snippet;
        ArrayList<String> imgList;

        try {
            for (int i=0; i<jsonArray.length(); i++) {
                business = jsonArray.getJSONObject(i);

                id = business.getString("business_id");

                name = business.getString("name");

                latlng = new LatLng(business.getJSONObject("location").getJSONObject("coordinate").getDouble("latitude"),
                        business.getJSONObject("location").getJSONObject("coordinate").getDouble("longitude"));


                imgSrc = business.has("image_url")
                        ? business.getString("image_url").replace("ms.jpg", "ls.jpg")
                        : "";

                phoneNum = business.has("display_phone")
                        ? business.getString("display_phone")
                        : "Phone Number N/A";

                snippet = business.has("snippet_text")
                        ? business.getString("snippet_text")
                        : "";

                rank = business.has("rec_order")
                        ? business.getInt("rec_order")
                        : Integer.MAX_VALUE;

                rating = business.has("rating")
                        ? business.getDouble("rating")
                        : 0.0;

                distance = business.has("distance")
                        ? business.getDouble("distance")
                        : -1.0;

                maxDistance = (distance > maxDistance)
                        ? distance
                        : maxDistance;

                int maxCatCount = 4;
                category = new String();
                if (business.has("categories")) {
                    for (int j = 0; j < Math.min(business.getJSONArray("categories").getJSONArray(0).length(), maxCatCount); j++) {
                        category += business.getJSONArray("categories").getJSONArray(0).get(j)+" ";
                    }
                }

                address = new String();
                if (business.has("location") && business.getJSONObject("location").has("display_address")) {
                    for (int j = 0; j < business.getJSONObject("location").getJSONArray("display_address").length(); j++) {
                        address += business.getJSONObject("location").getJSONArray("display_address").get(j)+" ";
                    }
                }
                else {
                    address = "Address N/A";
                }

                imgList = new ArrayList<String>();
                for (int j = 0; j < business.getJSONArray("img_urls_and_descs").length(); j++) {
                    imgList.add(business.getJSONArray("img_urls_and_descs").getJSONArray(j).getString(0));
                }

                likelyPlaces.add(new PlaceState(id, name, address, rank, rating, distance, latlng, imgSrc, category, phoneNum, snippet, imgList));
            }

            Log.i("hungry-api", "Parsing Completed.");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        responseDelegate.onPostExecute(this, likelyPlaces);

    }

}
