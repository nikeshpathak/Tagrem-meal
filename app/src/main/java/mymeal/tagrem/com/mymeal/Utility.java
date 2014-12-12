package mymeal.tagrem.com.mymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nikesh on 11/19/2014.
 */
public class Utility {

    static String TAG = "SecretMail";
    public final static String MEAL_STORE = "Secret_store";

    public static void showMessage(Context context,String value)
    {
        if(context !=null && value !=null)
        Toast.makeText(context,value,Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(String str)
    {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern =Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static void ShowMessage(Context context,String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void ShowMessageOnThread(final Activity context,final String message)
    {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Checks and returns whether there is an Internet connectivity or not. This
     * would be useful to check the network connectivity before making a network
     * call.
     *
     * @param context
     * @return "True" -> is Connected , "False" -> if not.
     */
    public synchronized static boolean isNetworkAvailable(Context context) {
        boolean isConnected = false;
        final ConnectivityManager connectivityService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityService != null) {
            final NetworkInfo networkInfo = connectivityService
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * Use this method to show dialog box where user able to write event on both button positive or negative
     * @param context Interface to global information about an application environment. This is an abstract class whose implementation is provided by the Android system. It allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc
     * @param title Show dialog box title
     * @param msg dialog box message content
     * @param btn1 Button Object that indicate positiveButton click
     * @param btn2 Button Object that indicate Negative button click
     * @param listener1 listener1 listen btn1 click
     * @param listener2 listener2 listen btn2 click
     * @return AlertDialog Object , we need to show "show()" to show dialogbox
     */
    public static AlertDialog showDialog(Context context, String title, String msg,
                                         String btn1, String btn2,
                                         DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(true)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        return alert;

    }

    /**
     * Use this method to show dialog box where user able to write event on only positive button another one is only dispose the dialog box
     * @param context Interface to global information about an application environment. This is an abstract class whose implementation is provided by the Android system. It allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc
     * @param title Show dialog box title
     * @param msg dialog box message content
     * @param btn1 Button Object that indicate positiveButton click
     * @param btn2 Button Object that indicate Negative button click(on btn1 click just dispose dialog)
     * @param listener listener listen btn1 click
     * @return AlertDialog Object , we need to show "show()" to show dialogbox
     */
    public static AlertDialog showDialog(Context context, String title, String msg,
                                         String btn1, String btn2, DialogInterface.OnClickListener listener) {

        return showDialog(context, title, msg, btn1, btn2, listener,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

    }

    /**
     * Use this method show dialog box that only show information. that not get any specific command for user
     * @param context Interface to global information about an application environment. This is an abstract class whose implementation is provided by the Android system. It allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc
     * @param title Show dialog box title
     * @param msg dialog box message content
     * @param listener listener listen Ok button click
     * @return AlertDialog Object , we need to show "show()" to show dialogbox
     */
    public static AlertDialog showDialog(Context context, String title, String msg,
                                         DialogInterface.OnClickListener listener) {

        return showDialog(context, title, msg, "OK", null, listener, null);
    }



    public enum LogType
    {
        DEBUG,
        ERROR,
        INFO,
        VERBOSE,
        WRAN
    }

    /**
     * Use this method to log debug,error,info,wran
     * @param value Error message or information etc
     * @param logtype Use this enum to define message type
     */
    public static void Log(String value,LogType logtype)
    {
        if(value !=null) {
            switch (logtype) {
                case DEBUG:
                    Log.d(TAG, value);
                    break;
                case ERROR:
                    Log.e(TAG, value);
                    break;
                case INFO:
                    Log.i(TAG, value);
                    break;
                case VERBOSE:
                    Log.v(TAG, value);
                    break;
                case WRAN:
                    Log.w(TAG, value);
                    break;
                default:
                    Log.e(TAG, value);
                    break;
            }
        }
    }

    public static void StoreData(Context context,String key,String Value)
    {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(MEAL_STORE, context.MODE_PRIVATE).edit();
            editor.putString(key, Value);
            editor.commit();
        }
        catch(Exception ex)
        {
            Utility.Log(ex.getMessage(),LogType.ERROR);
        }
    }

    public static String GetStoreData(Context context,String key)
    {
        SharedPreferences prefs = context.getSharedPreferences(MEAL_STORE, context.MODE_PRIVATE);
        String restoredText = prefs.getString(key, null);
        if (restoredText != null) {
            String name = prefs.getString(key, null);
            return name;
        }
        else
            return null;
    }

    public static void ClearData(Context context)
    {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(MEAL_STORE, context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }
        catch(Exception ex)
        {
            Utility.Log(ex.getMessage(),LogType.ERROR);
        }
    }

    public static String getFilterEmailAddress(String value)
    {
        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(value);
        while (m.find()) {
            return m.group();
        }
        return null;
    }

    public static Boolean isTimeToAlarm()
    {
        Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    if (calendar.get(Calendar.HOUR) == 9 && calendar.get(Calendar.MINUTE) < 30 && calendar.get(Calendar.AM_PM) == Calendar.AM) {
                        return true;
                    }
                }
        return false;
    }
}
