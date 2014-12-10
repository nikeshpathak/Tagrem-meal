package mymeal.tagrem.com.mymeal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Service on 11/18/2014.
 */
public class MealService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
                if(Utility.isTimeToAlarm()) {
                    Intent myintent = new Intent(MealService.this, DialogActivity.class);
                    myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myintent);
                }
        }
        catch(Exception ex)
        {
            Utility.Log(ex.getMessage(), Utility.LogType.ERROR);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
