package mymeal.tagrem.com.mymeal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nikesh on 11/18/2014.
 */
public class MealReciver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            StartAlarmManager(context);
        }
    }

    public static void StartAlarmManager(Context context)
    {
        Intent alarmIntent = new Intent(context, MealService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.AM);
      //  manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),TimeUnit.HOURS.toMillis(24),pendingIntent);
    }
}
