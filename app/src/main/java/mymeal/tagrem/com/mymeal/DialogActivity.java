package mymeal.tagrem.com.mymeal;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import mymeal.tagrem.com.mymeal.mail.Mail;

/**
 * Created by Nikesh on 11/18/2014.
 */
public class DialogActivity extends Activity{

    CustomDialog customDialog;
    private static String foodValue = "YES";
    private static String Email;
    private static String Password;
    Ringtone ringtone;
    protected  PowerManager.WakeLock wakeLock;
    KeyguardManager km;
    KeyguardManager.KeyguardLock kl;
    boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.dialog_activity);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "capture_it.ttf");
        Email = Utility.GetStoreData(DialogActivity.this,"EMAIL");
        Password = Utility.GetStoreData(DialogActivity.this,"PASSWORD");
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.txtDialogTitle.setTypeface(typeface);
        customDialog.show();

        try {
            PowerManager TempPowerManager = (PowerManager) DialogActivity.this.getSystemService(Context.POWER_SERVICE);
            wakeLock = TempPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "TempWakeLock");
            km = (KeyguardManager) DialogActivity.this.getSystemService(Context.KEYGUARD_SERVICE);
            kl = km.newKeyguardLock("INFO");
            wakeLock.acquire();
            kl.disableKeyguard();
        }
        catch(Exception ex)
        {
        }
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        Thread.sleep(300);
                        Vibrator v = (Vibrator) DialogActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(100);
                    }
                }
                catch (Exception ex)
                {
                    Utility.Log(ex.getMessage(), Utility.LogType.ERROR);
                }
            }
        }).start();

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
         ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ringtone.play();

        customDialog.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                ringtone.stop();
                startService(new Intent(DialogActivity.this, SendMailService.class));
                finish();
                isRunning = false;
            }
        });

        customDialog.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.showDialog(DialogActivity.this,"Cancel alert!","Are you sure you want to cancel today lunch in office?","Yes","Cancel",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customDialog.dismiss();
                        ringtone.stop();
                        foodValue = "NO";
                        startService(new Intent(DialogActivity.this,SendMailService.class));
                        finish();
                    }
                }).show();
            }
        });

        customDialog.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(R.id.fullMeal == checkedId)
                {
                    foodValue = "YES";
                } else if(R.id.miniMeal == checkedId)
                {
                    foodValue = "YES MINI";
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wakeLock !=null)
            wakeLock.release();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Utility.isTimeToAlarm())
        {
            Utility.ShowMessage(DialogActivity.this,"Invalid time!");
        }
    }

    public static class SendMailService  extends Service
    {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            if(Utility.isNetworkAvailable(getApplicationContext())) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail mail = new Mail(Email, Password, foodValue);
                        mail.send(false);
                    }
                }).start();
            }
            else
            {
                Utility.ShowMessage(getApplicationContext(),getApplicationContext().getString(R.string.no_network_message));
            }
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
