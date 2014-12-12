package mymeal.tagrem.com.mymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mymeal.tagrem.com.mymeal.mail.Mail;


public class MainActivity extends Activity {

    Button btnSubmit;
    EditText txtUserName, txtPassword;
    String DomainName = "@tagrem.com";
    String emailaddresss;
    AlertDialog alertDialog;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            String storeUser = Utility.GetStoreData(MainActivity.this, "EMAIL");
            String storePass = Utility.GetStoreData(MainActivity.this, "PASSWORD");
            Typeface typeface = Typeface.createFromAsset(getAssets(), "capture_it.ttf");

            if (storeUser != null && storePass != null) {
                alertDialog = Utility.showDialog(MainActivity.this, "", "Tagrem meal system already activated on your phone", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MealReciver.StartAlarmManager(MainActivity.this);
                                alertDialog.dismiss();
                                finish();
                            }
                        }
                );
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            txtUserName = (EditText) findViewById(R.id.txtUserName);
            txtPassword = (EditText) findViewById(R.id.txtPassword);
            txtTitle = (TextView)findViewById(R.id.txtTitle);
            txtTitle.setTypeface(typeface);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emailaddresss = txtUserName.getText().toString() + DomainName;
                    if (Utility.isNetworkAvailable(MainActivity.this)) {
                        if (IsValid())
                            new AsynExecute(emailaddresss, txtPassword.getText().toString()).execute();

                    } else
                        Utility.showMessage(MainActivity.this, "Network not available.");
                }
            });
        }
        catch(Exception ex)
        {
            Utility.Log(ex.getMessage(), Utility.LogType.ERROR);
        }
    }

    public boolean IsValid()
    {
        emailaddresss = txtUserName.getText().toString() + DomainName;
        if(!Utility.isValidEmail(emailaddresss))
        {
            txtUserName.setError("Please enter valid email address.");
            return false;
        }
        if(txtPassword ==null || txtPassword.getText().equals(""))
        {
            txtPassword.setError("Please enter valid password.");
        }
        return true;
    }

    private class AsynExecute extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressdailog;
        String username;
        String password;
        String message;

        AsynExecute(String username, String password) {
            this.username = username;
            this.password = password;
            this.message = "Hello this is auto generated mail form tagrem meal system.";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdailog = new ProgressDialog(MainActivity.this, R.style.MyTheme);
            progressdailog.setCancelable(false);
            progressdailog.setMessage("Processing... please wait.");
            progressdailog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Mail mail = new Mail(username, password, message);
            return mail.send(true);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (progressdailog != null)
                progressdailog.dismiss();
            if (s == true) {
                MealReciver.StartAlarmManager(MainActivity.this);
                Utility.showMessage(MainActivity.this, "Now Tagrem meal system sucessfully activated.");
                Utility.StoreData(MainActivity.this, "EMAIL", emailaddresss);
                Utility.StoreData(MainActivity.this, "PASSWORD", txtPassword.getText().toString());
                String username = Utility.GetStoreData(MainActivity.this, "EMAIL");
                String password = Utility.GetStoreData(MainActivity.this, "PASSWORD");
                finish();
            } else {
                Utility.showMessage(MainActivity.this, "Email and password not valid!");
            }
        }
    }
}
