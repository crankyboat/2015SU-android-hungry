package htc.cloud.intern.hungrytest.hungryapi;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by intern on 7/28/15.
 */
public interface AsyncResponse {

    void onPostExecute(ArrayList<?> arrayList);

}
