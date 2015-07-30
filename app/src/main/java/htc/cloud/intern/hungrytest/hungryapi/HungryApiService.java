package htc.cloud.intern.hungrytest.hungryapi;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by intern on 7/30/15.
 */
public class HungryApiService extends IntentService {

    public HungryApiService() {
        super("HungryApiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Call the Hungry API and cache the results

    }
}
