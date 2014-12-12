package mymeal.tagrem.com.mymeal;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Nikesh on 11/18/2014.
 */
public class CustomDialog extends Dialog {

    RadioGroup radioGroup;
    Button btnYes,btnNo;
    String foodValue = "";
    TextView txtDialogTitle;

    public CustomDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dailog);
        radioGroup = (RadioGroup)findViewById(R.id.myRadioGroup);
        btnYes = (Button)findViewById(R.id.btnYes);
        btnNo = (Button)findViewById(R.id.btnNo);
        txtDialogTitle = (TextView)findViewById(R.id.dialogTitle);
    }
}
